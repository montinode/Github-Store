package zed.rainxch.githubstore.core.data.repository

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import zed.rainxch.githubstore.core.data.local.db.dao.InstalledAppDao
import zed.rainxch.githubstore.core.data.local.db.dao.UpdateHistoryDao
import zed.rainxch.githubstore.core.data.local.db.entities.InstallSource
import zed.rainxch.githubstore.core.data.local.db.entities.InstalledApp
import zed.rainxch.githubstore.core.data.local.db.entities.UpdateHistory
import zed.rainxch.githubstore.core.domain.repository.InstalledAppsRepository
import zed.rainxch.githubstore.feature.details.data.Installer
import zed.rainxch.githubstore.feature.details.domain.repository.DetailsRepository

class InstalledAppsRepositoryImpl(
    private val dao: InstalledAppDao,
    private val historyDao: UpdateHistoryDao,
    private val detailsRepository: DetailsRepository,
    private val installer: Installer
) : InstalledAppsRepository {

    override fun getAllInstalledApps(): Flow<List<InstalledApp>> = dao.getAllInstalledApps()

    override fun getAppsWithUpdates(): Flow<List<InstalledApp>> = dao.getAppsWithUpdates()

    override fun getUpdateCount(): Flow<Int> = dao.getUpdateCount()

    override suspend fun getAppByPackage(packageName: String): InstalledApp? =
        dao.getAppByPackage(packageName)

    override suspend fun getAppByRepoId(repoId: Long): InstalledApp? =
        dao.getAppByRepoId(repoId)

    override suspend fun isAppInstalled(repoId: Long): Boolean =
        dao.getAppByRepoId(repoId) != null

    override suspend fun saveInstalledApp(app: InstalledApp) {
        dao.insertApp(app)
    }

    override suspend fun deleteInstalledApp(packageName: String) {
        dao.deleteByPackageName(packageName)
    }

    override suspend fun checkForUpdates(packageName: String): Boolean {
        val app = dao.getAppByPackage(packageName) ?: return false

        try {
            val latestRelease = detailsRepository.getLatestPublishedRelease(
                owner = app.repoOwner,
                repo = app.repoName,
                defaultBranch = ""
            )

            if (latestRelease != null) {
                val isUpdateAvailable = latestRelease.tagName != app.installedVersion

                val installableAssets = latestRelease.assets.filter { asset ->
                    installer.isAssetInstallable(asset.name)
                }

                val primaryAsset = installer.choosePrimaryAsset(installableAssets)

                Logger.d {
                    "Update check for ${app.appName}: current=${app.installedVersion}, " +
                            "latest=${latestRelease.tagName}, isUpdate=$isUpdateAvailable, " +
                            "primaryAsset=${primaryAsset?.name}"
                }

                dao.updateVersionInfo(
                    packageName = packageName,
                    available = isUpdateAvailable,
                    version = latestRelease.tagName,
                    assetName = primaryAsset?.name,
                    assetUrl = primaryAsset?.downloadUrl,
                    assetSize = primaryAsset?.size,
                    releaseNotes = latestRelease.description ?: "",
                    timestamp = System.currentTimeMillis()
                )

                return isUpdateAvailable
            }
        } catch (e: Exception) {
            Logger.e { "Failed to check updates for $packageName: ${e.message}" }
            dao.updateLastChecked(packageName, System.currentTimeMillis())
        }

        return false
    }

    override suspend fun checkAllForUpdates() {
        val apps = dao.getAllInstalledApps().first()
        apps.forEach { app ->
            if (app.updateCheckEnabled) {
                try {
                    checkForUpdates(app.packageName)
                } catch (e: Exception) {
                    Logger.w { "Failed to check updates for ${app.packageName}: ${e.message}" }
                }
            }
        }
    }

    override suspend fun updateAppVersion(
        packageName: String,
        newVersion: String,
        newAssetName: String,
        newAssetUrl: String
    ) {
        val app = dao.getAppByPackage(packageName) ?: return

        Logger.d {
            "Updating app version: $packageName from ${app.installedVersion} to $newVersion"
        }

        historyDao.insertHistory(
            UpdateHistory(
                packageName = packageName,
                appName = app.appName,
                repoOwner = app.repoOwner,
                repoName = app.repoName,
                fromVersion = app.installedVersion,
                toVersion = newVersion,
                updatedAt = System.currentTimeMillis(),
                updateSource = InstallSource.THIS_APP,
                success = true
            )
        )

        dao.updateApp(
            app.copy(
                installedVersion = newVersion,
                installedAssetName = newAssetName,
                installedAssetUrl = newAssetUrl,
                latestVersion = newVersion,
                latestAssetName = newAssetName,
                latestAssetUrl = newAssetUrl,
                isUpdateAvailable = false,
                lastUpdatedAt = System.currentTimeMillis(),
                lastCheckedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updatePendingStatus(packageName: String, isPending: Boolean) {
        val app = dao.getAppByPackage(packageName) ?: return
        dao.updateApp(app.copy(isPendingInstall = isPending))
    }
}
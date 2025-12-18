package zed.rainxch.githubstore.core.domain.repository

import kotlinx.coroutines.flow.Flow
import zed.rainxch.githubstore.core.data.local.db.entities.InstalledApp

interface InstalledAppsRepository {
    fun getAllInstalledApps(): Flow<List<InstalledApp>>
    fun getAppsWithUpdates(): Flow<List<InstalledApp>>
    fun getUpdateCount(): Flow<Int>
    suspend fun getAppByPackage(packageName: String): InstalledApp?
    suspend fun getAppByRepoId(repoId: Long): InstalledApp?
    suspend fun isAppInstalled(repoId: Long): Boolean
    
    suspend fun saveInstalledApp(app: InstalledApp)
    suspend fun deleteInstalledApp(packageName: String)
    
    suspend fun checkForUpdates(packageName: String): Boolean
    suspend fun checkAllForUpdates()
    
    suspend fun updateAppVersion(
        packageName: String,
        newVersion: String,
        newAssetName: String,
        newAssetUrl: String
    )
    suspend fun updatePendingStatus(packageName: String, isPending: Boolean)
}

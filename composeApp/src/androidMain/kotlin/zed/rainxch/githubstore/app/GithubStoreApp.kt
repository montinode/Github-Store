package zed.rainxch.githubstore.app

import android.app.Application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import zed.rainxch.githubstore.app.di.initKoin
import zed.rainxch.githubstore.core.data.PackageMonitor
import zed.rainxch.githubstore.core.domain.repository.FavoritesRepository
import zed.rainxch.githubstore.core.domain.repository.InstalledAppsRepository

class GithubStoreApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GithubStoreApp)
        }

        val installedAppsRepository = get<InstalledAppsRepository>()
        val packageMonitor = get<PackageMonitor>()
        val lifecycleScope = get<CoroutineScope>()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val installedPackageNames = packageMonitor.getAllInstalledPackageNames()

                val appsInDb = installedAppsRepository.getAllInstalledApps().first()

                appsInDb.forEach { app ->
                    if (!installedPackageNames.contains(app.packageName)) {
                        Logger.d { "App ${app.packageName} no longer installed (not in system packages), removing from DB" }
                        installedAppsRepository.deleteInstalledApp(app.packageName)
                    }
                }

                Logger.d { "Robust system existence sync completed" }
            } catch (e: Exception) {
                Logger.e { "Failed to sync existence with system: ${e.message}" }
            }

            installedAppsRepository.checkAllForUpdates()
        }
    }
}
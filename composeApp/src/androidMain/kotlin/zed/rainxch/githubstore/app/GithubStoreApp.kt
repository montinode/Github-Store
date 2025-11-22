package zed.rainxch.githubstore.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import zed.rainxch.githubstore.core.presentation.utils.AppContextHolder
import zed.rainxch.githubstore.app.di.authModule
import zed.rainxch.githubstore.app.di.coreModule
import zed.rainxch.githubstore.app.di.initKoin

class GithubStoreApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GithubStoreApp)
        }
    }
}
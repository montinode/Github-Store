package zed.rainxch.githubstore

import androidx.compose.ui.window.ComposeUIViewController
import zed.rainxch.githubstore.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
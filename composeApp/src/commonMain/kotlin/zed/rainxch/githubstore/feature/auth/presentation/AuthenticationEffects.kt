package zed.rainxch.githubstore.feature.auth.presentation

sealed interface AuthenticationEffects {
    data class OpenBrowser(val url: String) : AuthenticationEffects
    data class CopyToClipboard(val label: String, val text: String) : AuthenticationEffects
}
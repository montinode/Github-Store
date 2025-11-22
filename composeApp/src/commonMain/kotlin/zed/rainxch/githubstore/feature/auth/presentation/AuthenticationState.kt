package zed.rainxch.githubstore.feature.auth.presentation

import zed.rainxch.githubstore.feature.auth.data.DeviceStart

sealed interface  AuthenticationState {
    data object LoggedOut : AuthenticationState
    data class DevicePrompt(
        val start: DeviceStart,
        val copied: Boolean = false
    ) : AuthenticationState
    data object Pending : AuthenticationState
    data object LoggedIn : AuthenticationState
    data class Error(val message: String) : AuthenticationState
}
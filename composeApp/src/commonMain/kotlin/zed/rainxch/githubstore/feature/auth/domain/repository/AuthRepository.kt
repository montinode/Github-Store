package zed.rainxch.githubstore.feature.auth.domain.repository

import kotlinx.coroutines.flow.Flow
import zed.rainxch.githubstore.feature.auth.data.DeviceStart
import zed.rainxch.githubstore.feature.auth.data.DeviceTokenSuccess

/**
 * Clean-architecture Auth contract for the domain layer.
 */
interface AuthRepository {
    /** Emits the latest persisted token string (Bearer), or null when logged out. */
    val accessTokenFlow: Flow<String?>

    /** Start the GitHub Device Authorization flow. */
    suspend fun startDeviceFlow(scope: String): DeviceStart

    /** Poll for the device token result. Returns either success or throws a meaningful error. */
    suspend fun awaitDeviceToken(start: DeviceStart): DeviceTokenSuccess

    /** Clear session and token persistence. */
    suspend fun logout()
}
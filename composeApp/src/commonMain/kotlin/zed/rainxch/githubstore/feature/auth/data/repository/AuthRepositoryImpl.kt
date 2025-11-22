package zed.rainxch.githubstore.feature.auth.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import zed.rainxch.githubstore.core.data.TokenDataSource
import zed.rainxch.githubstore.feature.auth.data.DeviceStart
import zed.rainxch.githubstore.feature.auth.data.DeviceTokenSuccess
import zed.rainxch.githubstore.feature.auth.data.GitHubAuthApi
import zed.rainxch.githubstore.feature.auth.data.getGithubClientId
import zed.rainxch.githubstore.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val tokenDataSource: TokenDataSource,
    private val scopeText: String = DEFAULT_SCOPE
) : AuthRepository {

    override val accessTokenFlow: Flow<String?>
        get() = tokenDataSource.tokenFlow.map { it?.accessToken }

    override suspend fun startDeviceFlow(scope: String): DeviceStart =
        withContext(Dispatchers.Default) {
            val clientId = getGithubClientId()
            require(clientId.isNotBlank()) { "Missing GitHub CLIENT_ID. Add GITHUB_CLIENT_ID to local.properties (Android/Desktop) or env (iOS)." }
            GitHubAuthApi.startDeviceFlow(clientId, scope.ifBlank { scopeText })
        }

    override suspend fun awaitDeviceToken(start: DeviceStart): DeviceTokenSuccess =
        withContext(Dispatchers.Default) {
            val clientId = getGithubClientId()
            val timeoutMs = start.expiresInSec * 1000L
            var remainingMs = timeoutMs
            var intervalMs = (start.intervalSec.coerceAtLeast(1)) * 1000L
            var result: DeviceTokenSuccess? = null
            while (result == null) {
                if (remainingMs <= 0L) throw CancellationException("Device code expired")
                val res = GitHubAuthApi.pollDeviceToken(clientId, start.deviceCode)
                val success = res.getOrNull()
                if (success != null) {
                    tokenDataSource.save(success)
                    result = success
                    break
                }
                val msg = (res.exceptionOrNull()?.message ?: "").lowercase()
                when {
                    "authorization_pending" in msg -> {
                        delay(intervalMs)
                        remainingMs -= intervalMs
                    }

                    "slow_down" in msg -> {
                        intervalMs += 2000
                        delay(intervalMs)
                        remainingMs -= intervalMs
                    }

                    "access_denied" in msg -> throw CancellationException("User denied access")
                    "expired_token" in msg || "expired_device_code" in msg -> throw CancellationException(
                        "Device code expired"
                    )

                    else -> error("Unexpected error: ${msg}")
                }
            }
            result!!
        }

    override suspend fun logout() {
        tokenDataSource.clear()
    }

    companion object {
        const val DEFAULT_SCOPE = "read:user repo"
    }
}
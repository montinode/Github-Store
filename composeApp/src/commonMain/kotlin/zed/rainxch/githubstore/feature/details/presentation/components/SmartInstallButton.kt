package zed.rainxch.githubstore.feature.details.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zed.rainxch.githubstore.core.domain.model.GithubAsset
import zed.rainxch.githubstore.feature.details.presentation.DetailsState
import zed.rainxch.githubstore.feature.details.presentation.DownloadStage
import zed.rainxch.githubstore.feature.install.DownloadProgress

@Composable
fun SmartInstallButton(
    isDownloading: Boolean,
    isInstalling: Boolean,
    progress: Int?,
    primaryAsset: GithubAsset?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: DetailsState
) {
    val enabled = primaryAsset != null && !isDownloading && !isInstalling

    val animatedProgress by animateFloatAsState(
        targetValue = (progress ?: 0) / 100f,
        animationSpec = tween(durationMillis = 500)
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            if (state.isDownloading || state.downloadStage != DownloadStage.IDLE) {  // Fallback for compat
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when (state.downloadStage) {
                        DownloadStage.PREPARING -> {
                            CircularProgressIndicator(/* small */)
                            Text("Preparing...")
                        }

                        DownloadStage.DOWNLOADING -> {
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                color = MaterialTheme.colorScheme.surfaceContainer
                            )
                            Text("Downloading... ${progress ?: 0}%")
                        }

                        DownloadStage.VERIFYING -> {
                            CircularProgressIndicator(/* small */)
                            Text("Verifying app...")
                        }

                        DownloadStage.INSTALLING -> {
                            CircularProgressIndicator(/* small */)
                            Text("Installing...")
                        }

                        else -> {}  // Idle
                    }
                }
            } else {
                Text(
                    text = if (primaryAsset != null) "Install ${primaryAsset.contentType}" else "Not Available",
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
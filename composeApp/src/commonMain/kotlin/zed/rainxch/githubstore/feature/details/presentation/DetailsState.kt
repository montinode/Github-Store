package zed.rainxch.githubstore.feature.details.presentation

import zed.rainxch.githubstore.core.domain.model.GithubRepoSummary
import zed.rainxch.githubstore.core.domain.model.GithubRelease
import zed.rainxch.githubstore.core.domain.model.GithubAsset
import zed.rainxch.githubstore.feature.details.domain.repository.RepoStats

data class DetailsState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val repository: GithubRepoSummary? = null,
    val latestRelease: GithubRelease? = null,
    val installableAssets: List<GithubAsset> = emptyList(),
    val primaryAsset: GithubAsset? = null,

    val stats: RepoStats? = null,
    val readmeMarkdown: String? = null,

    val installLogs: List<InstallLogItem> = emptyList(),

    // In-app download/install status
    val isDownloading: Boolean = false,
    val downloadProgressPercent: Int? = null,
    val isInstalling: Boolean = false,
    val downloadError: String? = null,
    val installError: String? = null,

    val downloadStage: DownloadStage = DownloadStage.IDLE,
)

data class InstallLogItem(
    val timeIso: String,
    val assetName: String,
    val assetSizeBytes: Long,
    val releaseTag: String,
    val result: String // Started, Success, Error: message
)

enum class DownloadStage {
    IDLE, PREPARING, DOWNLOADING, VERIFYING, INSTALLING
}
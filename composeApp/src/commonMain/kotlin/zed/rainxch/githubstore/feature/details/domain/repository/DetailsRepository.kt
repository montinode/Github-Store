package zed.rainxch.githubstore.feature.details.domain.repository

import zed.rainxch.githubstore.core.domain.model.GithubRelease
import zed.rainxch.githubstore.core.domain.model.GithubRepoSummary

data class RepoStats(
    val stars: Int,
    val forks: Int,
    val openIssues: Int,
    val contributors: Int
)

interface DetailsRepository {
    /**
     * Resolve repository basic info by GitHub numeric repository id.
     */
    suspend fun getRepositoryById(id: Long): GithubRepoSummary

    /**
     * Returns the latest published (non-draft and non-prerelease) release or null when none.
     */
    suspend fun getLatestPublishedRelease(owner: String, repo: String): GithubRelease?

    /**
     * Fetch README markdown (raw) from default branch. Returns null if not available.
     */
    suspend fun getReadme(owner: String, repo: String): String?

    /**
     * Fetch repository statistics needed by the Details screen.
     */
    suspend fun getRepoStats(owner: String, repo: String): RepoStats
}
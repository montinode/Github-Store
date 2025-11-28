package zed.rainxch.githubstore.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepoNetworkModel(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val owner: GithubOwnerNetworkModel,
    val description: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("forks_count") val forksCount: Int,
    val language: String? = null,
    val topics: List<String>? = null,
    @SerialName("releases_url") val releasesUrl: String,
    @SerialName("updated_at") val updatedAt: String
)
package zed.rainxch.githubstore.feature.details.data

interface FileLocationsProvider {
    fun appDownloadsDir(): String
    fun userDownloadsDir(): String
    fun setExecutableIfNeeded(path: String)
}

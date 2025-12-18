package zed.rainxch.githubstore.core.data

import zed.rainxch.githubstore.core.domain.model.SystemPackageInfo

interface PackageMonitor {
    suspend fun isPackageInstalled(packageName: String): Boolean

    suspend fun getInstalledPackageInfo(packageName: String): SystemPackageInfo?

    suspend fun getAllInstalledPackageNames(): Set<String>
}
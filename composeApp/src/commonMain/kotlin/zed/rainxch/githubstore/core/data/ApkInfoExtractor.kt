package zed.rainxch.githubstore.core.data

import zed.rainxch.githubstore.core.domain.model.ApkPackageInfo

interface ApkInfoExtractor {
    suspend fun extractPackageInfo(filePath: String): ApkPackageInfo?
}
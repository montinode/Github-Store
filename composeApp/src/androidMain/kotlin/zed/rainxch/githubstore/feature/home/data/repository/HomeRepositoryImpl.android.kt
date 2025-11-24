package zed.rainxch.githubstore.feature.home.data.repository

class AndroidPlatform : Platform {
    override val type: PlatformType
        get() = PlatformType.ANDROID

}

actual fun getPlatform(): Platform {
    return AndroidPlatform()
}
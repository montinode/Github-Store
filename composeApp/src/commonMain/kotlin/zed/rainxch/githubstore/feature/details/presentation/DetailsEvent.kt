package zed.rainxch.githubstore.feature.details.presentation

sealed interface DetailsEvent {
    data class OnOpenRepositoryInApp(val repositoryId: Int) : DetailsEvent
    data class InstallTrackingFailed(val message: String) : DetailsEvent
    data class OnMessage(val message: String) : DetailsEvent
}
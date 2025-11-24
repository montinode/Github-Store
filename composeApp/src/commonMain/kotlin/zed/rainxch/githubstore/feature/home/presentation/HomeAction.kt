package zed.rainxch.githubstore.feature.home.presentation

import zed.rainxch.githubstore.feature.home.presentation.model.HomeCategory

sealed interface HomeAction {
    data object Refresh : HomeAction
    data object Retry : HomeAction
    data object LoadMore : HomeAction
    data class SwitchCategory(val category: HomeCategory) : HomeAction
}
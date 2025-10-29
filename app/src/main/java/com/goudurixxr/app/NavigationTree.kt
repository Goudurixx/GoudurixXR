package com.goudurixxr.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goudurixxr.ui.screens.Screens
import com.goudurixxr.ui.screens.videoPlayer.videoPlayer.VideoPlayerScreen
import com.goudurixxr.ui.screens.videos.VideoListScreen

@Composable
fun NavigationTree(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Videos(),
        modifier = modifier,
    ) {

        composable(route = Screens.VideoPlayer()) {
            VideoPlayerScreen(
                onBackPressed = navController::popBackStack,
            )
        }
        composable(Screens.Videos()) {
            VideoListScreen(
                onVideoSelected = { video -> navController.openVideoPlayer(video.title) },
            )
        }
    }
}

internal fun NavHostController.openVideoPlayer(videoTitle: String) {
    navigate(
        Screens.VideoPlayer.withArgs(videoTitle)
    )
}


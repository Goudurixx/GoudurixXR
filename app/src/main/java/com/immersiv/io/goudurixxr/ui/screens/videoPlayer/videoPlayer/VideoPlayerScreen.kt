package com.immersiv.io.goudurixxr.ui.screens.videoPlayer.videoPlayer

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardControlKey
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.ContentEdge
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialColumn
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.fillMaxWidth
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.onGloballyPositioned
import androidx.xr.compose.subspace.layout.rotate
import com.immersiv.io.goudurixxr.R
import com.immersiv.io.goudurixxr.data.entities.Video
import com.immersiv.io.goudurixxr.ui.components.BackButton
import com.immersiv.io.goudurixxr.ui.components.Error
import com.immersiv.io.goudurixxr.ui.components.Loading
import com.immersiv.io.goudurixxr.ui.screens.footballField.FootballFieldScreen
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.VideoPlayerControls
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.VideoPlayerOverlay
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.VideoPlayerPulse
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.VideoPlayerPulseState
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.VideoPlayerState
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.rememberVideoPlayerPulseState
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.rememberVideoPlayerState

object VideoPlayerScreen {
    const val MOVIE_ID_BUNDLE_KEY = "movieId"
}

/**
 *
 * @param onBackPressed The callback to invoke when the user presses the back button.
 * @param videoPlayerScreenViewModel The view model for the video player screen.
 */
@Composable
fun VideoPlayerScreen(
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
) {
    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val uiState = uiState) {
        is VideoPlayerScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is VideoPlayerScreenUiState.Error -> {
            Error(modifier = Modifier.fillMaxSize())
        }

        is VideoPlayerScreenUiState.Done -> {
            VideoPlayerScreenContent(
                video = uiState.video,
                player = uiState.player,
                isReadyToPlay = uiState.isReadyToPlay,
                onBackPressed = {
                    uiState.player.clearMediaItems()
                    onBackPressed()
                }
            )
        }
    }

    DisposableEffect(videoPlayerScreenViewModel) {
        videoPlayerScreenViewModel.requestPlayer()
        onDispose {
            videoPlayerScreenViewModel.releasePlayer()
        }
    }
}

@Composable
private fun VideoPlayerScreenContent(
    video: Video,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit
) {
    BackHandler(onBack = onBackPressed)

    if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
        var videoPlayerHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Subspace {
            SpatialColumn(
                modifier = SubspaceModifier.fillMaxWidth().height(900.dp)
            ) {
                SpatialPanel(
                    modifier = SubspaceModifier.onGloballyPositioned {
                        with(density) {
                            videoPlayerHeight = it.size.height.toDp()
                        }
                    }
                ) {
                    VideoPlayer(
                        video = video,
                        player = player,
                        isReadyToPlay = isReadyToPlay,
                        onBackPressed = onBackPressed
                    )
                }
                FootballFieldScreen(
                    showFootballField = true,
                    modifier = SubspaceModifier.offset(y = -videoPlayerHeight / 2).rotate(
                        pitch = 0f,
                        yaw = 90f,
                        roll = 0f,
                    )
                )
            }
        }
    } else {
        VideoPlayer(
            video = video,
            player = player,
            isReadyToPlay = isReadyToPlay,
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(
    video: Video,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit = {}
) {

    val pulseState = rememberVideoPlayerPulseState()

    val videoPlayerState = rememberVideoPlayerState(player = player, hideSeconds = 4)
    val videoSize by videoPlayerState.videoSize.collectAsStateWithLifecycle(null)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isReadyToPlay) {
        if (isReadyToPlay) {
            player.play()
            videoPlayerState.showControls()
        }
    }

    LaunchedEffect(videoPlayerState.isControlsVisible) {
        if (!videoPlayerState.isControlsVisible) {
            focusRequester.tryRequestFocus()
        }
    }

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onClick(player, videoPlayerState),
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            player = player,
            modifier = Modifier
                .resizeWithContentScale(
                    contentScale = ContentScale.Fit,
                    sourceSizeDp = videoSize
                ),
        )
        VideoPlaybackControls(
            video = video,
            player = player,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            onBackPressed = onBackPressed,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .focusRequester(focusRequester)
                .onPreviewKeyEvent {
                    if (videoPlayerState.isControlsVisible) {
                        videoPlayerState.showControls()
                    }
                    false
                }
                .focusGroup()
                .fillMaxSize()
        )
    }
}

@Composable
private fun VideoPlaybackControls(
    video: Video,
    player: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    modifier: Modifier = Modifier,
    isSpatialUiEnabled: Boolean = LocalSpatialCapabilities.current.isSpatialUiEnabled,
    onBackPressed: () -> Unit = {}
) {
    Box(modifier = modifier) {
        if (!isSpatialUiEnabled) {
            VideoPlayerControlsInOverlay(
                video = video,
                player = player,
                videoPlayerState = videoPlayerState,
                videoPlayerPulseState = pulseState,
                onBackPressed = onBackPressed,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .focusGroup()
            )
        } else {
            SpatialVideoPlayerControls(
                video = video,
                player = player,
                videoPlayerState = videoPlayerState,
                modifier = Modifier
                    .focusGroup(),
            )
            VideoPlayerOverlay(
                isControlsVisible = videoPlayerState.isControlsVisible,
                backButton = {
                    BackButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
            Orbiter(
                position = ContentEdge.Bottom
            ) {
                AnimatedVisibility(
                    visible = videoPlayerState.isControlsVisible.not(),
                ) {
                    FilledIconButton(
                        onClick = videoPlayerState::showControls,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(0.5f),
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardControlKey,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpatialVideoPlayerControls(
    video: Video,
    player: Player,
    videoPlayerState: VideoPlayerState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        videoPlayerState.isControlsVisible
    ) {
        Orbiter(
            position = ContentEdge.Bottom,
            offset = 140.dp
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(32.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                    .padding(32.dp)
            ) {
                VideoPlayerControls(
                    video = video,
                    player = player,
                )
            }
        }
    }
}

@Composable
fun VideoPlayerControlsInOverlay(
    video: Video,
    player: Player,
    videoPlayerState: VideoPlayerState,
    videoPlayerPulseState: VideoPlayerPulseState,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    VideoPlayerOverlay(
        modifier = modifier,
        isControlsVisible = videoPlayerState.isControlsVisible,
        centerButton = { VideoPlayerPulse(videoPlayerPulseState) },
        controls = {
            VideoPlayerControls(
                video = video,
                player = player,
            )
        },
        backButton = {
            BackButton(
                onClick = onBackPressed,
                description = stringResource(R.string.back_button),
                modifier = Modifier.padding(8.dp)
            )
        }
    )
}

private fun Modifier.onClick(
    player: Player,
    videoPlayerState: VideoPlayerState,
): Modifier =
    clickable {
        when {
            !videoPlayerState.isControlsVisible -> {
                videoPlayerState.showControls()
            }

            player.isPlaying -> {
                player.pause()
            }

            else -> {
                player.play()
            }
        }
    }

fun FocusRequester.tryRequestFocus(): Result<Unit> {
    return try {
        this.requestFocus()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

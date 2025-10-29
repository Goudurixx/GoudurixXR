package com.goudurixxr.ui.screens.videoPlayer.components.button

import androidx.annotation.OptIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import com.goudurixxr.R

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val state: PlayPauseButtonState = rememberPlayPauseButtonState(player)

    VideoPlayerControlsIcon(
        modifier = modifier,
        icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Default.Pause,
        onClick = state::onClick,
        contentDescription = if (state.showPlay) stringResource(R.string.play_video) else stringResource(R.string.pause_video),
    )
}

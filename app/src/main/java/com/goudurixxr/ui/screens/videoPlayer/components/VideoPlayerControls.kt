package com.goudurixxr.ui.screens.videoPlayer.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.goudurixxr.data.entities.Video
import com.goudurixxr.ui.screens.videoPlayer.components.button.ImmersiveModeButton
import com.goudurixxr.ui.screens.videoPlayer.videoPlayer.tryRequestFocus
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayerControls(
    video: Video,
    player: Player,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.tryRequestFocus()
    }

    val scrollState = rememberScrollState()

    VideoPlayerMainFrame(
        mediaTitle = {
            VideoPlayerMediaTitle(
                title = video.title,
            )
        },
        mediaActions = {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ImmersiveModeButton()
            }
        },
        seeker = {
            VideoPlayerSeeker(
                player = player,
                onSeek = { player.seekTo(player.duration.times(it).toLong()) },
                contentDuration = player.duration.milliseconds,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
    )
}

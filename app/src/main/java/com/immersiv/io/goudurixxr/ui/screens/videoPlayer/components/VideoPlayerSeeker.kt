package com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.media3.common.Player
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components.button.PlayPauseButton
import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.videoPlayer.tryRequestFocus
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VideoPlayerSeeker(
    player: Player,
    contentDuration: Duration,
    modifier: Modifier = Modifier,
    onSeek: (Float) -> Unit = {
        player.seekTo(player.duration.times(it).toLong())
    },
) {
    var contentCurrentPosition by remember(contentDuration) {
        mutableLongStateOf(player.currentPosition)
    }
    val contentProgress = contentCurrentPosition.milliseconds

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = player.currentPosition
        }
    }

    val contentProgressString =
        contentProgress.toComponents { h, m, s, _ ->
            if (h > 0) {
                "$h:${m.padStartWith0()}:${s.padStartWith0()}"
            } else {
                "${m.padStartWith0()}:${s.padStartWith0()}"
            }
        }
    val contentDurationString =
        contentDuration.toComponents { h, m, s, _ ->
            if (h > 0) {
                "$h:${m.padStartWith0()}:${s.padStartWith0()}"
            } else {
                "${m.padStartWith0()}:${s.padStartWith0()}"
            }
        }

    val playPauseButton = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.focusProperties {
            onEnter = {
                playPauseButton.tryRequestFocus()
            }
        }
    ) {
        PlayPauseButton(
            player = player,
            modifier = Modifier.focusRequester(playPauseButton),
        )
        VideoPlayerControllerText(text = contentProgressString)
        VideoPlayerControllerIndicator(
            progress = (contentProgress / contentDuration).toFloat(),
            onSeek = onSeek,
        )
        VideoPlayerControllerText(text = contentDurationString)
    }
}

private fun Number.padStartWith0() = this.toString().padStart(2, '0')

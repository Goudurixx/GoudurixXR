package com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

@Composable
fun VideoPlayerMainFrame(
    mediaTitle: @Composable () -> Unit,
    seeker: @Composable () -> Unit,
    mediaActions: @Composable () -> Unit = {},
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            NonCompactVideoPlayerFrame(
                mediaTitle = mediaTitle,
                seeker = seeker,
                mediaActions = mediaActions,
            )
        }

        else -> {
            CompactVideoPlayerFrame(
                mediaTitle = mediaTitle,
                seeker = seeker,
                mediaActions = mediaActions,
            )
        }
    }
}

@Composable
private fun CompactVideoPlayerFrame(
    mediaTitle: @Composable () -> Unit,
    seeker: @Composable () -> Unit,
    mediaActions: @Composable () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        mediaTitle()
        Spacer(Modifier.height(16.dp))
        seeker()
        Spacer(Modifier.height(8.dp))
        mediaActions()
    }
}

@Composable
private fun NonCompactVideoPlayerFrame(
    mediaTitle: @Composable () -> Unit,
    seeker: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    mediaActions: @Composable () -> Unit = {},
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(Modifier.weight(1f)) { mediaTitle() }
            mediaActions()
        }
        Spacer(Modifier.height(16.dp))
        seeker()
    }
}

@Preview(device = "id:tv_4k")
@Composable
private fun MediaPlayerMainFramePreviewLayout() {
    VideoPlayerMainFrame(
        mediaTitle = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(64.dp)
            )
        },
        mediaActions = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(196.dp, 40.dp)
            )
        },
        seeker = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },
    )
}

@Preview(device = "id:tv_4k")
@Composable
private fun MediaPlayerMainFramePreviewLayoutWithoutMore() {
    VideoPlayerMainFrame(
        mediaTitle = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(64.dp)
            )
        },
        mediaActions = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(196.dp, 40.dp)
            )
        },
        seeker = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },
    )
}

@Preview
@Composable
private fun MediaPlayerMainFramePreviewLayoutOnPhone() {
    VideoPlayerMainFrame(
        mediaTitle = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(64.dp)
            )
        },
        mediaActions = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .size(196.dp, 40.dp)
            )
        },
        seeker = {
            Box(
                Modifier
                    .border(2.dp, Color.Red)
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        },
    )
}

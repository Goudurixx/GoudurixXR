package com.goudurixxr.ui.screens.videoPlayer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goudurixxr.ui.theme.GoudurixXRTheme

/**
 * Handles the visibility and animation of the controls.
 */
@Composable
fun VideoPlayerOverlay(
    modifier: Modifier = Modifier,
    isControlsVisible: Boolean = false,
    centerButton: @Composable () -> Unit = {},
    controls: @Composable () -> Unit = {},
    backButton: @Composable () -> Unit = {},
) {
    Box {
        AnimatedVisibility(isControlsVisible, Modifier, fadeIn(), fadeOut()) {
            Box(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                )
            ) {
                backButton()
            }
        }
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(isControlsVisible, Modifier, fadeIn(), fadeOut()) {
                CinematicScrim(Modifier.fillMaxSize())
            }

            Column {
                AnimatedVisibility(
                    isControlsVisible,
                    Modifier,
                    slideInVertically { it },
                    slideOutVertically { it }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 32.dp
                            )
                    ) {
                        controls()
                    }
                }
            }
            centerButton()
        }
    }
}

@Preview(device = "id:tv_4k")
@Composable
private fun VideoPlayerOverlayPreview() {
    GoudurixXRTheme() {
        Box(Modifier.fillMaxSize()) {
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                controls = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Blue)
                    )
                },
                centerButton = {
                    Box(
                        Modifier
                            .size(88.dp)
                            .background(Color.Green)
                    )
                },
                backButton = {
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.Cyan)
                    )
                }
            )
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun VideoPlayerOverlayPreviewForPhone() {
    GoudurixXRTheme() {
        Box(Modifier.fillMaxSize()) {
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                controls = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Blue)
                    )
                },
                centerButton = {
                    Box(
                        Modifier
                            .size(88.dp)
                            .background(Color.Green)
                    )
                },
                backButton = {
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.Cyan)
                    )
                }
            )
        }
    }
}

@Composable
fun CinematicScrim(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color.Black.copy(alpha = 0.1f),
        Color.Black.copy(alpha = 0.8f)
    )
) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(colors)
        )
    )
}


package com.goudurixxr.ui.screens.videoPlayer.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.runtime.Session
import com.goudurixxr.R

@Composable
fun ImmersiveModeButton(
    modifier: Modifier = Modifier,
    xrSession: Session? = LocalSession.current,
) {
    if (xrSession != null) {
        ActualImmersiveButton(
            modifier = modifier
        )
    }
}

@Composable
private fun ActualImmersiveButton(
    modifier: Modifier = Modifier,
) {
    val isSpatialUiEnabled = LocalSpatialCapabilities.current.isSpatialUiEnabled
    val spatialConfiguration = LocalSpatialConfiguration.current

    val icon = if (isSpatialUiEnabled) {
        Icons.Default.FullscreenExit
    } else {
        Icons.Default.Fullscreen
    }
    val description = if (isSpatialUiEnabled) {
        R.string.home_space_mode
    } else {
        R.string.full_space_mode
    }

    VideoPlayerControlsIcon(
        modifier = modifier,
        icon = icon,
        contentDescription = stringResource(description),
        onClick = {
            if (isSpatialUiEnabled) {
                spatialConfiguration.requestHomeSpaceMode()
            } else {
                spatialConfiguration.requestFullSpaceMode()
            }
        },
    )
}

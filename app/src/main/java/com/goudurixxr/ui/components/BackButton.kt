package com.goudurixxr.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.goudurixxr.ui.screens.videoPlayer.components.button.VideoPlayerControlsIcon


@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    description: String? = null,
    isRequired: Boolean = true
) {
    if (isRequired) {
        VideoPlayerControlsIcon(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            contentDescription = description,
        )
    }
}
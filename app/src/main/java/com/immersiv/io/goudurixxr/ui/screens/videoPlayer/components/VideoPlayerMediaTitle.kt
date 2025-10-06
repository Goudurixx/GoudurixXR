
package com.immersiv.io.goudurixxr.ui.screens.videoPlayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.immersiv.io.goudurixxr.ui.theme.GoudurixXRTheme

enum class VideoPlayerMediaTitleType { AD, LIVE, DEFAULT }

@Composable
fun VideoPlayerMediaTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
    }
}

@Preview(name = "TV Series", device = "id:tv_4k")
@Composable
private fun VideoPlayerMediaTitlePreviewSeries() {
    GoudurixXRTheme() {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "True Detective",
            )
        }
    }
}

@Preview(name = "Live", device = "id:tv_4k")
@Composable
private fun VideoPlayerMediaTitlePreviewLive() {
    GoudurixXRTheme {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "MacLaren Reveal Their 2022 Car: The MCL36",
            )
        }
    }
}

@Preview(name = "Ads", device = "id:tv_4k")
@Composable
private fun VideoPlayerMediaTitlePreviewAd() {
    GoudurixXRTheme {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "Samsung Galaxy Note20 | Ultra 5G",
            )
        }
    }
}

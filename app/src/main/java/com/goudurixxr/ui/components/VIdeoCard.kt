package com.goudurixxr.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.goudurixxr.R
import com.goudurixxr.data.entities.Video
import com.goudurixxr.ui.theme.GoudurixXRTheme

@Composable
fun VideoCard(
    video: Video,
    onClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier.clickable(
            onClick = { onClick(video) }
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Gray.copy(alpha = 0.7f)
        )
    ) {
        AsyncImage(
            model = video.thumbnail,
            contentDescription = video.title,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder_image),
            modifier = Modifier.aspectRatio(1f)
        )
        Text(
            text = video.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

@Preview
@Composable
private fun PreviewVideoCard() {
    GoudurixXRTheme() {
        VideoCard(
            video = Video(
                title = "Preview video",
                thumbnail = "",
                url = ""
            ),
            onClick = {}
        )
    }
}

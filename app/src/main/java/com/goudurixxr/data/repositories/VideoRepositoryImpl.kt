package com.goudurixxr.data.repositories

import com.goudurixxr.data.entities.Logo
import com.goudurixxr.data.entities.Video
import com.goudurixxr.data.network.VideoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val videoService: VideoService,
) : VideoRepository {
    private var videos = MutableStateFlow(emptyList<Video>())
    private var logos = MutableStateFlow(emptyList<Logo>())

    override fun getVideos(): Flow<List<Video>> = flow {
        emit(videos.value)
        logos.update {
            videoService.fetchLogos()
        }
        videos.update {
            val videoList = videoService.fetchVideos() +
                Video(
                    title = "Big Buck Bunny https mp4",
                    thumbnail = "https://peach.blender.org/wp-content/themes/bf-bunny/graphics/header1.jpg",
                    url = "https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4"
                ) +
                Video(
                    title = "Big Buck Bunny MP4",
                    thumbnail = "https://cdn-icons-png.flaticon.com/512/2611/2611310.png",
                    url = "asset:///bbb_320x180.mp4"
                ) +
                Video(
                    title = "Random HLS video",
                    thumbnail = "https://bitmovin.com/wp-content/uploads/2015/08/apple_hls_6401.jpg",
                    url = "https://ireplay.tv/test/blender.m3u8"
                )  //ADDITIONAL Videos to display how it goes with less intensive video flux
            videoList.filter {
                it.url.contains("https") && it.channel != null
            }.map {
                it.copy(
                    thumbnail = logos.value.firstOrNull { logo ->
                        it.channel == logo.channel
                    }?.url ?: it.thumbnail
                )
            }
        }
        emit(videos.value)
    }

    override fun getVideo(title: String): Video =
        videos.value.first { it.title == title }

}
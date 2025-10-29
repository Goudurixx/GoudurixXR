package com.goudurixxr.data.repositories

import com.goudurixxr.data.entities.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(): Flow<List<Video>>
    fun getVideo(title: String): Video
}
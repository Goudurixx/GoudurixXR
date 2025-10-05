package com.immersiv.io.goudurixxr.data.repositories

import com.immersiv.io.goudurixxr.data.entities.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(): Flow<List<Video>>
    fun getVideo(title: String): Video
}
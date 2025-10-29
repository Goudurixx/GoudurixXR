package com.goudurixxr.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val channel: String? = null,

    val url: String, // This is the HLS stream URL

    val referrer: String? = null,

    val userAgent: String? = null,

    val timeshift: String? = null,

    val title: String,

    val quality: String? = null,

    val thumbnail: String = "https://www.shutterstock.com/image-photo/tv-test-card-rainbow-bars-600nw-611065304.jpg",
)

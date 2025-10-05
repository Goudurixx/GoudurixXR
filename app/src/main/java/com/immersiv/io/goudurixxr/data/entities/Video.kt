package com.immersiv.io.goudurixxr.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val title: String,
    val thumbnail: String,
    val url: String,
)

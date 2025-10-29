package com.goudurixxr.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    val channel: String,
    val feed: String? = null,
    val tags: List<String>,
    val width: Float,
    val height: Float,
    val format: String? = null,
    val url: String
)
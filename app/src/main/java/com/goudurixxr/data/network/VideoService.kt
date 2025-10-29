package com.goudurixxr.data.network

import android.util.Log
import com.goudurixxr.BuildConfig
import com.goudurixxr.data.entities.Video
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Logger for the Ktor client.
 */
@Singleton
class HttpLogger : Logger {
    override fun log(message: String) {
        Log.d("HttpLogger", message)
    }
}

/**
 * Ktor client for the Video API.
 */
class VideoService @Inject constructor() {

    private val endpoint = API_URL

    private val httpClient = HttpClient {
        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT
            requestTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }

        install(Logging) {
            logger = HttpLogger()
            level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
        }

        install(HttpCookies)

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        defaultRequest {
            url(API_URL)
            header("Accept", "*/*")
        }
    }

    private fun createVideosUrl(): Url = with(URLBuilder(endpoint)) {
        appendPathSegments("streams.json")
        build()
    }

    suspend fun fetchVideos(): List<Video> {
        return httpClient
            .get(
                url = createVideosUrl(),
                block = { header(HttpHeaders.ContentType, ContentType.Application.Json) }
            )
            .body()
    }

    companion object {
        private const val API_URL = "https://iptv-org.github.io/api/"
        private const val TIMEOUT = 10000L
    }
}
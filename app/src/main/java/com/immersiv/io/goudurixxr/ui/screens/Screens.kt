package com.immersiv.io.goudurixxr.ui.screens

import com.immersiv.io.goudurixxr.ui.screens.videoPlayer.videoPlayer.VideoPlayerScreen

enum class Screens(
    private val args: List<String>? = null,
    private val isMainNavigation: Boolean = false
) {

    Videos(isMainNavigation = true),

    VideoPlayer(
        listOf(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY),
    );

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }
}
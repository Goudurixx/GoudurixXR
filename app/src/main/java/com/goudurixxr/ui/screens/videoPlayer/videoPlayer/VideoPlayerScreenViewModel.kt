package com.goudurixxr.ui.screens.videoPlayer.videoPlayer

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.goudurixxr.PlaybackService
import com.goudurixxr.data.entities.Video
import com.goudurixxr.data.repositories.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: VideoRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val player = MutableStateFlow<Player?>(null)
    private val sessionToken = MutableStateFlow<SessionToken?>(null)

    private val isReadyToPlay = MutableStateFlow(false)
    private val currentMediaIem = MutableStateFlow<MediaItem?>(null)

    private val video = savedStateHandle
        .getStateFlow<String?>(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY, null)
        .map {
            if (it != null) {
                Log.e("VM", "$it")
                repository.getVideo(title = it)
            } else {
                null
            }
        }

    val uiState = combine(
        video,
        player,
        isReadyToPlay,
        currentMediaIem
    ) { details, player, isReady, current ->
        Log.e("VM", "details $details")
        Log.e("VM", "player $player")
        Log.e("VM", "isReady $isReady")
        Log.e("VM", "current $current")
        when {
            details == null -> VideoPlayerScreenUiState.Error
            player == null -> VideoPlayerScreenUiState.Loading
            player.mediaItemCount == 0 -> {
                preparePlayer(player, details)
                VideoPlayerScreenUiState.Loading
            }

            else -> {
                val detailsForCurrent = movieDetailsForMediaItem(current) ?: details
                VideoPlayerScreenUiState.Done(
                    video = detailsForCurrent,
                    player = player,
                    isReadyToPlay = isReady,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = VideoPlayerScreenUiState.Loading
    )

    @OptIn(UnstableApi::class)
    fun requestPlayer() {
        player.value?.release()
        createMediaController { createdPlayer ->

            createdPlayer.addListener(
                object : Player.Listener {
                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        isReadyToPlay.tryEmit(createdPlayer.isReadyToPlay())
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        currentMediaIem.tryEmit(mediaItem)
                        isReadyToPlay.tryEmit(false)
                    }
                }
            )
            player.tryEmit(createdPlayer)
        }
    }

    fun releasePlayer() {
        viewModelScope.launch {
            Log.d("ViewModel", "starting to release the player")
            player.value?.let { activePlayer ->
                // 1. Explicitly stop playback. This command is sent to the session.
                if (activePlayer.isPlaying) {
                    activePlayer.stop()
                }

                // 2. Clear the media items from the player's playlist.
                activePlayer.clearMediaItems()

                // 3. Release the controller, disconnecting the ViewModel from the service.
                activePlayer.release()
            }

            // 4. Update the local state to null to reflect the release.
            player.value = null
            Log.d("ViewModel", "Player released and disconnected.")
            val l = player.tryEmit(null)
            Log.e("VM", "TryEmit : $l")
        }
    }

    private fun createMediaController(
        onControllerCreated: (MediaController) -> Unit
    ) {
        sessionToken.update {
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        }
        val future = sessionToken.value?.let { MediaController.Builder(context, it) }?.buildAsync()
        future?.addListener(
            {
                onControllerCreated(future.get())
            },
            context.mainExecutor
        )
    }

    private fun preparePlayer(player: Player, video: Video) {
        if (player.isPlaying) {
            player.stop()
        }
        player.clearMediaItems()
        player.addMediaItem(video.intoMediaItem())

        player.prepare()
    }

    private suspend fun movieDetailsForMediaItem(mediaItem: MediaItem?): Video? {
        return if (mediaItem != null) {
            repository.getVideo(mediaItem.mediaId)
        } else {
            null
        }
    }
}

private fun MediaController.isReadyToPlay(): Boolean {
    return playWhenReady && contentDuration > 0
}

@Immutable
sealed class VideoPlayerScreenUiState {
    data object Loading : VideoPlayerScreenUiState()
    data object Error : VideoPlayerScreenUiState()
    data class Done(
        val video: Video,
        val player: Player,
        val isReadyToPlay: Boolean
    ) : VideoPlayerScreenUiState()
}


private fun Video.intoMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(title)
        .setUri(url)
        .setMediaMetadata(intoMediaMetaData())
        .build()
}

private fun Video.intoMediaMetaData(): MediaMetadata {
    return MediaMetadata.Builder()
        .setTitle(title)
        .setMediaType(MediaMetadata.MEDIA_TYPE_MOVIE)
        .setArtworkUri(thumbnail.toUri())
        .build()
}

package com.immersiv.io.goudurixxr.ui.screens.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immersiv.io.goudurixxr.core.Result
import com.immersiv.io.goudurixxr.core.asResultWithLoading
import com.immersiv.io.goudurixxr.data.entities.Video
import com.immersiv.io.goudurixxr.data.repositories.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
) : ViewModel() {

    val uiState: StateFlow<VideoListScreenUiState> = videoRepository.getVideos().asResultWithLoading().map {
        when (it) {
            is Result.Error -> {
                VideoListScreenUiState.Error(it.exception.localizedMessage ?: "Unknown error")
            }

            Result.Loading -> {
                VideoListScreenUiState.Loading
            }

            is Result.Success<List<Video>> -> {
                VideoListScreenUiState.Done(it.data)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VideoListScreenUiState.Loading
    )
}


sealed interface VideoListScreenUiState {
    object Loading : VideoListScreenUiState
    data class Error(val message: String) : VideoListScreenUiState
    data class Done(val videoList: List<Video>) : VideoListScreenUiState
}

package com.immersiv.io.goudurixxr.ui.screens.videos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialColumn
import androidx.xr.compose.subspace.SpatialCurvedRow
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SubspaceComposable
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.fillMaxWidth
import androidx.xr.compose.subspace.layout.widthIn
import com.immersiv.io.goudurixxr.data.entities.Video
import com.immersiv.io.goudurixxr.ui.components.Error
import com.immersiv.io.goudurixxr.ui.components.Loading
import com.immersiv.io.goudurixxr.ui.components.MainTitle
import com.immersiv.io.goudurixxr.ui.components.VideoCard


@Composable
fun VideoListScreen(
    onVideoSelected: (Video) -> Unit,
    videoListViewModel: VideoListViewModel = hiltViewModel(),
) {

    val uiState by videoListViewModel.uiState.collectAsStateWithLifecycle()

    if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {

        Subspace {
            SpatialVideoListContent(
                uiState = uiState,
                onVideoSelected = onVideoSelected,
                modifier = SubspaceModifier,
            )
        }
    } else {
        VideoListContent(
            uiState = uiState,
            onVideoSelected = onVideoSelected,
            modifier = Modifier,
        )
    }
}


@Composable
private fun VideoListContent(
    uiState: VideoListScreenUiState,
    onVideoSelected: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        when (uiState) {
            is VideoListScreenUiState.Loading -> {
                Loading(
                    modifier = Modifier
                )
            }

            is VideoListScreenUiState.Error -> {
                Error(
                    modifier = Modifier,
                    additionnalMessage = uiState.message,
                )
            }

            is VideoListScreenUiState.Done -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MainTitle()
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = 132.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.videoList) { video ->
                            VideoCard(
                                video = video,
                                onClick = onVideoSelected,
                                modifier = Modifier
                                    .padding(8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@SubspaceComposable
private fun SpatialVideoListContent(
    uiState: VideoListScreenUiState,
    onVideoSelected: (Video) -> Unit,
    modifier: SubspaceModifier = SubspaceModifier,
) {

    SpatialColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        SpatialPanel(
            shape = SpatialRoundedCornerShape(CornerSize(16.dp))
        ) {
            MainTitle()
        }
        when (uiState) {
            is VideoListScreenUiState.Loading -> {
                SpatialPanel() {
                    Loading(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            is VideoListScreenUiState.Error -> {
                SpatialPanel() {
                    Error(
                        modifier = Modifier,
                        additionnalMessage = uiState.message,
                    )
                }
            }

            is VideoListScreenUiState.Done -> {
                SpatialCurvedRow(
                    modifier = SubspaceModifier
                ) {
                    uiState.videoList.forEachIndexed { index, video ->
                        SpatialPanel(
                            modifier = SubspaceModifier.widthIn(max = 200.dp)
                        ) {
                            VideoCard(
                                video = video,
                                onClick = onVideoSelected,
                                modifier = Modifier
                                    .padding(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.immersiv.io.goudurixxr.ui.screens.footballField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.ExperimentalSubspaceVolumeApi
import androidx.xr.compose.subspace.SceneCoreEntity
import androidx.xr.compose.subspace.SubspaceComposable
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.scale
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity


// Bugdroid glb height in meters
private const val footballFieldLength = 2.08f

// The desired amount of the available layout height to use for the bugdroid
private const val fillRatio = 0.5f


@OptIn(ExperimentalSubspaceVolumeApi::class)
@Composable
@SubspaceComposable
fun FootballFieldScreen(
    showFootballField: Boolean,
    viewModel: FootballFieldScreenViewModel = hiltViewModel(),
    modifier: SubspaceModifier = SubspaceModifier
) {
    if (showFootballField) {
        val xrSession = checkNotNull(LocalSession.current)
        // Load the GltfModel data before creating the entity.
        var gltfModel by remember { mutableStateOf<GltfModel?>(null) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            if (gltfModel == null) {
                gltfModel = viewModel.getOrLoadModel(xrSession, context)
            }
        }

        gltfModel?.let { gltfModel ->
            Subspace {
                var scale by remember { mutableFloatStateOf(0.05f) }
                SceneCoreEntity(
                    factory = {
                        GltfModelEntity.create(xrSession, gltfModel).also { entity ->
                            entity.startAnimation(
                                loop = true, animationName = "Armature|Take 001|BaseLayer"
                            )
                        }
                    },
                    modifier = modifier.scale(scale).resizable()
                )
            }
        }
    }

    // Clean up the cache when the composable leaves the composition.
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearCache()
        }
    }
}

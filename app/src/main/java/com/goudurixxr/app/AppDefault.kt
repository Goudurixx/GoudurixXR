package com.goudurixxr.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.platform.SpatialConfiguration
import androidx.xr.compose.spatial.ContentEdge
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.subspace.SpatialBox
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SubspaceComposable
import androidx.xr.compose.subspace.layout.SpatialAlignment
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.width
import androidx.xr.runtime.Session
import com.goudurixxr.R

@OptIn(ExperimentalMaterial3XrApi::class)
@Composable
fun AppDefault(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val xrSession = LocalSession.current
    val isSpatialUiEnabled = LocalSpatialCapabilities.current.isSpatialUiEnabled
    val spatialConfiguration = LocalSpatialConfiguration.current


    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FullSpaceModeToggle(
                xrSession = xrSession,
                spatialConfiguration = spatialConfiguration,
                isSpatialUiEnabled = isSpatialUiEnabled
            )
        }
    ) { padding ->
        AnimatedVisibility(
            true
        ) {
            NavigationTree(
                navController = navController,
                modifier = Modifier.padding(paddingValues = padding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3XrApi::class)
@SubspaceComposable
@Composable
fun SpatialApp(
    navController: NavHostController,
    modifier: SubspaceModifier = SubspaceModifier,
) {
    val topBar = remember { FocusRequester() }
    val xrSession = LocalSession.current
    val isSpatialUiEnabled = LocalSpatialCapabilities.current.isSpatialUiEnabled
    val spatialConfiguration = LocalSpatialConfiguration.current


    SpatialBox(modifier) {
        SpatialPanel(
            modifier = SubspaceModifier.align(
                alignment = SpatialAlignment.Center
            )
                .width(1280.dp)
                .height(800.dp)
                .resizable()
                .movable(),
        ) {
            AnimatedVisibility(
                true
            ) {
                NavigationTree(
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
        Orbiter(
            position = ContentEdge.End
        ) {
            FullSpaceModeToggle(
                xrSession = xrSession,
                spatialConfiguration = spatialConfiguration,
                isSpatialUiEnabled = isSpatialUiEnabled
            )
        }
    }
}

@Composable
fun FullSpaceModeToggle(
    xrSession: Session?,
    spatialConfiguration: SpatialConfiguration,
    isSpatialUiEnabled: Boolean,
) {

    if (xrSession != null) {
        val icon = if (isSpatialUiEnabled) {
            R.drawable.ic_home_space_mode_switch
        } else {
            R.drawable.ic_full_space_mode_switch
        }
        val description = if (isSpatialUiEnabled) {
            R.string.home_space_mode
        } else {
            R.string.full_space_mode
        }

        IconButton(
            onClick = {
                if (isSpatialUiEnabled) {
                    spatialConfiguration.requestHomeSpaceMode()
                } else {
                    spatialConfiguration.requestFullSpaceMode()
                }
            },
            content = {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(description),
                    tint = MaterialTheme.colorScheme.primary
                )
            })
    }
}
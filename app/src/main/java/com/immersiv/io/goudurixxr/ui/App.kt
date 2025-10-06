package com.immersiv.io.goudurixxr.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import com.immersiv.io.goudurixxr.app.AppDefault
import com.immersiv.io.goudurixxr.app.SpatialApp

@Composable
fun App(
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
        Subspace {
            SpatialApp(
                navController = navController,
                modifier = modifier,
            )
        }
    } else {
        AppDefault(
            navController = navController,
            modifier = modifier,
        )
    }
}
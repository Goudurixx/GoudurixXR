package com.goudurixxr.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import com.goudurixxr.app.AppDefault
import com.goudurixxr.app.SpatialApp

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
            )
        }
    } else {
        AppDefault(
            navController = navController,
            modifier = modifier,
        )
    }
}
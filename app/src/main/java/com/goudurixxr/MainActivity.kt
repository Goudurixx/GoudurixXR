package com.goudurixxr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.Modifier
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import com.goudurixxr.ui.App
import com.goudurixxr.ui.theme.GoudurixXRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3XrApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GoudurixXRTheme {
                EnableXrComponentOverrides {
                    Surface {
                        App(
                            onActivityBackPressed = onBackPressedDispatcher::onBackPressed,
                            modifier = Modifier
                                .safeDrawingPadding()
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                                        LocalAbsoluteTonalElevation.current
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
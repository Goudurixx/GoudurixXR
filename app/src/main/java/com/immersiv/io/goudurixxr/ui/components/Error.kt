package com.immersiv.io.goudurixxr.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.immersiv.io.goudurixxr.R

@Composable
fun Error(
    modifier: Modifier = Modifier,
    additionnalMessage: String? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = stringResource(R.string.something_went_wrong),
            )
            additionnalMessage?.let { message ->
                Text(
                    text = message,
                )
            }
        }
    }
}

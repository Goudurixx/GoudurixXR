package com.immersiv.io.goudurixxr.ui.screens.footballField

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.xr.runtime.Session
import com.immersiv.io.goudurixxr.FootballFieldGltfModelCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FootballFieldScreenViewModel @Inject constructor(
    private val footballFieldGltfModelCache: FootballFieldGltfModelCache
) : ViewModel() {

    suspend fun getOrLoadModel(xrSession: Session, context: Context) =
        footballFieldGltfModelCache.getOrLoadModel(xrSession, context)

    fun clearCache() =
        footballFieldGltfModelCache.clearCache()

}
package com.immersiv.io.goudurixxr

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.xr.runtime.Session
import androidx.xr.scenecore.GltfModel
import com.immersiv.io.goudurixxr.R
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Singleton object to cache the GltfModel.
 */
@Singleton
class FootballFieldGltfModelCache @Inject constructor() {
    private var cachedModel: GltfModel? = null

    @SuppressLint("RestrictedApi")
    suspend fun getOrLoadModel(
        xrCoreSession: Session,
        context: Context,
    ): GltfModel? {
        return if (cachedModel == null) {
            try {
                val inputStream: InputStream =
                    context.resources.openRawResource(R.raw.football_field)
                cachedModel = GltfModel.create(
                    xrCoreSession, inputStream.readBytes(), "FOOTBALL_FIELD"
                )
                cachedModel
            } catch (e: Exception) {
                Log.e(TAG, "Error loading GLTF model", e)
                null
            }
        } else {
            cachedModel
        }
    }

    fun clearCache() {
        cachedModel = null
    }

    companion object {
        const val TAG = "FootballFieldGltfModelCache"
    }
}
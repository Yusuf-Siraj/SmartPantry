package com.example.smartpantryai

import android.content.Context
import android.util.Log
import ai.onnxruntime.OnnxJavaType
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import java.io.File
import java.io.FileOutputStream
import java.nio.LongBuffer

object OnDeviceRecipeGenerator {

    private lateinit var env: OrtEnvironment
    private lateinit var session: OrtSession
    private var initializedSuccessfully = false
    private const val TAG = "RecipeGenerator"
    private const val MODEL_ASSET_PATH = "models/flan_t5_small.onnx"
    private const val MODEL_FILE_NAME_IN_CACHE = "flan_t5_small_cached.onnx"

    private fun getModelPath(context: Context): String {
        val modelFile = File(context.cacheDir, MODEL_FILE_NAME_IN_CACHE)
        if (!modelFile.exists()) {
            context.assets.open(MODEL_ASSET_PATH).use { input ->
                FileOutputStream(modelFile).use { output ->
                    input.copyTo(output)
                }
            }
            Log.i(TAG, "Model copied to cache.")
        }
        return modelFile.absolutePath
    }

    fun init(context: Context) {
        if (initializedSuccessfully) return
        try {
            env = OrtEnvironment.getEnvironment()
            val modelPath = getModelPath(context)
            session = env.createSession(modelPath)
            initializedSuccessfully = true
            Log.i(TAG, "ONNX model initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ONNX", e)
            initializedSuccessfully = false
        }
    }

    fun isReady(): Boolean = initializedSuccessfully

    fun generateRecipe(inputIds: LongArray): String {
        if (!isReady()) return "Error: ONNX not initialized"
        if (inputIds.isEmpty()) return "Error: Input IDs empty"

        return try {
            val shape = longArrayOf(1, inputIds.size.toLong())
            OnnxTensor.createTensor(env, LongBuffer.wrap(inputIds), shape).use { tensor ->
                val inputs = mapOf("input_ids" to tensor)
                session.run(inputs).use { results ->
                    val outputTensor = results[0].value as Array<LongArray>
                    val rawIds = outputTensor[0]
                    "Raw output IDs: ${rawIds.contentToString()}"
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating recipe", e)
            "Error generating recipe: ${e.message}"
        }
    }

    fun close() {
        if (::session.isInitialized) session.close()
        if (::env.isInitialized) env.close()
        initializedSuccessfully = false
    }
}

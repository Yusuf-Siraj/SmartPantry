package com.example.smartpantryai

import android.content.Context
import android.util.Log
import ai.djl.sentencepiece.SpTokenizer

class SentencePieceTokenizer(
    private val context: Context,
    private val modelAssetFileName: String
) {
    private var spTokenizer: SpTokenizer? = null
    private val TAG = "SentencePieceTokenizer"

    fun initialize() {
        try {
            context.assets.open(modelAssetFileName).use { inputStream ->
                spTokenizer = SpTokenizer(inputStream)
            }
            Log.i(TAG, "Tokenizer initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize tokenizer", e)
        }
    }

    fun isReady(): Boolean = spTokenizer != null

    fun encode(text: String): LongArray {
        val tokenizer = spTokenizer ?: return LongArray(0)
        return try {
            tokenizer.encodeToLong(text)
        } catch (e: Exception) {
            Log.e(TAG, "Encoding failed", e)
            LongArray(0)
        }
    }

    fun decode(ids: LongArray): String {
        val tokenizer = spTokenizer ?: return ""
        return try {
            tokenizer.decode(ids)
        } catch (e: Exception) {
            Log.e(TAG, "Decoding failed", e)
            ""
        }
    }
}

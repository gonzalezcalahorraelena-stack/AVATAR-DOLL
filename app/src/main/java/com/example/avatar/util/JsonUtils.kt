package com.example.avatar.util

import com.example.avatar.model.AmbientLighting
import com.example.avatar.model.ArtStyle
import com.example.avatar.model.AvatarConfig
import com.example.avatar.model.BodyType
import com.example.avatar.model.EyeExpression
import com.example.avatar.model.Gender
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtils {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(AvatarConfig::class.java)

    fun toJson(config: AvatarConfig): String {
        return try {
            adapter.indent("  ").toJson(config)
        } catch (e: Exception) {
            e.printStackTrace()
            "{}"
        }
    }

    fun fromJson(jsonStr: String): AvatarConfig? {
        return try {
            adapter.fromJson(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

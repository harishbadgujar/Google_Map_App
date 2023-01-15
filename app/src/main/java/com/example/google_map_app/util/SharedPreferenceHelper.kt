package com.example.google_map_app.util

import android.content.Context
import android.content.SharedPreferences
import com.example.google_map_app.BuildConfig

class SharedPreferenceHelper  constructor(
     context: Context
) {

    companion object {
        private const val PREF_TOKEN = "token"
        const val LOCATION_DATA="LOCATION_DATA"
    }


    private val context: Context = context.applicationContext

    @Volatile
    private var sharedPref: SharedPreferences? = null

    private fun getSharedPerf(): SharedPreferences {
        return sharedPref ?: synchronized(this) {
            context.getSharedPreferences(
                "${BuildConfig.APPLICATION_ID}.main",
                Context.MODE_PRIVATE
            )
        }
    }

    fun reset() {
        getSharedPerf().edit().clear().apply()
    }

    fun <T> putValue(key: String, value: T) {
        val editor = getSharedPerf().edit()
        when (value) {
            is String -> editor.putString(key, value as String).apply()
            is Int -> editor.putInt(key, value as Int).apply()
            is Float -> editor.putFloat(key, value as Float).apply()
            is Boolean -> editor.putBoolean(key, value as Boolean).apply()
            is Long -> editor.putLong(key, value as Long).apply()
        }
    }

    fun getString(key: String): String? {
        return getSharedPerf().getString(key, "")
    }

    fun getLong(key: String): Long? {
        return getSharedPerf().getLong(key, 0L)
    }

    fun getFloat(key: String): Float? {
        return getSharedPerf().getFloat(key, 0f)
    }

    fun getBoolWithFalse(key: String, default: Boolean = false): Boolean? {
        return getSharedPerf().getBoolean(key, default)
    }
    fun getBoolWithFalsee(key: String, default: Boolean = false): Boolean {
        return getSharedPerf().getBoolean(key, default)
    }
    fun getBoolWithTrue(key: String, default: Boolean = true): Boolean? {
        return getSharedPerf().getBoolean(key, default)
    }

    fun getInt(key: String): Int? {
        return getSharedPerf().getInt(key, 0)
    }

    fun getIntFromString(key: String): Int? {
        return Integer.parseInt(getSharedPerf().getString(key, "0")!!)
    }

    fun clear() {
        getSharedPerf().edit().clear().apply()
    }
}
package org.swm.att.data.remote.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class AttEncryptedPrefDataSource @Inject constructor(
    private val attEncryptedPref: SharedPreferences
) {

    fun getAccessToken(
        preferenceKey: PreferenceKey
    ): String {
        val key = preferenceKey.name
        return attEncryptedPref.getString(key, "") as String
    }

    fun setAccessToken(
        preferenceKey: PreferenceKey,
        value: String
    ) {
        val key = preferenceKey.name
        attEncryptedPref.edit().putString(key, value).apply()
    }

    fun getStoreId(
        preferenceKey: PreferenceKey
    ): Int {
        val key = preferenceKey.name
        return attEncryptedPref.getInt(key, -1)
    }

    fun setStoreId(
        preferenceKey: PreferenceKey,
        value: Int
    ) {
        val key = preferenceKey.name
        attEncryptedPref.edit().putInt(key, value).apply()
    }

    companion object {
        enum class PreferenceKey {
            ACCESS_TOKEN,
            REFRESH_TOKEN,
            STORE_ID
        }
    }
}
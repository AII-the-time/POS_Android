package org.swm.att.data.remote.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class AttEncryptedPrefDataSource @Inject constructor(
    private val attEncryptedPref: SharedPreferences
) {

    fun getToken(
        preferenceKey: PreferenceKey
    ): String? {
        val key = preferenceKey.name
        return attEncryptedPref.getString(key, null)
    }

    fun setToken(
        preferenceKey: PreferenceKey,
        value: String
    ) {
        val key = preferenceKey.name
        attEncryptedPref.edit().putString(key, value).commit()
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
        attEncryptedPref.edit().putInt(key, value).commit()
    }

    fun logout() {
        attEncryptedPref.edit().apply {
            remove(PreferenceKey.ACCESS_TOKEN.name).apply()
            remove(PreferenceKey.REFRESH_TOKEN.name).apply()
            remove(PreferenceKey.STORE_ID.name).apply()
        }.apply()
    }

    companion object {
        enum class PreferenceKey {
            ACCESS_TOKEN,
            REFRESH_TOKEN,
            STORE_ID
        }
    }
}
package org.swm.att.data.remote.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class AttEncryptedPrefDataSource @Inject constructor(
    private val attEncryptedPref: SharedPreferences
) {

    fun getStrValue(
        preferenceKey: PreferenceKey
    ): String {
        val key = preferenceKey.name
        return attEncryptedPref.getString(key, "") as String
    }

    fun setStrValue(
        preferenceKey: PreferenceKey,
        value: String
    ) {
        val key = preferenceKey.name
        attEncryptedPref.edit().putString(key, value).apply()
    }

    companion object {
        enum class PreferenceKey {
            ACCESS_TOKEN,
            REFRESH_TOKEN
        }
    }
}
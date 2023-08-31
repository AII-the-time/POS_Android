package org.swm.att.common_ui.util.extension

import android.os.Build
import android.os.Bundle
import java.io.Serializable

inline fun <reified T: Serializable> Bundle.customGetSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}
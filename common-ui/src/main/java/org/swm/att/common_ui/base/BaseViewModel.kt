package org.swm.att.common_ui.base

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.swm.att.domain.entity.HttpResponseException

abstract class BaseViewModel: ViewModel() {
    protected val attExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "attExceptionHandler: ${throwable.message}")
        if (throwable is HttpResponseException) {
            Log.e(TAG, "attExceptionHandler| ResponseCode(${throwable.httpCode}, ErrorMessage(${throwable.message}}")
        }
    }
}
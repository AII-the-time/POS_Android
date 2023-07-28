package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.HttpResponseException
import org.swm.att.data.remote.HttpResponseStatus
import retrofit2.Response

abstract class BaseNetworkDataSource {
    protected fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw HttpResponseException(
                status = HttpResponseStatus.create(response.code()),
                httpCode =  response.code(),
                errorRequestUrl = response.raw().request.url.toString(),
                msg = "Http request Failed (${response.code()}) ${response.message()}, $errorBody",
                cause = Throwable(errorBody)
            )
        }
    }
}
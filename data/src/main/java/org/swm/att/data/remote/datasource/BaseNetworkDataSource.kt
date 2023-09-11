package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.HttpResponseStatus
import retrofit2.Response

abstract class BaseNetworkDataSource {
    protected fun <T> checkResponse(response: Response<T>): Flow<T> {
        if (response.isSuccessful) {
            return flow { emit(response.body()!!) }
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
package org.swm.att.data.remote.datasource

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.swm.att.data.remote.response.BaseErrorDTO
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.HttpResponseStatus
import retrofit2.Response

abstract class BaseNetworkDataSource {

    protected fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = jsonAdapter.fromJson(errorBody)
            throw HttpResponseException(
                status = HttpResponseStatus.create(response.code()),
                httpCode = response.code(),
                errorRequestUrl = response.raw().request.url.toString(),
                msg = errorResponse?.toast ?: "예상치 못한 오류가 발생했습니다.\n 다시 시도해주세요!",
                cause = Throwable(errorBody)
            )
        }
    }

    companion object {
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<BaseErrorDTO> = moshi.adapter(BaseErrorDTO::class.java)
    }
}
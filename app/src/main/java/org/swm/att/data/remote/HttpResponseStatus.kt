package org.swm.att.data.remote

enum class HttpResponseStatus {
    OK,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR;

    companion object {
        fun create(httpCode: Int): HttpResponseStatus {
            return when(httpCode) {
                200 -> OK
                400 -> BAD_REQUEST
                401 -> UNAUTHORIZED
                403 -> FORBIDDEN
                404 -> NOT_FOUND
                500 -> INTERNAL_SERVER_ERROR
                else -> throw IllegalArgumentException("Invalid HTTP code: $httpCode")
            }
        }
    }

}

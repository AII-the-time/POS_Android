package org.swm.att.domain.entity

import java.lang.Exception

//java.lang.Exception을 상속해 만든 Exception
class HttpResponseException(
    val status: HttpResponseStatus,
    val httpCode: Int,
    val errorRequestUrl: String,
    msg: String,
    cause: Throwable?
): Exception(msg, cause)

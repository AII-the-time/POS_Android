package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginDTO(
    @field:Json(name = "businessRegistrationNumber")
    val businessRegistrationNumber: String?,
    @field:Json(name = "certificatedPhoneToken")
    val certificatedPhoneToken: String?
)
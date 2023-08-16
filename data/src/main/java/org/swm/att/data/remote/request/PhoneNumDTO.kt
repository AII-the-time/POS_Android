package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneNumDTO(
    @field:Json(name = "phone")
    val phone: String?
)

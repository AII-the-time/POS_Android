package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CertificationDTO(
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "certificationCode")
    val certificationCode: String?,
    @field:Json(name = "phoneCertificationToken")
    val phoneCertificationToken: String?
)
package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.CertificatedPhoneTokenVO

@JsonClass(generateAdapter = true)
data class CertificatedPhoneTokenDTO(
    @field:Json(name = "certificatedPhoneToken")
    val certificatedPhoneToken: String?
) {
    fun toVO() = CertificatedPhoneTokenVO(
        certificatedPhoneToken = certificatedPhoneToken ?: "Unknown"
    )
}
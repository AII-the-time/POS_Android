package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO

@JsonClass(generateAdapter = true)
data class TokenForCertificationPhoneDTO(
    @field:Json(name = "tokenForCertificatePhone")
    val tokenForCertificatePhone: String?
) {
    fun toVO() = TokenForCertificationPhoneVO(
        tokenForCertificatePhone = tokenForCertificatePhone ?: "Unknown"
    )
}
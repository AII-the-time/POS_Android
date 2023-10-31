package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO

@JsonClass(generateAdapter = true)
data class TokenForCertificationPhoneDTO(
    @field:Json(name = "tokenForCertificationPhone")
    val tokenForCertificationPhone: String?
) {
    fun toVO() = TokenForCertificationPhoneVO(
        tokenForCertificationPhone = tokenForCertificationPhone ?: "Unknown"
    )
}
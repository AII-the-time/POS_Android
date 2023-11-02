package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable
import org.swm.att.domain.entity.response.TokenVO

@Serializable
@JsonClass(generateAdapter = true)
data class TokenDTO(
    @field:Json(name = "accessToken")
    val accessToken: String? = "",
    @field:Json(name = "refreshToken")
    val refreshToken: String? = ""
) {
    fun toVO() = TokenVO(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: ""
    )
}
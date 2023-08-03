package org.swm.att.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val accessToken: String? = null,
    val refreshToken: String? = null
)
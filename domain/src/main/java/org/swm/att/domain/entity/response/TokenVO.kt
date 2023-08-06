package org.swm.att.domain.entity.response

data class TokenVO(
    val accessToken: String,
    val refreshToken: String
)
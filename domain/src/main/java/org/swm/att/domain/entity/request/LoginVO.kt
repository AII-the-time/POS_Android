package org.swm.att.domain.entity.request

data class LoginVO (
    val businessRegistrationNumber: String,
    val certificatedPhoneToken: String
)
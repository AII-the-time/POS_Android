package org.swm.att.domain.entity.request

data class StoreVO(
    val name: String,
    val address: String,
    val openingHours: List<OpeningHourVO>
)
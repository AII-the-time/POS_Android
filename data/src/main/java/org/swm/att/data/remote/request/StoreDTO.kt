package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.data.remote.request.OpeningHourDTO
import org.swm.att.domain.entity.request.StoreVO

@JsonClass(generateAdapter = true)
data class StoreDTO(
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "address")
    val address: String?,
    @field:Json(name = "openingHours")
    val openingHours: List<OpeningHourDTO>?
) {
    fun toVO() = StoreVO (
        name = name ?: "Unknown",
        address = address ?: "Unknown",
        openingHours = openingHours?.map { it.toVO() } ?: listOf()
    )
}

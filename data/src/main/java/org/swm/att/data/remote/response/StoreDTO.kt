package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StoreVO

@JsonClass(generateAdapter = true)
data class StoreDTO(
    @field:Json(name = "storeId")
    val storeId: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "address")
    val address: String?
) {
    fun toVO() = StoreVO(
        storeId = storeId ?: -1,
        name = name ?: "Unknown",
        address = address ?: "Unknown"
    )
}
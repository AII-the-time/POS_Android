package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StoreIdVO

@JsonClass(generateAdapter = true)
data class StoreIdDTO(
    @field:Json(name = "storeId")
    val storeId: Int?
) {
    fun toVO() = StoreIdVO(
        storeId = storeId ?: -1
    )
}
package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StoreListVO

@JsonClass(generateAdapter = true)
data class StoreListDTO(
    @field:Json(name = "stores")
    val stores: List<StoreDTO>?
) {
    fun toVO() = StoreListVO(
        stores = stores?.map { it.toVO() } ?: listOf()
    )
}
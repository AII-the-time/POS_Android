package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MileageVO

@JsonClass(generateAdapter = true)
data class MileageDTO(
    @field:Json(name = "mileageId")
    val mileageId: Int?,
    @field:Json(name = "mileage")
    val mileage: Int?
) {
    fun toVO() = MileageVO(
        mileageId = mileageId ?: -1,
        mileage = mileage ?: -1
    )
}

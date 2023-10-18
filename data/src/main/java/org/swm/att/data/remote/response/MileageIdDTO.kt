package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MileageIdVO

@JsonClass(generateAdapter = true)
data class MileageIdDTO(
    @field:Json(name = "mileageId")
    val mileageId: Int?
) {
    fun toVO() = MileageIdVO(
        mileageId = mileageId ?: -1
    )
}

package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MileagePaymentOfBillVO

@JsonClass(generateAdapter = true)
data class MileagePaymentOfBillDTO(
    @field:Json(name = "use")
    val use: String?,
    @field:Json(name = "save")
    val save: String?
) {
    fun toVO() = MileagePaymentOfBillVO(
        use = use?.toBigDecimal() ?: (-1).toBigDecimal(),
        save = save?.toBigDecimal() ?: (-1).toBigDecimal()
    )
}

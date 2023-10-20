package org.swm.att.domain.entity.response

data class StockVO(
    val id: Int,
    val name: String,
    val isMixed: Boolean,
    val isNew: Boolean? = false
) {
    constructor(newItemName: String): this(
        id = -1,
        name = newItemName,
        isMixed = false,
        isNew = false
    )
}
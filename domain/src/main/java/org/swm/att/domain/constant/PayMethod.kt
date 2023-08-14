package org.swm.att.domain.constant

enum class PayMethod {
    CARD,
    CASH,
    MILEAGE,
    EASY;

    companion object {
        fun toString(payMethod: PayMethod): String {
            return when (payMethod) {
                CARD -> "CARD"
                CASH -> "CASH"
                MILEAGE -> "MILEAGE"
                EASY -> "EASY"
            }
        }
    }
}
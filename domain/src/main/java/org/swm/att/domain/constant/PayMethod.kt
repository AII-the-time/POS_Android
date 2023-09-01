package org.swm.att.domain.constant

enum class PayMethod {
    CARD,
    CASH,
    MILEAGE,
    EASY,
    UNKNOWN;

    companion object {
        fun toString(payMethod: PayMethod): String {
            return when (payMethod) {
                CARD -> "CARD"
                CASH -> "CASH"
                MILEAGE -> "MILEAGE"
                EASY -> "EASY"
                else -> "UNKNOWN"
            }
        }

        fun toPayMethod(payMethod:String): PayMethod {
            return when (payMethod) {
                "CARD" -> CARD
                "CASH" -> CASH
                "MILEAGE" -> MILEAGE
                "EASY" -> EASY
                else -> UNKNOWN
            }
        }
    }
}
package org.swm.att.domain.constant

enum class PayMethod {
    CARD,
    CASH,
    BANK,
    UNKNOWN;

    companion object {
        fun toString(payMethod: PayMethod): String {
            return when (payMethod) {
                CARD -> "CARD"
                CASH -> "CASH"
                BANK -> "BANK"
                else -> "UNKNOWN"
            }
        }

        fun toPayMethod(payMethod:String): PayMethod {
            return when (payMethod) {
                "CARD" -> CARD
                "CASH" -> CASH
                "BANK" -> BANK
                else -> UNKNOWN
            }
        }
    }
}
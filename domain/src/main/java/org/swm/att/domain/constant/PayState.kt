package org.swm.att.domain.constant

enum class PayState {
    PAID,
    WAITING,
    CANCELED,
    UNKNOWN;

    companion object {
        fun toPayState(payState: String): PayState {
            return when (payState) {
                "PAID" -> PAID
                "WAITING" -> WAITING
                "CANCELED" -> CANCELED
                else -> UNKNOWN
            }
        }
    }
}
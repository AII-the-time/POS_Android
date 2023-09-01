package org.swm.att.common_ui.util.state

sealed interface NetworkState<out T> {
    object Init: NetworkState<Nothing>

    data class Success<T>(
        val data: T
    ): NetworkState<T>

    data class Failure(
        val msg: String?
    ): NetworkState<Nothing>
}
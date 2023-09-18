package org.swm.att.common_ui.util.state

sealed class UiState<out T> {
    // Loading의 경우, 상태의 데이터가 필요 없기 때문에 단 하나의 인스턴스만 있어도 된다.
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T? = null): UiState<T>()
    data class Error<T>(val errorMsg: String?): UiState<T>()
}
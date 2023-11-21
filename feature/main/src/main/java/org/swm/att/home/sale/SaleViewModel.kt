package org.swm.att.home.sale

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.SduiBaseResponseVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    private val _getSduiReportDataState: MutableStateFlow<UiState<SduiBaseResponseVO>> = MutableStateFlow(UiState.Loading)
    val getSduiReportDataState = _getSduiReportDataState

    fun getSduiReportData() {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.getUserReport().collect() { result ->
                result.onSuccess {
                    _getSduiReportDataState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "매출 리포트를 가져오는 데 오류가 발생했습니다."
                    _getSduiReportDataState.value = UiState.Error(errorMsg)
                }
            }
        }
    }
}
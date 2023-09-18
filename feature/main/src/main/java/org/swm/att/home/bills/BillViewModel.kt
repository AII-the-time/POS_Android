package org.swm.att.home.bills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.repository.AttOrderRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository
): BaseViewModel() {
    private val _selectedBillInfo = MutableStateFlow<UiState<OrderReceiptVO>>(UiState.Loading)
    val selectedBillInfo: StateFlow<UiState<OrderReceiptVO>> = _selectedBillInfo
    private val _selectedBillInfoData = MutableLiveData<OrderReceiptVO>()
    val selectedBillInfoData: LiveData<OrderReceiptVO> = _selectedBillInfoData

    private val _selectedBillId = MutableLiveData(0)
    val selectedBillId: LiveData<Int> = _selectedBillId
    private val _currentSelectedBillId = MutableLiveData<Int>()
    val currentSelectedBillId: LiveData<Int> = _currentSelectedBillId
    private val _filteringStartDate = MutableLiveData<Date>()
    val filteringStartDate: LiveData<Date> = _filteringStartDate
    private val _filteringEndDate = MutableLiveData<Date?>()
    val filteringEndDate: LiveData<Date?> = _filteringEndDate

    fun getSelectedBillInfo(storeId: Int, selectedBillId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            // 추후 storeId, selectedBillId 사용
            attOrderRepository.getOrderBill(1, 1)
                .collect { result ->
                    result.onSuccess {
                        _selectedBillInfoData.value = it
                        _selectedBillInfo.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "결제 내역 불러오기 실패"
                        _selectedBillInfo.value = UiState.Error(errorMsg)
                    }
                }
        }
    }


    fun setCurrentSelectedBillId(currentSelectedBillId: Int) {
        _currentSelectedBillId.value = currentSelectedBillId
    }

    fun changeSelectedState() {
        _selectedBillId.postValue(currentSelectedBillId.value)
    }

    fun getBillsForFilteringDates(startDate: Date, endDate: Date?) {
        _filteringStartDate.value = startDate
        _filteringEndDate.value = endDate
        // filtering api 연결 필요
    }

}
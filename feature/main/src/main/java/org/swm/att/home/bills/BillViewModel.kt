package org.swm.att.home.bills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.OrderBillVO
import org.swm.att.domain.entity.response.OrderBillsVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.repository.AttOrderRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository
): BaseSelectableViewViewModel() {
    private val _selectedBillInfo = MutableStateFlow<UiState<OrderReceiptVO>>(UiState.Loading)
    val selectedBillInfo: StateFlow<UiState<OrderReceiptVO>> = _selectedBillInfo
    private val _selectedBillInfoData = MutableLiveData<OrderReceiptVO>()
    val selectedBillInfoData: LiveData<OrderReceiptVO> = _selectedBillInfoData

    private val _orderBills = MutableStateFlow<UiState<OrderBillsVO>>(UiState.Loading)
    val orderBills: StateFlow<UiState<OrderBillsVO>> = _orderBills
    private val _orderBillsData = MutableLiveData<List<OrderBillVO>>()
    val orderBillsData: LiveData<List<OrderBillVO>> = _orderBillsData

    private val _selectedBillId = MutableLiveData<Int>()
    val selectedBillId: LiveData<Int> = _selectedBillId
    private val _currentSelectedBillId = MutableLiveData<Int>()
    val currentSelectedBillId: LiveData<Int> = _currentSelectedBillId
    private val _filteringStartDate = MutableLiveData<Date>()
    val filteringStartDate: LiveData<Date> = _filteringStartDate
    private val _filteringEndDate = MutableLiveData<Date?>()
    val filteringEndDate: LiveData<Date?> = _filteringEndDate
    private var page: Int = 1

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            // 추후 storeId, selectedBillId 사용
            attOrderRepository.getOrderBill(storeId, selectedItemId)
                .collect { result ->
                    result.onSuccess {
                        _selectedBillInfoData.value = it
                        _selectedBillInfo.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "결제 상세 내역 불러오기 실패"
                        _selectedBillInfo.value = UiState.Error(errorMsg)
                    }
                }
        }
    }

    fun getNextOrderBills(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            val date = filteringStartDate.value?.let { startDate ->
                Formatter.getStringByDateTimeBaseFormatter(startDate.getUTCDateTime())
            }
            attOrderRepository.getOrderBills(storeId, page, date, 20)
                .collect { result ->
                    result.onSuccess {
                        val data = _orderBillsData.value?.toMutableList() ?: mutableListOf()
                        data.addAll(it.orders)
                        if (page == 1 && data.isNotEmpty()) {
                            data[0].isFocused = true
                            _currentSelectedBillId.postValue(0)
                            getSelectedItem(storeId, data[0].id)
                        }
                        _orderBillsData.postValue(data)
                        _orderBills.value = UiState.Success(it)
                        page += 1
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "결제 내역 불러오기 실패"
                        _orderBills.value = UiState.Error(errorMsg)
                    }
                }
        }
    }

    override fun setCurrentSelectedItemId(position: Int) {
        val currentOrderBills = _orderBillsData.value
        val pastSelectedId = currentSelectedBillId.value
        currentOrderBills?.let { bills ->
            bills[position].isFocused = true
            pastSelectedId?.let {
                if (it != position) {
                    bills[it].isFocused = false
                }
            }
            _orderBillsData.postValue(bills)
        }

        pastSelectedId?.let {
            changeSelectedState()
        }

        _currentSelectedBillId.postValue(position)
    }

    override fun changeSelectedState() {
        _selectedBillId.postValue(currentSelectedBillId.value)
    }

    fun getBillsForFilteringDates(startDate: Date) {
        if (startDate == filteringStartDate.value) return
        _filteringStartDate.value = startDate
        page = 1
        _orderBillsData.value = listOf()
        getNextOrderBills(1)
    }

    fun getSizeOfOrderBills(): Int {
        return orderBillsData.value?.size ?: 0
    }

}
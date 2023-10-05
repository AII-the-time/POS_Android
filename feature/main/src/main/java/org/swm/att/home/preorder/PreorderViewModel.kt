package org.swm.att.home.preorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.PreOrderBillVO
import org.swm.att.domain.entity.response.PreOrdersVO
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.repository.AttOrderRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PreorderViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository
): BaseViewModel() {
    private val _selectedPreorderInfo = MutableStateFlow<UiState<PreOrderBillVO>>(UiState.Loading)
    val selectedPreorderInfo: StateFlow<UiState<PreOrderBillVO>> = _selectedPreorderInfo
    private val _selectedPreorderInfoData = MutableLiveData<PreOrderBillVO>()
    val selectedPreorderInfoData: LiveData<PreOrderBillVO> = _selectedPreorderInfoData
    private val _currentSelectedValidPreorderId = MutableLiveData<Int>()
    val currentSelectedValidPreorderId: LiveData<Int> = _currentSelectedValidPreorderId
    private val _currentSelectedPastPreorderId = MutableLiveData<Int>()
    val currentSelectedPastPreorderId: LiveData<Int> = _currentSelectedPastPreorderId
    private val _selectedValidPreorderId = MutableLiveData(0)
    val selectedValidPreorderId: LiveData<Int> = _selectedValidPreorderId
    private val _selectedPastPreorderId = MutableLiveData<Int>()
    val selectedPastPreorderId: LiveData<Int> = _selectedPastPreorderId
    private val _filteringStartDate = MutableLiveData<Date?>()
    private val filteringStartDate: LiveData<Date?> = _filteringStartDate

    private val _getPreOrdersState = MutableStateFlow<UiState<PreOrdersVO>>(UiState.Loading)
    val getPreOrdersState: StateFlow<UiState<PreOrdersVO>> = _getPreOrdersState
    private val _preOrdersData = MutableLiveData<List<PreorderVO>>()
    val preOrdersData: LiveData<List<PreorderVO>> = _preOrdersData
    private var page: Int = 1

    fun getSelectedPreorderDetail(selectedPreorderId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.getPreOrderBill(1, selectedPreorderId).collect { result ->
                result.onSuccess {
                    _selectedPreorderInfoData.postValue(it)
                    _selectedPreorderInfo.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "예약 내역 불러오기 실패"
                    _getPreOrdersState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun setCurrentSelectedPreorderId(currentSelectedPreorderId: Int, isValid: Boolean) {
        if (isValid) {
            _currentSelectedValidPreorderId.value = currentSelectedPreorderId
        } else {
            _currentSelectedPastPreorderId.value = currentSelectedPreorderId
        }
    }

    fun changeSelectedState(isValid: Boolean) {
        if (isValid) {
            _selectedValidPreorderId.postValue(currentSelectedValidPreorderId.value)
        } else {
            _selectedPastPreorderId.postValue(currentSelectedPastPreorderId.value)
        }
    }

    fun getPreordersForFilteringDates(startDate: Date) {
        _filteringStartDate.value = startDate
        page = 1
        getNextValidPreOrders(1)
    }

    fun getNextValidPreOrders(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            val date = filteringStartDate.value?.let { startDate ->
                Formatter.getStringByDateTimeBaseFormatter(startDate)
            }
            attOrderRepository.getPreOrders(storeId, page, date).collect { result ->
                result.onSuccess {
                    val data = _preOrdersData.value?.toMutableList() ?: mutableListOf()
                    data.addAll(it.preOrders)
                    _preOrdersData.postValue(data)
                    _getPreOrdersState.value = UiState.Success(it)
                    page += 1
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "예약 내역 불러오기 실패"
                    _getPreOrdersState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun isEndOfValidPreOrders(): Boolean {
        _getPreOrdersState.value.apply {
            if (this is UiState.Success) {
                this.data?.let {
                    return it.endPage > page
                }
            } else {
                return false
            }
        }
        return false
    }

    fun getSelectedMenus(): OrderedMenusVO {
        return OrderedMenusVO(
            menus = selectedPreorderInfoData.value?.orderitems?.map {
                OrderedMenuVO(
                    id = it.id,
                    name = it.menuName,
                    price = it.price.toInt(),
                    count = it.count,
                    options = it.options,
                    detail = it.detail
                )
            } ?: listOf()
        )
    }
}
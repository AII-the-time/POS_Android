package org.swm.att.home.preorder

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
) : BaseSelectableViewViewModel() {
    private val _selectedPreorderInfo = MutableStateFlow<UiState<PreOrderBillVO>>(UiState.Loading)
    val selectedPreorderInfo: StateFlow<UiState<PreOrderBillVO>> = _selectedPreorderInfo
    private val _selectedPreorderInfoData = MutableLiveData<PreOrderBillVO>()
    val selectedPreorderInfoData: LiveData<PreOrderBillVO> = _selectedPreorderInfoData
    private val _currentSelectedPreorderId = MutableLiveData<Int>()
    val currentSelectedPreorderId: LiveData<Int> = _currentSelectedPreorderId
    private val _selectedPreorderId = MutableLiveData(0)
    val selectedPreorderId: LiveData<Int> = _selectedPreorderId
    private val _filteringStartDate = MutableLiveData<Date?>()
    private val filteringStartDate: LiveData<Date?> = _filteringStartDate

    private val _getPreOrdersState = MutableStateFlow<UiState<PreOrdersVO>>(UiState.Loading)
    val getPreOrdersState: StateFlow<UiState<PreOrdersVO>> = _getPreOrdersState
    private val _preOrdersData = MutableLiveData<List<PreorderVO>>()
    val preOrdersData: LiveData<List<PreorderVO>> = _preOrdersData
    private var page: Int = 1

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.getPreOrderBill(1, selectedItemId).collect { result ->
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

    override fun setCurrentSelectedItemId(position: Int) {
        val preorderList = preOrdersData.value
        val pastSelectedId = currentSelectedPreorderId.value
        preorderList?.let { preorders ->
            preorders[position].isFocused = true
            pastSelectedId?.let { pastId ->
                if (pastId != position) {
                    preorders[pastId].isFocused = false
                }
            }
            _preOrdersData.postValue(preorders)
        }

        pastSelectedId?.let {
            changeSelectedState()
        }

        _currentSelectedPreorderId.postValue(position)
    }

    override fun changeSelectedState() {
        _selectedPreorderId.postValue(currentSelectedPreorderId.value)
    }

    fun getPreordersForFilteringDates(startDate: Date) {
        if (startDate == filteringStartDate.value) return
        _filteringStartDate.value = startDate
        page = 1
        _preOrdersData.value = listOf()
        getNextValidPreOrders(1)
    }

    fun getNextValidPreOrders(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            val date = filteringStartDate.value?.let { startDate ->
                Formatter.getStringByDateTimeBaseFormatter(startDate.getUTCDateTime())
            }
            attOrderRepository.getPreOrders(storeId, page, date).collect { result ->
                result.onSuccess {
                    val data = _preOrdersData.value?.toMutableList() ?: mutableListOf()
                    data.addAll(it.preOrders)
                    _preOrdersData.postValue(data)
                    _getPreOrdersState.value = UiState.Success(it)
                    if (page == 1 && data.isNotEmpty()) {
                        data[0].isFocused = true
                        _currentSelectedPreorderId.postValue(0)
                        getSelectedItem(storeId, data[0].id)
                    }
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
                    return it.lastPage > page - 1
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
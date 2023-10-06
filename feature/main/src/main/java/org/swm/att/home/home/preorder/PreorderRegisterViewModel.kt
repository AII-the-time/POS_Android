package org.swm.att.home.home.preorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.util.Formatter.getStringByDateTimeBaseFormatter
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PreOrderedMenusVO
import org.swm.att.domain.repository.AttOrderRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PreorderRegisterViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository
) : BaseViewModel() {
    private val _orderedMenus = MutableStateFlow<OrderedMenusVO?>(null)
    val orderedMenus: StateFlow<OrderedMenusVO?> = _orderedMenus
    private val _preorderDate = MutableStateFlow<Date>(Calendar.getInstance().time)
    val preorderDate: StateFlow<Date> = _preorderDate
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber
    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> = _totalPrice
    private val _postPreOrderState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postPreOrderState: StateFlow<UiState<Unit>> = _postPreOrderState

    fun setOrderedMenus(orderedMenusVO: OrderedMenusVO) {
        _orderedMenus.value = orderedMenusVO
        getTotalPrice()
    }

    private fun getTotalPrice() {
        _orderedMenus.value?.let {
            val totalPrice = it.menus?.sumOf { menu -> menu.price * (menu.count ?: 1) }.toString()
            _totalPrice.postValue(totalPrice)
        }
    }

    fun setPreorderDate(date: Date) {
        _preorderDate.value = date
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun postPreOrder(phoneNumber: String, memo: String?) {
        viewModelScope.launch(attExceptionHandler) {
            val preOrderedMenus = PreOrderedMenusVO(
                totalPrice.value ?: "0",
                orderedMenus.value?.menus ?: listOf(),
                phoneNumber,
                memo,
                getStringByDateTimeBaseFormatter(preorderDate.value.getUTCDateTime())
            )
            attOrderRepository.postPreOrder(1, preOrderedMenus).collect { result ->
                result.onSuccess {
                    _postPreOrderState.value = UiState.Success()
                }.onFailure {
                    val errorMsg =
                        if (it is HttpResponseException) it.message else "예약 주문 등록 실패하였습니다."
                    _postPreOrderState.value = UiState.Error(errorMsg)
                }
            }
        }
    }
}
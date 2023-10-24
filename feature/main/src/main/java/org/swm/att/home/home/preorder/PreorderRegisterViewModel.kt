package org.swm.att.home.home.preorder

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.home.util.alarm.AlarmManager
import org.swm.att.common_ui.util.Formatter.getStringByDateTimeBaseFormatter
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PreOrderedMenusVO
import org.swm.att.domain.entity.response.PreorderIdVO
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PreorderRegisterViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository,
    @ApplicationContext private val context: Context,
    private val attPosUserRepository: AttPosUserRepository
) : BaseViewModel() {
    //uiState
    private val _orderedMenus = MutableStateFlow<OrderedMenusVO?>(null)
    val orderedMenus: StateFlow<OrderedMenusVO?> = _orderedMenus
    private val _preorderDate = MutableStateFlow<Date>(Calendar.getInstance().time)
    val preorderDate: StateFlow<Date> = _preorderDate
    private val _postPreOrderState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postPreOrderState: StateFlow<UiState<Unit>> = _postPreOrderState
    private val _updatePreorderState = MutableStateFlow<UiState<PreorderIdVO>>(UiState.Loading)
    val updatePreorderState: StateFlow<UiState<PreorderIdVO>> = _updatePreorderState

    //ui data
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber
    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> = _totalPrice

    //api
    fun postPreOrder(phoneNumber: String, memo: String?) {
        viewModelScope.launch(attExceptionHandler) {
            val preOrderedMenus = PreOrderedMenusVO(
                totalPrice = totalPrice.value ?: "0",
                menus = orderedMenus.value?.menus ?: listOf(),
                phone = phoneNumber,
                memo = memo,
                orderedFor = getStringByDateTimeBaseFormatter(preorderDate.value.getUTCDateTime())
            )
            val storeId = attPosUserRepository.getStoreId()
            attOrderRepository.postPreOrder(storeId, preOrderedMenus).collect { result ->
                result.onSuccess {
                    _postPreOrderState.value = UiState.Success()
                    addPreorderToAlarmManager(
                        getStringByDateTimeBaseFormatter(preorderDate.value.getUTCDateTime()),
                        phoneNumber,
                        orderedMenus.value?.menus?.size ?: -1,
                        it.preOrderId
                    )
                }.onFailure {
                    val errorMsg =
                        if (it is HttpResponseException) it.message else "예약 주문 등록 실패하였습니다."
                    _postPreOrderState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun updatePreorder(phoneNumber: String, memo: String?, preorderId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            val preOrderedMenus = PreOrderedMenusVO(
                id = preorderId,
                totalPrice = totalPrice.value ?: "0",
                menus = orderedMenus.value?.menus ?: listOf(),
                phone = phoneNumber,
                memo = memo,
                orderedFor = getStringByDateTimeBaseFormatter(preorderDate.value.getUTCDateTime())
            )
            val storeId = attPosUserRepository.getStoreId()
            attOrderRepository.updatePreorder(storeId, preOrderedMenus).collect { result ->
                result.onSuccess {
                    _updatePreorderState.value = UiState.Success()
                    resetPreorderToAlarmManager(
                        getStringByDateTimeBaseFormatter(preorderDate.value.getUTCDateTime()),
                        phoneNumber,
                        orderedMenus.value?.menus?.size ?: -1,
                        it.preOrderId
                    )
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "예약 주문 수정 실패"
                    _updatePreorderState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    //set value
    fun setOrderedMenus(orderedMenusVO: OrderedMenusVO) {
        _orderedMenus.value = orderedMenusVO
        getTotalPrice()
    }

    fun setPreorderDate(date: Date) {
        _preorderDate.value = date
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    //etc
    private fun getTotalPrice() {
        _orderedMenus.value?.let {
            val totalPrice = it.menus?.sumOf { menu -> menu.price * (menu.count ?: 1) }.toString()
            _totalPrice.postValue(totalPrice)
        }
    }

    //alarmManager
    private fun addPreorderToAlarmManager(
        preorderDate: String,
        phoneNumber: String,
        totalOrderCount: Int,
        preorderId: Int
    ) {
        AlarmManager.setPreorderAlarm(context, preorderDate, phoneNumber, totalOrderCount, preorderId)
    }

    private fun resetPreorderToAlarmManager(
        preorderDate: String,
        phoneNumber: String,
        totalOrderCount: Int,
        preorderId: Int
    ) {
        AlarmManager.updateAlarm(context, preorderDate, phoneNumber, totalOrderCount, preorderId)
    }
}
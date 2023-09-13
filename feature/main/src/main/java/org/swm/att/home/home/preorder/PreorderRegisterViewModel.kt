package org.swm.att.home.home.preorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.swm.att.common_ui.util.Formatter.getStringBaseCurrencyUnit
import org.swm.att.domain.entity.request.OrderedMenusVO
import java.util.Calendar
import java.util.Date

class PreorderRegisterViewModel: ViewModel() {
    private val _orderedMenus = MutableStateFlow<OrderedMenusVO?>(null)
    val orderedMenus: StateFlow<OrderedMenusVO?> = _orderedMenus
    private val _preorderDate = MutableStateFlow<Date>(Calendar.getInstance().time)
    val preorderDate: StateFlow<Date> = _preorderDate
    private val _phoneNumber = MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> = _phoneNumber
    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> = _totalPrice

    fun setOrderedMenus(orderedMenusVO: OrderedMenusVO) {
        _orderedMenus.value = orderedMenusVO
        getTotalPrice()
    }

    private fun getTotalPrice() {
        _orderedMenus.value?.let {
            val totalPrice = getStringBaseCurrencyUnit(it.menus?.sumOf { menu -> menu.price * (menu.count ?: 1) }.toString())
            _totalPrice.postValue(totalPrice)
        }
    }

    fun setPreorderDate(date: Date) {
        _preorderDate.value = date
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }
}
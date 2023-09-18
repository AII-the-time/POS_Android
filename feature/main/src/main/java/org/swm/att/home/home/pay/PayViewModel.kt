package org.swm.att.home.home.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PaymentResultVO
import org.swm.att.domain.repository.AttOrderRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val attOrderRepository: AttOrderRepository
): BaseViewModel() {
    private val _orderedMenuMap = MutableLiveData<MutableMap<OrderedMenuVO, Int>?>()
    val orderedMenuMap: LiveData<MutableMap<OrderedMenuVO, Int>?> = _orderedMenuMap
    private val _totalOrderMenuList = MutableLiveData<List<OrderedMenuVO>>()
    val totalOrderMenuList: LiveData<List<OrderedMenuVO>> = _totalOrderMenuList
    private val _totalPrice = MutableLiveData<Int>()
    private val totalPrice: LiveData<Int> = _totalPrice
    private val _orderVO = MutableLiveData<OrderVO>()
    private val orderVO: LiveData<OrderVO> = _orderVO
    private val _mileage = MutableLiveData<MileageVO>()
    val mileage: LiveData<MileageVO> = _mileage
    private val _useMileage = MutableLiveData<Stack<String>>()
    val useMileage: LiveData<Stack<String>> = _useMileage
    private val _patchMileageState = MutableStateFlow<UiState<MileageVO>>(UiState.Loading)
    val patchMileageState: StateFlow<UiState<MileageVO>> = _patchMileageState
    private val _postUseMileageState = MutableStateFlow<UiState<PaymentResultVO>>(UiState.Loading)
    val postUseMileageState: StateFlow<UiState<PaymentResultVO>> = _postUseMileageState
    private val _postOrderState = MutableStateFlow<UiState<OrderVO>>(UiState.Loading)
    val postOrderState: StateFlow<UiState<OrderVO>> = _postOrderState

    fun setMileage(mileageVO: MileageVO) {
        _mileage.postValue(mileageVO)
    }

    fun setOrderedMenuMap(orderedMenusVO: OrderedMenusVO) {
        orderedMenusVO.menus?.let {
            _totalOrderMenuList.postValue(it)
            _totalPrice.postValue(it.sumOf { orderedMenu ->
                orderedMenu.price * (orderedMenu.count ?: 1)
            })
            val selectedOrderedMenuMap = (_orderedMenuMap.value ?: mapOf()).toMutableMap()
            it.forEach { orderedMenu ->
                selectedOrderedMenuMap[orderedMenu] = orderedMenu.count ?: 1
            }
            _orderedMenuMap.postValue(selectedOrderedMenuMap)
        }
    }

    fun getOrderedMenuList(): List<OrderedMenuVO>{
        val selectedMenuMap = _orderedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(it)
        }
        return orderedMenuList
    }

    fun getSelectedOrderedMenuList(): List<OrderedMenuVO> {
        val selectedMenuMap = _orderedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(it)
        }
        return orderedMenuList
    }

    fun addUseMileageStr(str: String) {
        val mileage = _useMileage.value ?: Stack()
        mileage.push(str)
        _useMileage.postValue(mileage)
    }

    fun removeUseMileageStr() {
        val mileage = _useMileage.value ?: Stack()
        if (mileage.isNotEmpty()) {
            mileage.pop()
        }
        _useMileage.postValue(mileage)
    }

    fun useAllMileage() {
        val mileageStr = _mileage.value?.mileage.toString()
        val mileageStack = Stack<String>()
        mileageStr.forEach {
            mileageStack.push(it.toString())
        }
        _useMileage.postValue(mileageStack)
    }

    fun clearUseMileage() {
        _useMileage.postValue(Stack())
    }

    fun getOrderIdAndPostPayment(payMethod: PayMethod) {
        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.postOrder(
                1,
                totalPrice.value ?: 0,
                OrderedMenusVO(
                    menus = totalOrderMenuList.value
                )
            ).collect { result ->
                result.onSuccess {
                    postPayment(payMethod, it.orderId)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "주문 실패"
                    _postOrderState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    private fun postPayment(payMethod: PayMethod, id: Int) {
        val cost = (getPriceFromMap(orderedMenuMap.value)) - (useMileage.value?.joinToString("")?.toInt() ?: 0)
        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.postPayment(
                1,
                PaymentVO(
                    orderId = id,
                    paymentMethod = payMethod,
                    mileageId = mileage.value?.mileageId,
                    useMileage = if (mileage.value != null) { useMileage.value?.joinToString("")?.toInt() ?: 0 } else { null } ,
                    saveMileage = if (mileage.value != null) { (cost * 0.1).toInt() } else { null }// 임시로 10퍼센트 설정
                )
            ).collect { result ->
                result.onSuccess {
                    _postOrderState.value = UiState.Success()
                    _orderedMenuMap.postValue(mutableMapOf())
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "결제 실패"
                    _postOrderState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    companion object {
        private fun getPriceFromMap(menuWithCountMap: Map<OrderedMenuVO, Int>?): Int {
            return menuWithCountMap?.map { it.key.price * it.value }?.sum() ?: 0
        }
    }

}
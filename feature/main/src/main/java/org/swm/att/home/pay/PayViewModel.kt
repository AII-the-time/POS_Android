package org.swm.att.home.pay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.NetworkState
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository,
    private val attOrderRepository: AttOrderRepository
): BaseViewModel() {
    //결제 진행 여부에 따른 분리
    private val _orderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val orderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _orderedMenuMap
    private val _selectedOrderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val selectedOrderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _selectedOrderedMenuMap
    private val _totalOrderMenuList= MutableLiveData<List<OrderedMenuVO>>()
    private val totalOrderMenuList: LiveData<List<OrderedMenuVO>> = _totalOrderMenuList

    //주문 전체 가격
    private val _totalPrice = MutableLiveData<Int>()
    private val totalPrice: LiveData<Int> = _totalPrice
    private val _orderVO = MutableLiveData<OrderVO>()
    private val orderVO: LiveData<OrderVO> = _orderVO

    private val _mileage = MutableLiveData<MileageVO>()
    val mileage: LiveData<MileageVO> = _mileage
    private val _useMileage = MutableLiveData<Stack<String>>()
    val useMileage: LiveData<Stack<String>> = _useMileage

    private val _patchMileageState: MutableLiveData<NetworkState<MileageVO>> = MutableLiveData(NetworkState.Init)
    val patchMileageState: LiveData<NetworkState<MileageVO>> = _patchMileageState

    fun setMileage(mileageVO: MileageVO) {
        _mileage.postValue(mileageVO)
    }

    fun setOrderedMenuMap(orderedMenusVO: OrderedMenusVO) {
        orderedMenusVO.menus?.let {
            _totalOrderMenuList.postValue(it)
            _totalPrice.postValue(it.sumOf { orderedMenu -> orderedMenu.menu.price * (orderedMenu.count ?: 1) })
            val selectedOrderedMenuMap = (_selectedOrderedMenuMap.value ?: mapOf()).toMutableMap()
            it.forEach {  orderedMenu ->
                selectedOrderedMenuMap[orderedMenu.menu] = orderedMenu.count ?: 0
            }
            _selectedOrderedMenuMap.postValue(selectedOrderedMenuMap)
        }
    }

    fun getOrderedMenuList(): List<OrderedMenuVO>{
        val selectedMenuMap = _orderedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(
                OrderedMenuVO(
                    menu = it,
                    count = selectedMenuMap[it]
                )
            )
        }
        return orderedMenuList
    }

    fun getSelectedOrderedMenuList(): List<OrderedMenuVO> {
        val selectedMenuMap = _selectedOrderedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(
                OrderedMenuVO(
                    menu = it,
                    count = selectedMenuMap[it]
                )
            )
        }
        return orderedMenuList
    }

    fun moveOrderedMenuToSelectedList(orderedMenuVO: OrderedMenuVO) {
        val orderedMenuMap = (_orderedMenuMap.value ?: mapOf()).toMutableMap()
        val selectedOrderedMenuMap = (_selectedOrderedMenuMap.value ?: mapOf()).toMutableMap()

        orderedMenuMap.remove(orderedMenuVO.menu)
        selectedOrderedMenuMap[orderedMenuVO.menu] = orderedMenuVO.count ?: 0

        _orderedMenuMap.postValue(orderedMenuMap)
        _selectedOrderedMenuMap.postValue(selectedOrderedMenuMap)
    }

    fun moveSelectedMenuToOrderedList(orderedMenuVO: OrderedMenuVO) {
        val orderedMenuMap = (_orderedMenuMap.value ?: mapOf()).toMutableMap()
        val selectedOrderedMenuMap = (_selectedOrderedMenuMap.value ?: mapOf()).toMutableMap()

        selectedOrderedMenuMap.remove(orderedMenuVO.menu)
        orderedMenuMap[orderedMenuVO.menu] = orderedMenuVO.count ?: 0

        _orderedMenuMap.postValue(orderedMenuMap)
        _selectedOrderedMenuMap.postValue(selectedOrderedMenuMap)
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

    fun postOrder(payMethod: PayMethod) {
        if (checkLeftPrice()) {
            if (orderVO.value == null){
                getOrderIdAndPostPayment(payMethod)
            } else {
                postUseMileage(orderVO.value!!.orderId)
                postPayment(payMethod, orderVO.value!!.orderId)
            }
        }

    }

    private fun getOrderIdAndPostPayment(payMethod: PayMethod) {
        viewModelScope.launch {
            try {
                attOrderRepository.postOrder(
                    1,
                    mileage.value?.mileageId,
                    totalPrice.value ?: 0,
                    OrderedMenusVO(
                        menus = totalOrderMenuList.value
                    )
                ).onSuccess { orderVO ->
                    _orderVO.postValue(orderVO)
                    postUseMileage(orderVO.orderId)
                    postPayment(payMethod, orderVO.orderId)
                }
            } catch (e: Exception) {
                Log.d("PayViewModel", "postOrder: ${e.message}")
            }
        }
    }

    private fun postPayment(payMethod: PayMethod, id: Int) {
        val cost = (getPriceFromMap(selectedOrderedMenuMap.value)) - (useMileage.value?.joinToString("")?.toInt() ?: 0)
        viewModelScope.launch {
            try {
                attOrderRepository.postPayment(
                    1,
                    PaymentVO(
                        paymentMethod = payMethod,
                        price = cost,
                        orderId = id
                    )
                ).onSuccess {paymentResultVO ->
                    if (paymentResultVO.leftPrice == 0) {
                        patchMileage()
                        // 결제 프로세스 끝!
                    }
                    _selectedOrderedMenuMap.postValue(mutableMapOf())
                    _totalPrice.postValue(paymentResultVO.leftPrice)
                }
            } catch (e: Exception) {
                Log.d("PayViewModel", "postPayment: ${e.message}")
            }
        }
    }

    private fun postUseMileage(id: Int) {
        useMileage.value?.let { mileageStack ->
            if (mileageStack.isNotEmpty()) {
                viewModelScope.launch {
                    try {
                        attOrderRepository.postPayment(
                            1,
                            PaymentVO(
                                paymentMethod = PayMethod.MILEAGE,
                                price = mileageStack.joinToString("").toInt(),
                                orderId = id
                            )
                        ).onSuccess {
                            _totalPrice.postValue(it.leftPrice)
                        }
                    } catch (e: Exception) {
                        Log.d("PayViewModel", "postUseMileage: ${e.message}")
                    }
                }
            }
        }
    }

    private fun patchMileage() {
        if (totalPrice.value != null && mileage.value != null) {
            viewModelScope.launch {
                attPosUserRepository.patchMileage(
                    1,
                    MileageVO(
                        mileageId = mileage.value?.mileageId ?: -1,
                        //default로 10% 적립
                        mileage = (totalPrice.value!! * 0.1).toInt()
                    )
                ).onSuccess {
                    _patchMileageState.postValue(NetworkState.Success(it))
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "마일리지 적립 실패"
                    _patchMileageState.postValue(NetworkState.Failure(errorMsg))
                }
            }
        }
    }

    private fun checkLeftPrice(): Boolean {
        totalPrice.value?.let {
            return it > 0
        }
        return false
    }

    companion object {
        private fun getPriceFromMap(menuWithCountMap: Map<MenuVO, Int>?): Int {
            return menuWithCountMap?.map { it.key.price * it.value }?.sum() ?: 0
        }
    }

}
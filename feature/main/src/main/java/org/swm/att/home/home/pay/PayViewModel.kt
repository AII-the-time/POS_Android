package org.swm.att.home.home.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.state.NetworkState
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PaymentResultVO
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository,
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
    private val _patchMileageState: MutableLiveData<NetworkState<MileageVO>> = MutableLiveData(
        NetworkState.Init)
    val patchMileageState: LiveData<NetworkState<MileageVO>> = _patchMileageState
    private val _postUseMileageState: MutableLiveData<NetworkState<PaymentResultVO>> = MutableLiveData(
        NetworkState.Init)
    val postUseMileageState: LiveData<NetworkState<PaymentResultVO>> = _postUseMileageState
    private val _postOrderState: MutableLiveData<NetworkState<OrderVO>> = MutableLiveData(
        NetworkState.Init)
    val postOrderState: LiveData<NetworkState<OrderVO>> = _postOrderState
    private val _postPaymentState: MutableLiveData<NetworkState<PaymentResultVO>> = MutableLiveData(
        NetworkState.Init)
    val postPaymentState: LiveData<NetworkState<PaymentResultVO>> = _postPaymentState

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

    fun postOrder(payMethod: PayMethod) {
        if (checkLeftPrice()) {
            if (orderVO.value == null){
                getOrderIdAndPostPayment(payMethod)
            } else {
                postUseMileage(payMethod, orderVO.value!!.orderId)
            }
        }

    }

    private fun getOrderIdAndPostPayment(payMethod: PayMethod) {

        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.postOrder(
                1,
                totalPrice.value ?: 0,
                OrderedMenusVO(
                    menus = totalOrderMenuList.value
                )
            ).onSuccess {
                _postOrderState.postValue(NetworkState.Success(it))
                postUseMileage(payMethod, it.orderId)
            }.onFailure {
                val errorMsg = if (it is HttpResponseException) it.message else "주문 실패"
                _postOrderState.postValue(NetworkState.Failure(errorMsg))
            }
        }
    }

    private fun postPayment(payMethod: PayMethod, id: Int) {
        val cost = (getPriceFromMap(orderedMenuMap.value)) - (useMileage.value?.joinToString("")?.toInt() ?: 0)
        viewModelScope.launch(attExceptionHandler) {
            attOrderRepository.postPayment(
                1,
                PaymentVO(
                    paymentMethod = payMethod,
                    price = cost,
                    orderId = id
                )
            ).onSuccess {paymentResultVO ->
                _postPaymentState.postValue(NetworkState.Success(paymentResultVO))
                _orderedMenuMap.postValue(mutableMapOf())
                _totalPrice.postValue(paymentResultVO.leftPrice)
            }.onFailure {
                val errorMsg = if (it is HttpResponseException) it.message else "결제 실패"
                _postPaymentState.postValue(NetworkState.Failure(errorMsg))
            }
        }
    }

    private fun postUseMileage(payMethod: PayMethod, id: Int) {
        if (useMileage.value.isNullOrEmpty().not()) {
            viewModelScope.launch(attExceptionHandler) {
                attOrderRepository.postPayment(
                    1,
                    PaymentVO(
                        paymentMethod = PayMethod.MILEAGE,
                        price = useMileage.value?.joinToString("")?.toInt() ?: 0,
                        orderId = id
                    )
                ).onSuccess {
                    _postUseMileageState.postValue(NetworkState.Success(it))
                    _useMileage.postValue(Stack())
                    postPayment(payMethod, id)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "마일리지 사용 실패"
                    _postUseMileageState.postValue(NetworkState.Failure(errorMsg))
                }
            }
        } else {
            postPayment(payMethod, id)
        }
    }

     fun patchMileage() {
        if (totalPrice.value != null && mileage.value != null) {
            viewModelScope.launch(attExceptionHandler) {
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
        private fun getPriceFromMap(menuWithCountMap: Map<OrderedMenuVO, Int>?): Int {
            return menuWithCountMap?.map { it.key.price * it.value }?.sum() ?: 0
        }
    }

}
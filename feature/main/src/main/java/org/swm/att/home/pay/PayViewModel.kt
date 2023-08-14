package org.swm.att.home.pay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.repository.AttPosUserRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): ViewModel() {
    private val _orderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val orderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _orderedMenuMap
    private val _selectedOrderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val selectedOrderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _selectedOrderedMenuMap
    private val _mileage = MutableLiveData<MileageVO>()
    val mileage: LiveData<MileageVO> = _mileage
    private val _useMileage = MutableLiveData<Stack<String>>()
    val useMileage: LiveData<Stack<String>> = _useMileage

    fun setOrderedMenuMap(orderedMenusVO: OrderedMenusVO) {
        orderedMenusVO.menus?.let {
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

    fun getMileage(phoneNumber: String) {
        viewModelScope.launch {
            try {
                attPosUserRepository.getMileage(1, phoneNumber).onSuccess {
                    _mileage.postValue(it)
                }
            } catch (e: Exception) {
                Log.d("PayViewModel", "getMileage: ${e.message}")
            }
        }
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

}
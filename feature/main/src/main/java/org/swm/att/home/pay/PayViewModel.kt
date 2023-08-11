package org.swm.att.home.pay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.MenuVO
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(

): ViewModel() {
    private val _orderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val orderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _orderedMenuMap
    private val _selectedOrderedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val selectedOrderedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _selectedOrderedMenuMap

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

}
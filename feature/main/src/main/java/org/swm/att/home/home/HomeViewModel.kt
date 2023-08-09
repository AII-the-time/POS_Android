package org.swm.att.home.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.repository.AttPosRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val attPosRepository: AttPosRepository
): ViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>?>()
    val selectedMenuMap: LiveData<MutableMap<MenuVO, Int>?> = _selectedMenuMap
    private val _categoryList = MutableLiveData<CategoriesVO>()
    val categoryList = _categoryList
    private val _midPhoneNumber = MutableLiveData<Stack<String>>()
    val midPhoneNumber: LiveData<Stack<String>> = _midPhoneNumber
    private val _endPhoneNumber = MutableLiveData<Stack<String>>()
    val endPhoneNumber: LiveData<Stack<String>> = _endPhoneNumber

    fun getCategories() {
        viewModelScope.launch {
            try {
                // mock data를 위해 임시로 sotreId를 1로 지정
                attPosRepository.getMenu(1).onSuccess {
                    _categoryList.postValue(it)
                }
            } catch (e: Exception) {
                Log.d("MenuViewModel", "getMenuList: ${e.message}")
            }
        }
    }

    fun addSelectedMenu(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()

        if (selectedMenuMap.containsKey(menu)) {
            selectedMenuMap[menu] = selectedMenuMap[menu]!! + 1
        } else {
            selectedMenuMap[menu] = 1
        }

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun minusSelectedMenuItem(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        if (count == 1) {
            selectedMenuMap.remove(menu)
        } else {
            selectedMenuMap[menu] = count - 1
        }

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun plusSelectedMenuItem(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        selectedMenuMap[menu] = count + 1

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun deletedAllMenuItem() {
        _selectedMenuMap.postValue(mutableMapOf())
    }

    fun addPhoneNumber(number: String) {
        val mid = _midPhoneNumber.value ?: Stack()
        if (mid.size < 4) {
            mid.push(number)
            _midPhoneNumber.postValue(mid)
        } else {
            val end = _endPhoneNumber.value ?: Stack()
            if(end.size < 4) {
                end.push(number)
                _endPhoneNumber.postValue(end)
            }
        }
    }

    fun removePhoneNumber() {
        val end = _endPhoneNumber.value ?: Stack()
        if (end.isNotEmpty()) {
            end.pop()
            _endPhoneNumber.value = end
        } else {
            val mid = _midPhoneNumber.value ?: Stack()
            if (mid.isNotEmpty()) {
                mid.pop()
                _midPhoneNumber.value = mid
            }
        }
    }

    fun clearPhoneNumber() {
        _midPhoneNumber.postValue(Stack())
        _endPhoneNumber.postValue(Stack())
    }

    fun getOrderedMenusVO(): OrderedMenusVO {
        val selectedMenuMap = _selectedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(
                OrderedMenuVO(
                    it,
                    selectedMenuMap[it]
                )
            )
        }

        return OrderedMenusVO(orderedMenuList)
    }
}
package org.swm.att.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.repository.AttMenuRepository
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<OrderedMenuVO, Int>?>()
    val selectedMenuMap: LiveData<MutableMap<OrderedMenuVO, Int>?> = _selectedMenuMap
    private val _midPhoneNumber = MutableLiveData<Stack<String>>()
    val midPhoneNumber: LiveData<Stack<String>> = _midPhoneNumber
    private val _endPhoneNumber = MutableLiveData<Stack<String>>()
    val endPhoneNumber: LiveData<Stack<String>> = _endPhoneNumber
    private val _getMenuState = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getMenuState: StateFlow<UiState<CategoriesVO>> = _getMenuState

    fun setSelectedMenusVO(selectedMenusVO: OrderedMenusVO) {
        selectedMenusVO.menus?.let {
            val selectedOrderedMenuMap = mutableMapOf<OrderedMenuVO, Int>()
            it.forEach { orderedMenu ->
                selectedOrderedMenuMap[orderedMenu] = orderedMenu.count ?: 1
            }
            _selectedMenuMap.postValue(selectedOrderedMenuMap)
        }
    }

    fun getCategories() {
        viewModelScope.launch(attExceptionHandler) {
            // mock data를 위해 임시로 storeId를 1로 지정
            attMenuRepository.getMenu(1)
                .collect { result ->
                    result.onSuccess { category ->
                        _getMenuState.value = UiState.Success(category)
                    }.onFailure { e ->
                        val errorMsg = if (e is HttpResponseException) e.message else "메뉴 불러오기 실패"
                        _getMenuState.value = UiState.Error(errorMsg)
                    }
                }
        }
    }

    fun addSelectedMenu(menu: OrderedMenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        if (selectedMenuMap.containsKey(menu)) {
            selectedMenuMap[menu] = selectedMenuMap[menu]!! + 1
        } else {
            selectedMenuMap[menu] = 1
        }

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun minusSelectedMenuItem(menu: OrderedMenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        if (count == 1) {
            selectedMenuMap.remove(menu)
        } else {
            selectedMenuMap[menu] = count - 1
        }

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun plusSelectedMenuItem(menu: OrderedMenuVO) {
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

    fun clearSelectedMenuList() {
        _selectedMenuMap.postValue(mutableMapOf())
    }

    fun getOrderedMenusVO(): OrderedMenusVO {
        val selectedMenuMap = _selectedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            orderedMenuList.add(it)
        }

        return OrderedMenusVO(orderedMenuList)
    }

    fun getPhoneNumber(): String {
        val mid = _midPhoneNumber.value ?: Stack()
        val end = _endPhoneNumber.value ?: Stack()
        return "010${mid.joinToString("")}${end.joinToString("")}"
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11
    }
}
package org.swm.att.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<OrderedMenuVO, Int>?>()
    val selectedMenuMap: LiveData<MutableMap<OrderedMenuVO, Int>?> = _selectedMenuMap
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
            val storeId = attPosUserRepository.getStoreId()
            attMenuRepository.getMenu(storeId)
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

    fun clearSelectedMenuList() {
        _selectedMenuMap.postValue(mutableMapOf())
    }

    fun getOrderedMenusVO(): OrderedMenusVO {
        val selectedMenuMap = _selectedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            it.count = selectedMenuMap[it]
            orderedMenuList.add(it)
        }
        return OrderedMenusVO(orderedMenuList)
    }

    fun clearGetMenuState() {
        _getMenuState.value = UiState.Loading
    }
}
package org.swm.att.home.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.repository.AttPosRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val attPosRepository: AttPosRepository
): ViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>>(mutableMapOf())
    val selectedMenuMap: LiveData<MutableMap<MenuVO, Int>> = _selectedMenuMap
    private val _categoryList = MutableLiveData<CategoriesVO>()
    val categoryList = _categoryList

    fun getCategories() {
        viewModelScope.launch {
            try {
                // mock data를 위해 임시로 sotreId를 1로 지정
                attPosRepository.getMenu(1).onSuccess {
                    _categoryList.value = it
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

        _selectedMenuMap.value = selectedMenuMap
    }

    fun minusSelectedMenuItem(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        if (count == 1) {
            selectedMenuMap.remove(menu)
        } else {
            selectedMenuMap[menu] = count - 1
        }

        _selectedMenuMap.value = selectedMenuMap
    }

    fun plusSelectedMenuItem(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        selectedMenuMap[menu] = count + 1

        _selectedMenuMap.value = selectedMenuMap
    }

    fun deletedAllMenuItem() {
        _selectedMenuMap.value = mutableMapOf()
    }
}
package org.swm.att.presenter.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.entity.response.MenuVO

class HomeViewModel: ViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<org.swm.att.domain.entity.response.MenuVO, Int>>(mutableMapOf())
    val selectedMenuMap: LiveData<MutableMap<org.swm.att.domain.entity.response.MenuVO, Int>> = _selectedMenuMap

    fun addSelectedMenu(menu: org.swm.att.domain.entity.response.MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()

        if (selectedMenuMap.containsKey(menu)) {
            selectedMenuMap[menu] = selectedMenuMap[menu]!! + 1
        } else {
            selectedMenuMap[menu] = 1
        }

        _selectedMenuMap.value = selectedMenuMap
    }

    fun minusSelectedMenuItem(menu: org.swm.att.domain.entity.response.MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        if (count == 1) {
            selectedMenuMap.remove(menu)
        } else {
            selectedMenuMap[menu] = count - 1
        }

        _selectedMenuMap.value = selectedMenuMap
    }

    fun plusSelectedMenuItem(menu: org.swm.att.domain.entity.response.MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        selectedMenuMap[menu] = count + 1

        _selectedMenuMap.value = selectedMenuMap
    }

    fun deleteSelectedMenuItem(menu: org.swm.att.domain.entity.response.MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        selectedMenuMap.remove(menu)
        _selectedMenuMap.value = selectedMenuMap
    }

    fun deletedAllMenuItem() {
        _selectedMenuMap.value = mutableMapOf()
    }
}
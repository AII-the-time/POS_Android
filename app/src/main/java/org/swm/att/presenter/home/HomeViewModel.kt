package org.swm.att.presenter.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.entity.response.MenuVO

class HomeViewModel: ViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<MenuVO, Int>>(mutableMapOf())
    val selectedMenuMap: LiveData<MutableMap<MenuVO, Int>> = _selectedMenuMap

    fun addSelectedMenu(menu: MenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()

        if (selectedMenuMap.containsKey(menu)) {
            selectedMenuMap[menu] = selectedMenuMap[menu]!! + 1
        } else {
            selectedMenuMap[menu] = 1
        }

        _selectedMenuMap.value = selectedMenuMap
    }
}
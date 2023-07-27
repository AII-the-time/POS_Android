package org.swm.att.presenter.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.repository.AttPosRepository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val attPosRepository: AttPosRepository
): ViewModel() {
    private val _menuList = MutableLiveData<List<MenuVO>>(arrayListOf())
    val menuList = _menuList

    fun getMenuList() {
        viewModelScope.launch {
            // mock data를 위해 임시로 sotreId를 1로 지정
            attPosRepository.getMenu(1).onSuccess {
                _menuList.value = it
            }.onFailure {

            }
        }
    }

}
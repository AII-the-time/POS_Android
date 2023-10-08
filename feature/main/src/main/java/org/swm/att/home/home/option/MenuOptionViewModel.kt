package org.swm.att.home.home.option

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
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

@HiltViewModel
class MenuOptionViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseViewModel() {
    private val _selectedOptionMap = MutableLiveData<Map<String, OptionTypeVO>>()
    val selectedOptionMap: LiveData<Map<String, OptionTypeVO>> = _selectedOptionMap
    private val _getMenuInfoState = MutableStateFlow<UiState<MenuWithRecipeVO>>(UiState.Loading)
    val getMenuInfoState: StateFlow<UiState<MenuWithRecipeVO>> = _getMenuInfoState

    fun changeSelectedOption(groupId: String, optionType: OptionTypeVO) {
        val optionsMap = (selectedOptionMap.value ?: mutableMapOf()).toMutableMap()
        optionsMap[groupId] = optionType
        _selectedOptionMap.postValue(optionsMap)
    }

    fun removeOption(groupId: String) {
        val optionsMap = (selectedOptionMap.value ?: mutableMapOf()).toMutableMap()
        optionsMap.remove(groupId)
        _selectedOptionMap.postValue(optionsMap)
    }

    fun getMenuInfo(menuId: Int) {
        _getMenuInfoState.value = UiState.Loading
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenuInfo(1, menuId).collect { result ->
                result.onSuccess { menu ->
                    menu.menuId = menuId
                    _getMenuInfoState.value = UiState.Success(menu)
                }.onFailure {  e ->
                    val errorMsg = if (e is HttpResponseException) e.message else "메뉴 상세 불러오기 실패"
                    _getMenuInfoState.value = UiState.Error(errorMsg)
                }
            }
        }
    }
}
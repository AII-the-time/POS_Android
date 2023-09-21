package org.swm.att.home.recipe

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttOrderRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseSelectableViewViewModel() {
    private val _selectedMenuInfo = MutableStateFlow<UiState<MenuVO>>(UiState.Loading)
    val selectedMenuInfo: StateFlow<UiState<MenuVO>> = _selectedMenuInfo
    private val _selectedMenuId = MutableStateFlow(0)
    val selectedMenuId: StateFlow<Int> = _selectedMenuId
    private val _getCategories = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getCategories: StateFlow<UiState<CategoriesVO>> = _getCategories

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
    }

    override fun setCurrentSelectedItemId(position: Int) {
    }

    fun getRegisteredMenus(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenu(storeId).collect { result ->
                result.onSuccess {
                    _getCategories.value = UiState.Success(it)
                }.onFailure {  e ->
                    val errorMsg = if (e is HttpResponseException) e.message else "메뉴 불러오기 실패"
                    _getCategories.value = UiState.Error(errorMsg)
                }
            }
        }
    }

}
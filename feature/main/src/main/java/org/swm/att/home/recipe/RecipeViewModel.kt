package org.swm.att.home.recipe

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.swm.att.common_ui.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.response.MenuVO

class RecipeViewModel: BaseSelectableViewViewModel() {
    private val _selectedMenuInfo = MutableStateFlow<UiState<MenuVO>>(UiState.Loading)
    val selectedMenuInfo: StateFlow<UiState<MenuVO>> = _selectedMenuInfo
    private val _selectedMenuId = MutableStateFlow(0)
    val selectedMenuId: StateFlow<Int> = _selectedMenuId

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        /* TODO */
    }

    override fun setCurrentSelectedItemId(position: Int) {
        /* TODO */
    }

}
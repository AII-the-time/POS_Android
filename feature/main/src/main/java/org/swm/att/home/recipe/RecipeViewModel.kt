package org.swm.att.home.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseSelectableViewViewModel() {
    private val _selectedMenuInfo = MutableStateFlow<UiState<MenuWithRecipeVO>>(UiState.Loading)
    val selectedMenuInfo: StateFlow<UiState<MenuWithRecipeVO>> = _selectedMenuInfo
    private val _selectedCategory = MutableLiveData<CategoryVO>()
    val selectedCategory: LiveData<CategoryVO> = _selectedCategory
    private val _selectedMenuId = MutableLiveData<Int>()
    val selectedMenuId: LiveData<Int> = _selectedMenuId
    private val _currentSelectedMenuId = MutableLiveData<Int>()
    val currentSelectedMenuId: LiveData<Int> = _currentSelectedMenuId
    private val _getCategories = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getCategories: StateFlow<UiState<CategoriesVO>> = _getCategories
    private val _postCategoryState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postCategoryState: StateFlow<UiState<Unit>> = _postCategoryState

    private val _isModify = MutableLiveData(false)
    val isModify: LiveData<Boolean> = _isModify
    private val _isCreate = MutableLiveData(false)
    val isCreate: LiveData<Boolean> = _isCreate

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        _selectedMenuInfo.value = UiState.Loading
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenuInfo(1, selectedItemId).collect() { result ->
                result.onSuccess {
                    _selectedMenuInfo.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "메뉴 상세 불러오기 실패"
                    _selectedMenuInfo.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    override fun setCurrentSelectedItemId(position: Int) {
        if (position != -1) {
            changeCreateState(false)
        }
        _currentSelectedMenuId.value = position
    }

    fun setSelectedMenuId(menuId: Int) {
        _selectedMenuId.value = menuId
    }

    fun setSelectedCategory(category: CategoryVO) {
        _selectedCategory.value = category
    }

    override fun changeSelectedState() {
        _selectedMenuId.postValue(currentSelectedMenuId.value)
    }

    fun getRegisteredMenus(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenu(storeId).collect { result ->
                result.onSuccess {
                    _getCategories.value = UiState.Success(it)
                    _selectedMenuId.value = 0
                }.onFailure {  e ->
                    val errorMsg = if (e is HttpResponseException) e.message else "메뉴 불러오기 실패"
                    _getCategories.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun changeModifyState() {
        _isModify.postValue(isModify.value?.not() ?: false)
    }

    fun changeCreateState(state: Boolean) {
        _isCreate.postValue(state)
    }

    fun postCategory(name: String) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.postCategory(1, name).collect { result ->
                result.onSuccess {
                    _postCategoryState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "카테고리 추가 실패"
                    _postCategoryState.value = UiState.Error(errorMsg)
                }
            }
        }
    }
}
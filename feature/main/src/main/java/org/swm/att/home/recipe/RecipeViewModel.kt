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
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseSelectableViewViewModel() {
    private val _selectedMenuInfo = MutableStateFlow<UiState<MenuWithRecipeVO>>(UiState.Loading)
    val selectedMenuInfo: StateFlow<UiState<MenuWithRecipeVO>> = _selectedMenuInfo
    private val _selectedCategory = MutableLiveData<CategoryVO?>()
    val selectedCategory: LiveData<CategoryVO?> = _selectedCategory
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

    private val _recipeListForNewMenu = MutableLiveData<List<RecipeVO>>()
    val recipeListForNewMenu: LiveData<List<RecipeVO>> = _recipeListForNewMenu

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        _selectedMenuInfo.value = UiState.Loading
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenuInfo(1, selectedItemId).collect() { result ->
                result.onSuccess {
                    _recipeListForNewMenu.postValue(it.recipe)
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
        val currentSelectedCategory = _selectedCategory.value
        val pastSelectedId = currentSelectedMenuId.value
        currentSelectedCategory?.let { category ->
            if (position != -1) {
                category.menus[position].isFocused = true
            }
            pastSelectedId?.let { pastId ->
                if (pastId != position && pastId != -1) {
                    category.menus[pastId].isFocused = false
                }
            }
            // focused 값을 변경함 새 리스트 submit
            _selectedCategory.postValue(category)
        }
        // 이전에 선택된 item 업데이트
        pastSelectedId?.let {
            changeSelectedState()
        }
        // 현재 선택된 item 업데이트
        _currentSelectedMenuId.postValue(position)
    }

    fun setSelectedCategory(category: CategoryVO) {
        _selectedCategory.value = category
        setDefaultSelectedState(category)
        resetRecipeListForNewMenu()
    }

    private fun setDefaultSelectedState(category: CategoryVO) {
        setCurrentSelectedItemId(0)
        _selectedCategory.value = null
        getSelectedItem(1, category.menus[0].id)
    }

    private fun resetRecipeListForNewMenu() {
        _recipeListForNewMenu.postValue(emptyList())
    }

    override fun changeSelectedState() {
        _selectedMenuId.postValue(currentSelectedMenuId.value)
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

    fun changeModifyState() {
        _isModify.postValue(isModify.value?.not() ?: false)
    }

    fun changeCreateState(state: Boolean) {
        _isCreate.postValue(state)
        _recipeListForNewMenu.postValue(emptyList())
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

    fun addTempNewRecipe() {
        val currentRecipeList = _recipeListForNewMenu.value?.toMutableList() ?: mutableListOf()
        val index = currentRecipeList.size
        currentRecipeList.add(
            index,
            RecipeVO(
                id = index,
                viewType = "RECIPE",
                name = "",
                amount = "",
                unit = "g"
            )
        )
        _recipeListForNewMenu.postValue(currentRecipeList)
    }

    fun deleteRecipeByPosition(position: Int?) {
        position?.let {
            val currentRecipeList = _recipeListForNewMenu.value?.toMutableList() ?: mutableListOf()
            currentRecipeList.removeAt(position)
            _recipeListForNewMenu.postValue(currentRecipeList)
        }
    }

    fun postNewMenu(storeId: Int, name: String?, price: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val newMenuItem = checkCreateNewMenu(name, price)
                attMenuRepository.postNewMenu(storeId, newMenuItem).collect { result ->
                    result.onSuccess {
                        _postCategoryState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "메뉴 추가 실패"
                        _postCategoryState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _postCategoryState.value = UiState.Error(e.message ?: "메뉴 추가 실패")
            }
        }
    }

    private fun checkCreateNewMenu(name: String?, price: String?): NewMenuVO {
        try {
            return NewMenuVO(
                name = requireNotNull(name),
                price = requireNotNull(price).toInt(),
                categoryId = requireNotNull(_selectedCategory.value?.categoryId),
                option = emptyList(),
                recipe = checkContentInRecipeListEmpty()
            )
        } catch (e: NumberFormatException) {
            throw Exception("가격은 숫자만 입력해주세요!")
        } catch (e: IllegalArgumentException) {
            throw Exception("메뉴 이름과 가격을 모두 입력해주세요!")
        }
    }

    private fun checkContentInRecipeListEmpty(): List<RecipeVO>? {
        for (recipe in _recipeListForNewMenu.value ?: emptyList()) {
            if (recipe.name.isEmpty() || recipe.amount.isEmpty()) {
                throw Exception("레시피 내용을 모두 입력해주세요!")
            }
        }
        return _recipeListForNewMenu.value
    }
}
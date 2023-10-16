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
import org.swm.att.domain.entity.response.CategoryIdVO
import org.swm.att.domain.entity.response.CategoryVO
import org.swm.att.domain.entity.response.MenuIdVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository
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
    private val _postCategoryState = MutableStateFlow<UiState<CategoryIdVO>>(UiState.Loading)
    val postCategoryState: StateFlow<UiState<CategoryIdVO>> = _postCategoryState
    private val _postMenuState = MutableStateFlow<UiState<MenuIdVO>>(UiState.Loading)
    val postMenuState: StateFlow<UiState<MenuIdVO>> = _postMenuState

    private val _isModify = MutableLiveData(false)
    val isModify: LiveData<Boolean> = _isModify
    private val _isCreate = MutableLiveData(false)
    val isCreate: LiveData<Boolean> = _isCreate

    private val _recipeListForNewMenu = MutableLiveData<List<RecipeVO>>()
    val recipeListForNewMenu: LiveData<List<RecipeVO>> = _recipeListForNewMenu

    private var storeId: Int? = null

    override fun getSelectedItem(selectedItemId: Int) {
        _selectedMenuInfo.value = UiState.Loading
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenuInfo(getStoreId(), selectedItemId).collect() { result ->
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

    fun setDefaultSelectedItem() {
        val currentSelectedCategory = _selectedCategory.value
        if (!currentSelectedCategory?.menus.isNullOrEmpty()) {
            currentSelectedCategory!!.menus[0].isFocused = true
            _currentSelectedMenuId.postValue(0)
        }
    }

    override fun setCurrentSelectedItemId(position: Int) {
        if (position != -1) {
            changeCreateState(false)
        }
        val currentSelectedCategory = _selectedCategory.value
        val pastSelectedId = currentSelectedMenuId.value
        if (!currentSelectedCategory?.menus.isNullOrEmpty()) {
            // 현재 선택된 item의 focused 값을 true로 변경
            if (position != -1) {
                currentSelectedCategory!!.menus[position].isFocused = true
                getSelectedItem(currentSelectedCategory.menus[position].id)
            }
            // 이전에 선택된 item의 focused 값을 false로 변경
            pastSelectedId?.let { pastId ->
                if (pastId != position && pastId != -1) {
                    currentSelectedCategory!!.menus[pastId].isFocused = false
                }
            }

            // 이전에 선택된 item 업데이트
            pastSelectedId?.let {
                changeSelectedState()
            }
            // 현재 선택된 item 업데이트
            _currentSelectedMenuId.postValue(position)
        }
    }

    fun setSelectedCategory(category: CategoryVO) {
        processPastSelectedItemBeforeCategoryChange()
        _selectedCategory.value = category
        resetRecipeListForNewMenu()
    }

    private fun processPastSelectedItemBeforeCategoryChange() {
        val pastSelectedCategory = _selectedCategory.value
        // 이전 category의 선택된 item의 focused 값을 false로 변경
        pastSelectedCategory?.let { category ->
            val pastSelectedId = currentSelectedMenuId.value
            pastSelectedId?.let { pastId ->
                if (pastId != -1) {
                    category.menus[pastId].isFocused = false
                    changeSelectedState()
                }
            }
        }
        // 이전 category의 선택된 item을 초기화
        _currentSelectedMenuId.value = -1
    }

    private fun resetRecipeListForNewMenu() {
        _recipeListForNewMenu.postValue(emptyList())
    }

    override fun changeSelectedState() {
        _selectedMenuId.postValue(currentSelectedMenuId.value)
    }

    fun getRegisteredMenus() {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenu(getStoreId()).collect { result ->
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
            attMenuRepository.postCategory(getStoreId(), name).collect { result ->
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

    fun postNewMenu(name: String?, price: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val newMenuItem = checkCreateNewMenu(name, price)
                attMenuRepository.postNewMenu(getStoreId(), newMenuItem).collect { result ->
                    result.onSuccess {
                        _postMenuState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "메뉴 추가 실패"
                        _postMenuState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _postMenuState.value = UiState.Error(e.message ?: "메뉴 추가 실패")
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

    private suspend fun getStoreId(): Int {
        if (storeId == null) {
            storeId = attPosUserRepository.getStoreId()
        }
        return storeId as Int
    }
}
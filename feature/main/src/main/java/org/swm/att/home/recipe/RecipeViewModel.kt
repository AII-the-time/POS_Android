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
import org.swm.att.domain.entity.response.OptionListVO
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.domain.entity.response.StockIdVO
import org.swm.att.domain.entity.response.StockWithMixedListVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository
): BaseSelectableViewViewModel() {
    //category
    private val _getCategories = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getCategories: StateFlow<UiState<CategoriesVO>> = _getCategories
    private val _postCategoryState = MutableStateFlow<UiState<CategoryIdVO>>(UiState.Loading)
    val postCategoryState: StateFlow<UiState<CategoryIdVO>> = _postCategoryState
    private val _selectedCategory = MutableLiveData<CategoryVO?>()
    val selectedCategory: LiveData<CategoryVO?> = _selectedCategory
    private val _deleteCategoryState = MutableStateFlow<UiState<CategoryIdVO>>(UiState.Loading)
    val deleteCategoryState: StateFlow<UiState<CategoryIdVO>> = _deleteCategoryState
    private val _updateCategoryState = MutableStateFlow<UiState<CategoryIdVO>>(UiState.Loading)
    val updateCategoryState: StateFlow<UiState<CategoryIdVO>> = _updateCategoryState

    //menu
    private val _selectedMenuInfo = MutableStateFlow<UiState<MenuWithRecipeVO>>(UiState.Loading)
    val selectedMenuInfo: StateFlow<UiState<MenuWithRecipeVO>> = _selectedMenuInfo
    private val _selectedMenuId = MutableLiveData<Int>()
    val selectedMenuId: LiveData<Int> = _selectedMenuId
    private val _currentSelectedMenuId = MutableLiveData<Int>()
    val currentSelectedMenuId: LiveData<Int> = _currentSelectedMenuId
    private val _postMenuState = MutableStateFlow<UiState<MenuIdVO>>(UiState.Loading)
    val postMenuState: StateFlow<UiState<MenuIdVO>> = _postMenuState
    private val _deleteMenuState = MutableStateFlow<UiState<MenuIdVO>>(UiState.Loading)
    val deleteMenuState: StateFlow<UiState<MenuIdVO>> = _deleteMenuState
    private val _updateMenuState = MutableStateFlow<UiState<MenuIdVO>>(UiState.Loading)
    val updateMenuState: StateFlow<UiState<MenuIdVO>> = _updateMenuState

    //option
    private val _getAllOfOptionState = MutableStateFlow<UiState<OptionListVO>>(UiState.Loading)
    val getAllOfOptionState: StateFlow<UiState<OptionListVO>> = _getAllOfOptionState
    private var selectedOptionList = mutableListOf<Int>()

    //stock
    private val _getAllOfStockState = MutableStateFlow<UiState<StockWithMixedListVO>>(UiState.Loading)
    val getAllOfStockState: StateFlow<UiState<StockWithMixedListVO>> = _getAllOfStockState
    private val _postNewStockState = MutableStateFlow<UiState<StockIdVO>>(UiState.Loading)
    val postNewStockState: StateFlow<UiState<StockIdVO>> = _postNewStockState

    //local value
    private val _isModify = MutableLiveData<Boolean>(null)
    val isModify: LiveData<Boolean?> = _isModify
    private val _isCreate = MutableLiveData<Boolean>(null)
    val isCreate: LiveData<Boolean?> = _isCreate
    private val _recipeMapForNewMenu = MutableLiveData<MutableMap<Int, RecipeVO>>()
    val recipeMapForNewMenu: LiveData<MutableMap<Int, RecipeVO>> = _recipeMapForNewMenu
    private val _optionList = MutableLiveData<List<OptionVO>>()
    val optionList: LiveData<List<OptionVO>> = _optionList
    private var storeId: Int? = null
    private var lastSelectedMenuId: Int? = null
    private val _isCategoryModify = MutableLiveData(false)
    val isCategoryModify: LiveData<Boolean> = _isCategoryModify

    //category
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

    fun deleteCategory() {
        viewModelScope.launch(attExceptionHandler) {
            val categoryId = requireNotNull(_selectedCategory.value?.categoryId)
            attMenuRepository.deleteCategory(getStoreId(), categoryId).collect { result ->
                result.onSuccess {
                    _deleteCategoryState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "카테고리 삭제 실패"
                    _deleteCategoryState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun updateCategory(categoryName: String) {
        viewModelScope.launch(attExceptionHandler) {
            val categoryId = requireNotNull(_selectedCategory.value?.categoryId)
            attMenuRepository.updateCategory(getStoreId(), categoryId, categoryName).collect { result ->
                result.onSuccess {
                    _updateCategoryState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "카테고리 수정 실패"
                    _updateCategoryState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun setSelectedCategory(category: CategoryVO) {
        processPastSelectedItemBeforeCategoryChange()
        _selectedCategory.value = category
        initRecipeMap(null)
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

    //menu
    override fun getSelectedItem(selectedItemId: Int) {
        lastSelectedMenuId = selectedItemId
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenuInfo(getStoreId(), selectedItemId).collect() { result ->
                result.onSuccess {
                    initRecipeMap(it.recipe)
                    initOptionList(it.option)
                    it.id = selectedItemId
                    _selectedMenuInfo.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "메뉴 상세 불러오기 실패"
                    _selectedMenuInfo.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun deleteMenu() {
        viewModelScope.launch(attExceptionHandler) {
            lastSelectedMenuId?.let { id ->
                attMenuRepository.deleteMenu(getStoreId(), id).collect { result ->
                    result.onSuccess {
                        _deleteMenuState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "메뉴 삭제 실패"
                        _deleteMenuState.value = UiState.Error(errorMsg)
                    }
                }
            }
        }
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

    fun postNewMenu(name: String?, price: String?, recipes: List<RecipeVO>?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val newMenuItem = checkCreateNewMenu(null, name, price, recipes)
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

    fun updateMenu(name: String?, price: String?, recipes: List<RecipeVO>?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val updateMenuItem = checkCreateNewMenu(lastSelectedMenuId, name, price, recipes)
                attMenuRepository.updateMenu(getStoreId(), updateMenuItem).collect { result ->
                    result.onSuccess {
                        _updateMenuState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "메뉴 수정 실패"
                        _updateMenuState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _updateMenuState.value = UiState.Error(e.message ?: "메뉴 수정 실패")
            }
        }
    }

    fun setDefaultSelectedItem() {
        val currentSelectedCategory = _selectedCategory.value
        if (!currentSelectedCategory?.menus.isNullOrEmpty()) {
            currentSelectedCategory!!.menus[0].isFocused = true
            getSelectedItem(currentSelectedCategory.menus[0].id)
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

    override fun changeSelectedState() {
        _selectedMenuId.postValue(currentSelectedMenuId.value)
    }

    private fun checkCreateNewMenu(id: Int?, name: String?, price: String?, recipes: List<RecipeVO>?): NewMenuVO {
        try {
            return NewMenuVO(
                id = id,
                name = requireNotNull(name),
                price = requireNotNull(price).toInt(),
                categoryId = requireNotNull(_selectedCategory.value?.categoryId),
                option = selectedOptionList,
                recipe = checkContentInRecipeListEmpty(recipes)
            )
        } catch (e: NumberFormatException) {
            throw Exception("가격은 숫자만 입력해주세요!")
        } catch (e: IllegalArgumentException) {
            throw Exception("메뉴 이름과 가격을 모두 입력해주세요!")
        }
    }

    private fun checkContentInRecipeListEmpty(recipes: List<RecipeVO>?): List<RecipeVO>? {
        for (recipe in recipes ?: emptyList()) {
            if (recipe.coldRegularAmount?.isNullOrEmpty() == true) {
                throw Exception("레시피 내용을 모두 입력해주세요!")
            }
        }
        return recipes
    }

    fun getLastSelectedMenu() {
        lastSelectedMenuId?.let {
            _selectedMenuInfo.value = UiState.Loading
            getSelectedItem(it)
        }
    }

    fun checkLastSelectedMenu(id: Int): Boolean {
        lastSelectedMenuId?.let { lastSelectedId ->
            return id == lastSelectedId
        }
        return false
    }

    //option
    fun getAllOfOption() {
        viewModelScope.launch(attExceptionHandler) {
            val storeId = attPosUserRepository.getStoreId()
            attMenuRepository.getAllOfOption(storeId).collect { result ->
                result.onSuccess {
                    _getAllOfOptionState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "옵션 불러오기 실패"
                    _getAllOfOptionState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun addSelectedOption(optionId: Int) {
        selectedOptionList.add(optionId)
    }

    fun removeSelectedOption(optionId: Int) {
        selectedOptionList.remove(optionId)
    }

    fun clearSelectedOption() {
        selectedOptionList = mutableListOf()
    }

    //stock
    fun getAllOfStocks(query: String) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getAllOfStock(getStoreId(), query).collect { result ->
                result.onSuccess {
                    _getAllOfStockState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "재고 불러오기 실패"
                    _getAllOfStockState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun postNewStock(newItemName: String) {
        viewModelScope.launch(attExceptionHandler) {
            val newStock = StockWithMixedVO(newItemName)
            attMenuRepository.postNewStock(getStoreId(), newStock).collect { result ->
                result.onSuccess {
                    _postNewStockState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "재고 추가 실패"
                    _postNewStockState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    //recipe
    private fun initRecipeMap(recipes: List<RecipeVO>?) {
        val recipeMap = recipes?.map { it.id to it }?.toMap() ?: emptyMap()
        _recipeMapForNewMenu.postValue(recipeMap.toMutableMap())
    }

    //option
    private fun initOptionList(option: List<OptionVO>) {
        _optionList.postValue(option)
        selectedOptionList = mutableListOf()
        option.forEach { optionVO ->
            optionVO.options.forEach { optionType ->
                if (optionType.isSelectable) {
                    addSelectedOption(optionType.id)
                }
            }
        }
    }

    //local value
    fun changeModifyState(state: Boolean) {
        _isModify.postValue(state)
    }

    fun changeCreateState(state: Boolean) {
        _isCreate.postValue(state)
        if (state) {
            _recipeMapForNewMenu.postValue(mutableMapOf())
        }
    }

    fun changeCategoryModifyState(state: Boolean) {
        _isCategoryModify.postValue(state)
    }

    fun addNewRecipe(stockWithMixedVO: StockWithMixedVO) {
        val currentRecipeMap = recipeMapForNewMenu.value ?: mutableMapOf()
        currentRecipeMap[stockWithMixedVO.id] = RecipeVO(stockWithMixedVO)
        _recipeMapForNewMenu.postValue(currentRecipeMap)
    }

    fun deleteRecipeByPosition(stockId: Int) {
        val currentRecipeMap = recipeMapForNewMenu.value
        currentRecipeMap?.let { map ->
            map.remove(stockId)
            _recipeMapForNewMenu.postValue(currentRecipeMap!!)
        }
    }

    private suspend fun getStoreId(): Int {
        if (storeId == null) {
            storeId = attPosUserRepository.getStoreId()
        }
        return storeId as Int
    }

    fun setGetAllOfOptionStateDefault() {
        _getAllOfOptionState.value = UiState.Loading
    }

    fun setGetAllOfStockStateDefault() {
        _getAllOfStockState.value = UiState.Loading
    }

    fun getEditState(): String {
        return if (isCreate.value == true) {
            RecipeFragment.CREATE
        } else if (isModify.value == true) {
            RecipeFragment.MODIFY
        } else {
            RecipeFragment.NONE
        }
    }

    fun resetUpdateStockState() {
        _updateMenuState.value = UiState.Loading
    }
}
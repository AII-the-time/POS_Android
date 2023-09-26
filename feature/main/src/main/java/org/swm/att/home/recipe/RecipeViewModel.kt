package org.swm.att.home.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): BaseSelectableViewViewModel() {
    private val _selectedMenuInfo = MutableLiveData<MenuWithRecipeVO>()
    val selectedMenuInfo: LiveData<MenuWithRecipeVO> = _selectedMenuInfo
    private val _selectedCategory = MutableLiveData<CategoryVO>()
    val selectedCategory: LiveData<CategoryVO> = _selectedCategory
    private val _selectedMenuId = MutableLiveData<Int>()
    val selectedMenuId: LiveData<Int> = _selectedMenuId
    private val _currentSelectedMenuId = MutableLiveData<Int>()
    val currentSelectedMenuId: LiveData<Int> = _currentSelectedMenuId
    private val _getCategories = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getCategories: StateFlow<UiState<CategoriesVO>> = _getCategories

    private val _isModify = MutableLiveData(false)
    val isModify: LiveData<Boolean> = _isModify

    override fun getSelectedItem(storeId: Int, selectedItemId: Int) {
        _selectedMenuInfo.postValue(
            MenuWithRecipeVO(
                categoryId = -1,
                categoryName = "커피",
                menuName = "아메리카노",
                price = 3000.toBigDecimal(),
                option = listOf(
                    OptionVO(
                        id = 1,
                        optionType = "샷추가",
                        options = listOf(
                            OptionTypeVO(
                                id = 7,
                                name = "1샷",
                                price = 500
                            ),
                            OptionTypeVO(
                                id = 8,
                                name = "2샷",
                                price = 500
                            ),
                            OptionTypeVO(
                                id = 9,
                                name = "3샛",
                                price = 500
                            )
                        )
                    ),
                    OptionVO(
                        id = 3,
                        optionType = "온도",
                        options = listOf(
                            OptionTypeVO(
                                id = -1,
                                name = "hot",
                                price = 0
                            ),
                            OptionTypeVO(
                                id = -1,
                                name = "ice",
                                price = 0
                            )
                        )
                    )
                ),
                recipe = listOf(
                    RecipeVO(
                        id = 1,
                        name = "원두",
                        amount = 30,
                        unit = "g"
                    ),
                    RecipeVO(
                        id = 2,
                        name = "얼음",
                        amount = 30,
                        unit = "개"
                    ),
                    RecipeVO(
                        id = 3,
                        name = "물",
                        amount = 30,
                        unit = "ml"
                    )
                )
            )
        )
    }

    override fun setCurrentSelectedItemId(position: Int) {
        _currentSelectedMenuId.value = position
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

}
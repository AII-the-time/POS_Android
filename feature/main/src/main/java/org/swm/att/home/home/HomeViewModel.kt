package org.swm.att.home.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.util.alarm.AlarmManager
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository,
    private val attOrderRepository: AttOrderRepository,
    @ApplicationContext private val context: Context
): BaseViewModel() {
    private val _selectedMenuMap = MutableLiveData<MutableMap<OrderedMenuVO, Int>?>()
    val selectedMenuMap: LiveData<MutableMap<OrderedMenuVO, Int>?> = _selectedMenuMap
    private val _getMenuState = MutableStateFlow<UiState<CategoriesVO>>(UiState.Loading)
    val getMenuState: StateFlow<UiState<CategoriesVO>> = _getMenuState

    private val _storeIdExist = MutableLiveData<Int>()
    val storeIdExist: LiveData<Int> = _storeIdExist

    private val todayPreorderList = arrayListOf<PreorderVO>()
    private var page = 1

    private val _registerStoreState = MutableStateFlow<UiState<StoreIdVO>>(UiState.Loading)
    val registerStoreState: StateFlow<UiState<StoreIdVO>> = _registerStoreState

    fun checkStoreId() {
        viewModelScope.launch(attExceptionHandler) {
            val storeId = attPosUserRepository.getStoreId()
            _storeIdExist.postValue(storeId)
        }
    }


    fun setSelectedMenusVO(selectedMenusVO: OrderedMenusVO) {
        selectedMenusVO.menus?.let {
            val selectedOrderedMenuMap = mutableMapOf<OrderedMenuVO, Int>()
            it.forEach { orderedMenu ->
                selectedOrderedMenuMap[orderedMenu] = orderedMenu.count ?: 1
            }
            _selectedMenuMap.postValue(selectedOrderedMenuMap)
        }
    }

    fun getCategories(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getMenu(storeId)
                .collect { result ->
                    result.onSuccess { category ->
                        _getMenuState.value = UiState.Success(category)
                    }.onFailure { e ->
                        val errorMsg = if (e is HttpResponseException) e.message else "메뉴 불러오기 실패"
                        _getMenuState.value = UiState.Error(errorMsg)
                    }
                }
        }
    }

    fun addSelectedMenu(menu: OrderedMenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        if (selectedMenuMap.containsKey(menu)) {
            selectedMenuMap[menu] = selectedMenuMap[menu]!! + 1
        } else {
            selectedMenuMap[menu] = 1
        }

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun minusSelectedMenuItem(menu: OrderedMenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        selectedMenuMap[menu]?.let {
            val count = selectedMenuMap[menu]!!

            if (count == 1) {
                selectedMenuMap.remove(menu)
            } else {
                selectedMenuMap[menu] = count - 1
            }

            _selectedMenuMap.postValue(selectedMenuMap)
        }
    }

    fun plusSelectedMenuItem(menu: OrderedMenuVO) {
        val selectedMenuMap = (_selectedMenuMap.value ?: mapOf()).toMutableMap()
        val count = selectedMenuMap[menu]!!

        selectedMenuMap[menu] = count + 1

        _selectedMenuMap.postValue(selectedMenuMap)
    }

    fun deletedAllMenuItem() {
        _selectedMenuMap.postValue(mutableMapOf())
    }

    fun clearSelectedMenuList() {
        _selectedMenuMap.postValue(mutableMapOf())
    }

    fun getOrderedMenusVO(): OrderedMenusVO {
        val selectedMenuMap = _selectedMenuMap.value ?: mapOf()
        val orderedMenuList = mutableListOf<OrderedMenuVO>()
        selectedMenuMap.keys.forEach {
            it.count = selectedMenuMap[it]
            orderedMenuList.add(it)
        }
        return OrderedMenusVO(orderedMenuList)
    }

    fun clearGetMenuState() {
        _getMenuState.value = UiState.Loading
    }

    fun registerStore() {
        viewModelScope.launch(attExceptionHandler) {
            // 임시 token을 활용해 가게 바로 등록
            // for test with base data
            //attPosUserRepository.registerStoreForTest(
            // real
            attPosUserRepository.registerStore(
                StoreVO(
                    name = "temp",
                    address = "temp",
                    openingHours = emptyList()
                )
            ).collect { result ->
                result.onSuccess {
                    _registerStoreState.value = UiState.Success(it)
                    setStoreId(it.storeId)
                    _storeIdExist.postValue(it.storeId)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "가게 등록 실패"
                    _registerStoreState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun getTodayPreorder(storeId: Int) {
        viewModelScope.launch(attExceptionHandler) {
            val date =
                Formatter.getStringByDateTimeBaseFormatter(Calendar.getInstance().time.getUTCDateTime())
            attOrderRepository.getPreOrders(storeId, page, date).collect { result ->
                result.onSuccess {
                    todayPreorderList.addAll(it.preOrders)
                    page += 1
                    if (page < it.lastPage) {
                        getTodayPreorder(storeId)
                    } else {
                        setPreorderAlarm()
                    }
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "예약 내역 불러오기 실패"
                    Log.e("getTodayPreorder", errorMsg!!)
                }
            }
        }
    }

    private fun setPreorderAlarm() {
        for (preorderItem in todayPreorderList) {
            // 각 아이템에 대한 알람 시간과 기타 설정 가져옴
            AlarmManager.setPreorderAlarm(
                context,
                preorderItem.orderedFor,
                preorderItem.phone,
                preorderItem.totalCount,
                preorderItem.id
            )
        }
    }

    fun setStoreId(storeId: Int) {
        viewModelScope.launch(attExceptionHandler)  {
            attPosUserRepository.saveStoreId(storeId)
        }
    }

    fun clearUiValues() {
        _registerStoreState.value = UiState.Loading
        _getMenuState.value = UiState.Loading
        clearSelectedMenuList()
    }

    fun isRegisteredPreorderAlarm(): Boolean {
        return todayPreorderList.isNotEmpty()
    }
}
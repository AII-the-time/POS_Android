package org.swm.att.home.stock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter.getStringByDateTimeBaseFormatter
import org.swm.att.common_ui.util.getIntOrNull
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.StockIdVO
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.domain.entity.response.StockWithStateListVO
import org.swm.att.domain.entity.response.StockWithStateVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.stock.StockFragment.Companion.ALL
import org.swm.att.home.stock.StockFragment.Companion.CREATE
import org.swm.att.home.stock.StockFragment.Companion.LACK
import org.swm.att.home.stock.StockFragment.Companion.MODIFY
import org.swm.att.home.stock.StockFragment.Companion.UNKNOWN
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository
): BaseSelectableViewViewModel() {
    //uiState
    private val _getStockWithStateListState = MutableStateFlow<UiState<StockWithStateListVO>>(UiState.Loading)
    val getStockWithStateListState: StateFlow<UiState<StockWithStateListVO>> = _getStockWithStateListState
    private val _postNewStockState = MutableStateFlow<UiState<StockIdVO>>(UiState.Loading)
    val postNewStockState: StateFlow<UiState<StockIdVO>> = _postNewStockState
    private val _updateStockState = MutableStateFlow<UiState<StockIdVO>>(UiState.Loading)
    val updateStockState: StateFlow<UiState<StockIdVO>> = _updateStockState
    private val _deleteStockState = MutableStateFlow<UiState<StockIdVO>>(UiState.Loading)
    val deleteStockState: StateFlow<UiState<StockIdVO>> = _deleteStockState

    //stock list
    private var allStocks: List<StockWithStateVO> = arrayListOf()
    private var cautionStocks: List<StockWithStateVO> = arrayListOf()
    private var unknownStocks: List<StockWithStateVO> = arrayListOf()
    private var currentBaseStockList: List<StockWithStateVO> = arrayListOf()
    private val _currentResultStockList = MutableLiveData<List<StockWithStateVO>>()
    val currentResultStockList: LiveData<List<StockWithStateVO>> = _currentResultStockList

    // selected stock
    private val _getStockByIdState = MutableStateFlow<UiState<StockVO>>(UiState.Loading)
    val getStockByIdState: StateFlow<UiState<StockVO>> = _getStockByIdState

    //date picker
    private val _lastInventoryDate = MutableLiveData<Date>()
    val lastInventoryDate: LiveData<Date> = _lastInventoryDate

    //unit
    private val _unitString = MutableLiveData<String>()
    val unitString: LiveData<String> = _unitString

    //local value
    private var storeId: Int? = null
    private val _isCreate = MutableLiveData<Boolean>(null)
    val isCreate: LiveData<Boolean?> = _isCreate
    private val _isModify = MutableLiveData<Boolean>(null)
    val isModify: LiveData<Boolean?> = _isModify
    private var lastSelectedStockId: Int? = null
    private val _lastSelectedStockState = MutableLiveData<String>()
    val lastSelectedStockState: LiveData<String> = _lastSelectedStockState

    //selectable override
    override fun getSelectedItem(selectedItemId: Int) {
        lastSelectedStockId = selectedItemId
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getStockById(getStoreId(), selectedItemId).collect { result ->
                result.onSuccess {
                    _getStockByIdState.value = UiState.Success(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "재고 정보 가져오기 실패"
                    _getStockByIdState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    override fun setCurrentSelectedItemId(position: Int) {
       /* nothing */
    }

    override fun changeSelectedState() {
        /* nothing */
    }

    //stock
    fun postNewStock(name: String?, currentAmount: String?, noticeThreshold: String?, perAmount: String?, perPrice: String?, unit: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val newStock = checkCreateNewStock(null, name, currentAmount, noticeThreshold, perAmount, perPrice, unit)
                attMenuRepository.postNewStock(getStoreId(), newStock).collect { result ->
                    result.onSuccess {
                        _postNewStockState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "재고 등록 실패"
                        _postNewStockState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _postNewStockState.value = UiState.Error(e.message ?: "재고 등록 실패")
            }
        }
    }

    fun updateStock(name: String?, currentAmount: String?, noticeThreshold: String?, perAmount: String?, perPrice: String?, unit: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val stock = checkCreateNewStock(lastSelectedStockId, name, currentAmount, noticeThreshold, perAmount, perPrice, unit)
                attMenuRepository.updateStock(getStoreId(), stock).collect { result ->
                    result.onSuccess {
                        _updateStockState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "재고 수정 실패"
                        _updateStockState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _updateStockState.value = UiState.Error(e.message ?: "재고 수정 실패")
            }
        }
    }

    private fun checkCreateNewStock(id: Int?, name: String?, currentAmount: String?, noticeThreshold: String?, perAmount: String?, perPrice: String?, unit: String?): StockVO {
        try {
            val inventoryDate = getStringByDateTimeBaseFormatter(lastInventoryDate.value?.getUTCDateTime() ?: Date().getUTCDateTime())
            return StockVO(
                id = id,
                name = requireNotNull(name),
                amount = currentAmount?.toInt(),
                unit = unit,
                price = perPrice,
                currentAmount = if (perAmount != null && currentAmount != null ) {perAmount.toInt() * currentAmount.toInt()} else null,
                noticeThreshold = noticeThreshold.getIntOrNull(),
                updatedAt = inventoryDate
            )
        } catch (e: NumberFormatException) {
            throw Exception("수량은 숫자만 입력해주세요!")
        } catch (e: IllegalArgumentException) {
            throw Exception("빈칸을 모두 채워주세요!")
        }
    }

    fun getStockWithStateList(selectedId: Int? = null) {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getStockWithStateList(getStoreId()).collect { result ->
                result.onSuccess {
                    _getStockWithStateListState.value = UiState.Success(it)
                    setBaseStockList(it.stocks)
                    _currentResultStockList.postValue(it.stocks)
                    if (selectedId == null) {
                        getSelectedItem(it.stocks.first().id)
                    } else {
                        getSelectedItem(selectedId)
                    }
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "재고 가져오기 실패"
                    _getStockWithStateListState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun deleteStock() {
        lastSelectedStockId?.let { id ->
            viewModelScope.launch(attExceptionHandler) {
                attMenuRepository.deleteStock(getStoreId(), id).collect { result ->
                    result.onSuccess {
                        _deleteStockState.value = UiState.Success(it)
                    }.onFailure {
                        Log.d("deleteStock", "deleteStock: ${it}")
                        val errorMsg = if (it is HttpResponseException) it.message else "재고 삭제 실패"
                        _deleteStockState.value = UiState.Error(errorMsg)
                    }
                }
            }
        }
    }

    private fun setBaseStockList(stocks: List<StockWithStateVO>) {
        currentBaseStockList = stocks
        allStocks = stocks
        cautionStocks = stocks.filter {
            it.state == "OUT_OF_STOCK" || it.state == "EMPTY"
        }
        unknownStocks = stocks.filter {
            it.state == "UNKNOWN"
        }
    }

    fun stockSearchResult(query: String?) {
        if (query.isNullOrEmpty()) {
            _currentResultStockList.postValue(currentBaseStockList)
        } else {
            val result = currentBaseStockList.filter {
                it.name.contains(query)
            } ?: arrayListOf()
            _currentResultStockList.postValue(result)
        }
    }

    fun getLastSelectedStock() {
        lastSelectedStockId?.let {
            getSelectedItem(it)
        }
    }

    //etc
    private suspend fun getStoreId(): Int {
        if (storeId == null) {
            storeId = attPosUserRepository.getStoreId()
        }
        return storeId as Int
    }

    fun changeCreateState(state: Boolean) {
        _isCreate.postValue(state)
    }

    fun changeModifyState(state: Boolean) {
        _isModify.postValue(state)
    }

    fun setDefaultStockList(type: String) {
        when(type) {
            ALL -> {
                currentBaseStockList = allStocks
                _currentResultStockList.postValue(allStocks)
            }
            LACK -> {
                currentBaseStockList = cautionStocks
                _currentResultStockList.postValue(cautionStocks)
            }
            UNKNOWN -> {
                currentBaseStockList = unknownStocks
                _currentResultStockList.postValue(unknownStocks)
            }
        }
    }

    fun setLastInventoryDate(date: Date) {
        _lastInventoryDate.postValue(date)
    }

    fun setUnitString(unit: String) {
        _unitString.postValue(unit)
    }

    fun getEditState(): String {
        return if (isCreate.value == true) {
            CREATE
        } else {
            MODIFY
        }
    }

    fun resetGetSelectedStockByIdState() {
        _getStockByIdState.value = UiState.Loading
    }

    fun resetUpdateStockState() {
        _updateStockState.value = UiState.Loading
    }

    fun setLastSelectedState(state: String) {
        _lastSelectedStockState.postValue(state)
    }
}
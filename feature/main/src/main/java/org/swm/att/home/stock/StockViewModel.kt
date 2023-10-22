package org.swm.att.home.stock

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
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.domain.entity.response.StockWithStateListVO
import org.swm.att.domain.entity.response.StockWithStateVO
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.stock.StockFragment.Companion.ALL
import org.swm.att.home.stock.StockFragment.Companion.LACK
import org.swm.att.home.stock.StockFragment.Companion.UNKNOWN
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository,
    private val attPosUserRepository: AttPosUserRepository
): BaseSelectableViewViewModel() {
    //stock
    private val _getStockWithStateListState = MutableStateFlow<UiState<StockWithStateListVO>>(UiState.Loading)
    val getStockWithStateListState: StateFlow<UiState<StockWithStateListVO>> = _getStockWithStateListState

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

    //local value
    private var storeId: Int? = null
    private val _isCreate = MutableLiveData<Boolean>(null)
    val isCreate: LiveData<Boolean?> = _isCreate
    private var lastSelectedStockId: Int? = null

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
    fun getStockWithStateList() {
        viewModelScope.launch(attExceptionHandler) {
            attMenuRepository.getStockWithStateList(getStoreId()).collect { result ->
                result.onSuccess {
                    _getStockWithStateListState.value = UiState.Success(it)
                    setBaseStockList(it.stocks)
                    _currentResultStockList.postValue(it.stocks)
                    getSelectedItem(it.stocks.first().id)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "재고 가져오기 실패"
                    _getStockWithStateListState.value = UiState.Error(errorMsg)
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
}
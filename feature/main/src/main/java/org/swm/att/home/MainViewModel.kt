package org.swm.att.home

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
import org.json.JSONObject
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.JWTUtils
import org.swm.att.common_ui.util.JWTUtils.unixTimeToDateTime
import org.swm.att.common_ui.util.getUTCDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.constant.NavDestinationType
import org.swm.att.home.util.alarm.AlarmManager
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository,
    private val attOrderRepository: AttOrderRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _refreshExist = MutableLiveData<Boolean>()
    val refreshExist: LiveData<Boolean> = _refreshExist
    private val _selectedScreen = MutableLiveData(NavDestinationType.Home)
    val selectedScreen: LiveData<NavDestinationType> = _selectedScreen
    private val _curSelectedScreen = MutableLiveData(NavDestinationType.Home)
    val curSelectedScreen: LiveData<NavDestinationType> = _curSelectedScreen
    private val _isGlobalAction = MutableLiveData(false)
    val isGlobalAction: LiveData<Boolean> = _isGlobalAction

    private val todayPreorderList = arrayListOf<PreorderVO>()
    private var page = 1

    private val _storeIdExist = MutableLiveData<Int>()
    val storeIdExist: LiveData<Int> = _storeIdExist
    private val _registerStoreState = MutableStateFlow<UiState<StoreIdVO>>(UiState.Loading)
    val registerStoreState: StateFlow<UiState<StoreIdVO>> = _registerStoreState

    fun checkRefreshToken() {
        viewModelScope.launch {
            val curRefreshToken = attPosUserRepository.getRefreshToken()
            if (curRefreshToken == "") {
                _refreshExist.postValue(false)
            } else {
                //refresh token 만료 확인
                _refreshExist.postValue(true)
                val refreshDecodeStr = JWTUtils.decodeToken(curRefreshToken)
                if (checkRefreshExpire(refreshDecodeStr)) {
                    refreshToken(curRefreshToken)
                }
            }
        }
    }

    fun checkStoreId() {
        viewModelScope.launch(attExceptionHandler) {
            val storeId = attPosUserRepository.getStoreId()
            Log.d("MainViewModel", "checkStoreId: $storeId")
            if (storeId == -1) {
                // TODO 새로운 가게 등록 화면으로 전환 -> _storeIdExist 활용
                // 임시 token을 활용해 가게 바로 등록
                attPosUserRepository.registerStore(
                    StoreVO(
                        name = "temp",
                        address = "temp",
                        openingHours = emptyList()
                    )
                ).collect { result ->
                    result.onSuccess {
                        _registerStoreState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "가게 등록 실패"
                        _registerStoreState.value = UiState.Error(errorMsg)
                    }
                }
            }

        }
    }

    fun setStoreId(storeId: Int) {
        viewModelScope.launch(attExceptionHandler)  {
            Log.d("MainViewModel", "setStoreId: $storeId")
            attPosUserRepository.saveStoreId(storeId)
        }
    }

    private fun checkRefreshExpire(refreshDecodeStr: String): Boolean {
        val expStr = JSONObject(refreshDecodeStr).getString("exp")
        val expDate = unixTimeToDateTime(expStr.toLong())
        return JWTUtils.isTokenExpired(expDate)
    }

    private fun refreshToken(curRefresh: String) {
        viewModelScope.launch {
            attPosUserRepository.refreshToken(curRefresh)
                .collect { result ->
                    result.onSuccess {
                        attPosUserRepository.saveRefreshToken(it.accessToken)
                        attPosUserRepository.saveAccessToken(it.refreshToken)
                    }.onFailure {
                        Log.d("MainViewModel", "${it.message}")
                    }
                }
        }
    }

    private fun setSelectedScreen(destination: NavDestinationType) {
        _selectedScreen.postValue(destination)
    }

    fun changedCurSelectedScreen() {
        _curSelectedScreen.postValue(selectedScreen.value)
    }

    fun directWithGlobalAction(destination: NavDestinationType) {
        _isGlobalAction.postValue(true)
        _selectedScreen.postValue(destination)
    }

    fun resetIsGlobalAction() {
        _isGlobalAction.postValue(false)
    }

    fun customNavRailIconClickListener(destination: NavDestinationType) {
        setSelectedScreen(destination)
    }

    fun isDestinationDiff(destination: NavDestinationType): Boolean {
        return curSelectedScreen.value != destination
    }

    fun getTodayPreorder() {
        viewModelScope.launch(attExceptionHandler) {
            val date =
                Formatter.getStringByDateTimeBaseFormatter(Calendar.getInstance().time.getUTCDateTime())
            val storeId = attPosUserRepository.getStoreId()
            attOrderRepository.getPreOrders(storeId, page, date).collect { result ->
                result.onSuccess {
                    todayPreorderList.addAll(it.preOrders)
                    page += 1
                    if (page < it.lastPage) {
                        getTodayPreorder()
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

    fun cancelAllPreorderAlarm() {
        AlarmManager.cancelAllAlarm(context)
    }
}
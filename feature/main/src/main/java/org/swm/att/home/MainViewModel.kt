package org.swm.att.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.JWTUtils
import org.swm.att.common_ui.util.JWTUtils.unixTimeToDateTime
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.repository.AttOrderRepository
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.constant.NavDestinationType
import org.swm.att.home.reciever.AlarmReceiver
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository,
    private val attOrderRepository: AttOrderRepository
) : BaseViewModel() {
    private val _refreshExist = MutableLiveData<Boolean>()
    val refreshExist: LiveData<Boolean> = _refreshExist
    private val _selectedScreen = MutableLiveData(NavDestinationType.Home)
    val selectedScreen: LiveData<NavDestinationType> = _selectedScreen
    private val _curSelectedScreen = MutableLiveData(NavDestinationType.Home)
    val curSelectedScreen: LiveData<NavDestinationType> = _curSelectedScreen
    private val _isGlobalAction = MutableLiveData(false)
    val isGlobalAction: LiveData<Boolean> = _isGlobalAction

    private lateinit var alarmManager: AlarmManager
    private val todayPreorderList = arrayListOf<PreorderVO>()
    private val todayPreorderPendingIntent = arrayListOf<PendingIntent>()
    private var page = 1

    fun checkRefreshToken() {
        viewModelScope.launch {
            val curRefreshToken = attPosUserRepository.getRefreshToken()
            if (curRefreshToken == "") {
                _refreshExist.postValue(false)
            } else {
                //refresh token 만료 확인
                val refreshDecodeStr = JWTUtils.decodeToken(curRefreshToken)
                if (checkRefreshExpire(refreshDecodeStr)) {
                    refreshToken(curRefreshToken)
                }
            }
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

    fun getTodayPreorder(storeId: Int, context: Context) {
        viewModelScope.launch(attExceptionHandler) {
            val date = Formatter.getStringByDateTimeBaseFormatter(Calendar.getInstance().time)
            attOrderRepository.getPreOrders(storeId, page, date).collect { result ->
                result.onSuccess {
                    todayPreorderList.addAll(it.preOrders)
                    page += 1
                    if (page < it.lastPage) {
                        getTodayPreorder(storeId, context)
                    } else {
                        setPreorderAlarm(context)
                    }
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "예약 내역 불러오기 실패"
                    Log.e("getTodayPreorder", errorMsg!!)
                }
            }
        }
    }

    private fun setPreorderAlarm(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTime = Calendar.getInstance().time

        for ((index, preorderItem) in todayPreorderList.withIndex()) {
            // 각 아이템에 대한 알람 시간과 기타 설정 가져옴
            val alarmTime = Formatter.getDateFromString(preorderItem.orderedFor)
            if (alarmTime.after(currentTime)) {
                // 알림을 발생시킬 때 사용하는 PendingIntent 생성, 전달할 데이터 설정 가능
                // api 31 이상부터 FLAG_MUTABLE / FLAG_IMMUTABLE 사용 가능
                val alarmIntent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    index,
                    alarmIntent,
                    FLAG_IMMUTABLE
                )
                todayPreorderPendingIntent.add(pendingIntent)
                //setExact를 통해 정확한 시간에 알람 실행되도록 함
                alarmManager.setExact(
                    AlarmManager.RTC,
                    alarmTime.time - TimeUnit.MINUTES.toMillis(10),
                    pendingIntent
                )
            }
        }
    }

    fun cancelAllPreorderAlarm() {
        for (pendingIntent in todayPreorderPendingIntent) {
            alarmManager.cancel(pendingIntent)
        }
    }
}
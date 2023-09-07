package org.swm.att.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.swm.att.common_ui.util.token.JWTUtils
import org.swm.att.common_ui.util.token.JWTUtils.unixTimeToDateTime
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.home.constant.NavDestinationType
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): ViewModel() {
    private val _refreshExist = MutableLiveData<Boolean>()
    val refreshExist: LiveData<Boolean> = _refreshExist
    private val _selectedScreen = MutableLiveData<NavDestinationType>()
    val selectedScreen: LiveData<NavDestinationType> = _selectedScreen
    private val _curSelectedScreen = MutableLiveData(NavDestinationType.Home)
    val curSelectedScreen: LiveData<NavDestinationType> = _curSelectedScreen
    private val _isGlobalAction = MutableLiveData(false)
    val isGlobalAction: LiveData<Boolean> = _isGlobalAction

    fun checkRefreshToken() {
        viewModelScope.launch {
            val curRefreshToken = attPosUserRepository.getRefreshToken()
            if (curRefreshToken == ""){
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
            try {
                attPosUserRepository.refreshToken(curRefresh).onSuccess { newTokenVO ->
                    attPosUserRepository.saveRefreshToken(newTokenVO.accessToken)
                    attPosUserRepository.saveAccessToken(newTokenVO.refreshToken)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "${e.message}")
            }
        }
    }

    fun setSelectedScreen(destination: NavDestinationType) {
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

}
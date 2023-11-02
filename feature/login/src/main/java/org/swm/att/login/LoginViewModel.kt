package org.swm.att.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.util.JWTUtils
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    private val _refreshExist = MutableLiveData<Boolean>()
    val refreshExist: LiveData<Boolean> = _refreshExist
    private val _refreshTokenState = MutableLiveData<Boolean>()
    val refreshTokenState: LiveData<Boolean> = _refreshTokenState

    fun checkRefreshToken() {
        viewModelScope.launch {
            val curRefreshToken = attPosUserRepository.getRefreshToken()
            if (curRefreshToken.isNullOrEmpty()) {
                _refreshExist.postValue(false)
            } else {
                //refresh token 만료 확인
                _refreshExist.postValue(true)
                Log.d("MainViewModel", "curRefreshToken: $curRefreshToken")
                val refreshDecodeStr = JWTUtils.decodeToken(curRefreshToken)
                if (checkRefreshExpire(refreshDecodeStr)) {
                    refreshToken(curRefreshToken)
                }
            }
        }
    }

    private fun checkRefreshExpire(refreshDecodeStr: String): Boolean {
        Log.d("MainViewModel", "refreshDecodeStr: $refreshDecodeStr")
        val expStr = JSONObject(refreshDecodeStr).getString("exp")
        val expDate = JWTUtils.unixTimeToDateTime(expStr.toLong())
        return JWTUtils.isTokenExpired(expDate)
    }

    private fun refreshToken(curRefresh: String) {
        viewModelScope.launch {
            attPosUserRepository.refreshToken(curRefresh)
                .collect { result ->
                    result.onSuccess {
                        _refreshTokenState.postValue(true)
                        attPosUserRepository.saveRefreshToken(it.accessToken)
                        attPosUserRepository.saveAccessToken(it.refreshToken)
                    }.onFailure {
                        _refreshTokenState.postValue(false)
                        Log.e("MainViewModel", "${it.message}")
                    }
                }
        }
    }
}
package org.swm.att

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.swm.att.common_ui.util.token.JWTUtils
import org.swm.att.common_ui.util.token.JWTUtils.unixTimeToDateTime
import org.swm.att.data.remote.response.TokenDTO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): ViewModel() {
    private val _refreshExist = MutableLiveData<Boolean>()
    val refreshExist: LiveData<Boolean> = _refreshExist

    fun checkRefreshToken(dataStore: DataStore<TokenDTO>) {
        viewModelScope.launch {
            val curToken = dataStore.data.first().toVO()
            if (curToken.refreshToken != null){
                _refreshExist.postValue(false)
            } else {
                //refresh token 만료 확인
                //val refreshDecodeStr = JWTUtils.decodeToken(curToken.refreshToken)
                val refreshDecodeStr = JWTUtils.decodeToken(BuildConfig.TMP_REFRESH_TOKEN)
                if (!checkRefreshExpire(refreshDecodeStr)) {
                    refreshToken(dataStore, curToken.refreshToken)
                }
            }

        }
    }

    private fun checkRefreshExpire(refreshDecodeStr: String): Boolean {
        val expStr = JSONObject(refreshDecodeStr).getString("exp")
        val expDate = unixTimeToDateTime(expStr.toLong())
        return JWTUtils.isTokenExpired(expDate)
    }

    private fun refreshToken(dataStore: DataStore<TokenDTO>, curRefresh: String) {
        viewModelScope.launch {
            try {
                attPosUserRepository.refreshToken(curRefresh).onSuccess { newTokenVO ->
                    dataStore.updateData {
                        TokenDTO(
                            accessToken = newTokenVO.accessToken,
                            refreshToken = newTokenVO.refreshToken
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "${e.message}")
            }
        }
    }



}
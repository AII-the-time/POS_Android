package org.swm.att.login.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.getValueOrNull
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.LoginVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.entity.response.StoreListVO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class RegisterStoreViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository,
): BaseViewModel() {
    //uiState
    private val _loginState = MutableStateFlow<UiState<TokenVO>>(UiState.Loading)
    val loginState: StateFlow<UiState<TokenVO>> = _loginState
    private val _registerStoreState = MutableStateFlow<UiState<StoreIdVO>>(UiState.Loading)
    val registerStoreState: StateFlow<UiState<StoreIdVO>> = _registerStoreState
    private val _getStoreState = MutableStateFlow<UiState<StoreListVO>>(UiState.Loading)
    val getStoreState: StateFlow<UiState<StoreListVO>> = _getStoreState

    //login
    fun login(businessRegistrationNumber: String, certificatedPhoneToken: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val userInfo = checkCreateUserInfo(businessRegistrationNumber, certificatedPhoneToken)
                attPosUserRepository.login(userInfo).collect { result ->
                    result.onSuccess {
                        _loginState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "사업자 등록 번호 제출에 실패했습니다."
                        _loginState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message)
            }
        }
    }

    fun getStore() {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.getStore().collect { result ->
                result.onSuccess {
                    _getStoreState.value = UiState.Success(it)
                }.onFailure {
                    _getStoreState.value = UiState.Error(it.message)
                }
            }
        }
    }

    private fun checkCreateUserInfo(businessRegistrationNumber: String, certificatedPhoneToken: String?): LoginVO {
        try {
            return LoginVO(
                businessRegistrationNumber,
                requireNotNull(certificatedPhoneToken)
            )
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("전화번호 인증을 다시 해 주세요!")
        }
    }

    //register store
    fun registerStore(storeName: String?) {
        viewModelScope.launch(attExceptionHandler) {
            try {
                val storeInfo = checkCreateStoreInfo(storeName)
                attPosUserRepository.registerStoreForTest(storeInfo).collect { result ->
                //attPosUserRepository.registerStore(storeInfo).collect { result ->
                    result.onSuccess {
                        _registerStoreState.value = UiState.Success(it)
                        saveStoreId(it.storeId)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "가게 등록에 실패했습니다."
                        _registerStoreState.value = UiState.Error(errorMsg)
                    }
                }
            } catch (e: Exception) {
                _registerStoreState.value = UiState.Error(e.message)
            }
        }
    }

    fun saveStoreId(storeId: Int) {
        attPosUserRepository.saveStoreId(storeId)
    }

    private fun checkCreateStoreInfo(storeName: String?): StoreVO {
        try {
            return StoreVO(
                name = requireNotNull(storeName.getValueOrNull()),
                address = "Unknown",
                openingHours = listOf()
            )
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("가게 이름을 입력해 주세요!")
        }
    }

    //token
    fun saveTokens(accessToken: String, refreshToken: String) {
        attPosUserRepository.saveAccessToken(accessToken)
        attPosUserRepository.saveRefreshToken(refreshToken)
    }
}
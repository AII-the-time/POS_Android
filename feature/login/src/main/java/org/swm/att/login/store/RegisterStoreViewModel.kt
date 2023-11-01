package org.swm.att.login.store

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.LoginVO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class RegisterStoreViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    //uiState
    private val _loginState = MutableStateFlow<UiState<TokenVO>>(UiState.Loading)
    val loginState: StateFlow<UiState<TokenVO>> = _loginState

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
}
package org.swm.att.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.CertificationVO
import org.swm.att.domain.entity.response.CertificatedPhoneTokenVO
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO
import org.swm.att.domain.repository.AttPosUserRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    //phoneNumber
    private val _postPhoneNumberState = MutableStateFlow<UiState<TokenForCertificationPhoneVO>>(UiState.Loading)
    val postPhoneNumberState: StateFlow<UiState<TokenForCertificationPhoneVO>> = _postPhoneNumberState

    //certificationCode
    private val _checkCertificationCodeState = MutableStateFlow<UiState<CertificatedPhoneTokenVO>>(UiState.Loading)
    val checkCertificationCodeState: StateFlow<UiState<CertificatedPhoneTokenVO>> = _checkCertificationCodeState

    //local
    private var tokenForCertificatePhone: String? = null
    private var phone: String? = null

    //phoneNumber
    fun postPhoneNumberForAuthentication(phoneNumber: String) {
        phone = phoneNumber
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.postPhoneNumberForAuthentication(phoneNumber)
                .collect { result ->
                    result.onSuccess {
                        tokenForCertificatePhone = it.tokenForCertificatePhone
                        _postPhoneNumberState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "전화번호 전송 실패"
                        _postPhoneNumberState.value = UiState.Error(errorMsg)
                    }
                }
        }
    }

    //certificationCode
    fun checkCertificationCode(code: String) {
        try {
            viewModelScope.launch(attExceptionHandler) {
                val certificationBody = checkCreateCertificationBody(code)
                attPosUserRepository.checkCertificationCode(certificationBody).collect { result ->
                    result.onSuccess {
                        _checkCertificationCodeState.value = UiState.Success(it)
                    }.onFailure {
                        val errorMsg = if (it is HttpResponseException) it.message else "인증번호 확인에 실패하였습니다.\n 다시 시도해주세요!"
                        _checkCertificationCodeState.value = UiState.Error(errorMsg)
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            _checkCertificationCodeState.value = UiState.Error(e.message)
        }
    }

    //local
    private fun checkCreateCertificationBody(code: String): CertificationVO {
        try {
            return CertificationVO(
                phoneCertificationToken = requireNotNull(tokenForCertificatePhone),
                phone = requireNotNull(phone),
                certificationCode = code
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("전화번호 입력 후, 전송된 인증번호를 입력해주세요!")
        }
    }
}
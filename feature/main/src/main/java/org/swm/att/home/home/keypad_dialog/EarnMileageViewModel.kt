package org.swm.att.home.home.keypad_dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.HttpResponseException
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class EarnMileageViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    private val _mileage = MutableLiveData<MileageVO?>()
    val mileage: LiveData<MileageVO?> = _mileage

    private val _getMileageState = MutableStateFlow<UiState<MileageVO>>(UiState.Loading)
    val getMileageState: StateFlow<UiState<MileageVO>> = _getMileageState
    private val _registerCustomerState = MutableStateFlow<UiState<MileageVO>>(UiState.Loading)
    val registerCustomerState: StateFlow<UiState<MileageVO>> = _registerCustomerState

    fun getMileage(phone: String) {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.getMileage(1, phone).collect { result ->
                result.onSuccess {
                    _getMileageState.value = UiState.Success(it)
                    _mileage.postValue(it)
                }.onFailure {
                    //추후 에러처리 필요
                    val errorMsg = if (it is HttpResponseException) it.message else "없는 회원입니다."
                    Log.d("setRegisterBtnVisibility", "getMileage: $errorMsg")
                    _getMileageState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun registerCustomer(phone: String) {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.registerCustomer(
                1,
                PhoneNumVO(
                    phone = phone
                )
            ).collect { result ->
                result.onSuccess {
                    _registerCustomerState.value = UiState.Success(
                            MileageVO(
                                mileageId = it.mileageId,
                                mileage = 0
                            )
                        )
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "고객 등록 실패"
                    _registerCustomerState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun initMileage() {
        _getMileageState.value = UiState.Loading
        _mileage.postValue(null)
    }
}
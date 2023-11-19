package org.swm.att.home.home.keypad_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.common_ui.state.UiState
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
    private var storeId: Int? = null

    fun getMileage(phone: String) {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.getMileage(getStoreId(), phone).collect { result ->
                result.onSuccess {
                    _getMileageState.value = UiState.Success(it)
                    _mileage.postValue(it)
                }.onFailure {
                    val errorMsg = if (it is HttpResponseException) it.message else "없는 회원입니다."
                    _getMileageState.value = UiState.Error(errorMsg)
                }
            }
        }
    }

    fun registerCustomer(phone: String) {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.registerCustomer(
                getStoreId(),
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

    private fun getStoreId(): Int {
        if (storeId == null) {
            storeId = attPosUserRepository.getStoreId()
        }
        return storeId as Int
    }

}
package org.swm.att.home.mileage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseViewModel
import org.swm.att.common_ui.util.NetworkState
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

    private val _getMileageState: MutableLiveData<NetworkState<MileageVO>> = MutableLiveData(NetworkState.Init)
    val getMileageState: LiveData<NetworkState<MileageVO>> = _getMileageState
    private val _registerCustomerState: MutableLiveData<NetworkState<MileageVO>> = MutableLiveData(NetworkState.Init)
    val registerCustomerState: LiveData<NetworkState<MileageVO>> = _registerCustomerState

    fun getMileage(phone: String) {
        viewModelScope.launch(attExceptionHandler) {
            attPosUserRepository.getMileage(1, phone)
                .onSuccess {
                    _getMileageState.postValue(NetworkState.Success(it))
                    _mileage.postValue(it)
                }.onFailure {
                    //추후 에러처리 필요
                    val errorMsg = if (it is HttpResponseException) it.message else "없는 회원입니다."
                    Log.d("setRegisterBtnVisibility", "getMileage: $errorMsg")
                    _getMileageState.postValue(NetworkState.Failure(errorMsg))
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
            ).onSuccess {
                _registerCustomerState.postValue(NetworkState.Success(MileageVO(
                    mileageId = it.mileageId,
                    mileage = 0
                )))
            }.onFailure {
                val errorMsg = if (it is HttpResponseException) it.message else "고객 등록 실패"
                _registerCustomerState.postValue(NetworkState.Failure(errorMsg))
            }
        }
    }

    fun initMileage() {
        _getMileageState.postValue(NetworkState.Init)
        _mileage.postValue(null)
    }
}
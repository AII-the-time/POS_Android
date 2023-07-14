package org.swm.att.presenter.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.swm.att.domain.entity.response.PongVO
import org.swm.att.domain.repository.PingRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pingRepository: PingRepository
) : ViewModel() {
    private val _pongData = MutableLiveData<PongVO>()
    val pongData: LiveData<PongVO> = _pongData

    fun getPong() {
        viewModelScope.launch {
            pingRepository.getPong()
                .onSuccess {
                    _pongData.postValue(it)
                }
        }
    }
}
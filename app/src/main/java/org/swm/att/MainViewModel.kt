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
import org.swm.att.data.remote.response.TokenDTO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): ViewModel() {
    private val _token = MutableLiveData<TokenVO>()
    val token: LiveData<TokenVO> = _token

    fun getRefreshToken(dataStore: DataStore<TokenDTO>) {
        viewModelScope.launch {
            _token.postValue(dataStore.data.first().toVO())
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            try {
                token.value?.let {
                    attPosUserRepository.refreshToken(it.refreshToken).onSuccess { newTokenVO ->
                        _token.postValue(newTokenVO)
                    }
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "refreshToken: ${e.message}")
            }
        }
    }

}
package org.swm.att.home.setting

import dagger.hilt.android.lifecycle.HiltViewModel
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val attPosUserRepository: AttPosUserRepository
): BaseViewModel() {
    fun logout() {
        attPosUserRepository.logout()
    }
}
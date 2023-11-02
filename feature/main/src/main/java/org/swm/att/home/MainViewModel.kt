package org.swm.att.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.swm.att.common_ui.presenter.base.BaseViewModel
import org.swm.att.home.constant.NavDestinationType

class MainViewModel: BaseViewModel() {
    private val _selectedScreen = MutableLiveData(NavDestinationType.Home)
    val selectedScreen: LiveData<NavDestinationType> = _selectedScreen
    private val _curSelectedScreen = MutableLiveData(NavDestinationType.Home)
    val curSelectedScreen: LiveData<NavDestinationType> = _curSelectedScreen
    private val _isGlobalAction = MutableLiveData(false)
    val isGlobalAction: LiveData<Boolean> = _isGlobalAction

    private fun setSelectedScreen(destination: NavDestinationType) {
        _selectedScreen.postValue(destination)
    }

    fun changedCurSelectedScreen() {
        _curSelectedScreen.postValue(selectedScreen.value)
    }

    fun directWithGlobalAction(destination: NavDestinationType) {
        _isGlobalAction.postValue(true)
        _selectedScreen.postValue(destination)
    }

    fun resetIsGlobalAction() {
        _isGlobalAction.postValue(false)
    }

    fun customNavRailIconClickListener(destination: NavDestinationType) {
        setSelectedScreen(destination)
    }

    fun isDestinationDiff(destination: NavDestinationType): Boolean {
        return curSelectedScreen.value != destination
    }
}
package org.swm.att.home.home.option

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.entity.response.OptionTypeVO

class MenuOptionViewModel: ViewModel() {
    private val _selectedOptionMap = MutableLiveData<Map<String, OptionTypeVO>>()
    val selectedOptionMap: LiveData<Map<String, OptionTypeVO>> = _selectedOptionMap

    fun changeSelectedOption(groupId: String, optionType: OptionTypeVO) {
        val optionsMap = (selectedOptionMap.value ?: mutableMapOf()).toMutableMap()
        optionsMap[groupId] = optionType
        _selectedOptionMap.postValue(optionsMap)
    }

    fun removeOption(groupId: String) {
        val optionsMap = (selectedOptionMap.value ?: mutableMapOf()).toMutableMap()
        optionsMap.remove(groupId)
        _selectedOptionMap.postValue(optionsMap)
    }

}
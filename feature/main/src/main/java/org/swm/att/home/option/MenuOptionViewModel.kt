package org.swm.att.home.option

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.entity.response.OptionVO

class MenuOptionViewModel: ViewModel() {
    private val _selectedOptionList = MutableLiveData<List<OptionVO>>()
    val selectedOptionList: LiveData<List<OptionVO>> = _selectedOptionList

    fun addSelectedOption(option: OptionVO) {
        val selectedOptionList = (_selectedOptionList.value ?: listOf()).toMutableList()
        selectedOptionList.add(option)
        _selectedOptionList.postValue(selectedOptionList)
    }

    fun removeSelectedOption(option: OptionVO) {
        val selectedOptionList = (_selectedOptionList.value ?: listOf()).toMutableList()
        selectedOptionList.remove(option)
        _selectedOptionList.postValue(selectedOptionList)
    }

}
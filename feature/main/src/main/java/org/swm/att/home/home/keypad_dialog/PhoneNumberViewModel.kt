package org.swm.att.home.home.keypad_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Stack

class PhoneNumberViewModel : ViewModel() {
    private val _midPhoneNumber = MutableLiveData<Stack<String>>()
    val midPhoneNumber: LiveData<Stack<String>> = _midPhoneNumber
    private val _endPhoneNumber = MutableLiveData<Stack<String>>()
    val endPhoneNumber: LiveData<Stack<String>> = _endPhoneNumber

    fun addPhoneNumber(number: String) {
        val mid = _midPhoneNumber.value ?: Stack()
        if (mid.size < 4) {
            mid.push(number)
            _midPhoneNumber.postValue(mid)
        } else {
            val end = _endPhoneNumber.value ?: Stack()
            if (end.size < 4) {
                end.push(number)
                _endPhoneNumber.postValue(end)
            }
        }
    }

    fun removePhoneNumber() {
        val end = _endPhoneNumber.value ?: Stack()
        if (end.isNotEmpty()) {
            end.pop()
            _endPhoneNumber.value = end
        } else {
            val mid = _midPhoneNumber.value ?: Stack()
            if (mid.isNotEmpty()) {
                mid.pop()
                _midPhoneNumber.value = mid
            }
        }
    }

    fun getPhoneNumber(): String {
        val mid = _midPhoneNumber.value ?: Stack()
        val end = _endPhoneNumber.value ?: Stack()
        return "010${mid.joinToString("")}${end.joinToString("")}"
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11
    }

    fun clearPhoneNumber() {
        _midPhoneNumber.postValue(Stack())
        _endPhoneNumber.postValue(Stack())
    }
}
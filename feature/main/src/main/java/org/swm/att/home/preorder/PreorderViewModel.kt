package org.swm.att.home.preorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.OrderedMenuOfBillVO
import org.swm.att.domain.entity.response.PreorderDetailVO
import java.util.Date

class PreorderViewModel: ViewModel() {
    private val _selectedPreorderInfo = MutableLiveData<PreorderDetailVO>()
    val selectedPreorderInfo: LiveData<PreorderDetailVO> = _selectedPreorderInfo
    private val _currentSelectedValidPreorderId = MutableLiveData<Int>()
    val currentSelectedValidPreorderId: LiveData<Int> = _currentSelectedValidPreorderId
    private val _currentSelectedPastPreorderId = MutableLiveData<Int>()
    val currentSelectedPastPreorderId: LiveData<Int> = _currentSelectedPastPreorderId
    private val _selectedValidPreorderId = MutableLiveData(0)
    val selectedValidPreorderId: LiveData<Int> = _selectedValidPreorderId
    private val _selectedPastPreorderId = MutableLiveData<Int>()
    val selectedPastPreorderId: LiveData<Int> = _selectedPastPreorderId
    private val _filteringStartDate = MutableLiveData<Date?>()
    val filteringStartDate: LiveData<Date?> = _filteringStartDate
    private val _filteringEndDate = MutableLiveData<Date?>()
    val filteringEndDate: LiveData<Date?> = _filteringEndDate

    fun getSelectedPreorderDetail(selectedPreorderId: Int) {
        val mock = PreorderDetailVO(
            orderId = 1,
            totalCount = 3,
            totalPrice = "12,000원",
            orderedAt = "2021-08-01 12:00:00",
            phone = "01012341234",
            memo = "얼음 많이 주세요!",
            orderItems = listOf(
                OrderedMenuOfBillVO(
                    id = 1,
                    count = 3,
                    price = 6000.toBigDecimal(),
                    menuName = "아메리카노",
                    options = listOf(
                        OptionTypeVO(
                            id = 4,
                            name = "옵션1",
                            price = 1000,
                            isSelectable = false
                        )
                    ),
                    detail = "얼음 많이 주세요."
                ),
                OrderedMenuOfBillVO(
                    id = 1,
                    count = 3,
                    price = 6000.toBigDecimal(),
                    menuName = "카페라떼",
                    options = listOf(
                        OptionTypeVO(
                            id = 4,
                            name = "옵션1",
                            price = 1000,
                            isSelectable = false
                        )
                    ),
                    detail = "얼음 많이 주세요."
                )
            )
        )

        _selectedPreorderInfo.value = mock
    }

    fun setCurrentSelectedPreorderId(currentSelectedPreorderId: Int, isValid: Boolean) {
        if (isValid) {
            _currentSelectedValidPreorderId.value = currentSelectedPreorderId
        } else {
            _currentSelectedPastPreorderId.value = currentSelectedPreorderId
        }
    }

    fun changeSelectedState(isValid: Boolean) {
        if (isValid) {
            _selectedValidPreorderId.postValue(currentSelectedValidPreorderId.value)
        } else {
            _selectedPastPreorderId.postValue(currentSelectedPastPreorderId.value)
        }
    }

    fun getPreordersForFilteringDates(startDate: Date, endDate: Date?) {
        _filteringStartDate.value = startDate
        _filteringEndDate.value = endDate
        // filtering api 연결 필요
    }

}
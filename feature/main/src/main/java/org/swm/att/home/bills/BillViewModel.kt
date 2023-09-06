package org.swm.att.home.bills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import org.swm.att.domain.entity.response.MileagePaymentOfBillVO
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.entity.response.OrderedMenuOfBillVO
import org.swm.att.domain.entity.response.PaymentOfBillVO
import java.util.Date

class BillViewModel: ViewModel() {
    private val _selectedBillInfo = MutableLiveData<OrderReceiptVO>()
    val selectedBillInfo: LiveData<OrderReceiptVO> = _selectedBillInfo
    private val _selectedBillId = MutableLiveData(0)
    val selectedBillId: LiveData<Int> = _selectedBillId
    private val _currentSelectedBillId = MutableLiveData<Int>()
    val currentSelectedBillId: LiveData<Int> = _currentSelectedBillId
    private val _filteringStartDate = MutableLiveData<Date>()
    val filteringStartDate: LiveData<Date> = _filteringStartDate
    private val _filteringEndDate = MutableLiveData<Date?>()
    val filteringEndDate: LiveData<Date?> = _filteringEndDate

    fun getSelectedBillInfo(selectedBillId: Int) {
        val mock = OrderReceiptVO(
            paymentState = PayState.CANCELED,
            totalPrice = 6000.toBigDecimal(),
            createdAt = "2021-08-01 12:00:00",
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
                        )
                    ),
                    detail = "얼음 많이 주세요."
                )
            ),
            mileage = MileagePaymentOfBillVO(
                use = 100.toBigDecimal(),
                save = 100.toBigDecimal(),
            ),
            pay = PaymentOfBillVO(
                paymentMethod = PayMethod.CARD,
                price = 6000.toBigDecimal()
            )
        )

        _selectedBillInfo.value = mock
    }


    fun setCurrentSelectedBillId(currentSelectedBillId: Int) {
        _currentSelectedBillId.value = currentSelectedBillId
    }

    fun changeSelectedState() {
        _selectedBillId.postValue(currentSelectedBillId.value)
    }

    fun getBillsForFilteringDates(startDate: Date, endDate: Date?) {
        _filteringStartDate.value = startDate
        _filteringEndDate.value = endDate
        // filtering api 연결 필요
    }

}
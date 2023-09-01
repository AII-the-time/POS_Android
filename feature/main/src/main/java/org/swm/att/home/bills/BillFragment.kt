package org.swm.att.home.bills

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import org.swm.att.domain.entity.response.OrderBillVO
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderBillItemAdapter
import org.swm.att.home.adapter.OrderedMenuOfBillAdapter
import org.swm.att.home.databinding.FragmentBillBinding

class BillFragment : BaseFragment<FragmentBillBinding>(R.layout.fragment_bill) {
    private lateinit var orderBillItemAdapter: OrderBillItemAdapter
    private lateinit var orderedMenuOfBillAdapter: OrderedMenuOfBillAdapter
    private val billViewModel: BillViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrderBillItemRecyclerView()
        setDataBinding()
        setObserver()
        initMockData()
    }

    private fun initOrderBillItemRecyclerView() {
        orderBillItemAdapter = OrderBillItemAdapter(billViewModel)
        binding.rvBills.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderBillItemAdapter
        }

        orderedMenuOfBillAdapter = OrderedMenuOfBillAdapter()
        binding.rvReceiptMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuOfBillAdapter
        }
    }

    private fun setDataBinding() {
        binding.selectedBillReceipt = billViewModel.selectedBillInfo.value
    }

    private fun setObserver() {
        billViewModel.selectedBillInfo.observe(viewLifecycleOwner) {
            orderedMenuOfBillAdapter.submitList(it.orderItems)
            binding.selectedBillReceipt = it
        }
        billViewModel.currentSelectedBillId.observe(viewLifecycleOwner) {
            val pastId = billViewModel.selectedBillId.value
            binding.rvBills[it].setBackgroundResource(R.color.main_trans)
            billViewModel.changeSelectedState()
            pastId?.let { pastId ->
                if (pastId != it) {
                    billViewModel.selectedBillId.value?.let { pastId ->
                        binding.rvBills[pastId].setBackgroundResource(R.color.back_color)
                    }
                }
            }
        }
    }

    private fun initMockData() {
        val mock = listOf(
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.CANCELED,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 6000.toBigDecimal(),
                createdAt = "2021-08-01 12:00:00",
            ),
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.PAID,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 6000.toBigDecimal(),
                createdAt = "2021-08-01 12:00:00",
            ),
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.PAID,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 6000.toBigDecimal(),
                createdAt = "2021-08-10 12:00:00",
            ),
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.PAID,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 3000.toBigDecimal(),
                createdAt = "2021-08-01 12:00:00",
            ),
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.PAID,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 6000.toBigDecimal(),
                createdAt = "2021-08-01 12:00:00",
            ),
            OrderBillVO(
                orderId = 1,
                paymentState = PayState.PAID,
                paymentMethod = PayMethod.CARD,
                totalCount = 3,
                totalPrice = 6000.toBigDecimal(),
                createdAt = "2021-08-01 12:00:00",
            )
        )
        orderBillItemAdapter.submitList(mock)
        binding.totalCountOfBill = mock.size
    }
}
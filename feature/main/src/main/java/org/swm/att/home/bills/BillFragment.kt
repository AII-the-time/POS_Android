package org.swm.att.home.bills

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.home.R
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.adapter.OrderedMenuOfBillAdapter
import org.swm.att.home.databinding.FragmentBillBinding

@AndroidEntryPoint
class BillFragment : BaseFragment<FragmentBillBinding>(R.layout.fragment_bill) {
    private lateinit var orderBillItemAdapter: SelectableItemAdapter
    private lateinit var orderedMenuOfBillAdapter: OrderedMenuOfBillAdapter
    private val billViewModel: BillViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrderBillItemRecyclerView()
        setBillFilteringBtnClickListener()
        setDataBinding()
        setObserver()
        initOrderBillsData()
    }

    private fun initOrderBillItemRecyclerView() {
        orderBillItemAdapter = SelectableItemAdapter(billViewModel)
        binding.rvBills.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderBillItemAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem = (this@apply.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (billViewModel.getSizeOfOrderBills() <= lastVisibleItem + 5) {
                        billViewModel.getNextOrderBills(1)
                    }

                }
            })
        }

        orderedMenuOfBillAdapter = OrderedMenuOfBillAdapter()
        binding.rvReceiptMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuOfBillAdapter
        }
    }

    private fun setBillFilteringBtnClickListener() {
        binding.btnSearchBill.setOnClickListener {
            val dialog = DialogBillFiltering(billViewModel)
            dialog.show(childFragmentManager, "billFiltering")
        }
    }

    private fun setDataBinding() {
        binding.billViewModel = billViewModel
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                billViewModel.selectedBillInfo.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                orderedMenuOfBillAdapter.submitList(it.orderItems)

                            }
                        }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        is UiState.Loading -> { /* nothing */ }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                billViewModel.orderBills.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> { /* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        is UiState.Loading -> { /* nothing */ }
                    }
                }
            }
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

        billViewModel.orderBillsData.observe(viewLifecycleOwner) {
            orderBillItemAdapter.submitList(it)
            binding.totalCountOfBill = it.size
        }
    }

    private fun initOrderBillsData() {
        billViewModel.getNextOrderBills(1)
    }
}
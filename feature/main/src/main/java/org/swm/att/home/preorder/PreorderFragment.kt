package org.swm.att.home.preorder

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuOfBillAdapter
import org.swm.att.home.adapter.PreorderListItemAdapter
import org.swm.att.home.databinding.FragmentPreorderBinding

class PreorderFragment : BaseFragment<FragmentPreorderBinding>(R.layout.fragment_preorder) {
    private lateinit var preorderMenuOfBillAdapter: OrderedMenuOfBillAdapter
    private lateinit var validPreorderListAdapter: PreorderListItemAdapter
    private lateinit var pastPreorderListAdapter: PreorderListItemAdapter
    private val preorderViewModel: PreorderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreorderRecyclerView()
        setPreorderFilteringBtnClickListener()
        setObserver()
        setDataBinding() // api 달면 수정할 부분
        setModifyPreorderBtnClickListener()
        initMockData()
    }

    private fun initPreorderRecyclerView() {
        preorderMenuOfBillAdapter = OrderedMenuOfBillAdapter()
        binding.rvPreorderMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = preorderMenuOfBillAdapter
        }

        validPreorderListAdapter = PreorderListItemAdapter(preorderViewModel, true)
        binding.rvPreorder.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = validPreorderListAdapter
        }

        pastPreorderListAdapter = PreorderListItemAdapter(preorderViewModel, false)
        binding.rvPastPreorder.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pastPreorderListAdapter
        }
    }

    private fun setPreorderFilteringBtnClickListener() {
        binding.btnSearchPreorder.setOnClickListener {
            val dialog = DialogPreorderFiltering(preorderViewModel)
            dialog.show(childFragmentManager, "preorderFiltering")
        }
    }

    private fun setObserver() {
        preorderViewModel.selectedPreorderInfo.observe(viewLifecycleOwner) {
            preorderMenuOfBillAdapter.submitList(it.orderItems)
            binding.selectedPreorderBill = it
        }
        preorderViewModel.currentSelectedValidPreorderId.observe(viewLifecycleOwner) {
            val pastId = preorderViewModel.selectedValidPreorderId.value
            binding.rvPreorder[it].setBackgroundResource(R.color.main_trans)
            preorderViewModel.changeSelectedState(true)
            pastId?.let { pastId ->
                if (pastId != it) {
                    preorderViewModel.selectedValidPreorderId.value?.let { pastId ->
                        binding.rvPreorder[pastId].setBackgroundResource(R.color.back_color)
                    }
                }
            }
            preorderViewModel.selectedPastPreorderId.value?.let { pastId ->
                binding.rvPastPreorder[pastId].setBackgroundResource(R.color.back_color)
            }
        }
        preorderViewModel.currentSelectedPastPreorderId.observe(viewLifecycleOwner) {
            val pastId = preorderViewModel.selectedPastPreorderId.value
            binding.rvPastPreorder[it].setBackgroundResource(R.color.main_trans)
            preorderViewModel.changeSelectedState(false)
            pastId?.let { pastId ->
                if (pastId != it) {
                    preorderViewModel.selectedPastPreorderId.value?.let { pastId ->
                        binding.rvPastPreorder[pastId].setBackgroundResource(R.color.back_color)
                    }
                }
            }
            preorderViewModel.selectedValidPreorderId.value?.let {  pastId ->
                binding.rvPreorder[pastId].setBackgroundResource(R.color.back_color)
            }
        }
    }

    private fun setDataBinding() {
        binding.pastPreorderListSize = 6
        binding.validPreorderListSize = 6
    }

    private fun setModifyPreorderBtnClickListener() {
        binding.btnModifyPreorderList.setOnClickListener {
            val action = PreorderFragmentDirections.actionGlobalFragmentHome(preorderedMenus = preorderViewModel.selectedPreorderInfo.value)
            findNavController().navigate(action)
        }
    }

    private fun initMockData() {
        val mock = listOf(
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            ),
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            ),
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            ),
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            ),
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            ),
            PreorderVO(
                orderId = 1,
                totalCount = 3,
                totalPrice = "12,000원",
                orderedAt = "2021-08-01 12:00:00",
                phone = "01012341234",
                memo = "얼음 많이 주세요!",
            )
        )

        pastPreorderListAdapter.submitList(mock)
        validPreorderListAdapter.submitList(mock)
    }
}
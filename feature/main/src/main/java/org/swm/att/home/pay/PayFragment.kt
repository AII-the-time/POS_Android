package org.swm.att.home.pay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuAdapter
import org.swm.att.home.databinding.FragmentPayBinding

class PayFragment : BaseFragment<FragmentPayBinding>(R.layout.fragment_pay) {
    private val payViewModel by viewModels<PayViewModel>()
    private val args by navArgs<PayFragmentArgs>()
    private lateinit var orderedMenuAdapter: OrderedMenuAdapter
    private lateinit var selectedOrderedMenuAdapter: OrderedMenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrderedMenuRecyclerView()
        setBtnModifyOrderClickListener()
        setSelectedMenuList()
        setMenusMapObserver()
        setDataBinding()
    }

    private fun initOrderedMenuRecyclerView() {
        orderedMenuAdapter = OrderedMenuAdapter(payViewModel, false)
        binding.rvOrderedMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuAdapter
        }

        selectedOrderedMenuAdapter = OrderedMenuAdapter(payViewModel, true)
        binding.rvSelectedOrderItemList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = selectedOrderedMenuAdapter
        }

    }

    private fun setBtnModifyOrderClickListener() {
        binding.btnModifyOrder.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_pay_to_fragment_home)
        }
    }

    private fun setSelectedMenuList() {
        args.OrderedMenus?.let {
            payViewModel.setOrderedMenuMap(it)
        }
    }

    private fun setMenusMapObserver() {
        payViewModel.orderedMenuMap.observe(viewLifecycleOwner) {
            orderedMenuAdapter.submitList(payViewModel.getOrderedMenuList())
        }

        payViewModel.selectedOrderedMenuMap.observe(viewLifecycleOwner) {
            selectedOrderedMenuAdapter.submitList(payViewModel.getSelectedOrderedMenuList())
        }
    }

    private fun setDataBinding() {
        binding.payViewModel = payViewModel
    }
}
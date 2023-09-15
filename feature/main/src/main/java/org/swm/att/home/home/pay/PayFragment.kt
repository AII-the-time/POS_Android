package org.swm.att.home.home.pay

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.NetworkState
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuAdapter
import org.swm.att.home.databinding.FragmentPayBinding
import org.swm.att.home.home.mileage.UseMileageDialog

@AndroidEntryPoint
class PayFragment : BaseFragment<FragmentPayBinding>(R.layout.fragment_pay) {
    private val payViewModel by viewModels<PayViewModel>()
    private val args by navArgs<PayFragmentArgs>()
    private lateinit var orderedMenuAdapter: OrderedMenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrderedMenuRecyclerView()
        setMileage()
        setBtnModifyOrderClickListener()
        setSelectedMenuList()
        setMenusMapObserver()
        setDataBinding()
        setUseMileageBtnClickListener()
        setPayBtnClickListener()
        setPatchMileageStateObserver()
        setPostOrderStateObserver()
        setPostUseMileageStateObserver()
    }

    private fun initOrderedMenuRecyclerView() {
        orderedMenuAdapter = OrderedMenuAdapter()
        binding.rvOrderedMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuAdapter
        }
    }

    private fun setMileage() {
        args.Mileage?.let {
            payViewModel.setMileage(it)
        }
    }

    private fun setBtnModifyOrderClickListener() {
        binding.btnModifyOrder.setOnClickListener {
            val orderedMenus = OrderedMenusVO(payViewModel.totalOrderMenuList.value)
            val action =
                PayFragmentDirections.actionFragmentPayToFragmentHome(selectedMenus = orderedMenus)
            findNavController().navigate(action)
        }
    }

    private fun setSelectedMenuList() {
        args.OrderedMenus?.let {
            payViewModel.setOrderedMenuMap(it)
        }
    }

    private fun setMenusMapObserver() {
        payViewModel.orderedMenuMap.observe(viewLifecycleOwner) {
            orderedMenuAdapter.submitList(payViewModel.getSelectedOrderedMenuList())
        }
    }

    private fun setDataBinding() {
        binding.payViewModel = payViewModel
        binding.customerId = args.CustomerId
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setUseMileageBtnClickListener() {
        binding.btnUseMileage.setOnClickListener {
            val dialog = UseMileageDialog(payViewModel)
            dialog.show(requireActivity().supportFragmentManager, "UseMileageDialog")
        }
    }

    private fun setPayBtnClickListener() {
        binding.btnPayByCard.setOnClickListener {
            payViewModel.getOrderIdAndPostPayment(PayMethod.CARD)
        }
        binding.btnPayByCash.setOnClickListener {
            payViewModel.getOrderIdAndPostPayment(PayMethod.CASH)
        }
        binding.btnPayByEasy.setOnClickListener {
            payViewModel.getOrderIdAndPostPayment(PayMethod.BANK)
        }
    }

    private fun setPatchMileageStateObserver() {
        payViewModel.patchMileageState.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkState.Init -> {}
                is NetworkState.Success -> Toast.makeText(requireContext(), "보유 마일리지: ${it.data?.mileage}", Toast.LENGTH_SHORT).show()
                is NetworkState.Failure -> {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setPostOrderStateObserver() {
        payViewModel.postOrderState.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkState.Init -> {}
                is NetworkState.Success -> {
                    Toast.makeText(requireContext(), "결제가 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_fragment_pay_to_fragment_home)
                }
                is NetworkState.Failure -> Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPostUseMileageStateObserver() {
        payViewModel.postUseMileageState.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkState.Init -> {}
                is NetworkState.Success -> {
                    Toast.makeText(requireContext(), "마일리지 사용이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                }
                is NetworkState.Failure -> Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package org.swm.att.home.home.preorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuAdapter
import org.swm.att.home.databinding.FragmentPreorderRegisterBinding
import org.swm.att.home.home.keypad_dialog.PreorderInputUserPhoneNumDialog
import org.swm.att.home.home.preorder.PreorderRegisterFragmentArgs
import org.swm.att.home.home.preorder.PreorderRegisterFragmentDirections

@AndroidEntryPoint
class PreorderRegisterFragment : BaseFragment<FragmentPreorderRegisterBinding>(R.layout.fragment_preorder_register) {
    private lateinit var orderedMenuAdapter: OrderedMenuAdapter
    private val navArgs by navArgs<PreorderRegisterFragmentArgs>()
    private val preorderRegisterViewModel by viewModels<PreorderRegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreorderRecyclerView()
        setDataBinding()
        setOrderedMenus()
        setCustomerPhoneNumberForPreorder()
        setModifyPreorderBtnClickListener()
        setClDateClickListener()
        setClPhoneNumClickListener()
        setBtnPreOrderRegisterClickListener()
        setPreorderUpdateBtnClickListener()
        setObserver()
    }

    private fun initPreorderRecyclerView() {
        orderedMenuAdapter = OrderedMenuAdapter()
        binding.rvPreorderMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuAdapter
        }
    }

    private fun setDataBinding() {
        binding.preorderRegisterViewModel = preorderRegisterViewModel
        if (navArgs.preorderId != -1) {
            binding.isModify = true
        }
    }

    private fun setOrderedMenus() {
        navArgs.orderedMenus?.let {
            orderedMenuAdapter.submitList(it.menus)
            preorderRegisterViewModel.setOrderedMenus(it)
        }
    }

    private fun setCustomerPhoneNumberForPreorder() {
        navArgs.customerPhoneNumber?.let {
            preorderRegisterViewModel.setPhoneNumber(it)
        }
    }

    private fun setModifyPreorderBtnClickListener() {
        val preorderId = navArgs.preorderId
        val customerPhoneNumber = navArgs.customerPhoneNumber
        binding.btnModifyPreorderList.setOnClickListener {
            if (preorderId != -1) {
                preorderRegisterViewModel.orderedMenus.value?.let {
                    val action =
                        PreorderRegisterFragmentDirections.actionFragmentPreorderRegisterToFragmentHome(
                            selectedMenus = it,
                            isModify = true,
                            preOrderId = preorderId,
                            customerPhoneNumber = customerPhoneNumber
                        )
                    findNavController().navigate(action)
                }
            } else {
                preorderRegisterViewModel.orderedMenus.value?.let {
                    val action =
                        PreorderRegisterFragmentDirections.actionFragmentPreorderRegisterToFragmentHome(
                            it
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun setClDateClickListener() {
        binding.tvPreorderDate.setOnClickListener {
            val dateTimePickerDialog = DateTimePickerDialog(preorderRegisterViewModel)
            dateTimePickerDialog.show(childFragmentManager, "dateTimePickerDialog")
        }
    }

    private fun setClPhoneNumClickListener() {
        binding.tvPreorderClientPhoneNum.setOnClickListener {
            val inputUserPhoneNumDialog = PreorderInputUserPhoneNumDialog(preorderRegisterViewModel, navArgs.customerPhoneNumber)
            inputUserPhoneNumDialog.show(parentFragmentManager, "inputUserPhoneNumDialog")
        }
    }

    private fun setBtnPreOrderRegisterClickListener() {
        binding.btnPreorderRegister.setOnClickListener {
            val phoneNumber = binding.tvPreorderClientPhoneNum.text.toString()
            val memo = binding.edtPreorderDetail.text.toString()
            if (phoneNumber.isNotEmpty()) {
                preorderRegisterViewModel.postPreOrder(phoneNumber, memo)
            } else {
                Toast.makeText(requireContext(), "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPreorderUpdateBtnClickListener() {
        binding.btnPreorderUpdate.setOnClickListener {
            val phoneNumber = binding.tvPreorderClientPhoneNum.text.toString()
            val memo = binding.edtPreorderDetail.text.toString()
            if (phoneNumber.isNotEmpty()) {
                preorderRegisterViewModel.updatePreorder(phoneNumber, memo, navArgs.preorderId)
            } else {
                Toast.makeText(requireContext(), "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preorderRegisterViewModel.postPreOrderState.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "예약 주문 등록이 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_global_fragment_home)
                        }
                        is UiState.Loading -> { /* do nothing */
                        }

                        is UiState.Error -> Toast.makeText(
                            requireContext(),
                            uiState.errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preorderRegisterViewModel.updatePreorderState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(requireContext(), "예약 주문 수정 완료", Toast.LENGTH_SHORT).show()
                            val action =
                                PreorderRegisterFragmentDirections.actionFragmentPreorderRegisterToFragmentPreorder(
                                    navArgs.preorderId
                                )
                            findNavController().navigate(action)
                        }
                        is UiState.Loading -> { /* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
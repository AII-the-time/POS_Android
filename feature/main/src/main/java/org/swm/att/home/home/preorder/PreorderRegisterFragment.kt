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
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuAdapter
import org.swm.att.home.databinding.FragmentPreorderRegisterBinding
import org.swm.att.home.home.keypad_dialog.PreorderInputUserPhoneNumDialog

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
        setModifyPreorderBtnClickListener()
        setClDateClickListener()
        setClPhoneNumClickListener()
        setBtnPreOrderRegisterClickListener()
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
    }

    private fun setOrderedMenus() {
        navArgs.orderedMenus?.let {
            orderedMenuAdapter.submitList(it.menus)
            preorderRegisterViewModel.setOrderedMenus(it)
        }
    }

    private fun setModifyPreorderBtnClickListener() {
        binding.btnModifyPreorderList.setOnClickListener {
            preorderRegisterViewModel.orderedMenus.value?.let {
                val action = PreorderRegisterFragmentDirections.actionFragmentPreorderRegisterToFragmentHome(it)
                findNavController().navigate(action)
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
            val inputUserPhoneNumDialog = PreorderInputUserPhoneNumDialog(preorderRegisterViewModel)
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
    }
}
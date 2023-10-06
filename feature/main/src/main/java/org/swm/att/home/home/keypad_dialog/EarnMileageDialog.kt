package org.swm.att.home.home.keypad_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseDialog
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUserPhoneNumInputBinding
import org.swm.att.home.home.HomeFragmentDirections
import org.swm.att.home.home.HomeViewModel

@AndroidEntryPoint
class EarnMileageDialog(
    private val homeViewModel: HomeViewModel,
    private val preOrderId: Int,
): BaseDialog<DialogUserPhoneNumInputBinding>(R.layout.dialog_user_phone_num_input) {
    private val earnMileageViewModel by viewModels<EarnMileageViewModel>()
    private val phoneNumberViewModel: PhoneNumberViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneNumberViewModel.clearPhoneNumber()
        setBtnClickListener()
        setupCustomKeypad()
        setPhoneNumberObserver()
        setRegisterBtnClickListener()
        setGetMileageStateObserver()
        setRegisterCustomerStateObserver()
    }

    private fun setBtnClickListener() {
        binding.btnCloseDialog.setOnClickListener {
            dismiss()
            earnMileageViewModel.initMileage()
        }
        binding.btnPassEarnMileage.setOnClickListener {
            dismiss()
            earnMileageViewModel.initMileage()
            val orderedMenus = homeViewModel.getOrderedMenusVO()
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentPay(
                OrderedMenus = orderedMenus,
                preOrderId = preOrderId
            )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }


    private fun setupCustomKeypad() {
        binding.customKeypad.apply {
            setLifeCycleOwner(viewLifecycleOwner)
            setOnNumberItemClickListener {
                phoneNumberViewModel.addPhoneNumber(it)
            }
            setOnClearBtnClickListener {
                phoneNumberViewModel.removePhoneNumber()
            }
            setOnEnterBtnClickListener {
                val phoneNumber = phoneNumberViewModel.getPhoneNumber()
                if (phoneNumberViewModel.isPhoneNumberValid(phoneNumber)) {
                    earnMileageViewModel.getMileage(phoneNumber)
                } else {
                    Toast.makeText(requireContext(), "휴대폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setPhoneNumberObserver() {
        phoneNumberViewModel.midPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberMiddlePart.text = it.joinToString("")
        }
        phoneNumberViewModel.endPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberEndPart.text = it.joinToString("")
        }
    }

    private fun setGetMileageStateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                earnMileageViewModel.getMileageState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                navigateToPayFragment(it)
                            }
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                            binding.btnRegisterNewCustomer.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {/* nothing */}
                    }
                }
            }
        }
    }

    private fun navigateToPayFragment(mileage: MileageVO) {
        dismiss()
        earnMileageViewModel.initMileage()
        homeViewModel.clearSelectedMenuList()
        val orderedMenus = homeViewModel.getOrderedMenusVO()
        val customerId = phoneNumberViewModel.endPhoneNumber.value?.joinToString("")
        val action = HomeFragmentDirections
            .actionFragmentHomeToFragmentPay(
                OrderedMenus = orderedMenus,
                Mileage = mileage,
                CustomerId = customerId,
                preOrderId = preOrderId
            )
        findNavController().navigate(action)
    }

    private fun setRegisterBtnClickListener() {
        binding.btnRegisterNewCustomer.setOnClickListener {
            val phoneNumber = phoneNumberViewModel.getPhoneNumber()
            if (phoneNumberViewModel.isPhoneNumberValid(phoneNumber)) {
                earnMileageViewModel.registerCustomer(phoneNumber)
            } else {
                Toast.makeText(requireContext(), "휴대폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRegisterCustomerStateObserver() {
        lifecycleScope.launch {
            earnMileageViewModel.registerCustomerState.collect { uiState ->
                when(uiState) {
                    is UiState.Success -> {
                        Toast.makeText(requireContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        uiState.data?.let {
                            navigateToPayFragment(it)
                        }
                    }
                    is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    is UiState.Loading -> {/* nothing */}
                }
            }
        }
    }
}
package org.swm.att.home.home.keypad_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.state.NetworkState
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUserPhoneNumInputBinding
import org.swm.att.home.home.HomeFragmentDirections
import org.swm.att.home.home.HomeViewModel

@AndroidEntryPoint
class EarnMileageDialog(
    private val homeViewModel: HomeViewModel
): BaseDialog<DialogUserPhoneNumInputBinding>(R.layout.dialog_user_phone_num_input) {
    private val earnMileageViewModel by viewModels<EarnMileageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentPay(OrderedMenus = orderedMenus)
            NavHostFragment.findNavController(this).navigate(action)
        }
    }


    private fun setupCustomKeypad() {
        binding.customKeypad.apply {
            setLifeCycleOwner(viewLifecycleOwner)
            setOnNumberItemClickListener {
                homeViewModel.addPhoneNumber(it)
            }
            setOnClearBtnClickListener {
                homeViewModel.removePhoneNumber()
            }
            setOnEnterBtnClickListener {
                val phoneNumber = homeViewModel.getPhoneNumber()
                if (isPhoneNumberValid(phoneNumber)) {
                    earnMileageViewModel.getMileage(phoneNumber)
                } else {
                    Toast.makeText(requireContext(), "휴대폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11
    }

    private fun setPhoneNumberObserver() {
        homeViewModel.midPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberMiddlePart.text = it.joinToString("")
        }
        homeViewModel.endPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberEndPart.text = it.joinToString("")
        }
    }

    private fun setGetMileageStateObserver() {
        earnMileageViewModel.getMileageState.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkState.Init -> {}
                is NetworkState.Success -> {
                    navigateToPayFragment(it.data)
                }
                is NetworkState.Failure -> {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    binding.btnRegisterNewCustomer.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigateToPayFragment(mileage: MileageVO) {
        dismiss()
        earnMileageViewModel.initMileage()
        homeViewModel.clearSelectedMenuList()
        val orderedMenus = homeViewModel.getOrderedMenusVO()
        val customerId = homeViewModel.endPhoneNumber.value?.joinToString("")
        val action = HomeFragmentDirections
            .actionFragmentHomeToFragmentPay(OrderedMenus = orderedMenus, Mileage = mileage, CustomerId = customerId)
        findNavController().navigate(action)
    }

    private fun setRegisterBtnClickListener() {
        binding.btnRegisterNewCustomer.setOnClickListener {
            val phoneNumber = homeViewModel.getPhoneNumber()
            if (isPhoneNumberValid(phoneNumber)) {
                earnMileageViewModel.registerCustomer(phoneNumber)
            } else {
                Toast.makeText(requireContext(), "휴대폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRegisterCustomerStateObserver() {
        earnMileageViewModel.registerCustomerState.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkState.Init -> {}
                is NetworkState.Success -> {
                    Toast.makeText(requireContext(), "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    navigateToPayFragment(it.data)
                }
                is NetworkState.Failure -> {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
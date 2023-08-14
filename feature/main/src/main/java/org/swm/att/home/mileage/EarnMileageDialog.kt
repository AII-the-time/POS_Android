package org.swm.att.home.mileage

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogEarnMileageBinding
import org.swm.att.home.home.HomeFragmentDirections
import org.swm.att.home.home.HomeViewModel

class EarnMileageDialog(
    private val homeViewModel: HomeViewModel
): BaseDialog<DialogEarnMileageBinding>(R.layout.dialog_earn_mileage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnListener()
        setupCustomKeypad()
        setPhoneNumberObserver()
    }

    private fun setDialogCloseBtnListener() {
        binding.btnCloseEarnMileageDialog.setOnClickListener {
            homeViewModel.clearPhoneNumber()
            dismiss()
        }

        binding.btnPassEarnMileage.setOnClickListener {
            homeViewModel.clearPhoneNumber()
            dismiss()
            val orderedMenus = homeViewModel.getOrderedMenusVO()
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentPay(OrderedMenus = orderedMenus)
            NavHostFragment.findNavController(this).navigate(action)
        }
    }


    private fun setupCustomKeypad() {
        binding.ckpEarnMileage.apply {
            setLifeCycleOwner(viewLifecycleOwner)
            setOnNumberItemClickListener {
                homeViewModel.addPhoneNumber(it)
            }
            setOnClearBtnClickListener {
                homeViewModel.removePhoneNumber()
            }
            setOnEnterBtnClickListener {
                dismiss()
                val orderedMenus = homeViewModel.getOrderedMenusVO()
                val phoneNumber = homeViewModel.getPhoneNumber()
                val action = HomeFragmentDirections
                    .actionFragmentHomeToFragmentPay(OrderedMenus = orderedMenus, PhoneNumber = phoneNumber)
                NavHostFragment.findNavController(this@EarnMileageDialog).navigate(action)
            }
        }
    }

    private fun setPhoneNumberObserver() {
        homeViewModel.midPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberMiddlePart.text = it.joinToString("")
        }
        homeViewModel.endPhoneNumber.observe(viewLifecycleOwner) {
            binding.tvPhoneNumberEndPart.text = it.joinToString("")
        }
    }
}
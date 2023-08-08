package org.swm.att.home.dialog

import android.os.Bundle
import android.view.View
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogEarnMileageBinding
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
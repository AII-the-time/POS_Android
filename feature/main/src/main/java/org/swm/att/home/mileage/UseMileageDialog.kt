package org.swm.att.home.mileage

import android.os.Bundle
import android.view.View
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUseMileageBinding
import org.swm.att.home.pay.PayViewModel

class UseMileageDialog(
    private val payViewModel: PayViewModel
): BaseDialog<DialogUseMileageBinding>(R.layout.dialog_use_mileage) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnListener()
        setNotUseBtnClickListener()
        setCustomKeypad()
        setDataBindingData()
        setUseAllMileageBtnClickListener()
    }

    private fun setDialogCloseBtnListener() {
        binding.btnCloseUseMileageDialog.setOnClickListener {
            payViewModel.clearUseMileage()
            dismiss()
        }
    }

    private fun setNotUseBtnClickListener() {
        binding.btnNotUseMileage.setOnClickListener {
            payViewModel.clearUseMileage()
            dismiss()
        }
    }

    private fun setCustomKeypad() {
        binding.ckpUseMileage.apply {
            setLifeCycleOwner(viewLifecycleOwner)
            setOnNumberItemClickListener {
                payViewModel.addUseMileageStr(it)
            }
            setOnClearBtnClickListener {
                payViewModel.removeUseMileageStr()
            }
            setOnEnterBtnClickListener {
                dismiss()
            }
        }
    }

    private fun setDataBindingData() {
        binding.payViewModel = payViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setUseAllMileageBtnClickListener() {
        binding.btnUseAllMileage.setOnClickListener {
            payViewModel.useAllMileage()
        }
    }
}
package org.swm.att.home.home.keypad_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUserPhoneNumInputBinding
import org.swm.att.home.home.HomeViewModel
import org.swm.att.home.home.preorder.PreorderRegisterViewModel

class PreorderInputUserPhoneNumDialog(
    private val homeViewModel: HomeViewModel,
    private val preorderRegisterViewModel: PreorderRegisterViewModel
): BaseDialog<DialogUserPhoneNumInputBinding>(R.layout.dialog_user_phone_num_input) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.clearPhoneNumber()
        initView()
        setBtnClickListener()
        setUpCustomKeypad()
        setPhoneNumberObserver()
    }

    private fun initView() {
        binding.clEarnMileageBtns.visibility = View.GONE
        binding.tvEarnMileageDes.visibility = View.INVISIBLE
    }

    private fun setBtnClickListener() {
        binding.btnCloseDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpCustomKeypad() {
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
                if (homeViewModel.isPhoneNumberValid(phoneNumber)) {
                    dismiss()
                    preorderRegisterViewModel.setPhoneNumber(phoneNumber)
                } else {
                    Toast.makeText(requireContext(), "휴대폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
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
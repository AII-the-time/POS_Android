package org.swm.att.home.home.keypad_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import org.swm.att.common_ui.presenter.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUserPhoneNumInputBinding
import org.swm.att.home.home.preorder.PreorderRegisterViewModel

class PreorderInputUserPhoneNumDialog(
    private val preorderRegisterViewModel: PreorderRegisterViewModel
): BaseDialog<DialogUserPhoneNumInputBinding>(R.layout.dialog_user_phone_num_input) {
    private val phoneNumberViewModel: PhoneNumberViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneNumberViewModel.clearPhoneNumber()
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
                phoneNumberViewModel.addPhoneNumber(it)
            }
            setOnClearBtnClickListener {
                phoneNumberViewModel.removePhoneNumber()
            }
            setOnEnterBtnClickListener {
                val phoneNumber = phoneNumberViewModel.getPhoneNumber()
                if (phoneNumberViewModel.isPhoneNumberValid(phoneNumber)) {
                    dismiss()
                    preorderRegisterViewModel.setPhoneNumber(phoneNumber)
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
}
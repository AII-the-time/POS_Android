package org.swm.att.home.home.preorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogUserPhoneNumInputBinding
import org.swm.att.home.home.HomeViewModel

class PreorderInputUserPhoneNumDialog(
    private val homeViewModel: HomeViewModel
): BaseDialog<DialogUserPhoneNumInputBinding>(R.layout.dialog_user_phone_num_input) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setBtnClickListener()
        setUpCustomKeypad()
        setPhoneNumberObserver()
    }

    private fun initView() {
        binding.clEarnMileageBtns.visibility = View.GONE
        binding.tvEarnMileageDes.visibility = View.INVISIBLE
        binding.tvPreorderRequestDes.visibility = View.VISIBLE
        binding.clPreorderRequest.visibility = View.VISIBLE
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
                val request = binding.edtPreorderRequest.text.toString()
                if (homeViewModel.isPhoneNumberValid(phoneNumber)) {
                    homeViewModel.postPreOrder(phoneNumber, request)
                    dismiss()
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
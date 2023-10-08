package org.swm.att.common_ui.presenter.alarm

import org.swm.att.common_ui.R
import org.swm.att.common_ui.databinding.DailogPreorderAlarmBinding
import org.swm.att.common_ui.presenter.base.BaseActivity

class PreorderAlarmDialog :
    BaseActivity<DailogPreorderAlarmBinding>(R.layout.dailog_preorder_alarm) {
    override fun onResume() {
        super.onResume()
        setCloseDialogBtnClickListener()
        setDataBinding()
    }

    private fun setCloseDialogBtnClickListener() {
        binding.btnCloseAddCategoryDialog.setOnClickListener {
            finish()
        }
    }

    private fun setDataBinding() {
        val preorderDate = intent.getStringExtra("preorderDate")
        val phoneNumber = intent.getStringExtra("phoneNumber")?.substring(7) ?: "Unknown"
        val totalOrderCount = intent.getIntExtra("totalOrderCount", -1)
        binding.preorderDate = preorderDate
        binding.phoneNumber = phoneNumber
        binding.totalOrderCount = totalOrderCount
    }
}
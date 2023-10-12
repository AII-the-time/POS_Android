package org.swm.att.home.util.alarm

import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.home.R
import org.swm.att.home.databinding.DailogPreorderAlarmBinding

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
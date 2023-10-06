package org.swm.att.common_ui.presenter.alarm

import org.swm.att.common_ui.R
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.common_ui.databinding.DailogPreorderAlarmBinding

class PreorderAlarmDialog :
    BaseActivity<DailogPreorderAlarmBinding>(R.layout.dailog_preorder_alarm) {
    override fun onResume() {
        super.onResume()
        setCloseDialogBtnClickListener()
    }

    private fun setCloseDialogBtnClickListener() {
        binding.btnCloseAddCategoryDialog.setOnClickListener {
            finish()
        }
    }
}
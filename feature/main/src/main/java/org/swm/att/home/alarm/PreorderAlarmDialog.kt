package org.swm.att.home.alarm

import android.content.Intent
import android.net.Uri
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.home.R
import org.swm.att.home.databinding.DailogPreorderAlarmBinding

class PreorderAlarmDialog :
    BaseActivity<DailogPreorderAlarmBinding>(R.layout.dailog_preorder_alarm) {
    override fun onResume() {
        super.onResume()
        setCloseDialogBtnClickListener()
        setDataBinding()
        setAlarmDialogClickListener()
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

    private fun setAlarmDialogClickListener() {
        binding.clDialogPreorderAlarm.setOnClickListener {
            val preorderId = intent.getIntExtra("preorderId", -1)
            val uri = Uri.parse("http://www.cafepos.com/preorder?preorderId=$preorderId")
            val intent = Intent().also {
                it.data = uri
            }
            startActivity(intent)
            finish()
        }
    }
}
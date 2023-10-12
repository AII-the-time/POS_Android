package org.swm.att.home.util.alarm

import android.app.PendingIntent
import android.content.Intent
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.home.MainActivity
import org.swm.att.home.R
import org.swm.att.home.databinding.DailogPreorderAlarmBinding
import java.util.UUID

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
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.putExtra("preorderId", preorderId)
            PendingIntent.getActivity(
                this,
                UUID.randomUUID().hashCode(),
                mainIntent,
                PendingIntent.FLAG_IMMUTABLE
            ).send()
            finish()
        }
    }
}
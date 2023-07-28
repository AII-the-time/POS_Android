package org.swm.att

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.R
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : org.swm.att.common_ui.base.BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setDateAndTime()
        removeStatusBar()
        setMenuItemClickListener()
    }

    private fun setDateAndTime() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MM월 dd일 hh:mm")

        binding.tvDate.text = dateFormat.format(date)

    }

    private fun removeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun setMenuItemClickListener() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    //Handle setting icon press
                    true
                }

                else -> false
            }
        }
    }

}
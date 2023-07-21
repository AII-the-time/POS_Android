package org.swm.att.presenter

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.R
import org.swm.att.common_ui.BaseActivity
import org.swm.att.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        removeStatusBar()
    }

    private fun removeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

}
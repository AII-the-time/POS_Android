package org.swm.att

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.databinding.ActivityMainBinding
import org.swm.att.home.home.HomeFragment

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onResume() {
        super.onResume()
        checkRefreshToken()
        removeStatusBar()
        hideSystemUI()
        getAccesibility()
        setObserver()
        setNavRail()
    }

    @Suppress("DEPRECATION")
    private fun removeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun setNavRail() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit()
        binding.navRail.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_screen -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit()
                    true
                }

                else -> true
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if(controller != null) {
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    private fun checkRefreshToken() {
        mainViewModel.checkRefreshToken()
    }

    private fun setObserver() {
        mainViewModel.refreshExist.observe(this) { exist ->
            if (exist == false) {
                getAccesibility()
            }
        }
    }

    private fun getAccesibility() {
        // 로그인 및 회원가입 처리
    }

}
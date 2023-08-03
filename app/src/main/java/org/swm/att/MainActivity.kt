package org.swm.att

import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.datastore.dataStore
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.data.datastore.CryptoManager
import org.swm.att.data.datastore.TokenSerializer
import org.swm.att.databinding.ActivityMainBinding
import org.swm.att.home.home.HomeFragment

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private val Context.dataStore by dataStore(
        fileName = "user-setting.json",
        serializer = TokenSerializer(CryptoManager())
    )

    override fun onResume() {
        super.onResume()
        removeStatusBar()
        setNavRail()
        hideSystemUI()
        checkRefreshToken()
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
        mainViewModel.getRefreshToken(dataStore)

        mainViewModel.token.observe(this) {
            if(it.refreshToken == null) { //회원가입 처리

            } else {
                // 만료 처리
            }
        }
    }


}
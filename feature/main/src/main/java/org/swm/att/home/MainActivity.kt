package org.swm.att.home

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.home.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onResume() {
        super.onResume()
        checkRefreshToken()
        setBindingData()
        removeStatusBar()
        hideSystemUI()
        setObserver()
        setNavRail()
    }

    private fun checkRefreshToken() {
        mainViewModel.checkRefreshToken()
    }

    private fun setBindingData() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.KOREA)
        binding.barDate = dateFormat.format(date)
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
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = host.navController

        binding.posNavRail.setupWithNavController(navController)
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

    private fun setObserver() {
        mainViewModel.refreshExist.observe(this) { exist ->
            if (exist == false) {
//                로그인 및 회원가입으로 화면 전환
            }
        }
    }

}
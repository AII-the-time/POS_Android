package org.swm.att.home

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.home.constant.NavDestinationType
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
        setNavController()
        setBindingData()
        removeStatusBar()
        hideSystemUI()
        setObserver()
        setCustomNavRailClickListener()
    }

    private fun checkRefreshToken() {
        mainViewModel.checkRefreshToken()
    }

    private fun setNavController() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController
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

    private fun setCustomNavRailClickListener() {
        binding.btnHome.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Home)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Home)
            } else {
                binding.btnHome.isChecked = true
            }
        }
        binding.btnBills.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Bills)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Bills)
            } else {
                binding.btnBills.isChecked = true
            }
        }
        binding.btnPreorder.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Preorder)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Preorder)
            } else {
                binding.btnPreorder.isChecked = true
            }
        }
        binding.btnStock.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Stock)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Stock)
            } else {
                binding.btnStock.isChecked = true
            }
        }
        binding.btnRecipe.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Recipe)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Recipe)
            } else {
                binding.btnRecipe.isChecked = true
            }
        }
        binding.btnWorker.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Worker)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Worker)
            } else {
                binding.btnWorker.isChecked = true
            }
        }
        binding.btnSales.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Sales)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Sales)
            } else {
                binding.btnSales.isChecked = true
            }
        }
        binding.btnSetting.setOnClickListener {
            if (isNotDestinationSame(NavDestinationType.Setting)) {
                mainViewModel.setSelectedScreen(NavDestinationType.Setting)
            } else {
                binding.btnSetting.isChecked = true
            }
        }
    }

    private fun isNotDestinationSame(selected: NavDestinationType): Boolean {
        return mainViewModel.curSelectedScreen.value != selected
    }

    private fun setObserver() {
        mainViewModel.refreshExist.observe(this) { exist ->
            if (exist == false) {
//                로그인 및 회원가입으로 화면 전환
            }
        }

        mainViewModel.selectedScreen.observe(this) { destination ->
            mainViewModel.isGlobalAction.value?.let {
                if (!it) {
                    navController.navigate(destination.action)
                } else {
                    findViewById<CheckBox>(destination.viewId).isChecked = true
                }
                changeNavDestination()
                mainViewModel.resetIsGlobalAction()
            }
        }
    }

    private fun changeNavDestination() {
        mainViewModel.curSelectedScreen.apply {
            this.value?.let {
                findViewById<CheckBox>(it.viewId).isChecked = false
                mainViewModel.changedCurSelectedScreen()
            }
        }
    }
}
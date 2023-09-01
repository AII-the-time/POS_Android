package org.swm.att.home

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.common_ui.util.nav.NavDestination
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
            if (isNotDestinationSame(NavDestination.Home)) {
                mainViewModel.setSelectedScreen(NavDestination.Home)
            } else {
                binding.btnHome.isChecked = true
            }
        }
        binding.btnBills.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Bills)) {
                mainViewModel.setSelectedScreen(NavDestination.Bills)
            } else {
                binding.btnBills.isChecked = true
            }
        }
        binding.btnPreorder.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Preorder)) {
                mainViewModel.setSelectedScreen(NavDestination.Preorder)
            } else {
                binding.btnPreorder.isChecked = true
            }
        }
        binding.btnStock.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Stock)) {
                mainViewModel.setSelectedScreen(NavDestination.Stock)
            } else {
                binding.btnStock.isChecked = true
            }
        }
        binding.btnRecipe.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Recipe)) {
                mainViewModel.setSelectedScreen(NavDestination.Recipe)
            } else {
                binding.btnRecipe.isChecked = true
            }
        }
        binding.btnWorker.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Worker)) {
                mainViewModel.setSelectedScreen(NavDestination.Worker)
            } else {
                binding.btnWorker.isChecked = true
            }
        }
        binding.btnSales.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Sales)) {
                mainViewModel.setSelectedScreen(NavDestination.Sales)
            } else {
                binding.btnSales.isChecked = true
            }
        }
        binding.btnSetting.setOnClickListener {
            if (isNotDestinationSame(NavDestination.Setting)) {
                mainViewModel.setSelectedScreen(NavDestination.Setting)
            } else {
                binding.btnSetting.isChecked = true
            }
        }
    }

    private fun isNotDestinationSame(selected: NavDestination): Boolean {
        return mainViewModel.curSelectedScreen.value != selected
    }

    private fun setObserver() {
        mainViewModel.refreshExist.observe(this) { exist ->
            if (exist == false) {
//                로그인 및 회원가입으로 화면 전환
            }
        }

        mainViewModel.selectedScreen.observe(this) { destination ->
            when(destination) {
                NavDestination.Home -> {
                    navController.navigate(R.id.action_global_fragment_home)
                    changeNavDestination()
                }
                NavDestination.Bills -> {
                    navController.navigate(R.id.action_global_fragment_bill)
                    changeNavDestination()
                }
                NavDestination.Preorder -> {
                    navController.navigate(R.id.action_global_fragment_preorder)
                    changeNavDestination()
                }
                NavDestination.Stock -> {
                    navController.navigate(R.id.action_global_fragment_stock)
                    changeNavDestination()
                }
                NavDestination.Recipe -> {
                    navController.navigate(R.id.action_global_fragment_recipe)
                    changeNavDestination()
                }
                NavDestination.Worker -> {
                    navController.navigate(R.id.action_global_fragment_worker)
                    changeNavDestination()
                }
                NavDestination.Sales -> {
                    navController.navigate(R.id.action_global_fragment_sales)
                    changeNavDestination()
                }
                NavDestination.Setting -> {
                    navController.navigate(R.id.action_global_fragment_setting)
                    changeNavDestination()
                }
            }

        }
    }

    private fun changeNavDestination() {
        mainViewModel.curSelectedScreen.apply {
            when(this.value) {
                NavDestination.Home -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnHome.isChecked = false
                }
                NavDestination.Bills -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnBills.isChecked = false
                }
                NavDestination.Preorder -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnPreorder.isChecked = false
                }
                NavDestination.Stock -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnStock.isChecked = false
                }
                NavDestination.Recipe -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnRecipe.isChecked = false
                }
                NavDestination.Worker -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnWorker.isChecked = false
                }
                NavDestination.Sales -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnSales.isChecked = false
                }
                NavDestination.Setting -> {
                    mainViewModel.changedCurSelectedScreen()
                    binding.btnSetting.isChecked = false
                }
                else -> {
                    /* do nothing */
                }
            }
        }
    }

}
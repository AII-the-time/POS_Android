package org.swm.att.home

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseActivity
import org.swm.att.common_ui.util.nav.NavDestination
import org.swm.att.home.bills.BillFragment
import org.swm.att.home.databinding.ActivityMainBinding
import org.swm.att.home.home.HomeFragment
import org.swm.att.home.preorder.PreorderFragment
import org.swm.att.home.recipe.RecipeFragment
import org.swm.att.home.sale.SaleFragment
import org.swm.att.home.setting.SettingFragment
import org.swm.att.home.stock.StockFragment
import org.swm.att.home.worker.WorkerFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onResume() {
        super.onResume()
        checkRefreshToken()
        setBindingData()
        removeStatusBar()
        hideSystemUI()
        setObserver()
        setCustomNavRailClickListener()
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
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Bills -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, BillFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Preorder -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, PreorderFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Stock -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, StockFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Recipe -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, RecipeFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Worker -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, WorkerFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Sales -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SaleFragment()).commit()
                    changeNavDestination()
                }
                NavDestination.Setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SettingFragment()).commit()
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
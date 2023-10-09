package org.swm.att.home

import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.home.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPreorderAlarm()
    }

    override fun onResume() {
        super.onResume()
        checkRefreshToken()
        setNavController()
        setBindingData()
        setObserver()
    }

    private fun checkRefreshToken() {
        mainViewModel.checkRefreshToken()
    }

    private fun setNavController() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController
    }

    private fun setBindingData() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.KOREA)
        binding.barDate = dateFormat.format(date)
        binding.mainViewModel = mainViewModel
    }

    private fun setObserver() {
        mainViewModel.refreshExist.observe(this) { exist ->
            if (exist == false) {
//                로그인 및 회원가입으로 화면 전환
            }
        }

        mainViewModel.selectedScreen.observe(this) { destination ->
            mainViewModel.isGlobalAction.value?.let {
                findViewById<CheckBox>(destination.viewId).isChecked = true
                if (!it and mainViewModel.isDestinationDiff(destination)) {
                    navController.navigate(destination.action)
                } else {
                    mainViewModel.resetIsGlobalAction()
                }

                if (mainViewModel.isDestinationDiff(destination)) {
                    changeNavDestination()
                }
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

    private fun setPreorderAlarm() {
        mainViewModel.getTodayPreorder(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.cancelAllPreorderAlarm()
    }
}
package org.swm.att.home.main

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.home.R
import org.swm.att.home.main.constant.NavDestinationType
import org.swm.att.home.databinding.ActivityMainBinding
import org.swm.att.home.main.home.HomeFragmentDirections
import org.swm.att.home.main.alarm.AlarmManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: 로그인 과정 추가되면 수정
        //checkRefreshToken()
        setNavController()
        setBindingData()
        setObserver()
    }
//    private fun checkRefreshToken() {
//        mainViewModel.checkRefreshToken()
//    }

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
//        mainViewModel.refreshExist.observe(this) { exist ->
//            if (exist == false) {
//                // 로그인 및 회원가입으로 화면 전환
//            } else {
//                // storeId 확인
//                mainViewModel.checkStoreId()
//            }
//        }

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

    override fun onDestroy() {
        AlarmManager.cancelAllAlarm(this)
        super.onDestroy()
    }

    private fun checkPreorderAlarm(preorderId: Int?) {
        preorderId?.let {
            if (preorderId != -1) {
                val navHost =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val action = HomeFragmentDirections.actionGlobalFragmentPreorder(it)
                navHost.navController.navigate(action)
                mainViewModel.directWithGlobalAction(NavDestinationType.Preorder)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val preorderId = intent?.getIntExtra("preorderId", -1)
        checkPreorderAlarm(preorderId)
    }
}
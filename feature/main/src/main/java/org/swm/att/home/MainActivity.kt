package org.swm.att.home

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.common_ui.state.UiState
import org.swm.att.home.databinding.ActivityMainBinding
import org.swm.att.home.home.HomeFragmentDirections
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
        //checkRefreshToken()
        //TODO: 회원가입 화면이 나오기 전까지 임시 accessToken, refreshToken 사용
        checkStoreId()
        setBindingData()
        setObserver()
        setNavController()
    }

//    private fun checkRefreshToken() {
//        mainViewModel.checkRefreshToken()
//    }

    private fun checkStoreId() {
        mainViewModel.checkStoreId()
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
                // 로그인 및 회원가입으로 화면 전환
            } else {
                // storeId 확인
                mainViewModel.checkStoreId()
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.registerStoreState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                mainViewModel.setStoreId(it.storeId)
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> { Toast.makeText(this@MainActivity, uiState.errorMsg, Toast.LENGTH_SHORT).show() }
                    }
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
        mainViewModel.cancelAllPreorderAlarm()
        super.onDestroy()
    }

    private fun checkPreorderAlarm(preorderId: Int?) {
        preorderId?.let {
            if (preorderId != -1) {
                val navHost =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val action = HomeFragmentDirections.actionGlobalFragmentPreorder(it)
                navHost.navController.navigate(action)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val preorderId = intent?.getIntExtra("preorderId", -1)
        checkPreorderAlarm(preorderId)
    }
}
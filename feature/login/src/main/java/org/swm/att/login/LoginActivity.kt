package org.swm.att.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import org.swm.att.common_ui.navigator.AttNavigatorInjector
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.login.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginViewModel by viewModels<LoginViewModel>()
    private val attNavigator: FakeAttNavigator by lazy {
        EntryPointAccessors.fromActivity(this, AttNavigatorInjector::class.java).attNavigator()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRefreshToken()
        setObserver()
    }

    private fun checkRefreshToken() {
        loginViewModel.checkRefreshToken()
    }

    private fun setObserver() {
        loginViewModel.refreshExist.observe(this) { isExist ->
            if (isExist) {
                attNavigator.navigateToMain(this)
                this.finish()
            }
        }

        loginViewModel.refreshTokenState.observe(this) { isSuccess ->
            if (isSuccess) {
                attNavigator.navigateToMain(this)
                this.finish()
            } else {
                Toast.makeText(this, "오류가 발생했습니다. 다시 이용해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
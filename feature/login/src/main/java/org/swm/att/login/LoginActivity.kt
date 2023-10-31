package org.swm.att.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.common_ui.state.UiState
import org.swm.att.login.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPostPhoneNumberStateObserver()
        setCheckCertificationCodeStateObserver()
        setSubmitPhoneNumberBtnClickListener()
        setSubmitAuthenticationBtnClickListener()
    }

    //observe
    private fun setPostPhoneNumberStateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.postPhoneNumberState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(this@LoginActivity, "인증번호가 전송되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(this@LoginActivity, uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setCheckCertificationCodeStateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.checkCertificationCodeState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(this@LoginActivity, "인증되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(this@LoginActivity, uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //btn click listener
    private fun setSubmitPhoneNumberBtnClickListener() {
        binding.btnSubmitPhoneNumber.setOnClickListener {
            val phoneNumber = binding.tiePhoneNumber.text.toString()
            if (!phoneNumber.isNullOrEmpty()) {
                loginViewModel.postPhoneNumberForAuthentication(phoneNumber)
            } else {
                Toast.makeText(this@LoginActivity, "전화번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSubmitAuthenticationBtnClickListener() {
        binding.btnSubmitAuthenticationCode.setOnClickListener {
            val code = binding.tieAuthenticationCode.text
            if (!code.isNullOrEmpty()) {
                loginViewModel.checkCertificationCode(code.toString())
            } else {
                Toast.makeText(this@LoginActivity, "올바른 인증번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package org.swm.att.login.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.login.R
import org.swm.att.login.databinding.FragmentAuthenticationBinding

@AndroidEntryPoint
class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {
    private val loginViewModel by viewModels<AuthenticationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                            Toast.makeText(requireContext(), "인증번호가 전송되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(requireContext(), "인증되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "전화번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSubmitAuthenticationBtnClickListener() {
        binding.btnSubmitAuthenticationCode.setOnClickListener {
            val code = binding.tieAuthenticationCode.text
            if (!code.isNullOrEmpty()) {
                loginViewModel.checkCertificationCode(code.toString())
            } else {
                Toast.makeText(requireContext(), "올바른 인증번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package org.swm.att.login.store

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import org.swm.att.common_ui.navigator.AttNavigatorInjector
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.login.R
import org.swm.att.login.databinding.FragmentRegisterStoreBinding

@AndroidEntryPoint
class RegisterStoreFragment : BaseFragment<FragmentRegisterStoreBinding>(R.layout.fragment_register_store) {
    private val registerStoreViewModel by viewModels<RegisterStoreViewModel>()
    private val args by navArgs<RegisterStoreFragmentArgs>()
    private val attNavigator: FakeAttNavigator by lazy {
        EntryPointAccessors.fromActivity(
            requireActivity(),
            AttNavigatorInjector::class.java
        ).attNavigator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBtnBackClickListener()
        setBusinessRegistrationNumBtnCLickListener()
        setRegisterStoreBtnClickListener()
        setObserver()
    }

    private fun setOnBtnBackClickListener() {
        binding.tvReAuthentication.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_register_store_to_fragment_authentication)
        }
    }

    private fun setBusinessRegistrationNumBtnCLickListener() {
        binding.btnSubmitBusinessRegistrationNum.setOnClickListener {
            val businessRegistrationNumber = binding.tieBusinessRegistrationNum.text.toString()
            if (businessRegistrationNumber.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "사업자 등록 번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                registerStoreViewModel.login(businessRegistrationNumber, args.certificatedPhoneToken)
            }
        }
    }

    private fun setRegisterStoreBtnClickListener() {
        binding.btnRegisterStore.setOnClickListener {
            val storeName = binding.tieStoreName.text.toString()
            registerStoreViewModel.registerStore(storeName)
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerStoreViewModel.loginState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "사업자 등록 번호 제출이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                                registerStoreViewModel.saveTokens(it.accessToken, it.refreshToken)
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerStoreViewModel.registerStoreState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            attNavigator.navigateToMain(requireContext())
                            Toast.makeText(requireContext(), "가게 등록이 완료되었습니다!\n환영합니다!", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
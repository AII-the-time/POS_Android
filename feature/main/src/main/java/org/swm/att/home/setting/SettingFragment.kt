package org.swm.att.home.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import org.swm.att.common_ui.navigator.AttNavigatorInjector
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.home.R
import org.swm.att.home.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val settingViewModel by viewModels<SettingViewModel>()
    private val attNavigator: FakeAttNavigator by lazy {
        EntryPointAccessors.fromActivity(requireActivity(), AttNavigatorInjector::class.java).attNavigator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLogoutBtnClickListener()
    }

    private fun setLogoutBtnClickListener() {
        binding.btnLogout.setOnClickListener {
            settingViewModel.logout()
            attNavigator.navigateToLogin(requireActivity())
        }
    }
}
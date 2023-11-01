package org.swm.att.login.store

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.login.R
import org.swm.att.login.databinding.FragmentRegisterStoreBinding

class RegisterStoreFragment : BaseFragment<FragmentRegisterStoreBinding>(R.layout.fragment_register_store) {
    private val args by navArgs<RegisterStoreFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBtnBackClickListener()
    }

    private fun setOnBtnBackClickListener() {
        binding.tvReAuthentication.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_register_store_to_fragment_authentication)
        }
    }
}
package org.swm.att.presenter.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.R
import org.swm.att.common_ui.BaseFragment
import org.swm.att.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setPongData()
    }

    private fun setPongData() {
        homeViewModel.getPong()
    }

    private fun setObserver() {
        homeViewModel.pongData.observe(viewLifecycleOwner) {
//            binding.pongVO = it
        }
    }

}
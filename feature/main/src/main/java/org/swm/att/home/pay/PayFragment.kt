package org.swm.att.home.pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.home.R
import org.swm.att.home.databinding.FragmentPayBinding

class PayFragment : BaseFragment<FragmentPayBinding>(R.layout.fragment_pay) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtnModifyOrderClickListener()
    }

    private fun setBtnModifyOrderClickListener() {
        binding.btnModifyOrder.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_pay_to_fragment_home)
        }
    }
}
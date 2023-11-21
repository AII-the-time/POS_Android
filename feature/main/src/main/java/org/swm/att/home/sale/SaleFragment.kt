package org.swm.att.home.sale

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.adapter.SduiReportAdapter
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.home.R
import org.swm.att.home.databinding.FragmentSaleBinding

@AndroidEntryPoint
class SaleFragment : BaseFragment<FragmentSaleBinding>(R.layout.fragment_sale) {
    private lateinit var sduiReportAdapter: SduiReportAdapter
    private val saleViewModel by viewModels<SaleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSduiReportRecyclerView()
        setReportDataObserver()
        setReportData()
    }

    private fun initSduiReportRecyclerView() {
        sduiReportAdapter = SduiReportAdapter()
        binding.rvSduiReport.apply {
            adapter = sduiReportAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setReportDataObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                saleViewModel.getSduiReportDataState.collect() { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                sduiReportAdapter.submitList(it.responseData.viewContents)
                            }
                        }
                        is UiState.Loading -> { /* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setReportData() {
        saleViewModel.getSduiReportData()
    }
}
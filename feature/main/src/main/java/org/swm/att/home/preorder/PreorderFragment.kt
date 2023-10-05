package org.swm.att.home.preorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.home.MainViewModel
import org.swm.att.home.R
import org.swm.att.home.adapter.BaseRecyclerViewAdapter
import org.swm.att.home.adapter.PreorderListItemAdapter
import org.swm.att.home.constant.NavDestinationType
import org.swm.att.home.databinding.FragmentPreorderBinding

@AndroidEntryPoint
class PreorderFragment : BaseFragment<FragmentPreorderBinding>(R.layout.fragment_preorder) {
    private lateinit var preorderMenuOfBillAdapter: BaseRecyclerViewAdapter
    private lateinit var validPreorderListAdapter: PreorderListItemAdapter
    private lateinit var pastPreorderListAdapter: PreorderListItemAdapter
    private val preorderViewModel: PreorderViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreorderRecyclerView()
        setPreorderFilteringBtnClickListener()
        setObserver()
        setDataBinding() // api 달면 수정할 부분
        setPreorderBtnClickListener()
        setCancelPreorderBtnClickListener()
        initMockData()
    }

    private fun initPreorderRecyclerView() {
        preorderMenuOfBillAdapter = BaseRecyclerViewAdapter()
        binding.rvPreorderMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = preorderMenuOfBillAdapter
        }

        validPreorderListAdapter = PreorderListItemAdapter(preorderViewModel, true)
        binding.rvPreorder.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = validPreorderListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem =
                        (this@apply.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (preorderViewModel.preOrdersData.value?.size ?: 0 <= lastVisibleItem + 5 && !preorderViewModel.isEndOfValidPreOrders()) {
                        preorderViewModel.getNextValidPreOrders(1)
                    }
                }
            })
        }

        pastPreorderListAdapter = PreorderListItemAdapter(preorderViewModel, false)
        binding.rvPastPreorder.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pastPreorderListAdapter
        }
    }

    private fun setPreorderFilteringBtnClickListener() {
        binding.btnSearchPreorder.setOnClickListener {
            val dialog = DialogPreorderFiltering(preorderViewModel)
            dialog.show(childFragmentManager, "preorderFiltering")
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preorderViewModel.selectedPreorderInfo.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                preorderMenuOfBillAdapter.submitList(it.orderitems)
                            }
                        }

                        is UiState.Loading -> {/* do nothing */
                        }

                        is UiState.Error -> Toast.makeText(
                            requireContext(),
                            uiState.errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        preorderViewModel.currentSelectedValidPreorderId.observe(viewLifecycleOwner) {
            val pastId = preorderViewModel.selectedValidPreorderId.value
            binding.isValid = true
            binding.rvPreorder[it].setBackgroundResource(R.color.main_trans)
            preorderViewModel.changeSelectedState(true)
            pastId?.let { pastId ->
                if (pastId != it) {
                    preorderViewModel.selectedValidPreorderId.value?.let { pastId ->
                        binding.rvPreorder[pastId].setBackgroundResource(R.color.back_color)
                    }
                }
            }
            preorderViewModel.selectedPastPreorderId.value?.let { pastId ->
                binding.rvPastPreorder[pastId].setBackgroundResource(R.color.back_color)
            }
        }
        preorderViewModel.currentSelectedPastPreorderId.observe(viewLifecycleOwner) {
            val pastId = preorderViewModel.selectedPastPreorderId.value
            binding.rvPastPreorder[it].setBackgroundResource(R.color.main_trans)
            binding.isValid = false
            preorderViewModel.changeSelectedState(false)
            pastId?.let { pastId ->
                if (pastId != it) {
                    preorderViewModel.selectedPastPreorderId.value?.let { pastId ->
                        binding.rvPastPreorder[pastId].setBackgroundResource(R.color.back_color)
                    }
                }
            }
            preorderViewModel.selectedValidPreorderId.value?.let {  pastId ->
                binding.rvPreorder[pastId].setBackgroundResource(R.color.back_color)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preorderViewModel.getPreOrdersState.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {/* do nothing */
                        }

                        is UiState.Loading -> {/* do nothing */
                        }

                        is UiState.Error -> Toast.makeText(
                            requireContext(),
                            uiState.errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        preorderViewModel.preOrdersData.observe(viewLifecycleOwner) {
            validPreorderListAdapter.submitList(it)
            pastPreorderListAdapter.submitList(it)
        }
    }

    private fun setDataBinding() {
        binding.isValid = true
        binding.preorderViewModel = preorderViewModel
    }

    private fun setPreorderBtnClickListener() {
        binding.btnModifyPreorderList.setOnClickListener {
            val action = PreorderFragmentDirections.actionGlobalFragmentHome(
                selectedMenus = preorderViewModel.getSelectedMenus(),
                isModify = true
            )
            findNavController().navigate(action)
            mainViewModel.directWithGlobalAction(NavDestinationType.Home)
        }
        binding.btnPayBill.setOnClickListener {
            val action =
                PreorderFragmentDirections.actionGlobalFragmentHome(selectedMenus = preorderViewModel.getSelectedMenus())
            findNavController().navigate(action)
            mainViewModel.directWithGlobalAction(NavDestinationType.Home)
        }
    }

    private fun setCancelPreorderBtnClickListener() {
        binding.btnCancelPreorder.setOnClickListener {
            /* todo */
        }
    }

    private fun initMockData() {
        preorderViewModel.getNextValidPreOrders(1)
    }
}
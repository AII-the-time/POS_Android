package org.swm.att.home.preorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.home.MainViewModel
import org.swm.att.home.R
import org.swm.att.home.adapter.BaseRecyclerViewAdapter
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.constant.NavDestinationType
import org.swm.att.home.databinding.FragmentPreorderBinding

@AndroidEntryPoint
class PreorderFragment : BaseFragment<FragmentPreorderBinding>(R.layout.fragment_preorder) {
    private lateinit var preorderMenuOfBillAdapter: BaseRecyclerViewAdapter
    private lateinit var validPreorderListAdapter: SelectableItemAdapter
    private val preorderViewModel: PreorderViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val navArgs by navArgs<PreorderFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreorderRecyclerView()
        setPreorderFilteringBtnClickListener()
        setObserver()
        setDataBinding() // api 달면 수정할 부분
        setPreorderBtnClickListener()
        setCancelPreorderBtnClickListener()
        initData()
    }

    private fun initPreorderRecyclerView() {
        preorderMenuOfBillAdapter = BaseRecyclerViewAdapter()
        binding.rvPreorderMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = preorderMenuOfBillAdapter
        }

        validPreorderListAdapter = SelectableItemAdapter(preorderViewModel)
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
                        preorderViewModel.getNextValidPreOrders()
                    }
                }
            })
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
        preorderViewModel.currentSelectedPreorderId.observe(viewLifecycleOwner) {
            validPreorderListAdapter.notifyItemChanged(it)
        }
        preorderViewModel.selectedPreorderId.observe(viewLifecycleOwner) {
            validPreorderListAdapter.notifyItemChanged(it)
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
        }
    }

    private fun setDataBinding() {
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
                PreorderFragmentDirections.actionGlobalFragmentHome(
                    selectedMenus = preorderViewModel.getSelectedMenus(),
                    preOrderId = preorderViewModel.preOrdersData.value?.get(
                        preorderViewModel.selectedPreorderId.value ?: 0
                    )?.id ?: -1,
                    customerPhoneNumber = preorderViewModel.selectedPreorderInfoData.value?.phone
                )
            findNavController().navigate(action)
            mainViewModel.directWithGlobalAction(NavDestinationType.Home)
        }
    }

    private fun setCancelPreorderBtnClickListener() {
        binding.btnCancelPreorder.setOnClickListener {
            /* todo */
        }
    }

    private fun initData() {
        val preorderId = navArgs.preorderId
        preorderViewModel.setPreorderIdForAlarm(preorderId!!)
        preorderViewModel.getNextValidPreOrders()
    }
}
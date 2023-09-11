package org.swm.att.home.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.home.R
import org.swm.att.home.adapter.CategoryViewPagerAdapter
import org.swm.att.home.adapter.SelectedMenuAdapter
import org.swm.att.home.databinding.FragmentHomeBinding
import org.swm.att.home.home.keypad_dialog.EarnMileageDialog
import org.swm.att.home.home.menu.MenuFragment
import org.swm.att.home.home.preorder.DateTimePickerDialog

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var categoryViewPagerAdapter: CategoryViewPagerAdapter
    private lateinit var selectedMenuAdapter: SelectedMenuAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val args by navArgs<HomeFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.clearSelectedMenuList()
        initRecyclerView()
        setSelectedMenuList()
        setCategories()
        setCategoriesObserver()
        setSelectedMenuObserver()
        setDataBinding()
        setOrderBtnListener()
        setPreorderBtnClickListener()
        setModifyPreorderBtnClickListener()
    }

    private fun setSelectedMenuList() {
        args.selectedMenus?.let {
            homeViewModel.setSelectedMenusVO(it)
        }
        if (args.isModify) {
            binding.isModifyPreorder = true
        }
    }

    private fun initRecyclerView() {
        selectedMenuAdapter = SelectedMenuAdapter(homeViewModel)

        val animator = DefaultItemAnimator()
        animator.supportsChangeAnimations = false
        binding.rvPaymentMenu.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = selectedMenuAdapter
        }
    }

    private fun setCategories() {
        categoryViewPagerAdapter = CategoryViewPagerAdapter(this)
        binding.vpCategory.adapter = categoryViewPagerAdapter

        if (homeViewModel.getMenuState.value == UiState.Loading) {
            homeViewModel.getCategories()
        }
    }

    private fun setCategoriesObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getMenuState.collect { uiState ->
                    when (uiState) {
                        is UiState.Success ->  {
                            for(category in uiState.data.categories) {
                                categoryViewPagerAdapter.addFragment(MenuFragment(category))
                            }
                            TabLayoutMediator(binding.tabView, binding.vpCategory) { tab, position ->
                                tab.text = uiState.data.categories[position].category
                            }.attach()
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {/* nothing */}
                    }
                }
            }
        }
    }

    private fun setSelectedMenuObserver() {
        homeViewModel.selectedMenuMap.observe(viewLifecycleOwner) {
            it?.let {
                selectedMenuAdapter.submitList(it.toList())
            }
        }
    }

    private fun setDataBinding() {
        binding.homeViewHolder = homeViewModel
    }

    private fun setOrderBtnListener() {
        binding.btnOrder.setOnClickListener {
            homeViewModel.clearPhoneNumber()
            val mileageDialog = EarnMileageDialog(homeViewModel)
            mileageDialog.show(requireActivity().supportFragmentManager, "EarnMileageDialog")
        }
    }

    private fun setPreorderBtnClickListener() {
        binding.btnPreorder.setOnClickListener {
            homeViewModel.clearPhoneNumber()
            val dateTimePicker = DateTimePickerDialog(homeViewModel)
            dateTimePicker.show(requireActivity().supportFragmentManager, "DateTimePickerDialog")
        }
    }

    private fun setModifyPreorderBtnClickListener() {
        binding.btnModificationComplete.setOnClickListener {
            //주문 내역 업데이트 api 연결 필요
            homeViewModel.clearSelectedMenuList()
        }
    }

}
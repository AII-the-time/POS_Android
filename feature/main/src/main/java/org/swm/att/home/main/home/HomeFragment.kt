package org.swm.att.home.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.home.R
import org.swm.att.home.main.adapter.CategoryViewPagerAdapter
import org.swm.att.home.main.adapter.SelectedMenuAdapter
import org.swm.att.home.databinding.FragmentHomeBinding
import org.swm.att.home.main.home.keypad_dialog.EarnMileageDialog
import org.swm.att.home.main.home.menu.MenuFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var categoryViewPagerAdapter: CategoryViewPagerAdapter
    private lateinit var selectedMenuAdapter: SelectedMenuAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val args by navArgs<HomeFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearUiValues()
        initRecyclerView()
        setCategoriesObserver()
        setSelectedMenuObserver()
        setDataBinding()
        setOrderBtnListener()
        setPreorderBtnClickListener()
        setModifyPreorderBtnClickListener()
        checkStoreId()
    }

    private fun clearUiValues() {
        homeViewModel.clearUiValues()
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

    private fun setCategories(storeId: Int) {
        categoryViewPagerAdapter = CategoryViewPagerAdapter(this)
        binding.vpCategory.adapter = categoryViewPagerAdapter
        homeViewModel.getCategories(storeId)
    }

    private fun setCategoriesObserver() {
        homeViewModel.storeIdExist.observe(viewLifecycleOwner) {
            if (it != -1) { // storeId가 있는 경우
                setPreorderAlarm(it)
                setSelectedMenuList()
                setCategories(it)
            } else { // storeId가 없는 경우
                // TODO 새로운 가게 등록 화면으로 전환
                homeViewModel.registerStore()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.registerStoreState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(requireContext(), "가게 등록이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                            uiState.data?.let {
                                setPreorderAlarm(it.storeId)
                                setSelectedMenuList()
                                setCategories(it.storeId)
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> { Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show() }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getMenuState.collect { uiState ->
                    when (uiState) {
                        is UiState.Success ->  {
                            clearViewPagerAdapter()
                            uiState.data?.let {
                                for (index in it.categories.indices) {
                                    categoryViewPagerAdapter.addFragment(MenuFragment(index))
                                }
                                setTabMediator(it)
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UiState.Loading -> {/* nothing */}
                    }
                }
            }
        }
    }

    private fun clearViewPagerAdapter() {
        categoryViewPagerAdapter.clearFragment()
    }

    private fun setTabMediator(categoryList: CategoriesVO) {
        TabLayoutMediator(binding.tabView, binding.vpCategory) { tab, position ->
            tab.text = categoryList.categories[position].category
        }.attach()
    }

    private fun setSelectedMenuObserver() {
        homeViewModel.selectedMenuMap.observe(viewLifecycleOwner) {
            it?.let {
                selectedMenuAdapter.submitList(it.toList())
            }
        }
    }

    private fun setDataBinding() {
        binding.homeViewModel = homeViewModel
        binding.isPreorder = args.preOrderId != -1
    }

    private fun setOrderBtnListener() {
        binding.btnOrder.setOnClickListener {
            val mileageDialog =
                EarnMileageDialog(homeViewModel, args.preOrderId, args.customerPhoneNumber)
            mileageDialog.show(requireActivity().supportFragmentManager, "EarnMileageDialog")
        }
    }

    private fun setPreorderBtnClickListener() {
        binding.btnPreorder.setOnClickListener {
            val orderedMenus = homeViewModel.getOrderedMenusVO()
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentPreorderRegister(orderedMenus)
            findNavController().navigate(action)
        }
    }

    private fun setModifyPreorderBtnClickListener() {
        binding.btnModificationComplete.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentPreorderRegister(
                homeViewModel.getOrderedMenusVO(),
                args.preOrderId,
                args.customerPhoneNumber
            )
            findNavController().navigate(action)
        }
    }

    private fun checkStoreId() {
        val storeId = homeViewModel.storeIdExist.value
        if (storeId == null) {
            homeViewModel.checkStoreId()
        } else {
            setSelectedMenuList()
            setCategories(storeId)
        }
    }

    override fun onDestroy() {
        // TODO: activityViewModels와 기존 값 clear하는 방법 중 고민해보기
        homeViewModel.clearGetMenuState()
        super.onDestroy()
    }

    private fun setPreorderAlarm(storeId: Int) {
        if (!homeViewModel.isRegisteredPreorderAlarm()) {
            homeViewModel.getTodayPreorder(storeId)
        }
    }
}
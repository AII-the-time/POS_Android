package org.swm.att.home.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.home.R
import org.swm.att.home.adapter.CategoryViewPagerAdapter
import org.swm.att.home.adapter.SelectedMenuAdapter
import org.swm.att.home.databinding.FragmentHomeBinding
import org.swm.att.home.dialog.EarnMileageDialog
import org.swm.att.home.menu.MenuFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var categoryViewPagerAdapter: CategoryViewPagerAdapter
    private lateinit var selectedMenuAdapter: SelectedMenuAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")
        initRecyclerView()
        setCategories()
        setCategoriesObserver()
        setSelectedMenuObserver()
        setDataBinding()
        setOrderBtnListener()
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume")
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

        if (homeViewModel.categoryList.value == null) {
            homeViewModel.getCategories()
        }
    }

    private fun setCategoriesObserver() {
        homeViewModel.categoryList.observe(viewLifecycleOwner) {
            for(category in it.categories) {
                categoryViewPagerAdapter.addFragment(MenuFragment(category))
            }

            TabLayoutMediator(binding.tabView, binding.vpCategory) { tab, position ->
                tab.text = it.categories[position].category
            }.attach()

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
            val mileageDialog = EarnMileageDialog(homeViewModel)
            mileageDialog.show(requireActivity().supportFragmentManager, "EarnMileageDialog")
        }
    }

}
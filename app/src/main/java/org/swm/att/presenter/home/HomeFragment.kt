package org.swm.att.presenter.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.R
import org.swm.att.common_ui.BaseFragment
import org.swm.att.databinding.FragmentHomeBinding
import org.swm.att.presenter.adapter.CategoryViewPagerAdapter
import org.swm.att.presenter.adapter.SelectedMenuAdapter
import org.swm.att.presenter.menu.MenuFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var categoryViewPagerAdapter: CategoryViewPagerAdapter
    private lateinit var selectedMenuAdapter: SelectedMenuAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setTabLayout()
        setSelectedMenuObserver()
        setDataBinding()
    }

    private fun initRecyclerView() {
        selectedMenuAdapter = SelectedMenuAdapter()

        binding.rvPaymentMenu.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = selectedMenuAdapter
        }
    }

    private fun setTabLayout() {
        categoryViewPagerAdapter = CategoryViewPagerAdapter(this)
        //임의로 하나의 카테고리 추가
        categoryViewPagerAdapter.addFragment(MenuFragment())
        binding.vpCategory.adapter = categoryViewPagerAdapter

        TabLayoutMediator(binding.tabView, binding.vpCategory) { tab, position ->
            tab.text = "카페 및 쿠키"
        }.attach()
    }

    private fun setSelectedMenuObserver() {
        homeViewModel.selectedMenuMap.observe(viewLifecycleOwner) {
            selectedMenuAdapter.submitList(it.toList())
        }
    }

    private fun setDataBinding() {
        binding.homeViewHolder = homeViewModel
    }

}
package org.swm.att.home.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.swm.att.home.R
import org.swm.att.home.adapter.CategoryMenuAdapter
import org.swm.att.home.databinding.FragmentMenuBinding
import org.swm.att.common_ui.base.BaseFragment

@AndroidEntryPoint
class MenuFragment : org.swm.att.common_ui.base.BaseFragment<FragmentMenuBinding>(R.layout.fragment_menu) {
    private lateinit var categoryMenuAdapter: CategoryMenuAdapter
    private val menuViewModel: MenuViewModel by viewModels()
    private val homeViewModel: org.swm.att.home.home.HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setMenuObserver()
    }

    private fun initRecyclerView() {
        categoryMenuAdapter = CategoryMenuAdapter()
        val itemTouchHelperCallback =
            org.swm.att.common_ui.util.ItemTouchHelperCallback(categoryMenuAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binding.rvMenuForCategory)

        categoryMenuAdapter.setOnItemClickListener {
            homeViewModel.addSelectedMenu(it)
        }

        categoryMenuAdapter.setOnStartDragListener(object:
            org.swm.att.common_ui.util.StartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                helper.startDrag(viewHolder)
            }
        })

        binding.rvMenuForCategory.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 5)
            adapter = categoryMenuAdapter
        }

        menuViewModel.getMenuList()
    }

    private fun setMenuObserver() {
        menuViewModel.menuList.observe(viewLifecycleOwner) {
            categoryMenuAdapter.submitList(it)
        }
    }
}
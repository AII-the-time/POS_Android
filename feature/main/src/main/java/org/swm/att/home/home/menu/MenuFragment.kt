package org.swm.att.home.home.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.util.ItemTouchHelperCallback
import org.swm.att.common_ui.util.StartDragListener
import org.swm.att.domain.entity.response.CategoryVO
import org.swm.att.home.R
import org.swm.att.home.adapter.CategoryMenuAdapter
import org.swm.att.home.databinding.FragmentMenuBinding
import org.swm.att.home.home.HomeViewModel

class MenuFragment(
    private val category: CategoryVO
) : BaseFragment<FragmentMenuBinding>(R.layout.fragment_menu) {
    private lateinit var categoryMenuAdapter: CategoryMenuAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setMenuObserver()
    }

    private fun initRecyclerView() {
        categoryMenuAdapter = CategoryMenuAdapter(homeViewModel)
        val itemTouchHelperCallback =
            ItemTouchHelperCallback(categoryMenuAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binding.rvMenuForCategory)

        categoryMenuAdapter.setOnStartDragListener(object : StartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                helper.startDrag(viewHolder)
            }
        })

        binding.rvMenuForCategory.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryMenuAdapter
        }
    }

    private fun setMenuObserver() {
        categoryMenuAdapter.submitList(category.menus)
    }
}
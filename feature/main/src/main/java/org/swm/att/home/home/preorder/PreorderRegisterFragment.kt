package org.swm.att.home.home.preorder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.home.R
import org.swm.att.home.adapter.OrderedMenuAdapter
import org.swm.att.home.databinding.FragmentPreorderRegisterBinding

class PreorderRegisterFragment : BaseFragment<FragmentPreorderRegisterBinding>(R.layout.fragment_preorder_register) {
    private lateinit var orderedMenuAdapter: OrderedMenuAdapter
    private val navArgs by navArgs<PreorderRegisterFragmentArgs>()
    private val preorderRegisterViewModel by viewModels<PreorderRegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreorderRecyclerView()
        setDataBinding()
        setOrderedMenus()
        setModifyPreorderBtnClickListener()
    }

    private fun initPreorderRecyclerView() {
        orderedMenuAdapter = OrderedMenuAdapter()
        binding.rvPreorderMenuItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderedMenuAdapter
        }
    }

    private fun setDataBinding() {
        binding.preorderRegisterViewModel = preorderRegisterViewModel
    }

    private fun setOrderedMenus() {
        navArgs.orderedMenus?.let {
            orderedMenuAdapter.submitList(it.menus)
            preorderRegisterViewModel.setOrderedMenus(it)
        }
    }

    private fun setModifyPreorderBtnClickListener() {
        binding.btnModifyPreorderList.setOnClickListener {
            preorderRegisterViewModel.orderedMenus.value?.let {
                val action = PreorderRegisterFragmentDirections.actionFragmentPreroderRegisterToFragmentHome(selectedMenus = it)
                findNavController().navigate(action)
            }
        }
    }
}
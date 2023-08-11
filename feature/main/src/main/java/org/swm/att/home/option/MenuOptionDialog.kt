package org.swm.att.home.option

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.R
import org.swm.att.home.adapter.MenuOptionAdapter
import org.swm.att.home.databinding.DialogMenuOptionBinding
import org.swm.att.home.home.HomeViewModel

class MenuOptionDialog(
    private val homeViewModel: HomeViewModel,
    private val menuVO: MenuVO
): BaseDialog<DialogMenuOptionBinding>(R.layout.dialog_menu_option) {
    private lateinit var menuOptionAdapter: MenuOptionAdapter
    private val menuOptionViewModel by viewModels<MenuOptionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnListener()
        initOptionsRecyclerView()
        setOptionAddBtnClickListener()
    }

    private fun setDialogCloseBtnListener() {
        binding.btnCloseMenuOptionPickerDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initOptionsRecyclerView() {
        menuOptionAdapter = MenuOptionAdapter(menuOptionViewModel)
        binding.rvMenuOption.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuOptionAdapter
        }
        menuOptionAdapter.submitList(menuVO.options)
    }

    private fun setOptionAddBtnClickListener() {
        binding.btnMenuOptionAdd.setOnClickListener {
            val optionsPrice = menuOptionViewModel.selectedOptionList.value?.sumOf {
                it.types.sumOf { type -> type.price }
            } ?: 0
            val selectedMenuWithOptions = MenuVO(
                menuVO.id,
                menuVO.name,
                menuVO.price + optionsPrice,
                menuOptionViewModel.selectedOptionList.value ?: listOf()
            )
            homeViewModel.addSelectedMenu(selectedMenuWithOptions)
            dismiss()
        }
    }

}
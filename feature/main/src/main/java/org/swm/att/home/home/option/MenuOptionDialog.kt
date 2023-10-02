package org.swm.att.home.home.option

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.home.R
import org.swm.att.home.adapter.MenuOptionAdapter
import org.swm.att.home.databinding.DialogMenuOptionBinding
import org.swm.att.home.home.HomeViewModel

class MenuOptionDialog(
    private val homeViewModel: HomeViewModel,
    private val menuVO: MenuWithRecipeVO
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
        menuOptionAdapter.submitList(menuVO.option)
    }

    private fun setOptionAddBtnClickListener() {
        binding.btnMenuOptionAdd.setOnClickListener {
            val optionsPrice =
                menuOptionViewModel.selectedOptionMap.value?.values?.sumOf { it.price } ?: 0
            var detail: String? = null
            if (binding.edtMenuCustomOption.text.toString() != "") {
                detail = binding.edtMenuCustomOption.text.toString()
            }
            val selectedMenuWithOptions = OrderedMenuVO(
                id = menuVO.id,
                name = menuVO.menuName,
                price = menuVO.price.toInt() + optionsPrice,
                options = menuOptionViewModel.selectedOptionMap.value?.values?.toList()
                    ?.sortedBy { it.id } ?: emptyList(),
                detail = detail
            )
            homeViewModel.addSelectedMenu(selectedMenuWithOptions)
            dismiss()
        }
    }

}
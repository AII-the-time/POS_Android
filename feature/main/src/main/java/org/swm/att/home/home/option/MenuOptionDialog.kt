package org.swm.att.home.home.option

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.home.R
import org.swm.att.home.adapter.MenuOptionAdapter
import org.swm.att.home.databinding.DialogMenuOptionBinding
import org.swm.att.home.home.HomeViewModel

class MenuOptionDialog(
    private val homeViewModel: HomeViewModel
): BaseDialog<DialogMenuOptionBinding>(R.layout.dialog_menu_option) {
    private lateinit var menuOptionAdapter: MenuOptionAdapter
    private val menuOptionViewModel by viewModels<MenuOptionViewModel>()
    private lateinit var menu: MenuWithRecipeVO

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnListener()
        initOptionsRecyclerView()
        setOptionAddBtnClickListener()
        setSelectedMenuInfoObserver()
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
                id = menu.menuId ?: -1,
                name = menu.menuName,
                price = menu.price.toInt() + optionsPrice,
                options = menuOptionViewModel.selectedOptionMap.value?.values?.toList()
                    ?.sortedBy { it.id } ?: emptyList(),
                detail = detail
            )
            homeViewModel.addSelectedMenu(selectedMenuWithOptions)
            dismiss()
        }
    }

    private fun setSelectedMenuInfoObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getMenuInfoState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {/* nothing */
                        }

                        is UiState.Success -> {
                            uiState.data?.let {
                                menu = it
                                menuOptionAdapter.submitList(it.option)
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT)
                                .show()
                            dismiss()
                        }
                    }
                }
            }
        }
    }

}
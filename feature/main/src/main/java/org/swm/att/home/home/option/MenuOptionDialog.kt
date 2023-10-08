package org.swm.att.home.home.option

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseDialog
import org.swm.att.common_ui.state.UiState
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.home.R
import org.swm.att.home.adapter.MenuOptionAdapter
import org.swm.att.home.databinding.DialogMenuOptionBinding
import org.swm.att.home.home.HomeViewModel

@AndroidEntryPoint
class MenuOptionDialog(
    private val homeViewModel: HomeViewModel,
    private val selectedMenuId: Int
): BaseDialog<DialogMenuOptionBinding>(R.layout.dialog_menu_option) {
    private lateinit var menuOptionAdapter: MenuOptionAdapter
    private val menuOptionViewModel by viewModels<MenuOptionViewModel>()
    private lateinit var menu: MenuWithRecipeVO

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSelectedMenuInfoObserver()
        setDialogCloseBtnListener()
        initOptionsRecyclerView()
        setOptionAddBtnClickListener()
        getSelectedMenuOption()
    }

    private fun setDialogCloseBtnListener() {
        binding.btnCloseMenuOptionPickerDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initOptionsRecyclerView() {
        menuOptionAdapter = MenuOptionAdapter(menuOptionViewModel)
        binding.rvMenuOption.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuOptionAdapter
        }
    }

    private fun setOptionAddBtnClickListener() {
        binding.btnMenuOptionAdd.setOnClickListener {
            val optionsPrice = menuOptionViewModel.selectedOptionMap.value?.values?.sumOf { it.price } ?: 0
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
                menuOptionViewModel.getMenuInfoState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Success -> {
                            uiState.data?.let { menuWithRecipe ->
                                menu = menuWithRecipe
                                if (menuWithRecipe.option.isNotEmpty()) {
                                    menuOptionAdapter.submitList(menuWithRecipe.option)
                                } else {
                                    homeViewModel.addSelectedMenu(
                                        OrderedMenuVO(
                                            id = menuWithRecipe.menuId ?: -1,
                                            name = menuWithRecipe.menuName,
                                            price = menuWithRecipe.price.toInt(),
                                            options = emptyList()
                                        )
                                    )
                                }
                            }
                        }
                        is UiState.Error -> {
                            dismiss()
                            Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun getSelectedMenuOption() {
        menuOptionViewModel.getMenuInfo(selectedMenuId)
    }
}
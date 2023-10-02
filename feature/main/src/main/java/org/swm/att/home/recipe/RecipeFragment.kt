package org.swm.att.home.recipe

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.base.BaseFragment
import org.swm.att.common_ui.util.state.UiState
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.home.R
import org.swm.att.home.adapter.BaseInteractiveItemAdapter
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.databinding.FragmentRecipeBinding

@AndroidEntryPoint
class RecipeFragment : BaseFragment<FragmentRecipeBinding>(R.layout.fragment_recipe) {
    private val recipeViewModel by viewModels<RecipeViewModel>()
    private lateinit var registeredMenusAdapter: SelectableItemAdapter
    private lateinit var recipesAdapter: BaseInteractiveItemAdapter
    private lateinit var optionsAdapter: BaseInteractiveItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenusRecyclerView()
        setCategoryDetailBtnClickListener()
        setRecipeBtnsClickListener()
        setObserver()
        setDataBinding()
        initData()
    }

    private fun initMenusRecyclerView() {
        registeredMenusAdapter = SelectableItemAdapter(recipeViewModel)
        binding.rvCategoryMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = registeredMenusAdapter
        }

        recipesAdapter = BaseInteractiveItemAdapter(recipeViewModel)
        binding.rvMenuRecipe.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesAdapter
        }

        optionsAdapter = BaseInteractiveItemAdapter(recipeViewModel)
        binding.rvMenuOptions.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = optionsAdapter
        }
    }

    private fun setCategoryDetailBtnClickListener() {
        binding.ibCategoryDetail.setOnClickListener { view ->
            showMenu(view, R.menu.item_category_detail)
        }
    }

    private fun setRecipeBtnsClickListener() {
        binding.btnModifyRecipe.setOnClickListener {
            recipeViewModel.changeModifyState()
        }
        binding.btnRegisterRecipe.setOnClickListener {
            recipeViewModel.changeModifyState()
        }
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popUp = PopupMenu(requireContext(), view)
        popUp.apply {
            menuInflater.inflate(menuRes, popUp.menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_category_modify -> {
                        /*todo*/
                        true
                    }
                    R.id.item_category_delete -> {
                        /*todo*/
                        true
                    }
                    else -> false
                }
            }
        }
        popUp.show()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.getCategories.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                setCategoryChips(it)
                            }
                        }
                        is UiState.Error -> Toast.makeText(
                            requireContext(),
                            uiState.errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()

                        is UiState.Loading -> { /* nothing */ }
                    }
                }
            }
        }
        recipeViewModel.currentSelectedMenuId.observe(viewLifecycleOwner) {
            val pastId = recipeViewModel.selectedMenuId.value
            binding.rvCategoryMenuList[it].setBackgroundResource(R.color.main_trans)
            recipeViewModel.changeSelectedState()
            pastId?.let { pastId ->
                if (pastId != it) {
                    recipeViewModel.selectedMenuId.value?.let { pastId ->
                        binding.rvCategoryMenuList[pastId].setBackgroundResource(R.color.white)
                    }
                }
            }
        }
        recipeViewModel.selectedCategory.observe(viewLifecycleOwner) {
            registeredMenusAdapter.submitList(it.menus)
            recipeViewModel.setSelectedMenuId(0)
            binding.edtCategoryName.setText(String.format("%s(%d건)", it.category, it.menus.size))
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.selectedMenuInfo.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                binding.menuWithRecipe = it
                                recipesAdapter.submitList(it.recipe)
                                optionsAdapter.submitList(it.option)
                            }
                        }

                        is UiState.Loading -> {/* nothing */
                        }

                        is UiState.Error -> Toast.makeText(
                            requireContext(),
                            uiState.errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    private fun setCategoryChips(categoriesVO: CategoriesVO) {
        categoriesVO.categories.let {
            // category chip 추가
            for (index in it.indices) {
                val type = it[index]
                val chip = layoutInflater.inflate(R.layout.item_category_chip, binding.cgMenuCategories, false) as Chip
                chip.apply {
                    tag = type
                    id = index
                    text = type.category
                    isChecked = if (index == 0) {
                        recipeViewModel.setSelectedCategory(type)
                        true
                    } else {
                        false
                    }
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            recipeViewModel.setSelectedCategory(type)
                        }
                    }
                }
                binding.cgMenuCategories.addView(chip)
            }
            addAddItemChip(it.size)
        }
    }

    private fun addAddItemChip(chipId: Int) {
        // addItemChip의 경우, 항상 추가
        val addItemChip = layoutInflater.inflate(
            R.layout.item_category_chip,
            binding.cgMenuCategories,
            false
        ) as Chip
        addItemChip.apply {
            id = chipId
            text = "+"
            setOnClickListener {
                // 새로운 카테고리 추가 api 연결
            }
        }
        binding.cgMenuCategories.addView(addItemChip)
    }

    private fun setDataBinding() {
        binding.recipeViewModel = recipeViewModel
    }

    private fun initData() {
        //임시로 1번 storeId로 설정
        recipeViewModel.getRegisteredMenus(1)
    }

}
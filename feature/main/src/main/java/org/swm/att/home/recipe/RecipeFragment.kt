package org.swm.att.home.recipe

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
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
        setBtnRegisterMenuClickListener()
        setBtnAddRecipeClickListener()
        setBtnRegisterRecipeClickListener()
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
        binding.tvCategoryModify.setOnClickListener { view ->
//            showMenu(view, R.menu.item_category_detail)
            /* todo */
            Toast.makeText(requireContext(), "서비스 준비 중입니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRecipeBtnsClickListener() {
        binding.btnModifyRecipe.setOnClickListener {
//            recipeViewModel.changeModifyState()
            /* todo */
            Toast.makeText(requireContext(), "서비스 준비 중입니다!", Toast.LENGTH_SHORT).show()
        }

        binding.btnRegisterRecipe.setOnClickListener {
//            recipeViewModel.changeModifyState()
            /* todo */
            Toast.makeText(requireContext(), "서비스 준비 중입니다!", Toast.LENGTH_SHORT).show()
        }

        binding.btnDeleteRecipe.setOnClickListener {
            /* todo */
            Toast.makeText(requireContext(), "서비스 준비 중입니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBtnRegisterMenuClickListener() {
        binding.btnRegisterMenu.setOnClickListener {
            recipeViewModel.setCurrentSelectedItemId(-1)
            recipeViewModel.changeCreateState(true)
            recipeViewModel.getAllOfOption()
            binding.menuWithRecipe = null
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

    private fun setBtnAddRecipeClickListener() {
        binding.btnAddRecipe.setOnClickListener {
            recipeViewModel.addTempNewRecipe()
        }
    }

    private fun setBtnRegisterRecipeClickListener() {
        binding.btnRegisterRecipe.setOnClickListener {
            val name = binding.edtMenuName.text.toString()
            val price = binding.edtMenuPrice.text.toString()
            if (name.isNullOrEmpty() || price.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "메뉴 이름과 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                recipeViewModel.postNewMenu(
                    binding.edtMenuName.text.toString(),
                    binding.edtMenuPrice.text.toString()
                )
            }
        }
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

                        is UiState.Loading -> { /* nothing */
                        }
                    }
                }
            }
        }
        recipeViewModel.currentSelectedMenuId.observe(viewLifecycleOwner) {
            if (it != -1) {
                registeredMenusAdapter.notifyItemChanged(it)
            }
        }
        recipeViewModel.selectedMenuId.observe(viewLifecycleOwner) {
            if (it != -1) {
                registeredMenusAdapter.notifyItemChanged(it)
            }
        }
        recipeViewModel.selectedCategory.observe(viewLifecycleOwner) {
            it?.let {
                if (it.menus.isEmpty()) {
                    binding.menuWithRecipe = null
                }
                registeredMenusAdapter.submitList(it.menus)
                recipeViewModel.setDefaultSelectedItem()
                binding.edtCategoryName.setText(
                    String.format(
                        "%s(%d건)",
                        it.category,
                        it.menus.size
                    )
                )
            }
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.postMenuState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "메뉴가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                recipeViewModel.changeCreateState(false)
                                recipeViewModel.clearSelectedOption()
                                initData()
                            }
                        }
                        is UiState.Loading -> { /* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.postCategoryState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(
                                    requireContext(),
                                    "카테고리가 추가되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                initData()
                            }
                        }

                        is UiState.Loading -> { /* nothing */
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
        recipeViewModel.recipeListForNewMenu.observe(viewLifecycleOwner) {
            recipesAdapter.submitList(it)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.getAllOfOptionState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.option?.let {
                                if (it.isNotEmpty()) {
                                    optionsAdapter.submitList(it)
                                }
                                recipeViewModel.setGetAllOfOptionStateDefault()
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setCategoryChips(categoriesVO: CategoriesVO) {
        binding.cgMenuCategories.removeAllViews()
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
                        if (isChecked && recipeViewModel.selectedCategory.value != type) {
                            recipeViewModel.setSelectedCategory(type)
                            recipeViewModel.changeCreateState(false)
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
                val addCategoryDialog = AddCategoryDialog(recipeViewModel)
                addCategoryDialog.show(childFragmentManager, "addCategoryDialog")
            }
        }
        binding.cgMenuCategories.addView(addItemChip)
    }

    private fun setDataBinding() {
        binding.recipeViewModel = recipeViewModel
    }

    private fun initData() {
        recipeViewModel.getRegisteredMenus()
    }
}
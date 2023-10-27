package org.swm.att.home.recipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import org.swm.att.common_ui.util.Formatter
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.home.R
import org.swm.att.home.adapter.BaseInteractiveItemAdapter
import org.swm.att.home.adapter.CustomArrayAdapter
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.databinding.FragmentRecipeBinding
import org.swm.att.home.stock.StockFragment

@AndroidEntryPoint
class RecipeFragment : BaseFragment<FragmentRecipeBinding>(R.layout.fragment_recipe) {
    private val recipeViewModel by viewModels<RecipeViewModel>()
    private lateinit var registeredMenusAdapter: SelectableItemAdapter
    private lateinit var recipesAdapter: BaseInteractiveItemAdapter
    private lateinit var optionsAdapter: BaseInteractiveItemAdapter
    private lateinit var stockArrayAdapter: CustomArrayAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenusRecyclerView()
        initSearchView()
        setCategoryDetailBtnClickListener()
        setRecipeBtnsClickListener()
        setBtnRegisterMenuClickListener()
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
            itemAnimator = null
        }

        optionsAdapter = BaseInteractiveItemAdapter(recipeViewModel)
        binding.rvMenuOptions.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = optionsAdapter
            itemAnimator = null
        }
    }

    private fun initSearchView() {
        stockArrayAdapter = CustomArrayAdapter(
            requireContext(),
            R.layout.item_simple_text,
            listOf(),
            onExistedItemClickListener = { stock ->
                recipeViewModel.addNewRecipe(stock)
                binding.actSearchStock.setText("")
            },
            onAddNewItemClickListener = {
                val newItemName = binding.actSearchStock.text.toString()
                recipeViewModel.postNewStock(newItemName)
                binding.actSearchStock.isEnabled = false
            }
        )
        binding.actSearchStock.apply {
            setAdapter(stockArrayAdapter)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {/* nothing */}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {/* nothing */}

                override fun afterTextChanged(p0: Editable?) {
                    p0?.let {
                        if (!it.isNullOrEmpty()) {
                            recipeViewModel.getAllOfStocks(it.toString())
                        }
                    }
                }
            })
        }
    }

    private fun setCategoryDetailBtnClickListener() {
        binding.tvCategoryModify.setOnClickListener { view ->
            showMenu(view, R.menu.item_category_detail)
        }
    }

    private fun setRecipeBtnsClickListener() {
        binding.btnModifyRecipe.setOnClickListener {
            recipeViewModel.changeModifyState(true)
        }

        binding.btnDeleteRecipe.setOnClickListener {
            val dialog = MenuDeleteConfirmDialog(recipeViewModel)
            dialog.show(childFragmentManager, "MenuDeleteConfirmDialog")
        }
        binding.btnCancel.setOnClickListener {
            when(recipeViewModel.getEditState()) {
                CREATE -> {
                    recipeViewModel.changeCreateState(false)
                    recipeViewModel.getLastSelectedMenu()
                }
                MODIFY -> {
                    recipeViewModel.changeModifyState(false)
                    recipeViewModel.getLastSelectedMenu()
                }
            }
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

    private fun setBtnRegisterRecipeClickListener() {
        binding.btnRegisterRecipe.setOnClickListener {
            val name = binding.edtMenuName.text.toString()
            val price = Formatter.getBaseStringFromCurrencyUnit(binding.edtMenuPrice.text.toString())
            if (name.isNullOrEmpty() || price.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "메뉴 이름과 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                when(recipeViewModel.getEditState()) {
                    CREATE -> {
                        recipeViewModel.postNewMenu(name, price)
                    }
                    MODIFY -> {
                        recipeViewModel.updateMenu(name, price)
                    }
                }
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
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                        is UiState.Loading -> { /* nothing */ }
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
                    String.format("%s(%d건)", it.category, it.menus.size)
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
                                if (recipeViewModel.checkLastSelectedMenu(it.id)) {
                                    recipesAdapter.notifyDataSetChanged()
                                    optionsAdapter.notifyDataSetChanged()
                                }
                                changeEditState()
                            }
                        }
                        is UiState.Loading -> {/* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(requireContext(), "카테고리가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                initData()
                            }
                        }
                        is UiState.Loading -> { /* nothing */ }
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        recipeViewModel.recipeMapForNewMenu.observe(viewLifecycleOwner) {
            recipesAdapter.submitList(it.values.toList())
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.getAllOfStockState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                stockArrayAdapter.clear()
                                if (it.stocks.isNotEmpty()) {
                                    stockArrayAdapter.addAll(it.stocks)
                                } else {
                                    stockArrayAdapter.addCreateNewItem()
                                }
                                stockArrayAdapter.notifyDataSetChanged()
                                recipeViewModel.setGetAllOfStockStateDefault()
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.postNewStockState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "재고가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                val newStock = StockWithMixedVO(
                                    id = it.stockId,
                                    name = binding.actSearchStock.text.toString(),
                                    isMixed = false
                                )
                                binding.actSearchStock.apply {
                                    isEnabled = true
                                    setText("")
                                }
                                recipeViewModel.addNewRecipe(newStock)
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.deleteMenuState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(requireContext(), "메뉴 삭제 완료", Toast.LENGTH_SHORT).show()
                            initData()
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.updateMenuState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "메뉴 수정 완료", Toast.LENGTH_SHORT).show()
                                recipeViewModel.changeModifyState(false)
                                recipeViewModel.resetUpdateStockState()
                                initData()
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

    private fun changeEditState() {
        when(recipeViewModel.getEditState()) {
            StockFragment.CREATE -> recipeViewModel.changeCreateState(false)
            StockFragment.MODIFY -> recipeViewModel.changeModifyState(false)
        }
    }

    companion object {
        //state
        const val CREATE = "create"
        const val MODIFY = "modify"
        const val NONE = "none"
    }
}
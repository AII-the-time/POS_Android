package org.swm.att.home.recipe

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.HistoryVO
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.home.R
import org.swm.att.home.databinding.FragmentRecipeBinding
import org.swm.att.home.adapter.BaseInteractiveItemAdapter
import org.swm.att.home.adapter.CustomArrayAdapter
import org.swm.att.home.adapter.SelectableItemAdapter
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
        initMenuCostLineChart()
        initSearchView()
        setCategoryDetailBtnClickListener()
        setRecipeBtnsClickListener()
        setBtnRegisterMenuClickListener()
        setBtnRegisterRecipeClickListener()
        setBtnUpdateCategoryClickListener()
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

    @SuppressLint("ResourceAsColor")
    private fun initMenuCostLineChart() {
        with(binding.menuCostLineChart) {
            setGridBackgroundColor(Color.parseColor("#F5F5F5"))
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false

            val xAxis: XAxis = this.getXAxis()
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM // x축 데이터 표시 위치
            xAxis.granularity = 1f
            xAxis.textSize = 14f
            xAxis.textColor = Color.rgb(118, 118, 118)
            xAxis.spaceMin = 0.1f
            xAxis.spaceMax = 0.1f

            val yAxisLeft: YAxis = this.getAxisLeft()
            yAxisLeft.textSize = 14f
            yAxisLeft.textColor = Color.rgb(163, 163, 163)
            yAxisLeft.setDrawAxisLine(false)
            yAxisLeft.axisLineWidth = 2f
            yAxisLeft.axisMinimum = 0f

            val yAxis: YAxis = this.getAxisRight()
            yAxis.setDrawLabels(false)
            yAxis.textColor = Color.rgb(163, 163, 163)
            yAxis.setDrawAxisLine(false)
            yAxis.axisLineWidth = 2f
            yAxis.axisMinimum = 0f

            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = Legend.LegendForm.LINE
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
                        recipeViewModel.changeCategoryModifyState(true)
                        true
                    }
                    R.id.item_category_delete -> {
                        val dialog = CategoryDeleteConfirmDialog(recipeViewModel)
                        dialog.show(childFragmentManager, "CategoryDeleteConfirmDialog")
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
                val recipeList = getRecipeValues()
                when(recipeViewModel.getEditState()) {
                    CREATE -> {
                        recipeViewModel.postNewMenu(name, price, recipeList)
                    }
                    MODIFY -> {
                        recipeViewModel.updateMenu(name, price, recipeList)
                    }
                }
            }
        }
    }

    private fun getRecipeValues(): MutableList<RecipeVO> {
        val recipeList = mutableListOf<RecipeVO>()
        for (index in 0 until recipesAdapter.itemCount) {
            val holder = binding.rvMenuRecipe.findViewHolderForAdapterPosition(index) as MenuRecipeViewHolder
            val recipe = holder.getRecipe()
            recipeList.add(recipe)
        }
        return recipeList
    }

    private fun setBtnUpdateCategoryClickListener() {
        binding.btnCategoryUpdate.setOnClickListener {
            val categoryName = binding.edtCategoryName.text.toString()
            if (categoryName.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "카테고리 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                recipeViewModel.updateCategory(categoryName)
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
                binding.edtCategoryName.setText(String.format("%s", it.category))
                binding.tvMenuSize.text = String.format("(%s건)", it.menus.size)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.selectedMenuInfo.collect { uiState ->
                    when (uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                binding.menuWithRecipe = it
                                if (recipeViewModel.checkLastSelectedMenu(it.id)) {
                                    recipesAdapter.notifyDataSetChanged()
                                    optionsAdapter.notifyDataSetChanged()
                                }
                                changeEditState()
                                initMenuCostGraph(it.history)
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

        recipeViewModel.optionList.observe(viewLifecycleOwner) {
            optionsAdapter.submitList(it)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeViewModel.deleteCategoryState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "카테고리 삭제 완료", Toast.LENGTH_SHORT).show()
                                initData()
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
                recipeViewModel.updateCategoryState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                recipeViewModel.changeCategoryModifyState(false)
                                Toast.makeText(requireContext(), "카테고리 수정 완료", Toast.LENGTH_SHORT).show()
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

    private fun initMenuCostGraph(history: List<HistoryVO>) {
        // entries 구하기
        val entries = ArrayList<Entry>()
        history.map { reportGraphItemVO ->
            entries.add(Entry(reportGraphItemVO.date.toFloat(), reportGraphItemVO.price.toFloat()))
        }

        // lineDataSet 설정
        val lineDataSet = LineDataSet(entries, null)
        lineDataSet.apply {
            valueTextSize = 15f
            mode = LineDataSet.Mode.CUBIC_BEZIER
//            color = Color.parseColor(reportGraphData.graphColor)
//            setCircleColor(Color.parseColor(reportGraphData.graphColor))
            setDrawCircleHole(true)
            circleRadius = 5f
            setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            valueTextColor = Color.BLACK
        }

        // lineData 설정
        val dataSet: List<LineDataSet> = arrayListOf(lineDataSet, lineDataSet)
        val lineData = LineData(dataSet)
        binding.menuCostLineChart.apply {
            setDrawGridBackground(true)
            data = lineData
        }

        binding.menuCostLineChart.invalidate()
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
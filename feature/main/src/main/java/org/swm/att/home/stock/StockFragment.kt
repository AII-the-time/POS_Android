package org.swm.att.home.stock

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter.getDateBaseFormattingResult
import org.swm.att.common_ui.util.Formatter.getDateFromString
import org.swm.att.common_ui.util.Formatter.getSimpleStringFromDate
import org.swm.att.common_ui.util.getValueOrNull
import org.swm.att.domain.entity.response.HistoryVO
import org.swm.att.home.R
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.databinding.FragmentStockBinding
import java.util.Date

@AndroidEntryPoint
class StockFragment : BaseFragment<FragmentStockBinding>(R.layout.fragment_stock) {
    private val stockViewModel by viewModels<StockViewModel>()
    private lateinit var stockWithStateAdapter: SelectableItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStockCategoryChipGroup()
        initStocksRecyclerView()
        initStockCostLineChart()
        initSearchView()
        initUnitMenu()
        setAddStockBtnClickListener()
        setStockBtnsClickListener()
        setInventoryTextClickListener()
        setObserver()
        initStockData()
        setDataBinding()
    }

    private fun initStockCategoryChipGroup() {
        binding.cgStockCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedChipId = checkedIds.firstOrNull()
            checkedChipId?.let {
                val chip = group.findViewById<Chip>(it)
                binding.tvCategoryName.text = chip.text
            }
            when(checkedChipId) {
                R.id.chip_stock_all -> {
                    stockViewModel.setDefaultStockList(ALL)
                }
                R.id.chip_stock_caution -> {
                    stockViewModel.setDefaultStockList(LACK)
                }
                R.id.chip_stock_unknown -> {
                    stockViewModel.setDefaultStockList(UNKNOWN)
                }
            }
            resetSearchQuery()
        }
        binding.cgStockCategories.check(R.id.chip_stock_all)
    }

    private fun resetSearchQuery() {
        binding.actSearchStock.text = null
    }

    private fun initStocksRecyclerView() {
        stockWithStateAdapter = SelectableItemAdapter(stockViewModel)
        binding.rvCategoryStockList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stockWithStateAdapter
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initStockCostLineChart() {
        with(binding.stockCostLineChart) {
            setGridBackgroundColor(Color.parseColor("#FFFFFF"))
            animateX(1200, com.github.mikephil.charting.animation.Easing.EaseInSine)
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
            xAxis.axisMinimum = 0f

            val yAxisLeft: YAxis = this.getAxisLeft()
            yAxisLeft.textSize = 14f
            yAxisLeft.textColor = Color.rgb(163, 163, 163)
            yAxisLeft.setDrawAxisLine(false)
            yAxisLeft.axisLineWidth = 2f

            val yAxis: YAxis = this.getAxisRight()
            yAxis.axisLineWidth = 2f
            yAxis.setDrawLabels(false)
            yAxis.setDrawAxisLine(false)

            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = com.github.mikephil.charting.components.Legend.LegendForm.LINE
        }
    }


    private fun initSearchView() {
        binding.actSearchStock.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {/* nothing */}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {/* nothing */}

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    stockViewModel.stockSearchResult(it.toString())
                }
            }
        })
    }

    private fun initUnitMenu() {
        val unitArray = binding.root.context.resources.getStringArray(org.swm.att.common_ui.R.array.recipe_unit)
        val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.item_simple_text, unitArray)
        binding.actStockUnit.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, i, _ ->
                stockViewModel.setUnitString(unitArray[i])
            }
        }
    }

    private fun setAddStockBtnClickListener() {
        binding.btnAddStock.setOnClickListener {
            binding.stockDetail = null
            binding.actStockUnit.setText("g", false)
            binding.tvInventoryDate.setText(getDateBaseFormattingResult(Date()))
            binding.ivStockStateIcon.visibility = View.GONE
            stockViewModel.apply {
                setLastInventoryDate(Date())
                changeCreateState(true)
                setUnitString("g")
            }
        }
    }

    private fun setStockBtnsClickListener() {
        binding.btnCancelRegisterStock.setOnClickListener {
            when(stockViewModel.getEditState()) {
                CREATE -> {
                    stockViewModel.changeCreateState(false)
                    stockViewModel.getLastSelectedStock()
                }
                MODIFY -> {
                    stockViewModel.changeModifyState(false)
                    stockViewModel.getLastSelectedStock()
                }
            }
        }
        binding.btnRegisterNewStock.setOnClickListener {
            val name = binding.edtStockName.text.toString().getValueOrNull()
            val currentAmount = binding.edtStockAmount.text.toString().getValueOrNull()
            val noticeThreshold = binding.edtStockNotificationReferenceAmount.text.toString().getValueOrNull()
            val perAmount = binding.edtStockPerAmount.text.toString().getValueOrNull()
            val perPrice = binding.edtStockPerPrice.text.toString().getValueOrNull()
            val unit = binding.actStockUnit.text.toString().getValueOrNull()
            // TODO: 리팩토링 방법 생각하기
            if (name == null) {
                Toast.makeText(requireContext(), "재고 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                if ((currentAmount == null && noticeThreshold == null && perAmount == null && perPrice.isNullOrEmpty())
                        || (currentAmount != null && noticeThreshold != null && perAmount != null && perPrice != null)) {
                    when(stockViewModel.getEditState()) {
                        CREATE -> {
                            stockViewModel.postNewStock(name, currentAmount, noticeThreshold, perAmount, perPrice, unit)
                        }
                        MODIFY -> {
                            stockViewModel.updateStock(name, currentAmount, noticeThreshold, perAmount, perPrice, unit)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "필요한 값들을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnModifyStock.setOnClickListener {
            stockViewModel.changeModifyState(true)
        }
        binding.btnDeleteStock.setOnClickListener {
            val dialog = DialogStockDeleteConfirm(stockViewModel)
            dialog.show(childFragmentManager, "DialogStockDeleteConfirm")
        }
    }

    private fun setInventoryTextClickListener() {
        binding.tvInventoryDate.setOnClickListener {
            val datePickerDialog = DialogInventoryDatePicker(stockViewModel)
            datePickerDialog.show(childFragmentManager, "DialogInventoryDatePicker")
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stockViewModel.getStockWithStateListState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {/* nothing */}
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stockViewModel.getStockByIdState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                binding.stockDetail = it
                                binding.unitString = it.unit
                                binding.actStockUnit.setText(it.unit, false)
                                changeEditState()
                                stockViewModel.resetGetSelectedStockByIdState()
                                setStockCostLineChart(it.history)
                            }
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        stockViewModel.currentResultStockList.observe(viewLifecycleOwner) {
            stockWithStateAdapter.submitList(it)
            stockWithStateAdapter.notifyDataSetChanged()
        }

        stockViewModel.lastInventoryDate.observe(viewLifecycleOwner) {
            binding.tvInventoryDate.setText(getDateBaseFormattingResult(it))
        }

        stockViewModel.unitString.observe(viewLifecycleOwner) {
            binding.actStockUnit.setText(it, false)
            binding.unitString = it
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stockViewModel.postNewStockState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "재고 등록 완료", Toast.LENGTH_SHORT).show()
                                stockViewModel.changeCreateState(false)
                                initStockData(it.stockId)
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
                stockViewModel.updateStockState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            uiState.data?.let {
                                Toast.makeText(requireContext(), "재고 수정 완료", Toast.LENGTH_SHORT).show()
                                stockViewModel.changeModifyState(false)
                                initStockData(it.stockId)
                                stockViewModel.resetUpdateStockState()
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
                stockViewModel.deleteStockState.collect { uiState ->
                    when(uiState) {
                        is UiState.Success -> {
                            Toast.makeText(requireContext(), "재고 삭제 완료", Toast.LENGTH_SHORT).show()
                            initStockData()
                            binding.stockDetail = null
                        }
                        is UiState.Loading -> {/* nothing */}
                        is UiState.Error -> Toast.makeText(requireContext(), uiState.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setStockCostLineChart(history: List<HistoryVO>?) {
        history?.let {
            // entries 구하기
            val entries = ArrayList<Entry>()
            val labels = ArrayList<String>()
            history.withIndex().map { (index, history) ->
                entries.add(Entry(index.toFloat(), (history.price.toInt() / history.amount).toFloat()))
                labels.add(getSimpleStringFromDate(getDateFromString(history.date)))
            }

            // lineDataSet 설정
            val lineDataSet = LineDataSet(entries, null)
            lineDataSet.apply {
                valueTextSize = 15f
                mode = LineDataSet.Mode.CUBIC_BEZIER
                color = Color.parseColor("#2B1E12")
                setCircleColor(Color.parseColor("#2B1E12"))
                setDrawCircleHole(true)
                circleRadius = 5f
                setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
                valueTextColor = Color.BLACK
            }

            // lineData 설정
            val dataSet: List<LineDataSet> = arrayListOf(lineDataSet, lineDataSet)
            val lineData = LineData(dataSet)
            val xAxis = binding.stockCostLineChart.xAxis
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                labelCount = 5
            }
            binding.stockCostLineChart.apply {
                setDrawGridBackground(true)
                data = lineData
            }

            binding.stockCostLineChart.invalidate()
        }
    }

    private fun changeEditState() {
        when(stockViewModel.getEditState()) {
            CREATE -> stockViewModel.changeCreateState(false)
            MODIFY -> stockViewModel.changeModifyState(false)
        }
    }

    private fun initStockData(selectedId: Int? = null) {
        stockViewModel.getStockWithStateList(selectedId)
    }

    private fun setDataBinding() {
        binding.stockViewModel = stockViewModel
    }

    companion object {
        //stock
        const val ALL = "all"
        const val LACK = "lack"
        const val UNKNOWN = "unknown"
        //state
        const val CREATE = "create"
        const val MODIFY = "modify"
    }

}
package org.swm.att.home.main.stock

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
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.swm.att.common_ui.presenter.base.BaseFragment
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter.getDateBaseFormattingResult
import org.swm.att.common_ui.util.getValueOrNull
import org.swm.att.home.R
import org.swm.att.home.main.adapter.BaseRecyclerViewAdapter
import org.swm.att.home.main.adapter.SelectableItemAdapter
import org.swm.att.home.databinding.FragmentStockBinding
import java.util.Date

@AndroidEntryPoint
class StockFragment : BaseFragment<FragmentStockBinding>(R.layout.fragment_stock) {
    private val stockViewModel by viewModels<StockViewModel>()
    private lateinit var stockWithStateAdapter: SelectableItemAdapter
    private lateinit var usingMenuAdapter: BaseRecyclerViewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStockCategoryChipGroup()
        initStocksRecyclerView()
        initUsingMenuRecyclerView()
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

    private fun initUsingMenuRecyclerView() {
        usingMenuAdapter = BaseRecyclerViewAdapter()
        binding.rvUsingMenuList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usingMenuAdapter
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
                                binding.actStockUnit.setText(it.unit, false)
                                changeEditState()
                                stockViewModel.resetGetSelectedStockByIdState()
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
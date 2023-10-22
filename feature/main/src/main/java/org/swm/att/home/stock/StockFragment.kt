package org.swm.att.home.stock

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
import org.swm.att.home.R
import org.swm.att.home.adapter.BaseRecyclerViewAdapter
import org.swm.att.home.adapter.SelectableItemAdapter
import org.swm.att.home.databinding.FragmentStockBinding

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
        }
    }

    private fun initStockData() {
        stockViewModel.getStockWithStateList()
    }

    private fun setDataBinding() {
        binding.stockViewModel = stockViewModel
    }

    companion object {
        const val ALL = "all"
        const val LACK = "lack"
        const val UNKNOWN = "unknown"
    }

}
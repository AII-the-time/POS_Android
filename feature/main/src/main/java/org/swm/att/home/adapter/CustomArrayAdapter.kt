package org.swm.att.home.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.home.R
import java.util.UUID

class CustomArrayAdapter(
    private val context: Context,
    @LayoutRes private val resourceId: Int,
    private val stockList: List<StockVO>,
    private val onExistedItemClickListener: (StockVO) -> Unit,
    private val onAddNewItemClickListener: () -> Unit
): ArrayAdapter<StockVO>(context, resourceId, stockList) {
    private val stocks = ArrayList(stockList)
    override fun getCount(): Int {
        return stocks.size
    }

    override fun getItem(position: Int): StockVO? {
        return stocks[position]
    }

    override fun getItemId(position: Int): Long {
        return stocks[position].id.toLong()
    }

    fun addAll(stocks: List<StockVO>) {
        this.stocks.addAll(stocks)
    }
    override fun clear() {
        stocks.clear()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = convertView ?: View.inflate(context, resourceId, null)
        val searchView = convertView.findViewById<AppCompatTextView>(R.id.tv_simple_text)
        val stock = stocks[position]
        convertView.setOnClickListener {
            if (stock.isNew == true) {
                onAddNewItemClickListener()
            } else {
                onExistedItemClickListener(stock)
            }
        }
        try {
            searchView.text = stock.name
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return convertView
        }
    }

    fun addCreateNewItem() {
        stocks.add(StockVO(UUID.randomUUID().hashCode(), context.getString(org.swm.att.common_ui.R.string.tv_item_add_new), isMixed = false, isNew = true))
    }
}
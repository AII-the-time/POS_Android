package org.swm.att.home.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.home.R

class CustomArrayAdapter(
    private val context: Context,
    @LayoutRes private val resourceId: Int,
    private val stockList: List<StockVO>,
    private val onItemClickListener: (StockVO) -> Unit
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
        convertView.setOnClickListener{ onItemClickListener(stocks[position]) }
        try {
            val stock = stocks[position]
            convertView.findViewById<AppCompatTextView>(R.id.tv_simple_text).text = stock.name
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return convertView
        }
    }
}
package org.swm.att.common_ui.viewholder

import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import org.swm.att.common_ui.databinding.ItemSduiPiechartBinding
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent
import org.swm.att.domain.sever_driven_ui.response.piechart.ReportPieChartVO


class SduiPieChartViewHolder(
    private val binding: ItemSduiPiechartBinding
): BaseSduiViewHolder(binding) {
    override fun bind(reportContent: SduiReportContent) {
        setPieChart(reportContent as ReportPieChartVO)
    }

    private fun setPieChart(reportPieChartData: ReportPieChartVO) {
        binding.chartTitle = reportPieChartData.pieChartTitle
        val dataList = mutableListOf<PieEntry>()
        val colorList = mutableListOf<Int>()
        for (item in (reportPieChartData).pieChartItems) {
            dataList.add(PieEntry(item.categoryCount.toFloat(), item.categoryName))
            colorList.add(Color.parseColor(item.chartColor))
        }
        val dataSet = PieDataSet(dataList, null)
        dataSet.colors = colorList
        val pieData = PieData(dataSet)

        binding.reportPieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 60f
            setEntryLabelTextSize(10f)
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }
}
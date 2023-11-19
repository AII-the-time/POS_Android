package org.swm.att.common_ui.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.swm.att.common_ui.databinding.ItemSduiGraphBinding
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent
import org.swm.att.domain.sever_driven_ui.response.graph.ReportGraphVO


class SduiGraphViewHolder(
    private val binding: ItemSduiGraphBinding
): BaseSduiViewHolder(binding) {
    override fun bind(reportContent: SduiReportContent) {
        initGraph()
        setGraph(reportContent as ReportGraphVO)
    }

    @SuppressLint("ResourceAsColor")
    private fun initGraph() {
        with(binding.reportLineChart) {
            setGridBackgroundColor(Color.parseColor("#F5F5F5"))
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false

            val xAxis: XAxis = binding.reportLineChart.getXAxis()
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM // x축 데이터 표시 위치
            xAxis.granularity = 1f
            xAxis.textSize = 14f
            xAxis.textColor = Color.rgb(118, 118, 118)
            xAxis.spaceMin = 0.1f
            xAxis.spaceMax = 0.1f

            val yAxisLeft: YAxis = binding.reportLineChart.getAxisLeft()
            yAxisLeft.textSize = 14f
            yAxisLeft.textColor = Color.rgb(163, 163, 163)
            yAxisLeft.setDrawAxisLine(false)
            yAxisLeft.axisLineWidth = 2f
            yAxisLeft.axisMinimum = 0f

            val yAxis: YAxis = binding.reportLineChart.getAxisRight()
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

    private fun setGraph(reportGraphData: ReportGraphVO) {
        binding.chartTitle = reportGraphData.graphTitle

        // entries 구하기
        val entries = ArrayList<Entry>()
        reportGraphData.graphItems.map { reportGraphItemVO ->
            entries.add(Entry(reportGraphItemVO.graphKey.toFloat(), reportGraphItemVO.graphValue.toFloat()))
        }

        // lineDataSet 설정
        val lineDataSet = LineDataSet(entries, null)
        lineDataSet.apply {
            valueTextSize = 15f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = Color.parseColor(reportGraphData.graphColor)
            setCircleColor(Color.parseColor(reportGraphData.graphColor))
            setDrawCircleHole(true)
            circleRadius = 5f
            setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            valueTextColor = Color.BLACK
        }

        // lineData 설정
        val dataSet: List<LineDataSet> = arrayListOf(lineDataSet, lineDataSet)
        val lineData = LineData(dataSet)
        binding.reportLineChart.apply {
            setDrawGridBackground(true)
            data = lineData
        }

        binding.reportLineChart.invalidate()
    }
}
package com.wojtekmalek.expenseslog.ui.charts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper


class LineChartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chart = BarChart(this)
        setContentView(chart)

        var entryIndex = 0f
        val dataSet = RealmHelper.getCategories().mapIndexed { index, category ->
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            val colorForCategory = RealmHelper.getColorForCategory(this@LineChartActivity, category)

            if (categoryExpenses > 0) {
                val barEntries = listOf(BarEntry(entryIndex++, categoryExpenses))
                BarDataSet(barEntries, category.name).apply {
                    color = colorForCategory
                }
            } else {
                null
            }
        }.filterNotNull()


        val data = BarData(dataSet)
        chart.data = data

        chart.axisLeft.apply {
            val limit = RealmHelper.getLimit().toFloat()
            addLimitLine(LimitLine(limit, "Limit - $limit zł"))
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }
//        chart.xAxis.isEnabled = false

        chart.animateY(1000)
        chart.description = Description().apply { text = "" }
        chart.extraRightOffset = 30f

        chart.invalidate()
    }


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(
                    context,
                    LineChartActivity::class.java
            ))
        }
    }
}

package com.wojtekmalek.expenseslog.ui.charts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper


class LineChartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chart = HorizontalBarChart(this)
        setContentView(chart)

        val dataSet = RealmHelper.getCategories().mapIndexed { index, category ->
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            val colorForCategory = RealmHelper.getColorForCategory(this@LineChartActivity, category)

            val barEntries = listOf(BarEntry(categoryExpenses, index.toFloat()))
            BarDataSet(barEntries, category.name).apply {
                color = colorForCategory
            }
        }

        val data = BarData(dataSet)
        chart.data = data
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

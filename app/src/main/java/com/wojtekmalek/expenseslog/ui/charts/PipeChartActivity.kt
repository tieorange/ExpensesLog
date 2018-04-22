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


class PipeChartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chart = HorizontalBarChart(this)
        setContentView(chart)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 0f))
        entries.add(BarEntry(2f, 1f))
        entries.add(BarEntry(3f, 2f))
        entries.add(BarEntry(4f, 3f))
        entries.add(BarEntry(5f, 4f))
        entries.add(BarEntry(6f, 5f))


        val dataSet = RealmHelper.getCategories().mapIndexed { index, category ->
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            val barEntries = listOf<BarEntry>(BarEntry(categoryExpenses, index.toFloat()))
            BarDataSet(barEntries, category.name).apply {
                color = RealmHelper.getColorForCategory(category)
            }
        }

        val data = BarData(dataSet)
        chart.data = data
    }


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(
                    context,
                    PipeChartActivity::class.java
            ))
        }
    }
}

package com.wojtekmalek.expenseslog.ui.charts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper


class PieChartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chart = PieChart(this)
        setContentView(chart)

        val entries = RealmHelper.getCategories().mapIndexed { index, category ->
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            PieEntry(categoryExpenses, category.name).apply { y = index.toFloat() }
        }

        val dataSet = PieDataSet(entries, "").apply { colors = RealmHelper.getAllColors(this@PieChartActivity) }

        val data = PieData(dataSet)
        chart.data = data
        chart.invalidate()
    }


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(
                    context,
                    PieChartActivity::class.java
            ))
        }
    }
}

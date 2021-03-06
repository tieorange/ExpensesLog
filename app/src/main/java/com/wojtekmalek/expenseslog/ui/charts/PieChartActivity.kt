package com.wojtekmalek.expenseslog.ui.charts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.intrusoft.scatter.ChartData
import com.mcxiaoke.koi.log.logd
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.pie_chart.*


class PieChartActivity : Activity() {

    private val whiteColor by lazy {
        ContextCompat.getColor(this, R.color.material_light_white)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pie_chart)

        initPieChart()
//        initSimpleChart()
    }

    private fun initPieChart() {
        val entries = RealmHelper.getCategories().mapIndexed { index, category ->
            logd("PieChart", category.toString())
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            if (categoryExpenses > 0) {
                ChartData(
                        "${category.name} $categoryExpenses zł",
                        categoryExpenses,
                        whiteColor,
                        RealmHelper.getColorForCategory(this@PieChartActivity, category)
                )
            } else {
                null
            }
        }.filterNotNull()

        if (entries.isNotEmpty()) {
            pieChart.setChartData(entries)
        }
    }

    private fun initSimpleChart() {
        val entries = RealmHelper.getCategories().mapIndexed { index, category ->
            logd("PieChart", category.toString())
            val categoryExpenses = RealmHelper.getExpensesAmountByCategory(category)
            if (categoryExpenses > 0) {
                ChartData(
                        "${category.name} $categoryExpenses zł",
                        categoryExpenses,
                        whiteColor,
                        RealmHelper.getColorForCategory(this@PieChartActivity, category)
                )
            } else {
                null
            }
        }.filterNotNull()

        if (entries.isNotEmpty()) {
            pieChart.setChartData(entries)
        }
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

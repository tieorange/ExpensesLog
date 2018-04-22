package com.wojtekmalek.expenseslog.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.AddExpenseItemActivity
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import com.wojtekmalek.expenseslog.ui.charts.LineChartActivity
import com.wojtekmalek.expenseslog.ui.charts.PieChartActivity
import com.wojtekmalek.expenseslog.ui.history.HistoryActivity
import com.wojtekmalek.expenseslog.ui.setLimit.LimitActivity
import com.wojtekmalek.expenseslog.util.ItemClickSupport
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        drawer {
            divider {}
            primaryItem("History") { }.withOnDrawerItemClickListener { view, position, drawerItem ->
                HistoryActivity.startActivity(this@HomeActivity)
                true
            }
            primaryItem("Limit") {}.withOnDrawerItemClickListener { view, position, drawerItem ->
                LimitActivity.startActivity(this@HomeActivity)
                true
            }
            primaryItem("Paragons") {}
            primaryItem("Pipe chart") {}.withOnDrawerItemClickListener { view, position, drawerItem ->
                PieChartActivity.startActivity(this@HomeActivity)
                true
            }
            primaryItem("Line chart") {}.withOnDrawerItemClickListener { view, position, drawerItem ->
                LineChartActivity.startActivity(this@HomeActivity)
                true
            }
        }

        initList()
    }

    private fun initList() {
        list.layoutManager = GridLayoutManager(this, 2)
        val adapter = CategoriesAdapter(RealmHelper.getCategories())
        list.adapter = adapter
        ItemClickSupport.addTo(list).setOnItemClickListener { parent, view, position, id ->
            AddExpenseItemActivity.startActivity(this@HomeActivity, adapter.items[position].id)
        }
    }
}

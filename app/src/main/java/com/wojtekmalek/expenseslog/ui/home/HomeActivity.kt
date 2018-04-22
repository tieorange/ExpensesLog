package com.wojtekmalek.expenseslog.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.AddExpenseItemActivity
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import com.wojtekmalek.expenseslog.ui.charts.PipeChartActivity
import com.wojtekmalek.expenseslog.ui.history.HistoryActivity
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
            primaryItem("Limit") {}
            primaryItem("Paragons") {}
            primaryItem("Pipe chart") {}.withOnDrawerItemClickListener { view, position, drawerItem ->
                PipeChartActivity.startActivity(this@HomeActivity)
                true
            }
            primaryItem("Line chart") {}
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

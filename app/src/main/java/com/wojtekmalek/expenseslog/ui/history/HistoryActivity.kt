package com.wojtekmalek.expenseslog.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.R.id.toolbar
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        list.layoutManager = LinearLayoutManager(this)
        val expenses = RealmHelper.getExpenses()
        list.adapter = HistoryAdapter(expenses)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}

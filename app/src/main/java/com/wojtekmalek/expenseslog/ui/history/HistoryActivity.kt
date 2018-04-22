package com.wojtekmalek.expenseslog.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

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

package com.wojtekmalek.expenseslog.ui.setLimit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.mcxiaoke.koi.ext.toast
import com.mcxiaoke.koi.log.logd
import com.wojtekmalek.expenseslog.ExpensesApplication
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.model.Limit
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_limit.*

class LimitActivity : Activity() {

    var limitValue: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limit)

        limitValue = RealmHelper.getLimit()
        logd { "RealmHelper.getLimit()" + ExpensesApplication.realm.where<Limit>().findAll().map { it.value } }
        limit.setText(limitValue.toString(), TextView.BufferType.EDITABLE)

        limit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limitString = s.toString()
                if (!limitString.isEmpty())
                    limitString.toIntOrNull()?.let { limitValue = it }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        toast("Limit set: $limitValue")
        RealmHelper.saveLimit(limitValue)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LimitActivity::class.java))
        }
    }
}

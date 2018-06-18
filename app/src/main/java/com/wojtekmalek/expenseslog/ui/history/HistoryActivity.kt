package com.wojtekmalek.expenseslog.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.francescocervone.rxdrive.RxDrive
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.mcxiaoke.koi.ext.onClick
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.model.ExpenseItem
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*


class HistoryActivity : AppCompatActivity() {

    private lateinit var rxDrive: RxDrive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        list.layoutManager = LinearLayoutManager(this)
        val expenses = RealmHelper.getExpenses()
        list.adapter = HistoryAdapter(expenses)

        fab.onClick { exportHistory(expenses) }

        initRxDrive()
    }

    private fun initRxDrive() {
        rxDrive = RxDrive(GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                //                .addScope(Drive.SCOPE_FILE) //If you want to access to user files
                .addScope(Drive.SCOPE_APPFOLDER) //If you want to access to the app folder
        )
    }

    private fun exportHistory(expenses: List<ExpenseItem>) {
        HistoryExportHelper(this, RealmHelper.getExpenses()).getExpensesCsvFile()
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
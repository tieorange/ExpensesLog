package com.wojtekmalek.expenseslog

import android.app.Application
import com.vicpin.krealmextensions.saveAll
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.model.ExpenseItem
import io.realm.Realm

class ExpensesApplication : Application() {

    val categoriesList = listOf("Groceries", "Clothes", "Fun", "Electronics", "Bills", "Gas", "Others")
    val expensesList = listOf(ExpenseItem().apply { timeStamp = ExpenseItem.getDate(21, 4) },
            ExpenseItem().apply { timeStamp = ExpenseItem.getDate(22, 4) },
            ExpenseItem().apply { timeStamp = ExpenseItem.getDate(22, 4) })

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        realm = Realm.getDefaultInstance()
        initCategories()
    }

    private fun initCategories() {
        if (realm.where(Category::class.java).findAll().isNotEmpty()) return

        categoriesList.mapIndexed { index: Int, item ->
            Category().apply {
                id = index.toString()
                name = item
            }
        }.saveAll()

        expensesList.saveAll()
    }

    companion object {
        lateinit var realm: Realm
    }
}

package com.wojtekmalek.expenseslog

import android.app.Application
import com.vicpin.krealmextensions.saveAll
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.model.ExpenseItem
import io.realm.Realm

class ExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        realm = Realm.getDefaultInstance()
        initCategories()
    }

    private fun initCategories() {
        if (realm.where(Category::class.java).findAll().isNotEmpty()) return

        categoriesList.saveAll()
        expensesList.saveAll()
    }


    val categoriesList = listOf("Groceries", "Clothes", "Fun", "Electronics", "Bills", "Gas", "Others").mapIndexed { index: Int, item ->
        Category().apply {
            id = index.toString()
            name = item
        }
    }

    val expensesList = listOf(
            ExpenseItem().apply {
                timeStamp = ExpenseItem.getDate(21, 4)
                category = categoriesList[3]
                price = 13f
            },
            ExpenseItem().apply {
                timeStamp = ExpenseItem.getDate(22, 4)
                category = categoriesList[0]
                price = 50f
            },
            ExpenseItem().apply {
                timeStamp = ExpenseItem.getDate(22, 4)
                category = categoriesList[1]
                price = 25f
            })


    companion object {
        lateinit var realm: Realm
    }
}

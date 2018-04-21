package com.wojtekmalek.expenseslog

import android.app.Application
import com.vicpin.krealmextensions.allItems
import com.vicpin.krealmextensions.saveAll
import com.wojtekmalek.expenseslog.model.Category
import io.realm.Realm

class ExpensesApplication : Application() {

    val categoriesList = listOf("Groceries", "Clothes", "Fun", "Electronics", "Bills", "Gas", "Others")

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        realm = Realm.getDefaultInstance()
        initCategories()
    }

    private fun initCategories() {
        if (Category().allItems.isNotEmpty()) return

        categoriesList.mapIndexed { index: Int, item ->
            Category().apply {
                id = index.toString()
                name = item
            }
        }.saveAll()
    }

    companion object {
        lateinit var realm: Realm
    }
}

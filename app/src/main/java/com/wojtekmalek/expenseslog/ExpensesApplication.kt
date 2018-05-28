package com.wojtekmalek.expenseslog

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.vicpin.krealmextensions.saveAll
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.model.ExpenseItem
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import java.util.*


class ExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        realm = try {
            Realm.init(this)
            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            Realm.getInstance(config)
        } catch (ex: RealmMigrationNeededException) {
            Realm.getDefaultInstance()
        }

        initCategories()
    }

    private fun initCategories() {
        if (realm.where(Category::class.java).findAll().isNotEmpty()) return

        categoriesList.saveAll()
        expensesList.saveAll()
        RealmHelper.saveLimit(RealmHelper.LIMIT_DEFAULT)
    }


    val categoriesList = listOf("Groceries", "Clothes", "Fun", "Electronics", "Bills", "Gas", "Others").mapIndexed { index: Int, item ->
        Category().apply {
            id = index.toString()
            name = item
        }
    }

    val expensesList = listOf(
            ExpenseItem().apply {
                uuid = UUID.randomUUID().toString()
                timeStamp = ExpenseItem.getDate(21, 4)
                category = categoriesList[3]
                price = 13f
            },
            ExpenseItem().apply {
                uuid = UUID.randomUUID().toString()
                timeStamp = ExpenseItem.getDate(22, 4)
                category = categoriesList[0]
                price = 50f
            },
            ExpenseItem().apply {
                uuid = UUID.randomUUID().toString()
                timeStamp = ExpenseItem.getDate(22, 4)
                category = categoriesList[1]
                price = 25f
            })


    companion object {
        lateinit var realm: Realm
    }
}

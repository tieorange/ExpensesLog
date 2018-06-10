package com.wojtekmalek.expenseslog

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.vicpin.krealmextensions.saveAll
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException


class ExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        realm = try {
            Realm.init(this)
            val config = RealmConfiguration.Builder()
                    .schemaVersion(6)
                    .deleteRealmIfMigrationNeeded()
                    .build()
            Realm.setDefaultConfiguration(config);
            Realm.getInstance(config)
        } catch (ex: RealmMigrationNeededException) {
            Realm.getDefaultInstance()
        }

        initCategories()
    }

    private fun initCategories() {
        if (realm.where(Category::class.java).findAll().isNotEmpty()) return

        categoriesList.saveAll()
        RealmHelper.saveLimit(RealmHelper.LIMIT_DEFAULT)
    }


    val categoriesList = listOf("Groceries", "Clothes", "Fun", "Electronics", "Bills", "Gas", "Others").mapIndexed { index: Int, item ->
        Category().apply {
            id = index.toString()
            name = item
        }
    }


    companion object {
        lateinit var realm: Realm
    }
}

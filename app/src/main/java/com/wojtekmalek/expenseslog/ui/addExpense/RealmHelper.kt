package com.wojtekmalek.expenseslog.ui.addExpense

import android.content.Context
import android.support.v4.content.ContextCompat
import com.orhanobut.hawk.Hawk
import com.wojtekmalek.expenseslog.ExpensesApplication
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.model.ExpenseItem
import com.wojtekmalek.expenseslog.ui.paragon.ParagonItem
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*

object RealmHelper {
    val realm = ExpensesApplication.realm

    fun addExpense(price: Float, categoryId: String) {
        realm.executeTransaction {
            val uuid = UUID.randomUUID().toString()
            val expenseItem = realm.createObject(ExpenseItem::class.java, uuid)
            expenseItem.price = price
            expenseItem.timeStamp = Date().time
            val category = realm.where(Category::class.java).equalTo("id", categoryId).findFirst()
            expenseItem.category = category
        }
    }

    fun getCategories(): List<Category> {
        return realm.where(Category::class.java).findAll()
    }

    fun getExpenses(): List<ExpenseItem> {
        return realm.where<ExpenseItem>().findAll()
    }

    fun getExpensesAmountByCategory(category: Category): Float {
        var sum = 0f
        realm.where<ExpenseItem>().equalTo("category.id", category.id).findAll().forEach {
            sum += it.price
        }
        return sum
    }

    fun getColorForCategory(context: Context, category: Category): Int {
        val colorId = when (category.id) {
            "0" -> R.color.category1
            "1" -> R.color.category2
            "2" -> R.color.category3
            "3" -> R.color.category4
            "4" -> R.color.category5
            "5" -> R.color.category6
            else -> R.color.category7
        }

        return ContextCompat.getColor(context, colorId)
    }

    fun getAllColors(context: Context): List<Int> {
        return listOf(R.color.category1,
                R.color.category2,
                R.color.category3,
                R.color.category4,
                R.color.category5,
                R.color.category6,
                R.color.category7)
                .map {
                    ContextCompat.getColor(context, it)
                }
    }

    fun saveLimit(limit: Int) {
        Hawk.put(LIMIT, limit)
    }

    fun getLimit(): Int {
        return Hawk.get(LIMIT, LIMIT_DEFAULT)
    }

    fun getAllParagons(): List<ParagonItem> {
        return realm.where(ParagonItem::class.java).sort("timeStamp", Sort.DESCENDING).findAll()
    }

    fun addParagon(paragonItem: ParagonItem) {
        realm.executeTransaction {
            realm.copyToRealmOrUpdate(paragonItem)
        }
    }

    private const val LIMIT = "limit"
    const val LIMIT_DEFAULT = 50
}

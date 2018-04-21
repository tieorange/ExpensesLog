package com.wojtekmalek.expenseslog.ui.addExpense

import com.wojtekmalek.expenseslog.ExpensesApplication
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.model.ExpenseItem
import java.util.*

object RealmHelper {
    val realm = ExpensesApplication.realm

    fun addExpense(price: Float, categoryId: String) {
        realm.executeTransaction {


            val expenseItem = ExpensesApplication.realm.createObject(ExpenseItem::class.java)
            expenseItem.id = UUID.randomUUID().toString().toInt()
            expenseItem.price = price
            val category = realm.where(Category::class.java).equalTo("id", categoryId).findFirst()
            if(category == null){
            }else{
            expenseItem.category = category
            }
        }
    }

    fun getCategories(): List<Category> {
        return realm.where(Category::class.java).findAll()
    }

}

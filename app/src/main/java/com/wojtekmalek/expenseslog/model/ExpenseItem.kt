package com.wojtekmalek.expenseslog.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ExpenseItem : RealmObject() {

    @PrimaryKey
    open var id: Int = 0

    open var price: Float = 0f

    open var category: Category? = null
}
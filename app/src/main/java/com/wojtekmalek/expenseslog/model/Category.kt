package com.wojtekmalek.expenseslog.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Category : RealmObject() {

    var name: String = ""

    companion object {
        fun getDummy(): List<Category> {
            return (0..7).map { Category().apply { name = "Groceries" } }
        }
    }
}
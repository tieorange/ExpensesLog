package com.wojtekmalek.expenseslog.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category : RealmObject() {

    @PrimaryKey
    open var id: String = ""

    open var name: String = ""
}
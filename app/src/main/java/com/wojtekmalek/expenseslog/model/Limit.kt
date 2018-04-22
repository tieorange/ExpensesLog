package com.wojtekmalek.expenseslog.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Limit : RealmObject() {

    @PrimaryKey
    open var id = 1

    open var value: Int = 1000
}
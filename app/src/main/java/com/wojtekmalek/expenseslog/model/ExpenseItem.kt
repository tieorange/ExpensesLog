package com.wojtekmalek.expenseslog.model

import com.mcxiaoke.koi.ext.asString
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*


open class ExpenseItem : RealmObject() {
    @PrimaryKey
    open var uuid: String = ""

    open var price: Float = 0f

    open var category: Category? = null

    open var timeStamp: Long = 0L

    fun getTimeString(): String = Date(timeStamp).asString("HH:mm")

    companion object {
        fun getDummy(): List<ExpenseItem> {
            return listOf(ExpenseItem().apply { timeStamp = getDate(21, 4) },
                    ExpenseItem().apply { timeStamp = getDate(22, 4) },
                    ExpenseItem().apply { timeStamp = getDate(22, 4) })
        }

        fun getDate(day: Int, month: Int): Long {
            return GregorianCalendar(2018, month, day).time.time
        }
    }
}
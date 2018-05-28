package com.wojtekmalek.expenseslog.ui.paragon

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ParagonItem : RealmObject() {

    @PrimaryKey
    open var uuid: String = ""

    open var timeStamp: Long = 0L

    companion object {
        fun getDummy(): List<ParagonItem> {
            return listOf(ParagonItem().apply { timeStamp = getDate(21, 4) },
                    ParagonItem().apply { timeStamp = getDate(22, 4) },
                    ParagonItem().apply { timeStamp = getDate(22, 4) })
        }

        fun getDate(day: Int, month: Int): Long {
            return GregorianCalendar(2018, month, day).time.time
        }
    }
}
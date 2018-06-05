package com.wojtekmalek.expenseslog.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.model.ExpenseItem
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import com.wojtekmalek.expenseslog.util.bindView
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(val items: List<ExpenseItem>) : SectionedRecyclerViewAdapter<HistoryAdapter.SubheaderViewHolder, HistoryAdapter.ItemViewHolder>() {

    init {
        items.sortedBy { it.timeStamp }
    }

    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val currentItem = items[position]
        val nextItem = items[position + 1]
        return (!currentItem.timeStamp.hasSameDayOfMonth(nextItem.timeStamp))
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup, viewType: Int) =
            SubheaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))

    override fun onBindSubheaderViewHolder(subheaderViewHolder: SubheaderViewHolder, nextItemPosition: Int) =
            subheaderViewHolder.bind(items[nextItemPosition])

    override fun onBindItemViewHolder(holder: ItemViewHolder, position: Int) =
            holder.bind(items[position])

    override fun getItemSize() = items.size

    class SubheaderViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val date: TextView by bindView(R.id.dateText)

        fun bind(nextItem: ExpenseItem) {
            val fmt = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            date.text = fmt.format(nextItem.timeStamp)
        }
    }

    class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val price: TextView by bindView(R.id.price)
        val container: LinearLayout by bindView(R.id.historyItemContainer)

        fun bind(expenseItem: ExpenseItem) {
            price.text = expenseItem.price.toString()
            expenseItem.category?.let {
                val colorForCategory = RealmHelper.getColorForCategory(itemView.context, it)
                container.setBackgroundColor(colorForCategory)
            }
        }
    }
}

fun Long.hasSameDayOfMonth(timeStamp: Long): Boolean {
    val fmt = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return fmt.format(Date(this)) == fmt.format(Date(timeStamp))
}

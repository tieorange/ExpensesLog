package com.wojtekmalek.expenseslog.ui.home

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.model.Category
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import com.wojtekmalek.expenseslog.util.bindView


class CategoriesAdapter(val items: List<Category>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val contactView = inflater.inflate(R.layout.item_category, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]
        viewHolder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView by bindView(R.id.name)
        val categoryCard: CardView by bindView(R.id.categoryCard)

        fun bind(item: Category) {
            name.text = item.name
            categoryCard.setCardBackgroundColor(RealmHelper.getColorForCategory(view.context, item))
        }
    }
}
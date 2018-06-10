package com.wojtekmalek.expenseslog.ui.paragon

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wojtekmalek.expenseslog.ExpensesApplication
import com.wojtekmalek.expenseslog.R
import com.wojtekmalek.expenseslog.ui.addExpense.RealmHelper
import com.wojtekmalek.expenseslog.ui.history.hasSameDayOfMonth
import com.wojtekmalek.expenseslog.util.bindView
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

class ParagonsAdapter(
        allParagons: List<ParagonItem>,
        private val updateData: () -> Unit
) : SectionedRecyclerViewAdapter<ParagonsAdapter.SubheaderViewHolder, ParagonsAdapter.ItemViewHolder>() {

    val items = arrayListOf<ParagonItem>().apply { addAll(allParagons) }

    init {
        ExpensesApplication.realm.where(ParagonItem::class.java).findAll().addChangeListener { results: RealmResults<ParagonItem> ->
            this.items.clear()
            notifyDataChanged()
            this.items.addAll(results.sortedBy { it.timeStamp })
            notifyDataChanged()
        }
    }

    override fun onPlaceSubheaderBetweenItems(position: Int): Boolean {
        val currentItem = items[position]
        val nextItem = items[position + 1]
        return (!currentItem.timeStamp.hasSameDayOfMonth(nextItem.timeStamp))
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup, viewType: Int) =
            SubheaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_paragon, parent, false))

    override fun onBindSubheaderViewHolder(subheaderViewHolder: SubheaderViewHolder, nextItemPosition: Int) =
            subheaderViewHolder.bind(items[nextItemPosition])

    override fun onBindItemViewHolder(holder: ItemViewHolder, position: Int) =
            holder.bind(items[position])

    override fun getItemSize() = items.size

    fun addNew(currentPictureId: String) {
        val paragonItem = ParagonItem().apply {
            uuid = currentPictureId
            timeStamp = Date().time
        }
        RealmHelper.addParagon(paragonItem)

        updateData()
    }

    class SubheaderViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val date: TextView by bindView(R.id.dateText)

        fun bind(nextItem: ParagonItem) {
            val fmt = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            date.text = fmt.format(nextItem.timeStamp)
        }
    }

    inner class ItemViewHolder(item: View)
        : RecyclerView.ViewHolder(item) {

        val image: ImageView by bindView(R.id.image)

        fun bind(expenseItem: ParagonItem) {
            // TODO: mock
            val path = expenseItem.uuid
            if (!path.isBlank()) {
                val bitmap = BitmapFactory.decodeFile(path)
                image.setImageBitmap(bitmap)
            }

            //            image.setImageDrawable(ContextCompat.getDrawable(item.context, R.mipmap.ic_launcher))
        }
    }
}
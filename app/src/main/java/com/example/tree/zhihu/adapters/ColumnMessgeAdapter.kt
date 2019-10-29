package com.example.tree.zhihu.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.tree.zhihu.activity.ColumnMessageActivity
import com.example.tree.zhihu.gson.ColumnMessage
import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.R
import com.example.tree.zhihu.activity.WebContentActivity
import kotlinx.android.synthetic.main.activity_column_message.*

class ColumnMessgeAdapter(internal var context: Context, internal var columnMessage: ColumnMessage, internal var newsExtras: List<NewsExtra>, internal var bitmaps: List<Bitmap>) : RecyclerView.Adapter<ColumnMessgeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_column_message, viewGroup, false))

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.column_extra_linear.setOnClickListener {
            (context as ColumnMessageActivity).column_view_pager.currentItem = 1
            (context as ColumnMessageActivity).SetIdearFragment("" + columnMessage.stories[i].id)
        }
        viewHolder.te_title.text = columnMessage.stories[i].title
        viewHolder.te_date.text = columnMessage.stories[i].date
        if (newsExtras.size > i && bitmaps.size > i) {
            viewHolder.te_popular.text = "" + newsExtras[i].popularity
            viewHolder.te_comment.text = "" + newsExtras[i].comments
            viewHolder.image_title.setImageBitmap(bitmaps[i])
        }
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, WebContentActivity::class.java)
            intent.putExtra("id", columnMessage.stories[i].id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return columnMessage.stories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var te_title: TextView
        var te_comment: TextView
        var te_popular: TextView
        var te_date: TextView
        var image_title: ImageView
        var column_extra_linear: LinearLayout

        init {
            te_comment = itemView.findViewById<View>(R.id.text_agrees_with_the_column_message) as TextView
            te_popular = itemView.findViewById<View>(R.id.text_review_column_message) as TextView
            te_date = itemView.findViewById<View>(R.id.item_column_message_date) as TextView
            te_title = itemView.findViewById<View>(R.id.item_column_message_title) as TextView
            image_title = itemView.findViewById<View>(R.id.item_column_message_picture_title) as ImageView
            column_extra_linear = itemView.findViewById<View>(R.id.column_extra) as LinearLayout
        }
    }
}

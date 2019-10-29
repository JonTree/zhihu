package com.example.tree.zhihu.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.tree.zhihu.activity.ColumnMessageActivity
import com.example.tree.zhihu.gson.ColumnOverview
import com.example.tree.zhihu.R

class ShouyeAttentionAdapter(internal var context: Context, internal var columnOverview: ColumnOverview, internal var bitmaps: List<Bitmap>) : RecyclerView.Adapter<ShouyeAttentionAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_attention, viewGroup, false))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.text_tile.text = columnOverview.data[i].name
        myViewHolder.text_description.text = columnOverview.data[i].description
        if (bitmaps.size > i) {
            myViewHolder.image_title.setImageBitmap(bitmaps[i])
        }
        myViewHolder.itemView.setOnClickListener {
            val intent = Intent(context, ColumnMessageActivity::class.java)
            intent.putExtra("id", columnOverview.data[i].id)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return columnOverview.data.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image_title: ImageView
        var text_tile: TextView
        var text_description: TextView

        init {
            image_title = itemView.findViewById<View>(R.id.picture_title_topic) as ImageView
            text_tile = itemView.findViewById<View>(R.id.text_title_topic) as TextView
            text_description = itemView.findViewById<View>(R.id.text_description_topic) as TextView
        }
    }
}

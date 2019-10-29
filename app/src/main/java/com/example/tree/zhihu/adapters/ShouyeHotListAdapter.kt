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

import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.gson.NewsHotList
import com.example.tree.zhihu.activity.MainActivity
import com.example.tree.zhihu.R
import com.example.tree.zhihu.activity.WebContentActivity

class ShouyeHotListAdapter(private val context: Context, internal var newsHotList: NewsHotList, internal var bitmaps: List<Bitmap>, internal var newsExtras: List<NewsExtra>) : RecyclerView.Adapter<ShouyeHotListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_hot_list, viewGroup, false))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.hot_list_linear.setOnClickListener {
            (context as MainActivity).viewPager.currentItem = 1
            context.idearFragment.setId("" + newsHotList.recent[i].news_id)
        }

        //排名TextView设置
        setNumber(myViewHolder, i)
        myViewHolder.text_title.text = newsHotList.recent[i].title
        if (newsExtras.size > i && bitmaps.size > i) {
            myViewHolder.image_title.setImageBitmap(bitmaps[i])
            myViewHolder.text_review.text = "" + newsExtras[i].comments
            myViewHolder.text_agree.text = "" + newsExtras[i].popularity
        }
        myViewHolder.itemView.setOnClickListener {
            val intent = Intent(context, WebContentActivity::class.java)
            intent.putExtra("id", newsHotList.recent[i].news_id)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return newsHotList.recent.size
    }


    //排名TextView设置
    fun setNumber(myViewHolder: MyViewHolder, i: Int) {
        var i = i
        myViewHolder.number.text = "" + ++i
        if (i < 4) {
            myViewHolder.number.setTextColor(-0xebec3)
        } else if (i >= 4) {
            myViewHolder.number.setTextColor(-0x3d5a96)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var number: TextView
        var text_title: TextView
        var text_agree: TextView
        var text_review: TextView
        var image_title: ImageView
        var hot_list_linear: LinearLayout

        init {
            number = itemView.findViewById<View>(R.id.text_number_hot_list) as TextView
            text_agree = itemView.findViewById<View>(R.id.text_agrees_with_the_column_message) as TextView
            text_review = itemView.findViewById<View>(R.id.text_review_column_message) as TextView
            text_title = itemView.findViewById<View>(R.id.item_column_message_title) as TextView
            image_title = itemView.findViewById<View>(R.id.item_column_message_picture_title) as ImageView
            hot_list_linear = itemView.findViewById<View>(R.id.hot_list_linear) as LinearLayout
        }
    }

}

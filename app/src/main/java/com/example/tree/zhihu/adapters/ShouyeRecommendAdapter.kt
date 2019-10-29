package com.example.tree.zhihu.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView

import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.activity.MainActivity
import com.example.tree.zhihu.R
import com.example.tree.zhihu.activity.WebContentActivity


class ShouyeRecommendAdapter(internal var context: Context, internal var news: News, internal var newsExtras: List<NewsExtra>, internal var bitmaps: List<Bitmap>) : RecyclerView.Adapter<ShouyeRecommendAdapter.MyViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_recommend, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {

        viewHolder.image_select.setOnClickListener { v -> showPopupMenu(v) }
        viewHolder.recommend_comment_linear.setOnClickListener {
            (context as MainActivity).viewPager.currentItem = 1
            (context as MainActivity).idearFragment.setId("" + news.stories[i].id)
        }
        viewHolder.image_content.setImageBitmap(bitmaps[i])
        viewHolder.text_title.text = news.stories[i].title
        if (newsExtras.size > i) {
            viewHolder.text_popularty.text = "" + newsExtras[i].popularity
            viewHolder.text_comments.text = "" + newsExtras[i].comments
        }
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, WebContentActivity::class.java)
            intent.putExtra("id", news.stories[i].id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return news.stories.size
    }


    private fun showPopupMenu(view: View) {
        // 这里的view代表popupMenu需要依附的view
        val popupMenu = PopupMenu(context, view)
        // 获取布局文件
        popupMenu.menuInflater.inflate(R.menu.shouye_recommend, popupMenu.menu)
        popupMenu.show()
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener { true }
        popupMenu.setOnDismissListener {
            // 控件消失时的事件
        }
    }

    //监听点击事件
    override fun onClick(v: View) {
        when (v.id) {
            R.id.shouye_recommended_choice -> {
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_title: TextView
        var text_popularty: TextView
        var text_comments: TextView
        var image_content: ImageView
        var image_select: ImageView
        var recommend_comment_linear: LinearLayout


        init {
            text_title = itemView.findViewById<View>(R.id.item_text_title_shouye_recommend) as TextView
            text_popularty = itemView.findViewById<View>(R.id.news_popularity) as TextView
            text_comments = itemView.findViewById<View>(R.id.news_comments) as TextView
            image_content = itemView.findViewById<View>(R.id.item_image_content_shouye_recommend) as ImageView
            image_select = itemView.findViewById<View>(R.id.shouye_recommended_choice) as ImageView
            recommend_comment_linear = itemView.findViewById<View>(R.id.recommend_comment_linear) as LinearLayout
        }

    }

}

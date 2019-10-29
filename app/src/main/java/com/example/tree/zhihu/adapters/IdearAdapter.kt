package com.example.tree.zhihu.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.tree.zhihu.gson.LongComment
import com.example.tree.zhihu.gson.ShortComment
import com.example.tree.zhihu.R


class IdearAdapter(private val context: Context, private val shortComment: ShortComment, private val longComment: LongComment, internal var bitmaps: List<Bitmap>) : RecyclerView.Adapter<IdearAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_show, viewGroup, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        if (i < longComment.comments.size) {
            viewHolder.reply_name.text = longComment.comments[i].author + ":"
            viewHolder.reply_content.text = longComment.comments[i].content
            viewHolder.comment_likes.text = "" + longComment.comments[i].likes
            if (longComment.comments[i].reply_to != null) {
                viewHolder.reply_to_linear.visibility = View.VISIBLE
                viewHolder.reply_to_name.text = longComment.comments[i].reply_to.author + ":"
                viewHolder.reply_to_content.text = longComment.comments[i].reply_to.content
            }
            if (bitmaps.size > i) {
                viewHolder.reply_avatar.setImageBitmap(bitmaps[i])
            }
        } else {
            val mI = i - longComment.comments.size
            viewHolder.reply_name.text = shortComment.comments[mI].author + ":"
            viewHolder.reply_content.text = shortComment.comments[mI].content
            viewHolder.comment_likes.text = "" + shortComment.comments[mI].likes
            if (shortComment.comments[mI].reply_to != null) {
                viewHolder.reply_to_linear.visibility = View.VISIBLE
                viewHolder.reply_to_name.text = shortComment.comments[mI].reply_to.author + ":"
                viewHolder.reply_to_content.text = shortComment.comments[mI].reply_to.content
            }
            if (bitmaps.size > i) {
                viewHolder.reply_avatar.setImageBitmap(bitmaps[i])
            }
        }
    }

    override fun getItemCount(): Int {
        return shortComment.comments.size + longComment.comments.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var reply_avatar: ImageView
        var reply_name: TextView
        var reply_to_name: TextView
        var reply_to_content: TextView
        var reply_content: TextView
        var comment_likes: TextView
        var reply_to_linear: LinearLayout

        init {
            reply_avatar = itemView.findViewById(R.id.reply_avatar)
            reply_content = itemView.findViewById(R.id.reply_content)
            reply_name = itemView.findViewById(R.id.reply_name)
            reply_to_content = itemView.findViewById(R.id.reply_to_content)
            reply_to_name = itemView.findViewById(R.id.reply_to_name)
            comment_likes = itemView.findViewById(R.id.comment_likes)
            reply_to_linear = itemView.findViewById(R.id.reply_to_linear)
        }
    }
}

package com.example.tree.zhihu.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.tree.zhihu.gson.LongComment
import com.example.tree.zhihu.gson.ShortComment
import com.example.tree.zhihu.R
import com.example.tree.zhihu.adapters.IdearAdapter
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.tab_idear_fragment.*

import java.util.ArrayList
import java.util.Objects

class IdearFragment : BasePageFragment() {

    private lateinit var mLinearLayout: LinearLayout


    internal var longComment: LongComment? = null
    internal var shortComment: ShortComment? = null
    internal var bitmaps: MutableList<Bitmap> = ArrayList()

    internal var responseData: String? = null
    internal var id: String? = null
    internal var mId: String? = "啦啦啦啦" //防止重复加载
    internal val UPDATA_COMMENT = 1
    internal val UPDATA_IMAGE = 2


    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATA_COMMENT -> {
                    if (shortComment!!.comments.size + longComment!!.comments.size > 0)
                        tab_idear_no_background.visibility = View.GONE
                    else
                        tab_idear_no_background.visibility = View.VISIBLE
                    idear_recyclerView.adapter = IdearAdapter(activity!!, shortComment!!, longComment!!, bitmaps)
                }
                UPDATA_IMAGE -> idear_recyclerView.adapter!!.notifyItemChanged(msg.arg1)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab_idear_fragment, container, false)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //找到底部tab的布局
        mLinearLayout = Objects.requireNonNull<FragmentActivity>(activity).findViewById(R.id.bottom_tabs)
        //顶部导航栏
        //设置recyclerView
        idear_recyclerView.layoutManager = LinearLayoutManager(activity)
        //设置NestedScrollView
        idear_nested_scroll_view.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val mScrollX = px2dp(activity!!, scrollX.toFloat())
            val mScrollY = px2dp(activity!!, scrollY.toFloat())
            val mOldScrollX = px2dp(activity!!, oldScrollX.toFloat())
            val mOldScrollY = px2dp(activity!!, oldScrollY.toFloat())
            if (mOldScrollY >= 70) {
                idear_top.visibility = View.VISIBLE
            }
            if (mOldScrollY < 70) {
                idear_top.visibility = View.GONE
            }
            if (scrollY - oldScrollY > 10) {
                mLinearLayout.visibility = View.GONE
            } else if (oldScrollY - scrollY > 20) {
                mLinearLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun fetchData() {
        if ((id != null) or (id === mId)) {
            mId = id
            getComment()
        }
    }

    fun getComment() {
        Thread(Runnable {
            //长评论获取
            val gson = Gson()
            responseData = GetConnected.sendRequeestWithOkhttp(LongComment.getLongCommentUrl(id))
            longComment = gson.fromJson<LongComment>(responseData, object : TypeToken<LongComment>() {

            }.type)
            responseData = GetConnected.sendRequeestWithOkhttp(ShortComment.getShortCommentUrl(id))
            shortComment = gson.fromJson<ShortComment>(responseData, object : TypeToken<ShortComment>() {

            }.type)
            if ((longComment != null) and (shortComment != null)) {
                val message = Message()
                message.what = UPDATA_COMMENT
                handler.sendMessage(message)
                for (i in 0 until longComment!!.comments.size + shortComment!!.comments.size) {
                    if (i < longComment!!.comments.size) {
                        bitmaps.add(GetConnected.getImage(longComment!!.comments[i].avatar))
                        val message1 = Message()
                        message1.what = UPDATA_IMAGE
                        message1.arg1 = i
                        handler.sendMessage(message1)
                    } else {
                        val mI = i - longComment!!.comments.size
                        bitmaps.add(GetConnected.getImage(shortComment!!.comments[mI].avatar))
                        val message1 = Message()
                        message1.what = UPDATA_IMAGE
                        message1.arg1 = i
                        handler.sendMessage(message1)
                    }
                }
            }
        }).start()
    }

    fun setId(id: String) {
        this.id = id
        fetchData()
    }


    fun getMId(): String? {
        return id
    }

    companion object {

        //将dp转换为px
        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        //将px转换为dp
        fun px2dp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }

}


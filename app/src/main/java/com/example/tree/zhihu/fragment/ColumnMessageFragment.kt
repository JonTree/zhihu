package com.example.tree.zhihu.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.tree.zhihu.activity.ColumnMessageActivity
import com.example.tree.zhihu.gson.ColumnMessage
import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.R
import com.example.tree.zhihu.adapters.ColumnMessgeAdapter
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.example.tree.zhihu.tool.LocalCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.column_message_fragment.*
import kotlinx.android.synthetic.main.part_activity_bottom.*

import java.io.IOException
import java.text.ParseException
import java.util.ArrayList

class ColumnMessageFragment : BasePageFragment() {

    internal var nestedScrollView: NestedScrollView? = null


    internal var columnMessage: ColumnMessage? = null

    internal var newsExtras: MutableList<NewsExtra> = ArrayList()
    internal var bitmaps: MutableList<Bitmap> = ArrayList()
    private var responseData: String? = null
    internal val UPDATE_TITLE = 1
    internal val UPDATA_EXTRA_BITMAP = 2

    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_TITLE -> {
                    column_message_title.text = columnMessage!!.name
                    column_message_recycler_view.adapter = ColumnMessgeAdapter(activity!!, columnMessage!!, newsExtras, bitmaps)
                    tab_column_no_background.visibility = View.GONE
                }
                UPDATA_EXTRA_BITMAP -> column_message_recycler_view.adapter!!.notifyItemChanged(msg.arg1)
            }
        }
    }


    //取得数据
    val dateUrl: String
        get() {
            var s: String? = null
            val id = (activity as ColumnMessageActivity).id
            s = "https://news-at.zhihu.com/api/3/section/$id"
            return s
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.column_message_fragment, container, false)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //找到底部tab的布局
        column_message_recycler_view.layoutManager = LinearLayoutManager(activity)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun fetchData() {
        bottom_tabs.visibility = View.GONE
        if (!this.isDataInitiated) getColumnMessage()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getColumnMessage() {
        Thread(Runnable {
            responseData = GetConnected.sendRequeestWithOkhttp(dateUrl)
            val gson = Gson()
            columnMessage = gson.fromJson<ColumnMessage>(responseData, object : TypeToken<ColumnMessage>() {

            }.type)
            if (columnMessage != null) {
                for (i in 0 until columnMessage!!.stories.size) {
                    try {
                        columnMessage!!.stories[i].date = correctDateFormat(columnMessage!!.stories[i].date)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                }
                val message = Message()
                message.what = UPDATE_TITLE
                handler.sendMessage(message)
                for (i in 0 until columnMessage!!.stories.size) {
                    responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(columnMessage!!.stories[i].id))
                    //保存Json数据
                    try {
                        LocalCache.saveJsonData(activity, News.getNewsExtraUrl(columnMessage!!.stories[i].id), responseData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    newsExtras.add(gson.fromJson<Any>(responseData, object : TypeToken<NewsExtra>() {

                    }.type) as NewsExtra)
                    bitmaps.add(GetConnected.getImage(columnMessage!!.stories[i].images[0]))
                    val messageExtraBimap = Message()
                    messageExtraBimap.what = UPDATA_EXTRA_BITMAP
                    messageExtraBimap.arg1 = i
                    handler.sendMessage(messageExtraBimap)
                }
            }
        }).start()
    }

    //纠正日期格式
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Throws(ParseException::class)
    fun correctDateFormat(string: String): String {
        var s = SimpleDateFormat("yyyyMMdd")
        val date = s.parse(string)
        s = SimpleDateFormat("yyyy/MM/dd")
        return s.format(date)
    }
}

package com.example.tree.zhihu.fragment.shouye

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast

import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.gson.NewsHotList
import com.example.tree.zhihu.R
import com.example.tree.zhihu.adapters.ShouyeHotListAdapter
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.example.tree.zhihu.tool.LocalCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.util.ArrayList

class HotListFragment : BasePageFragment() {
    internal lateinit var recyclerView: RecyclerView
    private lateinit var mLinearLayout: LinearLayout
    internal lateinit var progressBar: ProgressBar


    internal lateinit var shouyeHotListAdapter: ShouyeHotListAdapter
    internal var newsHotList: NewsHotList? = null
    private var connectivityManager: ConnectivityManager? = null//用于判断是否有网络
    internal var info: NetworkInfo? = null
    internal var bitmaps: MutableList<Bitmap> = ArrayList()
    internal var newsExtras: MutableList<NewsExtra> = ArrayList()


    internal var hotNewsURL = "https://news-at.zhihu.com/api/3/news/hot"
    internal var mHotNewsUrl = "lalalalalaala"
    internal var responseData: String? = null

    internal val UPDATE_TITLE = 1
    internal val UPDATE_IMAGE_EXTRA = 2
    internal val SHOW_PROGRESS_BAR = 3
    internal val HIDE_PROGRESS_BAR = 4

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_TITLE -> {
                    shouyeHotListAdapter = ShouyeHotListAdapter(activity!!, newsHotList!!, bitmaps, newsExtras)
                    recyclerView.adapter = shouyeHotListAdapter
                    getHotListExtra()
                }
                UPDATE_IMAGE_EXTRA -> shouyeHotListAdapter.notifyItemChanged(msg.arg1)
                SHOW_PROGRESS_BAR -> progressBar.visibility = View.VISIBLE
                HIDE_PROGRESS_BAR -> progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shouye_hot_list_fragment, container, false)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化
        init(view)
        loadLocalData()
    }

    override fun fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            Toast.makeText(activity, "请检查网络设置", Toast.LENGTH_SHORT)

        } else {              //当前有已激活的网络连接
            if (this.isDataInitiated == false) {
                getHotListNews()
                mHotNewsUrl = hotNewsURL
            }
        }
    }

    //初始化
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun init(view: View) {
        connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager//获取当前网络的连接服务
        info = connectivityManager!!.activeNetworkInfo //获取活动的网络连接信息
        progressBar = view.findViewById<View>(R.id.hot_list_progress) as ProgressBar
        //找到底部tab的布局
        mLinearLayout = activity!!.findViewById<View>(R.id.bottom_tabs) as LinearLayout
        recyclerView = view.findViewById<View>(R.id.hot_list_recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    //获取热门消息
    fun getHotListNews() {
        Thread(Runnable {
            val messageShow = Message()
            messageShow.what = SHOW_PROGRESS_BAR
            handler.sendMessage(messageShow)
            responseData = GetConnected.sendRequeestWithOkhttp(hotNewsURL)
            val gson = Gson()
            newsHotList = gson.fromJson<NewsHotList>(responseData, object : TypeToken<NewsHotList>() {

            }.type)
            if (newsHotList != null) {
                try {
                    LocalCache.saveJsonData(activity, hotNewsURL, responseData)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val message = Message()
                message.what = UPDATE_TITLE
                handler.sendMessage(message)
            } else if (newsHotList == null) {
                val messageHide = Message()
                messageHide.what = HIDE_PROGRESS_BAR
                handler.sendMessage(messageHide)
            }
        }).start()
    }

    /*获取热榜消息额外消息
    * 嵌套线程
    * */
    fun getHotListExtra() {
        Thread(Runnable {
            val thread = Thread(Runnable {
                val gson = Gson()
                for (i in 0 until newsHotList!!.recent.size) {
                    responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(newsHotList!!.recent[i].news_id))
                    try {
                        LocalCache.saveJsonData(activity, News.getNewsExtraUrl(newsHotList!!.recent[i].news_id), responseData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    newsExtras.add(gson.fromJson<Any>(responseData, object : TypeToken<NewsExtra>() {

                    }.type) as NewsExtra)
                    val bitmap = GetConnected.getImage(newsHotList!!.recent[i].thumbnail)
                    bitmaps.add(bitmap)
                    LocalCache.setLocalImageCache(newsHotList!!.recent[i].thumbnail, bitmap)
                    val message = Message()
                    message.what = UPDATE_IMAGE_EXTRA
                    message.arg1 = i
                    handler.sendMessage(message)
                }
                val messageHide = Message()
                messageHide.what = HIDE_PROGRESS_BAR
                handler.sendMessage(messageHide)
            })
            thread.start()
            try {
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()

    }

    fun loadLocalData() {
        if (!LocalCache.readJsonData(activity, hotNewsURL).isEmpty()) {
            responseData = LocalCache.readJsonData(activity, hotNewsURL)
            val gson = Gson()
            newsHotList = gson.fromJson<NewsHotList>(responseData, object : TypeToken<NewsHotList>() {

            }.type)
            shouyeHotListAdapter = ShouyeHotListAdapter(activity!!, newsHotList!!, bitmaps, newsExtras)
            recyclerView.adapter = shouyeHotListAdapter
            for (i in 0 until newsHotList!!.recent.size) {
                responseData = LocalCache.readJsonData(activity, News.getNewsExtraUrl(newsHotList!!.recent[i].news_id))
                newsExtras.add(gson.fromJson<Any>(responseData, object : TypeToken<NewsExtra>() {

                }.type) as NewsExtra)
                val bitmap = LocalCache.getLocalImageCache(newsHotList!!.recent[i].thumbnail)
                bitmaps.add(bitmap)
                shouyeHotListAdapter.notifyItemChanged(i)
            }
        }
    }


}

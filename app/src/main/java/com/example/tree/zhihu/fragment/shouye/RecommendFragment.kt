package com.example.tree.zhihu.fragment.shouye

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper

import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.gson.NewsExtra
import com.example.tree.zhihu.gson.ShortComment
import com.example.tree.zhihu.activity.MainActivity
import com.example.tree.zhihu.R
import com.example.tree.zhihu.activity.WebContentActivity
import com.example.tree.zhihu.adapters.ShouyeRecommendAdapter
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.example.tree.zhihu.tool.LocalCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.util.ArrayList
import java.util.Objects

import android.content.ContentValues.TAG
import android.support.v4.app.FragmentActivity


class RecommendFragment : BasePageFragment(), View.OnClickListener {
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var mLinearLayout: LinearLayout
    internal lateinit var progressBar: ProgressBar
    internal lateinit var linearLayoutTopStoryFlipper: LinearLayout
    lateinit var viewFlipper: ViewFlipper


    internal var news: News? = null
    private lateinit var loadNews: News
    internal lateinit var shouyeRecommendAdapter: ShouyeRecommendAdapter
    private var info: NetworkInfo? = null
    private lateinit var shortComment: ShortComment
    private var top_stories: List<News.TopStoriesBean>? = null
    internal var newsExtras: MutableList<NewsExtra> = ArrayList()
    private var loadNewsExtras: MutableList<NewsExtra> = ArrayList()
    internal var bitmaps: MutableList<Bitmap> = ArrayList()
    private var loadBitmaps: MutableList<Bitmap> = ArrayList()
    private var shortCommentsList: MutableList<String> = ArrayList()


    private var responseData: String? = null
    private var newsUrl = "https://news-at.zhihu.com/api/4/news/latest"
    private var pastNewsUrl = ""
    private var mPastNewsUrl = "啦啦啦啦啦"
    internal val UPDATE_TITLE = 1
    internal val SHOW_PROGRESS_BAR = 2
    internal val HIDE_PROGRESS_BAR = 3
    internal val UPDATA_FLIPPER = 4
    internal val NEXT = 5
    internal val PRE = 6
    internal val START_FLIPPER = 7
    internal val SHOP_FLIPPER = 8


    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_TITLE -> {
                    shouyeRecommendAdapter = ShouyeRecommendAdapter(activity!!, news!!, newsExtras, bitmaps)
                    recyclerView.adapter = shouyeRecommendAdapter
                    progressBar.visibility = View.GONE
                }
                SHOW_PROGRESS_BAR -> progressBar.visibility = View.VISIBLE
                HIDE_PROGRESS_BAR -> {
                    Toast.makeText(activity, "网络连接失败", Toast.LENGTH_SHORT)
                    progressBar.visibility = View.VISIBLE
                }
                UPDATA_FLIPPER -> {
                    linearLayoutTopStoryFlipper.visibility = View.VISIBLE
                    viewFlipper.addView(msg.obj as View)
                }
                NEXT -> {
                    viewFlipper.stopFlipping()
                    viewFlipper.inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
                    viewFlipper.outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left)
                    viewFlipper.showNext()
                    viewFlipper.inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
                    viewFlipper.outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left)
                }
                PRE -> {
                    viewFlipper.stopFlipping()
                    viewFlipper.inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left)
                    viewFlipper.outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_right)
                    viewFlipper.showPrevious()
                    viewFlipper.inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
                    viewFlipper.outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left)
                }
                START_FLIPPER -> viewFlipper.startFlipping()
                SHOP_FLIPPER -> viewFlipper.stopFlipping()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shouye_recommend_fragment, container, false)
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化控件
        init(view)
        loadLocalData()
    }

    override fun fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            Toast.makeText(activity, "检查网络连接设置", Toast.LENGTH_SHORT).show()
        } else {              //当前有已激活的网络连接
            if (mPastNewsUrl !== newsUrl && mPastNewsUrl !== pastNewsUrl) {
                /*判断历史News是否为空
                 * 为空就获取当日数据
                 * */
                Log.d(TAG, "fetchData: $mPastNewsUrl")
                Log.d(TAG, "fetchData: $pastNewsUrl")
                if (pastNewsUrl.isEmpty()) {
                    //获取最新消息
                    getNews(newsUrl, UPDATE_TITLE)
                    mPastNewsUrl = newsUrl
                } else if (mPastNewsUrl !== pastNewsUrl) {
                    getNews(pastNewsUrl, UPDATE_TITLE)
                    mPastNewsUrl = pastNewsUrl
                    Log.d(TAG, "fetchData: $mPastNewsUrl")
                    Log.d(TAG, "fetchData: $pastNewsUrl")
                }
            }
        }

    }

    //初始化控件
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun init(view: View) {
        //用于判断是否有网络
        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        info = connectivityManager.activeNetworkInfo //获取活动的网络连接信息
        linearLayoutTopStoryFlipper = view.findViewById<View>(R.id.top_story_flipper_linear) as LinearLayout
        viewFlipper = view.findViewById<View>(R.id.top_story_flipper) as ViewFlipper
        //找到底部tab的布局
        mLinearLayout = Objects.requireNonNull<FragmentActivity>(activity).findViewById(R.id.bottom_tabs)
        progressBar = view.findViewById<View>(R.id.recommended_progress) as ProgressBar
        recyclerView = view.findViewById<View>(R.id.recommend_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }


    //获取最新新闻数据
    fun getNews(url: String, type: Int) {
        Thread {
            //发送消息显示ProgressBar
            val messageShow = Message()
            messageShow.what = SHOW_PROGRESS_BAR
            handler.sendMessage(messageShow)
            val gson = Gson()
            responseData = GetConnected.sendRequeestWithOkhttp(url)
            //判断当前是否为过去新闻，若不是则保存数据
            if (pastNewsUrl.isEmpty()) {
                //保存json数据
                try {
                    LocalCache.saveJsonData(activity, newsUrl, responseData)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            news = gson.fromJson<News>(responseData, object : TypeToken<News>() {

            }.type)
            /*判断是否存在真的获取了数据*/
            if (news != null) {
                /*判断是否需要获取top_story
                避免用户还没获得当日的新闻就直接查询的历史新闻
                 *  */
                if (news!!.top_stories == null && top_stories == null) {
                    responseData = GetConnected.sendRequeestWithOkhttp(newsUrl)
                    //保存json数据
                    try {
                        LocalCache.saveJsonData(activity, newsUrl, responseData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val news = gson.fromJson<News>(responseData, object : TypeToken<News>() {

                    }.type)
                    top_stories = news.top_stories
                    //开始设置top_story
                    getTopViewList()
                } else if (news!!.top_stories != null && top_stories == null) {
                    top_stories = news!!.top_stories
                    //开始设置top_story
                    getTopViewList()
                }
                //开始获取当前news中的额外信息以及图片
                bitmaps.clear()
                newsExtras.clear()
                for (i in 0 until news!!.stories.size) {
                    responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(news!!.stories[i].id))
                    //保存Json数据
                    try {
                        LocalCache.saveJsonData(activity, News.getNewsExtraUrl(news!!.stories[i].id), responseData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    newsExtras.add(gson.fromJson<Any>(responseData, object : TypeToken<NewsExtra>() {

                    }.type) as NewsExtra)
                    //获取图片
                    val bitmap = GetConnected.getImage(news!!.stories[i].images[0])
                    bitmaps.add(bitmap)
                    //保存图片
                    LocalCache.setLocalImageCache(news!!.stories[i].images[0], bitmap)
                }
                //发送信息更新item
                val message = Message()
                message.what = type
                handler.sendMessage(message)
            } else if (news == null) {
                val messageHide = Message()
                messageHide.what = HIDE_PROGRESS_BAR
                handler.sendMessage(messageHide)
            }
        }.start()
    }

    fun setPastNewsUrl(pastNewsUrl: String) {
        this.pastNewsUrl = pastNewsUrl
        fetchData()
    }

    fun getTopViewList() {
        /*利用循环来设置顶部Top_story
        必须在子线程里运行
         */
        for (i in top_stories!!.indices) {
            //整体的top_story布局设置
            val view = View.inflate(activity, R.layout.part_idear_fragments, null)
            (view.findViewById<View>(R.id.top_story_title) as TextView).text = top_stories!![i].title
            (view.findViewById<View>(R.id.top_story_image_title) as ImageView).setImageBitmap(GetConnected.getImage(top_stories!![i].image))
            (view.findViewById<View>(R.id.top_story_next) as ImageView).setOnClickListener(this)
            (view.findViewById<View>(R.id.top_story_pre) as ImageView).setOnClickListener(this)
//设置点击事件，点击进入到详细内容
            view.setOnClickListener {
                val intent = Intent(activity, WebContentActivity::class.java)
                intent.putExtra("id", top_stories!![i].id)
                activity!!.startActivity(intent)
            }
            /*填充标题下方小滚动评论框
             * 并设置可显示合格评论标准
             * */
            val data = GetConnected.sendRequeestWithOkhttp(NewsExtra.getExtraShortcommentUrl("" + top_stories!![i].id))
            val gson = Gson()
            shortComment = gson.fromJson(data, object : TypeToken<ShortComment>() {

            }.type)
            //先清空
            shortCommentsList.clear()
            //筛选合适的短评
            for (j in 0 until shortComment.comments.size) {
                if (shortComment.comments[j].content.length > 14 && shortComment.comments[j].content.length < 34) {
                    shortCommentsList.add(shortComment.comments[j].content)
                }
            }
            //将筛选好的设置到标题下方flipper中
            val shortCommentViewFlipper = view.findViewById<ViewFlipper>(R.id.news_little_view_flipper)
            for (k in shortCommentsList.indices) {
                val shortCommentView = View.inflate(activity, R.layout.short_comment_top_story, null)
                (shortCommentView.findViewById<View>(R.id.short_comment_show) as TextView).text = shortCommentsList[k]
                shortCommentViewFlipper.addView(shortCommentView)
            }
            shortCommentViewFlipper.setOnClickListener { v ->
                (activity as MainActivity).viewPager.currentItem = 1
                (activity as MainActivity).idearFragment.setId("" + news!!.top_stories[i].id)
            }
            val messageFlipper = Message()
            messageFlipper.obj = view
            messageFlipper.what = UPDATA_FLIPPER
            handler.sendMessage(messageFlipper)
        }
    }

    //加载本地数据
    fun loadLocalData() {
        //首次启动判断
        if (!LocalCache.readJsonData(activity, newsUrl).isEmpty()) {
            responseData = LocalCache.readJsonData(activity, newsUrl)
            val gson = Gson()
            loadNews = gson.fromJson(responseData, object : TypeToken<News>() {

            }.type)
            for (i in 0 until loadNews.stories.size) {
                responseData = LocalCache.readJsonData(activity, News.getNewsExtraUrl(loadNews.stories[i].id))
                loadNewsExtras.add(gson.fromJson<Any>(responseData, object : TypeToken<NewsExtra>() {

                }.type) as NewsExtra)
                //获取图片
                val bitmap = LocalCache.getLocalImageCache(loadNews.stories[i].images[0])
                loadBitmaps.add(bitmap)
            }
            shouyeRecommendAdapter = ShouyeRecommendAdapter(activity!!, loadNews, loadNewsExtras, loadBitmaps)
            recyclerView.adapter = shouyeRecommendAdapter
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.top_story_next -> Thread {
                //top_story上下页切换
                val messageNext = Message()
                messageNext.what = NEXT
                handler.sendMessage(messageNext)
                try {
                    //                            切换后暂停top_story的滚动
                    Thread.sleep(6000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                //                        重新启动
                val messageStart = Message()
                messageStart.what = START_FLIPPER
                handler.sendMessage(messageStart)
            }.start()
            R.id.top_story_pre -> Thread {
                val messagePre = Message()
                messagePre.what = PRE
                handler.sendMessage(messagePre)
                try {
                    Thread.sleep(6000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                val messageStart = Message()
                messageStart.what = START_FLIPPER
                handler.sendMessage(messageStart)
            }.start()
        }
    }
}

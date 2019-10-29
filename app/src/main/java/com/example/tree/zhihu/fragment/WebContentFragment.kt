package com.example.tree.zhihu.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.gson.NewsContent
import com.example.tree.zhihu.R
import com.example.tree.zhihu.activity.WebContentActivity
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import android.content.ContentValues.TAG

class WebContentFragment : BasePageFragment() {

    internal lateinit var webView: WebView
    internal lateinit var contentImageTitle: ImageView
    internal lateinit var contentDefaultTitle: TextView
    internal lateinit var contentImageSource: TextView
    private lateinit var mLinearLayout: LinearLayout
    internal lateinit var web_no_background: LinearLayout


    private var responseData: String? = null
    internal var newsContent: NewsContent? = null
    internal var bitmap: Bitmap? = null
    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            contentImageTitle.setImageBitmap(bitmap)
            contentDefaultTitle.text = newsContent!!.title
            contentImageSource.text = newsContent!!.image_source
            webView.loadDataWithBaseURL(null, newsContent!!.body, "txt/html", "utf-8", null)
            web_no_background.visibility = View.GONE
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.web_content_fragment, container, false)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById<View>(R.id.content_web_view) as WebView
        web_no_background = view.findViewById<View>(R.id.tab_web_no_background) as LinearLayout
        mLinearLayout = activity!!.findViewById<View>(R.id.bottom_tabs) as LinearLayout
        contentImageTitle = view.findViewById<View>(R.id.content_image_title) as ImageView
        contentDefaultTitle = view.findViewById<View>(R.id.content_default_title) as TextView
        contentImageSource = view.findViewById<View>(R.id.content_image_source) as TextView
        Log.d(TAG, "onViewCreated: ")
        setWebView()
    }

    override fun fetchData() {
        mLinearLayout.visibility = View.GONE
        if (this.isDataInitiated == false) getData()
    }

    fun getData() {
        val id = (activity as WebContentActivity).id
        if (id != -1) {
            Thread(Runnable {
                responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsContentUrl(id))
                val gson = Gson()
                newsContent = gson.fromJson<NewsContent>(responseData, object : TypeToken<NewsContent>() {

                }.type)
                if (newsContent != null) {
                    bitmap = GetConnected.getImage(newsContent!!.image)
                    val message = Message()
                    handler.sendMessage(message)
                }
            }).start()
        }
    }


    //配置WebView
    fun setWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadsImagesAutomatically = true  //支持自动加载图片
        webSettings.javaScriptEnabled = true//支持js
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //重置webview中img标签的图片大小
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName('img'); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "var img = objs[i];   " +
                        "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                        "}" +
                        "})()")
            }
        }
    }


}

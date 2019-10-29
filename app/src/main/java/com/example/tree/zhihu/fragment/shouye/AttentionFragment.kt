package com.example.tree.zhihu.fragment.shouye

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import com.example.tree.zhihu.gson.ColumnOverview
import com.example.tree.zhihu.R
import com.example.tree.zhihu.adapters.ShouyeAttentionAdapter
import com.example.tree.zhihu.tool.BasePageFragment
import com.example.tree.zhihu.tool.GetConnected
import com.example.tree.zhihu.tool.LocalCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.shouye_attention_fragment.*

import java.io.IOException
import java.util.ArrayList

class AttentionFragment : BasePageFragment() {

    internal var columnOverview: ColumnOverview? = null
    internal var bitmaps: MutableList<Bitmap> = ArrayList()
    internal var bitmap: Bitmap? = null
    private var connectivityManager: ConnectivityManager? = null//用于判断是否有网络
    private var isLoadLocalData: Boolean? = false  //判断是否恢复了本地数据
    private var info: NetworkInfo? = null


    private var responseData: String? = null
    private var columnUrl = "https://news-at.zhihu.com/api/3/sections"
    private var num: Int = 0
    internal val UPDATE_TITLE = 1
    internal val UPDATE_IMAGE = 2
    internal val SHOW_PROGRESS_BAR = 3
    internal val HIDE_PROGRESS_BAR = 4
    internal val INTERNET = 5
    private val READ_EXTERNAL = 6
    private val WRITE_EXTERNAL = 7


    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_TITLE -> if (columnOverview != null) {
                    attention_recyclerView.adapter = ShouyeAttentionAdapter(activity!!, columnOverview!!, bitmaps)
                    attention_progress.visibility = View.GONE
                    getTlitleImage()
                }
                UPDATE_IMAGE -> attention_recyclerView.adapter!!.notifyItemChanged(msg.arg1)
                SHOW_PROGRESS_BAR -> attention_progress.visibility = View.VISIBLE
                HIDE_PROGRESS_BAR -> attention_progress.visibility = View.GONE
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shouye_attention_fragment, container, false)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化
        init(view)
    }

    override fun fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            if ((!isLoadLocalData!!)!!) {
                localDataLoading()
                Toast.makeText(activity, "请检查网络设置", Toast.LENGTH_SHORT)
            }
        } else {              //当前有已激活的网络连接
            if (this.isDataInitiated == false) {
                getColumnInformation()
            }
        }

    }

    //初始化
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun init(view: View) {
        connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager//获取当前网络的连接服务
        info = connectivityManager!!.activeNetworkInfo //获取活动的网络连接信息
        //配置recyclerView
        attention_recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    //取得栏目信息
    fun getColumnInformation() {
        Thread(Runnable {
            val messageShow = Message()
            messageShow.what = SHOW_PROGRESS_BAR
            handler.sendMessage(messageShow)
            val gson = Gson()
            responseData = GetConnected.sendRequeestWithOkhttp(columnUrl)
            //保存获得的数据
            if (responseData != null) {
                try {
                    LocalCache.saveJsonData(activity, columnUrl, responseData)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            columnOverview = gson.fromJson<ColumnOverview>(responseData, object : TypeToken<ColumnOverview>() {

            }.type)
            val message = Message()
            message.what = UPDATE_TITLE
            handler.sendMessage(message)
        }).start()
    }

    //取得栏目信息之后取得每个栏目的图片逐一取得每个栏目的图片
    fun getTlitleImage() {
        Thread(Runnable {
            for (i in 0 until columnOverview!!.data.size) {
                val thread = Thread(Runnable {
                    bitmap = GetConnected.getImage(columnOverview!!.data[i].thumbnail)
                    bitmap?.let {
                        bitmaps.add(it)
                    }
                    if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        num = i
                        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL)
                    } else {
                        LocalCache.setLocalImageCache(columnOverview!!.data[i].thumbnail, bitmap)
                    }
                    val message = Message()
                    message.what = UPDATE_IMAGE
                    message.arg1 = i
                    handler.sendMessage(message)
                })
                thread.start()
                try {
                    thread.join()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }).start()

    }

    //本地数据已存在恢复
    fun localDataLoading() {
        responseData = LocalCache.readJsonData(activity, columnUrl)
        val gson = Gson()
        columnOverview = gson.fromJson<ColumnOverview>(responseData, object : TypeToken<ColumnOverview>() {

        }.type)
        isLoadLocalData = true
        if (columnOverview != null) {
            attention_recyclerView.adapter = ShouyeAttentionAdapter(activity!!, columnOverview!!, bitmaps)
            attention_progress.visibility = View.GONE
            for (i in 0 until columnOverview!!.data.size) {
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    num = i
                    ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL)
                } else {
                    bitmaps.add(LocalCache.getLocalImageCache(columnOverview!!.data[i].thumbnail))
                }
                attention_recyclerView.adapter!!.notifyItemChanged(i)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocalCache.setLocalImageCache(columnOverview!!.data[num].thumbnail, bitmap)
            } else {
                Toast.makeText(activity, "很遗憾，你没有允许权限请求", Toast.LENGTH_SHORT)
            }
            READ_EXTERNAL -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bitmaps.add(LocalCache.getLocalImageCache(columnOverview!!.data[num].thumbnail))
            } else {
                Toast.makeText(activity, "很遗憾，你没有允许权限请求", Toast.LENGTH_SHORT)
            }
        }
    }
}

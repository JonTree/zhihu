package com.example.tree.zhihu.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView

import com.example.tree.zhihu.R
import com.example.tree.zhihu.fragment.IdearFragment
import com.example.tree.zhihu.fragment.WebContentFragment
import com.example.tree.zhihu.tool.StatusBarUtil
import kotlinx.android.synthetic.main.activity_web_content.*
import kotlinx.android.synthetic.main.part_activity_bottom.*

import java.util.ArrayList

class WebContentActivity : AppCompatActivity(), View.OnClickListener {



    private lateinit var webContentFragment: WebContentFragment
    private lateinit var idearFragment: IdearFragment
    private lateinit var manager: FragmentManager


    internal var fragmentsList: MutableList<Fragment> = ArrayList()
    var id: Int = 0
        internal set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_content)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }
        val intent = intent
        id = intent.getIntExtra("id", -1)
        webContentFragment = WebContentFragment()
        idearFragment = IdearFragment()
        idearFragment.setId("" + id)
        manager = supportFragmentManager  //获取FragmentManager对象
        fragmentsList.add(webContentFragment)
        fragmentsList.add(idearFragment)
        web_view_pager.adapter = Myadapter(manager)
        id_bu_shouye_img.setOnClickListener(this)
        id_bu_idear_img.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_bu_shouye_img -> web_view_pager.currentItem = 0
            R.id.id_bu_idear_img -> web_view_pager.currentItem = 1
        }
    }

    fun SetIdearFragment(s: String) {
        idearFragment.setId(s)
        web_view_pager.currentItem = 1
    }

    fun getFragmentsList(): List<Fragment> {
        return fragmentsList
    }

    //ViewPager适配器
    internal inner class Myadapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return super.instantiateItem(container, position)

        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getCount(): Int {
            return fragmentsList.size
        }
    }
}

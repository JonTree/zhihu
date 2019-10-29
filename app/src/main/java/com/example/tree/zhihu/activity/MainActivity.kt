package com.example.tree.zhihu.activity

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.example.tree.zhihu.R
import com.example.tree.zhihu.fragment.IdearFragment
import com.example.tree.zhihu.fragment.ShouyeFragment
import com.example.tree.zhihu.tool.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.part_activity_bottom.*

import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var viewPager: ViewPager

    private lateinit var shouyeFragment: ShouyeFragment
    lateinit var idearFragment: IdearFragment
    private lateinit var viewPagerLayoutParams: FrameLayout.LayoutParams
    private lateinit var manager: FragmentManager


    internal var fragmentsList: MutableList<Fragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        //初始化按钮以及文字控件
        init()
        //默认选择第一个
        viewPager.currentItem = 0
    }


    @SuppressLint("ResourceType")
    private fun init() {
        viewPager = ViewPager(this@MainActivity)
        viewPagerLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        viewPager.offscreenPageLimit = 2
        viewPager.id = 12138
        shouyeFragment = ShouyeFragment()
        idearFragment = IdearFragment()
        fragmentsList.add(shouyeFragment)
        fragmentsList.add(idearFragment)
        manager = supportFragmentManager  //获取FragmentManager对象
        id_content.addView(viewPager, viewPagerLayoutParams)
        viewPager.adapter = Myadapter(manager)
        id_bu_shouye_img.setOnClickListener(this)
        id_bu_idear_img.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_bu_shouye_img -> viewPager.currentItem = 0
            R.id.id_bu_idear_img -> viewPager.currentItem = 1
        }
    }

    fun getFragmentsList(): List<Fragment> {
        return fragmentsList
    }

    //ViewPager适配器
    internal inner class Myadapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return super.instantiateItem(container, position)

        }

        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getCount(): Int {
            return fragmentsList.size
        }
    }
}

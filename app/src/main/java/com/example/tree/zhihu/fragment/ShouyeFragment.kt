package com.example.tree.zhihu.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.example.tree.zhihu.gson.News
import com.example.tree.zhihu.R
import com.example.tree.zhihu.fragment.shouye.AttentionFragment
import com.example.tree.zhihu.fragment.shouye.HotListFragment
import com.example.tree.zhihu.fragment.shouye.RecommendFragment
import com.example.tree.zhihu.tool.BasePageFragment

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class ShouyeFragment : BasePageFragment(), View.OnClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var historicalYear: EditText
    private lateinit var historicalMonth: EditText
    private lateinit var historicalDay: EditText
    private lateinit var te_ask: TextView
    private lateinit var imageView: ImageView
    private lateinit var mLinearLayout: LinearLayout


    private lateinit var historicalDate: StringBuilder

    internal var fragmentsList: MutableList<Fragment> = ArrayList()
    private var dateTodayIntegers = todaySDate
    private lateinit var tabLayout: TabLayout


    private val titles = arrayOf("话题", "News", "热榜")

    val todaySDate: List<Int>
        get() {
            val dateIntegers = ArrayList<Int>()
            val date = Date()
            var format = SimpleDateFormat("yyyy")
            dateIntegers.add(Integer.parseInt(format.format(date)))
            format = SimpleDateFormat("MM")
            dateIntegers.add(Integer.parseInt(format.format(date)))
            format = SimpleDateFormat("dd")
            dateIntegers.add(Integer.parseInt(format.format(date)))
            return dateIntegers
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab_shouye_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化
        initList(view)
    }

    //初始化首页内的Fragmnet并添到列表中
    private fun initList(view: View) {
        val onKeyListener = View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                queryButtonClickLogic()    //查询按钮点击逻辑
                return@OnKeyListener true
            }
            false
        }
        //找到底部tab的布局
        mLinearLayout = activity!!.findViewById<View>(R.id.bottom_tabs) as LinearLayout
        imageView = view.findViewById<View>(R.id.id_picture_ask) as ImageView
        historicalDay = view.findViewById<View>(R.id.historical_day) as EditText
        historicalDay.setOnClickListener(this)
        historicalDay.setOnKeyListener(onKeyListener)
        historicalMonth = view.findViewById<View>(R.id.historical_month) as EditText
        historicalMonth.setOnClickListener(this)
        historicalMonth.setOnKeyListener(onKeyListener)
        historicalYear = view.findViewById<View>(R.id.historical_year) as EditText
        historicalYear.setOnClickListener(this)
        historicalYear.setOnKeyListener(onKeyListener)
        historicalYear.hint = "" + dateTodayIntegers[0]
        historicalMonth.hint = "" + dateTodayIntegers[1]
        historicalDay.hint = "" + dateTodayIntegers[2]
        //查询的按钮
        te_ask = view.findViewById<View>(R.id.shouye_ask_textview) as TextView
        te_ask.setTextColor(0x0000000)
        te_ask.setOnClickListener(this)
        imageView.setOnClickListener(this)

        val attentionFragment = AttentionFragment()
        val recommendFragment = RecommendFragment()
        val hotListFragment = HotListFragment()

        fragmentsList.add(attentionFragment)
        fragmentsList.add(recommendFragment)
        fragmentsList.add(hotListFragment)
        //找到ViewPager的实例，并设置适配器
        viewPager = view.findViewById<View>(R.id.shouye_viewpager) as ViewPager
        //获取当下Fragment的子Fragment管理器，并绑定至ViePager适配器
        val myadapter = Myadapter(childFragmentManager)
        //设置ViewPager的适配器
        viewPager.adapter = myadapter
        viewPager.offscreenPageLimit = 3

        //设置tablayout
        tabLayout = view.findViewById<View>(R.id.tablayout_top) as TabLayout
        //Tab的样式
        tabLayout.tabMode = TabLayout.MODE_FIXED
        //联动
        tabLayout.setupWithViewPager(viewPager)
        //默认选择第二个
        tabLayout.getTabAt(1)!!.select()
    }

    //纠正用户输入格式
    fun correctMonthAndDateFormat(number: Int): String {
        var s: String? = null
        if (number < 10 && number >= 0) {
            s = "0$number"
            return s
        }
        return "" + number
    }

    //输入日期加一天得到真正Url后缀
    fun getTheRealUrlSuffix(historicalDate: StringBuilder): String {
        val format = SimpleDateFormat("yyyyMMdd")
        var date: Date? = null
        try {
            date = format.parse(historicalDate.toString())
            val c = GregorianCalendar()
            c.time = date
            c.add(Calendar.DATE, 1)
            date = c.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return format.format(date)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.shouye_ask_textview -> queryButtonClickLogic()    //查询按钮点击逻辑
            R.id.id_picture_ask -> queryButtonClickLogic()    //查询按钮点击逻辑
            R.id.historical_year -> historicalYear.text.clear()
            R.id.historical_month -> historicalMonth.text.clear()
            R.id.historical_day -> historicalDay.text.clear()
        }
    }

    //查询按钮点击逻辑
    fun queryButtonClickLogic() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
        //如果用户没有输入年   月  帮他填充当前年月，若没有设置日期则提醒
        historicalYear.text.toString().isEmpty()
        if (historicalYear.text.toString().isEmpty()) {
            historicalYear.setText("" + dateTodayIntegers[0])
        }
        if (historicalMonth.text.toString().isEmpty()) {
            historicalMonth.setText("" + dateTodayIntegers[1])
        }
        if (historicalDay.text.toString().isEmpty()) {
            historicalDay.setText("" + dateTodayIntegers[2])
        }
        //重置historicalDate
        historicalDate = StringBuilder()
        val year = Integer.parseInt(historicalYear.text.toString())
        val month = Integer.parseInt(historicalMonth.text.toString())
        val day = Integer.parseInt(historicalDay.text.toString())
        historicalDate.append(historicalYear.text.toString())
                .append(correctMonthAndDateFormat(month))
                .append(correctMonthAndDateFormat(day))

        if (!isValidDate(historicalDate.toString())) {
            Toast.makeText(activity, "请检查日期是否合法哦", Toast.LENGTH_SHORT).show()
        } else if (year < 2013 || year == 2013 && month < 5 || year == 2013 && month == 5 && day < 19) {
            Toast.makeText(activity, "输入日期过早，知乎还没出生呢", Toast.LENGTH_SHORT).show()
        } else if (year > dateTodayIntegers[0]
                || year == dateTodayIntegers[0] && month > dateTodayIntegers[1]
                || year == dateTodayIntegers[0] && month == dateTodayIntegers[1] && day > dateTodayIntegers[2]) {
            Toast.makeText(activity, "未来的事人家才不知道呢", Toast.LENGTH_SHORT).show()
        } else {
            (childFragmentManager.fragments[1] as RecommendFragment).setPastNewsUrl(News.getNewsUrl(getTheRealUrlSuffix(historicalDate)))
            tabLayout.getTabAt(1)!!.select()
        }
    }

    override fun fetchData() {
        mLinearLayout.visibility = View.GONE
    }

    //ViewPager适配器
    internal inner class Myadapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getCount(): Int {
            return fragmentsList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    companion object {

        fun isValidDate(str: String): Boolean {
            var convertSuccess = true
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
            val format = SimpleDateFormat("yyyyMMdd")
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.isLenient = false
                format.parse(str)
            } catch (e: ParseException) {
                // e.printStackTrace();
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                convertSuccess = false
            }

            return convertSuccess
        }
    }


}

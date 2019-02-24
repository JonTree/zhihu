package com.example.tree.zhihu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.fragment.shouye.AttentionFragment;
import com.example.tree.zhihu.fragment.shouye.HotListFragment;
import com.example.tree.zhihu.fragment.shouye.RecommendFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ShouyeFragment extends Fragment implements View.OnClickListener{

    ViewPager viewPager;
    EditText historicalYear;
    EditText historicalMonth;
    EditText historicalDay;
    TextView te_ask;

    StringBuilder historicalDate;

    List<Fragment> fragmentsList = new ArrayList<>();
    List<Integer> dateTodayIntegers = getTodaySDate();
    TabLayout tabLayout;


    private String[] titles = {"话题", "News", "热榜"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_shouye_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        initList(view);
    }

    //初始化首页内的Fragmnet并添到列表中
    private void initList(View view) {
        View.OnKeyListener onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    queryButtonClickLogic();    //查询按钮点击逻辑
                    return true;
                }
                return false;
            }
        };
        historicalDay = (EditText) view.findViewById(R.id.historical_day);
        historicalDay.setOnClickListener(this);
        historicalDay.setOnKeyListener(onKeyListener);
        historicalMonth = (EditText) view.findViewById(R.id.historical_month);
        historicalMonth.setOnClickListener(this);
        historicalMonth.setOnKeyListener(onKeyListener);
        historicalYear = (EditText) view.findViewById(R.id.historical_year);
        historicalYear.setOnClickListener(this);
        historicalYear.setOnKeyListener(onKeyListener);
        historicalYear.setHint("" + dateTodayIntegers.get(0));
        historicalMonth.setHint("" + dateTodayIntegers.get(1));
        historicalDay.setHint("" + dateTodayIntegers.get(2));
        //查询的按钮
        te_ask = (TextView) view.findViewById(R.id.shouye_ask_textview);
        te_ask.setOnClickListener(this);

        AttentionFragment attentionFragment = new AttentionFragment();
        RecommendFragment recommendFragment = new RecommendFragment();
        HotListFragment hotListFragment = new HotListFragment();

        fragmentsList.add(attentionFragment);
        fragmentsList.add(recommendFragment);
        fragmentsList.add(hotListFragment);
        //找到ViewPager的实例，并设置适配器
        viewPager = (ViewPager) view.findViewById(R.id.shouye_viewpager);
        //获取当下Fragment的子Fragment管理器，并绑定至ViePager适配器
        Myadapter myadapter = new Myadapter(getChildFragmentManager());
        //设置ViewPager的适配器
        viewPager.setAdapter(myadapter);
        viewPager.setOffscreenPageLimit(3);

        //设置tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_top);
        //Tab的样式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //联动
        tabLayout.setupWithViewPager(viewPager);
        //默认选择第二个
        tabLayout.getTabAt(1).select();
    }

    public List<Integer> getTodaySDate() {
        List<Integer> dateIntegers = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        dateIntegers.add(Integer.parseInt(format.format(date)));
        format = new SimpleDateFormat("MM");
        dateIntegers.add(Integer.parseInt(format.format(date)));
        format = new SimpleDateFormat("dd");
        dateIntegers.add(Integer.parseInt(format.format(date)));
        return dateIntegers;
    }

    //纠正用户输入格式
    public String correctMonthAndDateFormat(int number) {
        String s = null;
        if (number < 10 && number >= 0) {
            s = "0" + number;
            return s;
        }
        return "" + number;
    }

    //输入日期加一天得到真正Url后缀
    public String getTheRealUrlSuffix(StringBuilder historicalDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(historicalDate.toString());
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shouye_ask_textview:
                queryButtonClickLogic();    //查询按钮点击逻辑
                break;
            case R.id.historical_year:
                historicalYear.getText().clear();
                break;
            case R.id.historical_month:
                historicalMonth.getText().clear();
                break;
            case R.id.historical_day:
                historicalDay.getText().clear();
                break;
        }
    }

    //查询按钮点击逻辑
    public void queryButtonClickLogic() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        //如果用户没有输入年   月  帮他填充当前年月，若没有设置日期则提醒
        historicalYear.getText().toString().isEmpty();
        if (historicalYear.getText().toString().isEmpty()) {
            historicalYear.setText("" + dateTodayIntegers.get(0));
        }
        if (historicalMonth.getText().toString().isEmpty()) {
            historicalMonth.setText(""+dateTodayIntegers.get(1));
        }
        if (historicalDay.getText().toString().isEmpty()) {
            historicalDay.setText(""+dateTodayIntegers.get(2));
        }
        //重置historicalDate
        historicalDate = new StringBuilder();
        int year = Integer.parseInt(historicalYear.getText().toString());
        int month = Integer.parseInt(historicalMonth.getText().toString());
        int day = Integer.parseInt(historicalDay.getText().toString());
        historicalDate.append(historicalYear.getText().toString())
                .append(correctMonthAndDateFormat(month))
                .append(correctMonthAndDateFormat(day));

        if (!isValidDate(historicalDate.toString())) {
            Toast.makeText(getActivity(), "请检查日期是否合法哦", Toast.LENGTH_SHORT).show();
        } else if (year < 2013 || (year == 2013 && month < 5) || (year == 2013 && month == 5 && day < 19)) {
            Toast.makeText(getActivity(), "输入日期过早，知乎还没出生呢", Toast.LENGTH_SHORT).show();
        } else if (year > dateTodayIntegers.get(0)
                || (year == dateTodayIntegers.get(0) && month > dateTodayIntegers.get(1))
                || (year == dateTodayIntegers.get(0) && month == dateTodayIntegers.get(1) && day > dateTodayIntegers.get(2))) {
            Toast.makeText(getActivity(), "未来的事人家才不知道呢", Toast.LENGTH_SHORT).show();
        } else {
            ((RecommendFragment)getChildFragmentManager().getFragments().get(1)).setPastNewsUrl(News.getNewsUrl(getTheRealUrlSuffix(historicalDate)));
            tabLayout.getTabAt(1).select();
        }
    }

    //ViewPager适配器
    class Myadapter extends FragmentPagerAdapter {


        Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}

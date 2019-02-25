package com.example.tree.zhihu.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tree.zhihu.R;
import com.example.tree.zhihu.fragment.IdearFragment;
import com.example.tree.zhihu.fragment.WebContentFragment;
import com.example.tree.zhihu.tool.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class WebContentActivity extends AppCompatActivity implements View.OnClickListener{


    ViewPager viewPager;
    ImageButton bu_shouye;
    ImageButton bu_idear;
    TextView shouye;
    TextView idear;

    WebContentFragment webContentFragment;
    IdearFragment idearFragment;
    FragmentManager manager;


    List<Fragment> fragmentsList = new ArrayList<>();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        webContentFragment = new WebContentFragment();
        idearFragment = new IdearFragment();
        idearFragment.setId(""+id);
        manager = getSupportFragmentManager();  //获取FragmentManager对象
        fragmentsList.add(webContentFragment);
        fragmentsList.add(idearFragment);
        viewPager = (ViewPager) findViewById(R.id.web_view_pager);
        viewPager.setAdapter(new Myadapter(manager));
        shouye = (TextView) findViewById(R.id.shouye);
        idear = (TextView) findViewById(R.id.idear);
        bu_shouye = (ImageButton) findViewById(R.id.id_bu_shouye_img);
        bu_idear = (ImageButton) findViewById(R.id.id_bu_idear_img);
        bu_shouye.setOnClickListener(this);
        bu_idear.setOnClickListener(this);
    }

    public int getId() {
        return id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bu_shouye_img:
                viewPager.setCurrentItem(0);
                break;
            case R.id.id_bu_idear_img:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
    public void SetIdearFragment(String s) {
        idearFragment.setId(s);
        viewPager.setCurrentItem(1);
    }

    public List<Fragment> getFragmentsList() {
        return fragmentsList;
    }

    //ViewPager适配器
    class Myadapter extends FragmentStatePagerAdapter {


        Myadapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return super.instantiateItem(container, position);

        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }
    }
}

package com.example.tree.zhihu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tree.zhihu.PutQuestionActivity;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.fragment.shouyeFrament.AttentionFragment;
import com.example.tree.zhihu.fragment.shouyeFrament.HotListFragment;
import com.example.tree.zhihu.fragment.shouyeFrament.RecommendFragment;
import com.example.tree.zhihu.fragment.shouyeFrament.VideoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ShouyeFragment extends Fragment {

    ViewPager viewPager;
    List<Fragment> fragmentsList = new ArrayList<>();
    TabLayout tabLayout;
    TextView te_ask;
    private String[] titles = {"关注","推荐","热榜","视频"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_shouye_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化List
        initList();
        //找到ViewPager的实例，并设置适配器
        viewPager = (ViewPager)view.findViewById(R.id.shouye_viewpager);

        Myadapter myadapter = new Myadapter(getChildFragmentManager());
        viewPager.setAdapter(myadapter);
        //设置tablayout
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout_top);
        //样式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //联动
        tabLayout.setupWithViewPager(viewPager);
        //提问的按钮
        te_ask = (TextView) view.findViewById(R.id.shouye_ask_textview);
        te_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PutQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    //初始化首页内的Fragmnet并添到列表中
    private void initList() {
        AttentionFragment attentionFragment = new AttentionFragment();
        RecommendFragment recommendFragment = new RecommendFragment();
        HotListFragment hotListFragment = new HotListFragment();
        VideoFragment videoFragment = new VideoFragment();


        fragmentsList.add(attentionFragment);
        fragmentsList.add(recommendFragment);
        fragmentsList.add(hotListFragment);
        fragmentsList.add(videoFragment);
    }


    //ViewPager适配器
    class Myadapter extends FragmentPagerAdapter {


        public Myadapter(FragmentManager fm) {
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

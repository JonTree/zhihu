package com.example.tree.zhihu.fragment.shouyeFrament;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tree.zhihu.R;
import com.example.tree.zhihu.adapters.ShouyeAttentionAdapter;
import com.example.tree.zhihu.toolClass.AnimationUtil;

public class AttentionFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout mLinearLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shouye_attention_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.bottom_tabs);
        //配置recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.attention_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ShouyeAttentionAdapter(getActivity()));
        //配置关注页面的recyclerView的滑动的事件
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && location(dy)) {
                    mLinearLayout.setVisibility(View.GONE);
//                    mLinearLayout.setAnimation(AnimationUtil.moveToViewBottom());
                }
                if (dy < 0 && location(dy)) {
                    mLinearLayout.setVisibility(View.VISIBLE);
//                    mLinearLayout.setAnimation(AnimationUtil.moveToViewLocation());
                }


            }
        });
    }

    //优化的滑动判定
    public boolean location(int n) {
        int i;
        if (n > 0) i = n;
        else i = -n;
        if (i > 10) return true;
        return false;
    }
}

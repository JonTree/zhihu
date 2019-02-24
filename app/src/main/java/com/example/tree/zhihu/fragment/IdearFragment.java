package com.example.tree.zhihu.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tree.zhihu.gson.LongComment;
import com.example.tree.zhihu.gson.ShortComment;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.adapters.IdearAdapter;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IdearFragment extends BasePageFragment {

    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    AppBarLayout appBarLayout;
    LinearLayout mLinearLayout;
    LinearLayout idear_no_background;


    LongComment longComment;
    ShortComment shortComment;
    List<Bitmap> bitmaps = new ArrayList<>();

    String responseData;
    String id = null;
    String mId = "啦啦啦啦"; //防止重复加载
    final int UPDATA_COMMENT = 1;
    final int UPDATA_IMAGE = 2;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_COMMENT:
                    if (shortComment.getComments().size() + longComment.getComments().size() > 0)
                        idear_no_background.setVisibility(View.GONE);
                    else idear_no_background.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new IdearAdapter(getActivity(), shortComment, longComment, bitmaps));
                    break;
                case UPDATA_IMAGE:
                    recyclerView.getAdapter().notifyItemChanged(msg.arg1);
                    break;

            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_idear_fragment, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idear_no_background = (LinearLayout) view.findViewById(R.id.tab_idear_no_background);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_tabs);
        //顶部导航栏
        appBarLayout = (AppBarLayout) view.findViewById(R.id.idear_top);
        //设置recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.idear_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置NestedScrollView
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.idear_nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int mScrollX = px2dp(getActivity(), scrollX);
                int mScrollY = px2dp(getActivity(), scrollY);
                int mOldScrollX = px2dp(getActivity(), oldScrollX);
                int mOldScrollY = px2dp(getActivity(), oldScrollY);
                if (mOldScrollY >= 70) {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                if (mOldScrollY < 70) {
                    appBarLayout.setVisibility(View.GONE);
                }
                if ((scrollY - oldScrollY) > 10) {
                    mLinearLayout.setVisibility(View.GONE);
                } else if ((oldScrollY - scrollY) > 20) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void fetchData() {
        if (id != null | id == mId) {
            mId = id;
            getComment();
        }
    }

    public void getComment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //长评论获取
                Gson gson = new Gson();
                responseData = GetConnected.sendRequeestWithOkhttp(LongComment.getLongCommentUrl(id));
                longComment = gson.fromJson(responseData, new TypeToken<LongComment>() {
                }.getType());
                responseData = GetConnected.sendRequeestWithOkhttp(ShortComment.getShortCommentUrl(id));
                shortComment = gson.fromJson(responseData, new TypeToken<ShortComment>() {
                }.getType());
                if (longComment != null & shortComment !=null){
                    Message message = new Message();
                    message.what = UPDATA_COMMENT;
                    handler.sendMessage(message);
                    for (int i = 0; i < longComment.getComments().size() + shortComment.getComments().size(); i++) {
                        if (i < longComment.getComments().size()) {
                            bitmaps.add(GetConnected.getImage(longComment.getComments().get(i).getAvatar()));
                            Message message1 = new Message();
                            message1.what = UPDATA_IMAGE;
                            message1.arg1 = i;
                            handler.sendMessage(message1);
                        } else {
                            int mI = i - longComment.getComments().size();
                            bitmaps.add(GetConnected.getImage(shortComment.getComments().get(mI).getAvatar()));
                            Message message1 = new Message();
                            message1.what = UPDATA_IMAGE;
                            message1.arg1 = i;
                            handler.sendMessage(message1);
                        }
                    }
                }

            }
        }).start();
    }

    public void setId(String id) {
        this.id = id;
        fetchData();
    }


    public String getMId() {
        return id;
    }

    //将dp转换为px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //将px转换为dp
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}


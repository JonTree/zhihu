package com.example.tree.zhihu.fragment;

import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tree.zhihu.activity.ColumnMessageActivity;
import com.example.tree.zhihu.gson.ColumnMessage;
import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.adapters.ColumnMessgeAdapter;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.example.tree.zhihu.tool.LocalCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColumnMessageFragment extends BasePageFragment {

    RecyclerView recyclerView;
    TextView te_title;
    LinearLayout mLinearLayout;
    NestedScrollView nestedScrollView;
    LinearLayout column_no_background;



    ColumnMessage columnMessage;

    List<NewsExtra> newsExtras = new ArrayList<>();
    List<Bitmap> bitmaps = new ArrayList<>();
    String responseData;
    final int UPDATE_TITLE = 1;
    final int UPDATA_EXTRA_BITMAP = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TITLE:
                    te_title.setText(columnMessage.getName());
                    recyclerView.setAdapter(new ColumnMessgeAdapter(getActivity(), columnMessage, newsExtras, bitmaps));
                    column_no_background.setVisibility(View.GONE);
                    break;
                case UPDATA_EXTRA_BITMAP:
                    recyclerView.getAdapter().notifyItemChanged(msg.arg1);
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.column_message_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        column_no_background = (LinearLayout) view.findViewById(R.id.tab_column_no_background);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout)getActivity().findViewById(R.id.bottom_tabs);
        te_title = (TextView) view.findViewById(R.id.column_message_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.column_message_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void fetchData() {
        mLinearLayout.setVisibility(View.GONE);
        if (this.isDataInitiated!=true) getColumnMessage();
    }

    public void getColumnMessage() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                responseData = GetConnected.sendRequeestWithOkhttp(getDateUrl());
                Gson gson = new Gson();
                columnMessage = gson.fromJson(responseData, new TypeToken<ColumnMessage>() {
                }.getType());
                if (columnMessage != null) {
                    for (int i = 0; i < columnMessage.getStories().size(); i++) {
                        try {
                            columnMessage.getStories().get(i).setDate(correctDateFormat(columnMessage.getStories().get(i).getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    message.what = UPDATE_TITLE;
                    handler.sendMessage(message);
                    for (int i = 0; i < columnMessage.getStories().size(); i++) {
                        responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(columnMessage.getStories().get(i).getId()));
                        //保存Json数据
                        try {
                            LocalCache.saveJsonData(getActivity(), News.getNewsExtraUrl(columnMessage.getStories().get(i).getId()), responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        newsExtras.add((NewsExtra) gson.fromJson(responseData, new TypeToken<NewsExtra>() {
                        }.getType()));
                        bitmaps.add(GetConnected.getImage(columnMessage.getStories().get(i).getImages().get(0)));
                        Message messageExtraBimap = new Message();
                        messageExtraBimap.what = UPDATA_EXTRA_BITMAP;
                        messageExtraBimap.arg1 = i;
                        handler.sendMessage(messageExtraBimap);
                    }
                }

            }
        }).start();
    }


    //取得数据
    public String getDateUrl() {
        String s = null;
        int id = ((ColumnMessageActivity) getActivity()).getId();
        s = "https://news-at.zhihu.com/api/3/section/" + id;
        return s;
    }

    //纠正日期格式
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String correctDateFormat(String string) throws ParseException {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        Date date = s.parse(string);
        s = new SimpleDateFormat("yyyy/MM/dd");
        return s.format(date);
    }
}

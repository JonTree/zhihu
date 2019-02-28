package com.example.tree.zhihu.fragment.shouye;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.gson.NewsHotList;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.adapters.ShouyeHotListAdapter;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.example.tree.zhihu.tool.LocalCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotListFragment extends BasePageFragment {
    RecyclerView recyclerView;
    LinearLayout mLinearLayout;
    ProgressBar progressBar;


    ShouyeHotListAdapter shouyeHotListAdapter;
    NewsHotList newsHotList;
    private ConnectivityManager connectivityManager;//用于判断是否有网络
    NetworkInfo info;
    List<Bitmap> bitmaps = new ArrayList<>();
    List<NewsExtra> newsExtras = new ArrayList<>();


    String hotNewsURL = "https://news-at.zhihu.com/api/3/news/hot";
    String mHotNewsUrl = "lalalalalaala";
    String responseData;

    final int UPDATE_TITLE = 1;
    final int UPDATE_IMAGE_EXTRA = 2;
    final int SHOW_PROGRESS_BAR =3;
    final int HIDE_PROGRESS_BAR =4;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TITLE:
                    shouyeHotListAdapter = new ShouyeHotListAdapter(getActivity(),newsHotList,bitmaps,newsExtras);
                    recyclerView.setAdapter(shouyeHotListAdapter);
                    getHotListExtra();
                    break;
                case UPDATE_IMAGE_EXTRA:
                    shouyeHotListAdapter.notifyItemChanged(msg.arg1);
                    break;
                case SHOW_PROGRESS_BAR:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case HIDE_PROGRESS_BAR:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shouye_hot_list_fragment,container,false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        init(view);
        loadLocalData();
    }

    @Override
    public void fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT);

        } else {              //当前有已激活的网络连接
            if (this.isDataInitiated == false) {
                getHotListNews();
                mHotNewsUrl = hotNewsURL;
            }
        }
    }

    //初始化
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(View view) {
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        progressBar = (ProgressBar) view.findViewById(R.id.hot_list_progress);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.bottom_tabs);
        recyclerView = (RecyclerView) view.findViewById(R.id.hot_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //获取热门消息
    public void getHotListNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message messageShow = new Message();
                messageShow.what= SHOW_PROGRESS_BAR;
                handler.sendMessage(messageShow);
                responseData = GetConnected.sendRequeestWithOkhttp(hotNewsURL);
                Gson gson = new Gson();
                newsHotList = gson.fromJson(responseData, new TypeToken<NewsHotList>() {
                }.getType());
                if (newsHotList!=null){
                    try {
                        LocalCache.saveJsonData(getActivity(), hotNewsURL, responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = UPDATE_TITLE;
                    handler.sendMessage(message);
                }else if (newsHotList==null){
                    Message messageHide = new Message();
                    messageHide.what = HIDE_PROGRESS_BAR;
                    handler.sendMessage(messageHide);
                }
            }
        }).start();
    }

    /*获取热榜消息额外消息
    * 嵌套线程
    * */
    public void getHotListExtra(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        for (int i = 0; i < newsHotList.getRecent().size(); i++) {
                            responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(newsHotList.getRecent().get(i).getNews_id()));
                            try {
                                LocalCache.saveJsonData(getActivity(),News.getNewsExtraUrl(newsHotList.getRecent().get(i).getNews_id()),responseData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            newsExtras.add((NewsExtra) gson.fromJson(responseData, new TypeToken<NewsExtra>() {
                            }.getType()));
                            Bitmap bitmap = GetConnected.getImage(newsHotList.getRecent().get(i).getThumbnail());
                            bitmaps.add(bitmap);
                            LocalCache.setLocalImageCache(newsHotList.getRecent().get(i).getThumbnail(),bitmap);
                            Message message = new Message();
                            message.what = UPDATE_IMAGE_EXTRA;
                            message.arg1 = i;
                            handler.sendMessage(message);
                        }
                        Message messageHide = new Message();
                        messageHide.what = HIDE_PROGRESS_BAR;
                        handler.sendMessage(messageHide);
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void loadLocalData() {
        if (!LocalCache.readJsonData(getActivity(), hotNewsURL).isEmpty()){
            responseData = LocalCache.readJsonData(getActivity(), hotNewsURL);
            Gson gson = new Gson();
            newsHotList = gson.fromJson(responseData, new TypeToken<NewsHotList>() {
            }.getType());
            shouyeHotListAdapter = new ShouyeHotListAdapter(getActivity(),newsHotList,bitmaps,newsExtras);
            recyclerView.setAdapter(shouyeHotListAdapter);
            for (int i = 0; i < newsHotList.getRecent().size(); i++) {
                responseData = LocalCache.readJsonData(getActivity(), News.getNewsExtraUrl(newsHotList.getRecent().get(i).getNews_id()));
                newsExtras.add((NewsExtra) gson.fromJson(responseData, new TypeToken<NewsExtra>() {
                }.getType()));
                Bitmap bitmap = LocalCache.getLocalImageCache(newsHotList.getRecent().get(i).getThumbnail());
                bitmaps.add(bitmap);
                shouyeHotListAdapter.notifyItemChanged(i);
            }
        }
    }


}

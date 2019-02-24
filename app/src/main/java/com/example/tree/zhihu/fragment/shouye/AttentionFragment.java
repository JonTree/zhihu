package com.example.tree.zhihu.fragment.shouye;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tree.zhihu.gson.ColumnOverview;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.adapters.ShouyeAttentionAdapter;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.example.tree.zhihu.tool.LocalCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttentionFragment extends BasePageFragment {
    String TAG = "Attention";
    RecyclerView recyclerView;
    LinearLayout mLinearLayout;
    ProgressBar progressBar;

    ColumnOverview columnOverview;
    List<Bitmap> bitmaps = new ArrayList<>();
    Bitmap bitmap;
    private ConnectivityManager connectivityManager;//用于判断是否有网络
    Boolean isLoadLocalData = false;  //判断是否恢复了本地数据
    NetworkInfo info;


    String responseData;
    String columnUrl = "https://news-at.zhihu.com/api/3/sections";
    int num;
    final int UPDATE_TITLE = 1;
    final int UPDATE_IMAGE = 2;
    final int SHOW_PROGRESS_BAR = 3;
    final int HIDE_PROGRESS_BAR = 4;
    final int INTERNET = 5;
    final int READ_EXTERNAL = 6;
    final int WRITE_EXTERNAL = 7;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TITLE:
                    if (columnOverview != null) {
                        recyclerView.setAdapter(new ShouyeAttentionAdapter(getActivity(), columnOverview, bitmaps));
                        progressBar.setVisibility(View.GONE);
                        getTlitleImage();
                    }
                    break;
                case UPDATE_IMAGE:
                    recyclerView.getAdapter().notifyItemChanged(msg.arg1);
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
        View view = inflater.inflate(R.layout.shouye_attention_fragment, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        init(view);
    }

    @Override
    public void fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
            if(!isLoadLocalData){
                localDataLoading();
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT);
            }
        } else {              //当前有已激活的网络连接
            if (this.isDataInitiated == false){
                getColumnInformation();
            }
        }

    }

    //初始化
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(View view) {
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);//获取当前网络的连接服务
        info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        progressBar = (ProgressBar) view.findViewById(R.id.attention_progress);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.bottom_tabs);
        //配置recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.attention_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //取得栏目信息
    public void getColumnInformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message messageShow = new Message();
                messageShow.what = SHOW_PROGRESS_BAR;
                handler.sendMessage(messageShow);
                Gson gson = new Gson();
                responseData = GetConnected.sendRequeestWithOkhttp(columnUrl);
                //保存获得的数据
                if (responseData != null) {
                    try {
                        LocalCache.saveJsonData(getActivity(), columnUrl, responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                columnOverview = gson.fromJson(responseData, new TypeToken<ColumnOverview>() {
                }.getType());
                Message message = new Message();
                message.what = UPDATE_TITLE;
                handler.sendMessage(message);
            }
        }).start();
    }

    //取得栏目信息之后取得每个栏目的图片逐一取得每个栏目的图片
    public void getTlitleImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < columnOverview.getData().size(); i++) {
                    final int finalI = i;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap = GetConnected.getImage(columnOverview.getData().get(finalI).getThumbnail());
                            bitmaps.add(bitmap);
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                num = finalI;
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL);
                            } else {
                                LocalCache.setLocalImageCache(columnOverview.getData().get(finalI).getThumbnail(), bitmap);
                            }
                            Message message = new Message();
                            message.what = UPDATE_IMAGE;
                            message.arg1 = finalI;
                            handler.sendMessage(message);
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    //本地数据已存在恢复
    public void localDataLoading() {
        responseData = LocalCache.readJsonData(getActivity(), columnUrl);
        Gson gson = new Gson();
        columnOverview = gson.fromJson(responseData, new TypeToken<ColumnOverview>() {
        }.getType());
        isLoadLocalData = true;
        if (columnOverview != null) {
            recyclerView.setAdapter(new ShouyeAttentionAdapter(getActivity(), columnOverview, bitmaps));
            progressBar.setVisibility(View.GONE);
            for (int i = 0; i < columnOverview.getData().size(); i++) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    num = i;
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL);
                } else {
                    bitmaps.add(LocalCache.getLocalImageCache(columnOverview.getData().get(i).getThumbnail()));
                }
                recyclerView.getAdapter().notifyItemChanged(i);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocalCache.setLocalImageCache(columnOverview.getData().get(num).getThumbnail(), bitmap);
                } else {
                    Toast.makeText(getActivity(), "很遗憾，你没有允许权限请求", Toast.LENGTH_SHORT);
                }
                break;
            case READ_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bitmaps.add(LocalCache.getLocalImageCache(columnOverview.getData().get(num).getThumbnail()));
                } else {
                    Toast.makeText(getActivity(), "很遗憾，你没有允许权限请求", Toast.LENGTH_SHORT);
                }

        }
    }
}

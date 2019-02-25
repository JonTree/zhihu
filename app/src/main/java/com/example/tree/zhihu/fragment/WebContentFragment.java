package com.example.tree.zhihu.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.gson.NewsContent;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.activity.WebContentActivity;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WebContentFragment extends BasePageFragment {

    WebView webView;
    ImageView contentImageTitle;
    TextView contentDefaultTitle;
    TextView contentImageSource;
    LinearLayout mLinearLayout;
    LinearLayout web_no_background;


    String responseData;
    NewsContent newsContent;
    Bitmap bitmap = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            contentImageTitle.setImageBitmap(bitmap);
            contentDefaultTitle.setText(newsContent.getTitle());
            contentImageSource.setText(newsContent.getImage_source());
            webView.loadDataWithBaseURL(null, newsContent.getBody(), "txt/html", "utf-8", null);
            web_no_background.setVisibility(View.GONE);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_content_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) view.findViewById(R.id.content_web_view);
        web_no_background = (LinearLayout) view.findViewById(R.id.tab_web_no_background);
        mLinearLayout = (LinearLayout)getActivity().findViewById(R.id.bottom_tabs);
        contentImageTitle = (ImageView) view.findViewById(R.id.content_image_title);
        contentDefaultTitle = (TextView) view.findViewById(R.id.content_default_title);
        contentImageSource = (TextView) view.findViewById(R.id.content_image_source);
        setWebView();
    }

    @Override
    public void fetchData() {
        mLinearLayout.setVisibility(View.GONE);
        if (this.isDataInitiated == false) getData();
    }

    public void getData() {
        final int id = ((WebContentActivity)getActivity()).getId();
        if (id != -1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsContentUrl(id));
                    Gson gson = new Gson();
                    newsContent = gson.fromJson(responseData, new TypeToken<NewsContent>() {
                    }.getType());
                    if (newsContent != null){
                        bitmap = GetConnected.getImage(newsContent.getImage());
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }


    //配置WebView
    public void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setJavaScriptEnabled(true);//支持js
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //重置webview中img标签的图片大小
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName('img'); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "var img = objs[i];   " +
                        "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                        "}" +
                        "})()");
            }
        });
    }


}

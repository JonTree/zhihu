package com.example.tree.zhihu.fragment.shouye;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.gson.ShortComment;
import com.example.tree.zhihu.activity.MainActivity;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.activity.WebContentActivity;
import com.example.tree.zhihu.adapters.ShouyeRecommendAdapter;
import com.example.tree.zhihu.tool.BasePageFragment;
import com.example.tree.zhihu.tool.GetConnected;
import com.example.tree.zhihu.tool.LocalCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class RecommendFragment extends BasePageFragment implements View.OnClickListener {
    RecyclerView recyclerView;
    LinearLayout mLinearLayout;
    ProgressBar progressBar;
    LinearLayout linearLayoutTopStoryFlipper;
    public ViewFlipper viewFlipper;


    News news;
    News loadNews;
    ShouyeRecommendAdapter shouyeRecommendAdapter;
    NetworkInfo info;
    ShortComment shortComment;
    List<News.TopStoriesBean> top_stories;
    List<NewsExtra> newsExtras = new ArrayList<>();
    List<NewsExtra> loadNewsExtras = new ArrayList<>();
    List<Bitmap> bitmaps = new ArrayList<>();
    List<Bitmap> loadBitmaps = new ArrayList<>();
    List<String> shortCommentsList = new ArrayList<>();


    String responseData;
    String newsUrl = "https://news-at.zhihu.com/api/4/news/latest";
    String pastNewsUrl = "";
    String mPastNewsUrl = "啦啦啦啦啦";
    final int UPDATE_TITLE = 1;
    final int SHOW_PROGRESS_BAR = 2;
    final int HIDE_PROGRESS_BAR = 3;
    final int UPDATA_FLIPPER = 4;
    final int NEXT = 5;
    final int PRE = 6;
    final int START_FLIPPER = 7;
    final int SHOP_FLIPPER = 8;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TITLE:
                    shouyeRecommendAdapter = new ShouyeRecommendAdapter(getActivity(), news, newsExtras, bitmaps);
                    recyclerView.setAdapter(shouyeRecommendAdapter);
                    progressBar.setVisibility(View.GONE);
                    break;
                case SHOW_PROGRESS_BAR:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case HIDE_PROGRESS_BAR:
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case UPDATA_FLIPPER:
                    linearLayoutTopStoryFlipper.setVisibility(View.VISIBLE);
                    viewFlipper.addView((View) msg.obj);
                    break;
                case NEXT:
                    viewFlipper.stopFlipping();
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                    viewFlipper.showNext();
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                    break;
                case PRE:
                    viewFlipper.stopFlipping();
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right));
                    viewFlipper.showPrevious();
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                    break;
                case START_FLIPPER:
                    viewFlipper.startFlipping();
                    break;
                case SHOP_FLIPPER:
                    viewFlipper.stopFlipping();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shouye_recommend_fragment, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化控件
        init(view);
        loadLocalData();
    }

    @Override
    public void fetchData() {
        if (info == null) {   //当前没有已激活的网络连接（表示用户关闭了数据流量服务，也没有开启WiFi等别的数据服务）
                Toast.makeText(getActivity(), "检查网络连接设置", Toast.LENGTH_SHORT).show();
        } else {              //当前有已激活的网络连接
            if (mPastNewsUrl != newsUrl && mPastNewsUrl != pastNewsUrl) {
                /*判断历史News是否为空
                 * 为空就获取当日数据
                 * */
                Log.d(TAG, "fetchData: " + mPastNewsUrl);
                Log.d(TAG, "fetchData: " + pastNewsUrl);
                if (pastNewsUrl.isEmpty()) {
                    //获取最新消息
                    getNews(newsUrl, UPDATE_TITLE);
                    mPastNewsUrl = newsUrl;
                } else if (mPastNewsUrl != pastNewsUrl) {
                    getNews(pastNewsUrl, UPDATE_TITLE);
                    mPastNewsUrl = pastNewsUrl;
                    Log.d(TAG, "fetchData: " + mPastNewsUrl);
                    Log.d(TAG, "fetchData: " + pastNewsUrl);
                }
            }
        }

    }

    //初始化控件
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {
        //用于判断是否有网络
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo(); //获取活动的网络连接信息
        linearLayoutTopStoryFlipper = (LinearLayout) view.findViewById(R.id.top_story_flipper_linear);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.top_story_flipper);
        //找到底部tab的布局
        mLinearLayout = (LinearLayout) Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_tabs);
        progressBar = (ProgressBar) view.findViewById(R.id.recommended_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    //获取最新新闻数据
    public void getNews(final String url, final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //发送消息显示ProgressBar
                Message messageShow = new Message();
                messageShow.what = SHOW_PROGRESS_BAR;
                handler.sendMessage(messageShow);
                Gson gson = new Gson();
                responseData = GetConnected.sendRequeestWithOkhttp(url);
                //判断当前是否为过去新闻，若不是则保存数据
                if (pastNewsUrl.isEmpty()) {
                    //保存json数据
                    try {
                        LocalCache.saveJsonData(getActivity(), newsUrl, responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                news = gson.fromJson(responseData, new TypeToken<News>() {
                }.getType());
                /*判断是否存在真的获取了数据*/
                if (news != null) {
                    /*判断是否需要获取top_story
                    避免用户还没获得当日的新闻就直接查询的历史新闻
                     *  */
                    if (news.getTop_stories() == null && top_stories == null) {
                        responseData = GetConnected.sendRequeestWithOkhttp(newsUrl);
                        //保存json数据
                        try {
                            LocalCache.saveJsonData(getActivity(), newsUrl, responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        News news = gson.fromJson(responseData, new TypeToken<News>() {
                        }.getType());
                        top_stories = news.getTop_stories();
                        //开始设置top_story
                        getTopViewList();
                    } else if (news.getTop_stories() != null && top_stories == null) {
                        top_stories = news.getTop_stories();
                        //开始设置top_story
                        getTopViewList();
                    }
                    //开始获取当前news中的额外信息以及图片
                    bitmaps.clear();
                    newsExtras.clear();
                    for (int i = 0; i < news.getStories().size(); i++) {
                        responseData = GetConnected.sendRequeestWithOkhttp(News.getNewsExtraUrl(news.getStories().get(i).getId()));
                        //保存Json数据
                        try {
                            LocalCache.saveJsonData(getActivity(), News.getNewsExtraUrl(news.getStories().get(i).getId()), responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        newsExtras.add((NewsExtra) gson.fromJson(responseData, new TypeToken<NewsExtra>() {
                        }.getType()));
                        //获取图片
                        Bitmap bitmap = GetConnected.getImage(news.getStories().get(i).getImages().get(0));
                        bitmaps.add(bitmap);
                        //保存图片
                        LocalCache.setLocalImageCache(news.getStories().get(i).getImages().get(0),bitmap);
                    }
                    //发送信息更新item
                    Message message = new Message();
                    message.what = type;
                    handler.sendMessage(message);
                } else if (news == null) {
                    Message messageHide = new Message();
                    messageHide.what = HIDE_PROGRESS_BAR;
                    handler.sendMessage(messageHide);
                }
            }
        }).start();
    }

    public void setPastNewsUrl(String pastNewsUrl) {
        this.pastNewsUrl = pastNewsUrl;
        fetchData();
    }

    public void getTopViewList() {
        /*利用循环来设置顶部Top_story
        必须在子线程里运行
         */
        for (int i = 0; i < top_stories.size(); i++) {
            //整体的top_story布局设置
            View view = View.inflate(getActivity(), R.layout.part_idear_fragments, null);
            ((TextView) view.findViewById(R.id.top_story_title)).setText(top_stories.get(i).getTitle());
            ((ImageView) view.findViewById(R.id.top_story_image_title)).setImageBitmap(GetConnected.getImage(top_stories.get(i).getImage()));
            ((ImageView) view.findViewById(R.id.top_story_next)).setOnClickListener(this);
            ((ImageView) view.findViewById(R.id.top_story_pre)).setOnClickListener(this);
            final int finalI = i;
            //设置点击事件，点击进入到详细内容
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebContentActivity.class);
                    intent.putExtra("id", top_stories.get(finalI).getId());
                    getActivity().startActivity(intent);
                }
            });
            /*填充标题下方小滚动评论框
             * 并设置可显示合格评论标准
             * */
            String data = GetConnected.sendRequeestWithOkhttp(NewsExtra.getExtraShortcommentUrl("" + top_stories.get(i).getId()));
            Gson gson = new Gson();
            shortComment = gson.fromJson(data, new TypeToken<ShortComment>() {
            }.getType());
            //先清空
            shortCommentsList.clear();
            //筛选合适的短评
            for (int j = 0; j < shortComment.getComments().size(); j++) {
                if (shortComment.getComments().get(j).getContent().length() > 14 && shortComment.getComments().get(j).getContent().length() < 34) {
                    shortCommentsList.add(shortComment.getComments().get(j).getContent());
                }
            }
            //将筛选好的设置到标题下方flipper中
            ViewFlipper shortCommentViewFlipper = view.findViewById(R.id.news_little_view_flipper);
            for (int k = 0; k < shortCommentsList.size(); k++) {
                View shortCommentView = View.inflate(getActivity(), R.layout.short_comment_top_story, null);
                ((TextView) shortCommentView.findViewById(R.id.short_comment_show)).setText(shortCommentsList.get(k));
                shortCommentViewFlipper.addView(shortCommentView);
            }
            shortCommentViewFlipper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).getViewPager().setCurrentItem(1);
                    ((MainActivity)getActivity()).getIdearFragment().setId(""+news.getTop_stories().get(finalI).getId());
                }
            });
            Message messageFlipper = new Message();
            messageFlipper.obj = view;
            messageFlipper.what = UPDATA_FLIPPER;
            handler.sendMessage(messageFlipper);
        }
    }

    //加载本地数据
    public void loadLocalData() {
        //首次启动判断
        if (!LocalCache.readJsonData(getActivity(), newsUrl).isEmpty()){
            responseData = LocalCache.readJsonData(getActivity(), newsUrl);
            Gson gson = new Gson();
            loadNews = gson.fromJson(responseData, new TypeToken<News>() {
            }.getType());
            for (int i = 0; i < loadNews.getStories().size(); i++) {
                responseData = LocalCache.readJsonData(getActivity(), News.getNewsExtraUrl(loadNews.getStories().get(i).getId()));
                loadNewsExtras.add((NewsExtra) gson.fromJson(responseData, new TypeToken<NewsExtra>() {
                }.getType()));
                //获取图片
                Bitmap bitmap = LocalCache.getLocalImageCache(loadNews.getStories().get(i).getImages().get(0));
                loadBitmaps.add(bitmap);
            }
            shouyeRecommendAdapter = new ShouyeRecommendAdapter(getActivity(), loadNews, loadNewsExtras, loadBitmaps);
            recyclerView.setAdapter(shouyeRecommendAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_story_next:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //top_story上下页切换
                        Message messageNext = new Message();
                        messageNext.what = NEXT;
                        handler.sendMessage(messageNext);
                        try {
//                            切换后暂停top_story的滚动
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        重新启动
                        Message messageStart = new Message();
                        messageStart.what = START_FLIPPER;
                        handler.sendMessage(messageStart);
                    }
                }).start();
                break;
            case R.id.top_story_pre:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message messagePre = new Message();
                        messagePre.what = PRE;
                        handler.sendMessage(messagePre);
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message messageStart = new Message();
                        messageStart.what = START_FLIPPER;
                        handler.sendMessage(messageStart);
                    }
                }).start();
                break;
        }
    }
}

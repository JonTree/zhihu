package com.example.tree.zhihu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.tree.zhihu.gson.News;
import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.activity.MainActivity;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.activity.WebContentActivity;

import java.util.List;


public class ShouyeRecommendAdapter extends RecyclerView.Adapter<ShouyeRecommendAdapter.MyViewHolder> implements View.OnClickListener {

    Context context;
    News news;
    List<NewsExtra> newsExtras;
    List<Bitmap> bitmaps;


    public ShouyeRecommendAdapter(Context context, News news, List<NewsExtra> newsExtras, List<Bitmap> bitmaps) {
        this.context = context;
        this.news = news;
        this.newsExtras = newsExtras;
        this.bitmaps = bitmaps;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_recommend, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {

        viewHolder.image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);

            }
        });
        viewHolder.recommend_comment_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).getViewPager().setCurrentItem(1);
                ((MainActivity)context).getIdearFragment().setId(""+news.getStories().get(i).getId());
            }
        });
        viewHolder.image_content.setImageBitmap(bitmaps.get(i));
        viewHolder.text_title.setText(news.getStories().get(i).getTitle());
        if (newsExtras.size() > i) {
            viewHolder.text_popularty.setText("" + newsExtras.get(i).getPopularity());
            viewHolder.text_comments.setText("" + newsExtras.get(i).getComments());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebContentActivity.class);
                intent.putExtra("id", news.getStories().get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return news.getStories().size();
    }


    private void showPopupMenu(View view) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(context, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.shouye_recommend, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }

    //监听点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shouye_recommended_choice:
                break;
            case R.id.recommend_comment_linear:
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_title;
        TextView text_popularty;
        TextView text_comments;
        ImageView image_content;
        ImageView image_select;
        LinearLayout recommend_comment_linear;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = (TextView) itemView.findViewById(R.id.item_text_title_shouye_recommend);
            text_popularty = (TextView) itemView.findViewById(R.id.news_popularity);
            text_comments = (TextView) itemView.findViewById(R.id.news_comments);
            image_content = (ImageView) itemView.findViewById(R.id.item_image_content_shouye_recommend);
            image_select = (ImageView) itemView.findViewById(R.id.shouye_recommended_choice);
            recommend_comment_linear = (LinearLayout) itemView.findViewById(R.id.recommend_comment_linear);
        }

    }

}

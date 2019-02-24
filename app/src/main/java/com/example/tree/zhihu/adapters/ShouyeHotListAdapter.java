package com.example.tree.zhihu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.gson.NewsHotList;
import com.example.tree.zhihu.activity.MainActivity;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.activity.WebContentActivity;

import java.util.List;

public class ShouyeHotListAdapter extends RecyclerView.Adapter<ShouyeHotListAdapter.MyViewHolder> {

    private Context context;
    NewsHotList newsHotList;
    List<Bitmap> bitmaps;
    List<NewsExtra> newsExtras;

    public ShouyeHotListAdapter(Context context, NewsHotList newsHotList, List<Bitmap> bitmaps, List<NewsExtra> newsExtras) {
        this.context = context;
        this.newsExtras = newsExtras;
        this.bitmaps = bitmaps;
        this.newsHotList = newsHotList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_hot_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.hot_list_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).getViewPager().setCurrentItem(1);
                ((MainActivity)context).getIdearFragment().setId(""+newsHotList.getRecent().get(i).getNews_id());
            }
        });

        //排名TextView设置
        setNumber(myViewHolder,i);
        myViewHolder.text_title.setText(newsHotList.getRecent().get(i).getTitle());
        if (newsExtras.size() > i && bitmaps.size() > i) {
            myViewHolder.image_title.setImageBitmap(bitmaps.get(i));
            myViewHolder.text_review.setText(""+newsExtras.get(i).getComments());
            myViewHolder.text_agree.setText(""+newsExtras.get(i).getPopularity());
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebContentActivity.class);
                intent.putExtra("id", newsHotList.getRecent().get(i).getNews_id());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return newsHotList.getRecent().size();
    }


    //排名TextView设置
    public void setNumber(MyViewHolder myViewHolder,int i) {
        myViewHolder.number.setText("" + ++i);
        if (i < 4) {
            myViewHolder.number.setTextColor(0xFFF1413D);
        }else if (i>=4){
            myViewHolder.number.setTextColor(0xFFC2A56A);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        TextView text_title;
        TextView text_agree;
        TextView text_review;
        ImageView image_title;
        LinearLayout hot_list_linear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.text_number_hot_list);
            text_agree = (TextView) itemView.findViewById(R.id.text_agrees_with_the_column_message);
            text_review = (TextView) itemView.findViewById(R.id.text_review_column_message);
            text_title = (TextView) itemView.findViewById(R.id.item_column_message_title);
            image_title = (ImageView) itemView.findViewById(R.id.item_column_message_picture_title);
            hot_list_linear = (LinearLayout) itemView.findViewById(R.id.hot_list_linear);
        }
    }

}

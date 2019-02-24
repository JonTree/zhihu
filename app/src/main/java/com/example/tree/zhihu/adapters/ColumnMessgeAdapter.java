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

import com.example.tree.zhihu.activity.ColumnMessageActivity;
import com.example.tree.zhihu.gson.ColumnMessage;
import com.example.tree.zhihu.gson.NewsExtra;
import com.example.tree.zhihu.R;
import com.example.tree.zhihu.activity.WebContentActivity;

import java.util.List;

public class ColumnMessgeAdapter extends RecyclerView.Adapter<ColumnMessgeAdapter.ViewHolder> {
    Context context;
    ColumnMessage columnMessage;
    List<NewsExtra> newsExtras;
    List<Bitmap> bitmaps;


    public ColumnMessgeAdapter(Context context,ColumnMessage columnMessage,List<NewsExtra> newsExtras,List<Bitmap> bitmaps){
        this.context = context;
        this.columnMessage = columnMessage;
        this.bitmaps = bitmaps;
        this.newsExtras = newsExtras;
    }


    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_column_message,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.column_extra_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ColumnMessageActivity)context).getViewPager().setCurrentItem(1);
                ((ColumnMessageActivity)context).SetIdearFragment(""+columnMessage.getStories().get(i).getId());
            }
        });
        viewHolder.te_title.setText(columnMessage.getStories().get(i).getTitle());
        viewHolder.te_date.setText(columnMessage.getStories().get(i).getDate());
        if (newsExtras.size() > i && bitmaps.size() > i){
            viewHolder.te_popular.setText(""+newsExtras.get(i).getPopularity());
            viewHolder.te_comment.setText(""+newsExtras.get(i).getComments());
            viewHolder.image_title.setImageBitmap(bitmaps.get(i));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebContentActivity.class);
                intent.putExtra("id", columnMessage.getStories().get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return columnMessage.getStories().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView te_title;
        TextView te_comment;
        TextView te_popular;
        TextView te_date;
        ImageView image_title;
        LinearLayout column_extra_linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            te_comment =(TextView) itemView.findViewById(R.id.text_agrees_with_the_column_message);
            te_popular = (TextView)itemView.findViewById(R.id.text_review_column_message);
            te_date =(TextView) itemView.findViewById(R.id.item_column_message_date);
            te_title = (TextView) itemView.findViewById((R.id.item_column_message_title));
            image_title = (ImageView) itemView.findViewById(R.id.item_column_message_picture_title);
            column_extra_linear = (LinearLayout) itemView.findViewById((R.id.column_extra));
        }
    }
}

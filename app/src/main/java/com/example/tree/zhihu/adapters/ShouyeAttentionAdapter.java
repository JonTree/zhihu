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
import android.widget.TextView;

import com.example.tree.zhihu.activity.ColumnMessageActivity;
import com.example.tree.zhihu.gson.ColumnOverview;
import com.example.tree.zhihu.R;

import java.util.List;

public class ShouyeAttentionAdapter extends RecyclerView.Adapter<ShouyeAttentionAdapter.MyViewHolder> {

    Context context;
    ColumnOverview columnOverview;
    List<Bitmap> bitmaps;

    public ShouyeAttentionAdapter(Context context, ColumnOverview columnOverview, List<Bitmap> bitmaps) {
        this.context = context;
        this.columnOverview = columnOverview;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shouye_attention, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.text_tile.setText(columnOverview.getData().get(i).getName());
        myViewHolder.text_description.setText(columnOverview.getData().get(i).getDescription());
        if (bitmaps.size() > i) {
            myViewHolder.image_title.setImageBitmap(bitmaps.get(i));
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ColumnMessageActivity.class);
                intent.putExtra("id",columnOverview.getData().get(i).getId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return columnOverview.getData().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_title;
        TextView text_tile;
        TextView text_description;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_title = (ImageView) itemView.findViewById(R.id.picture_title_topic);
            text_tile = (TextView) itemView.findViewById(R.id.text_title_topic);
            text_description = (TextView) itemView.findViewById(R.id.text_description_topic);
        }
    }
}

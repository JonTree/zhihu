package com.example.tree.zhihu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tree.zhihu.GsonClass.LongComment;
import com.example.tree.zhihu.GsonClass.ShortComment;
import com.example.tree.zhihu.R;

import java.util.List;


public class IdearAdapter extends RecyclerView.Adapter<IdearAdapter.MyViewHolder> {
    private Context context;
    private LongComment longComment;
    private ShortComment shortComment;
    List<Bitmap> bitmaps;

    public IdearAdapter(Context context, ShortComment shortComment, LongComment longComment, List<Bitmap> bitmaps) {
        this.context = context;
        this.shortComment = shortComment;
        this.longComment = longComment;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_show, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        if (i < longComment.getComments().size()) {
            viewHolder.reply_name.setText(longComment.getComments().get(i).getAuthor() + ":");
            viewHolder.reply_content.setText(longComment.getComments().get(i).getContent());
            viewHolder.comment_likes.setText("" + longComment.getComments().get(i).getLikes());
            if (longComment.getComments().get(i).getReply_to() != null) {
                viewHolder.reply_to_linear.setVisibility(View.VISIBLE);
                viewHolder.reply_to_name.setText(longComment.getComments().get(i).getReply_to().getAuthor() + ":");
                viewHolder.reply_to_content.setText(longComment.getComments().get(i).getReply_to().getContent());
            }
            if (bitmaps.size() > i) {
                viewHolder.reply_avatar.setImageBitmap(bitmaps.get(i));
            }
        } else {
            int mI = i - longComment.getComments().size();
            viewHolder.reply_name.setText(shortComment.getComments().get(mI).getAuthor() + ":");
            viewHolder.reply_content.setText(shortComment.getComments().get(mI).getContent());
            viewHolder.comment_likes.setText("" + shortComment.getComments().get(mI).getLikes());
            if (shortComment.getComments().get(mI).getReply_to() != null) {
                viewHolder.reply_to_linear.setVisibility(View.VISIBLE);
                viewHolder.reply_to_name.setText(shortComment.getComments().get(mI).getReply_to().getAuthor() + ":");
                viewHolder.reply_to_content.setText(shortComment.getComments().get(mI).getReply_to().getContent());
            }
            if (bitmaps.size() > i) {
                viewHolder.reply_avatar.setImageBitmap(bitmaps.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return shortComment.getComments().size() + longComment.getComments().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView reply_avatar;
        TextView reply_name;
        TextView reply_to_name;
        TextView reply_to_content;
        TextView reply_content;
        TextView comment_likes;
        LinearLayout reply_to_linear;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reply_avatar = itemView.findViewById(R.id.reply_avatar);
            reply_content = itemView.findViewById(R.id.reply_content);
            reply_name = itemView.findViewById(R.id.reply_name);
            reply_to_content = itemView.findViewById(R.id.reply_to_content);
            reply_to_name = itemView.findViewById(R.id.reply_to_name);
            comment_likes = itemView.findViewById(R.id.comment_likes);
            reply_to_linear = itemView.findViewById(R.id.reply_to_linear);
        }
    }
}

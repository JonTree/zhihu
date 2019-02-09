package com.example.tree.zhihu.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tree.zhihu.R;
import com.example.tree.zhihu.fragment.ShouyeFragment;

public class ShouyeAttentionAdapter extends RecyclerView.Adapter<ShouyeAttentionAdapter.MyViewHolder> {

    Context mContext;
    public ShouyeAttentionAdapter(Context context){
        mContext = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_attention,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


    }


    @Override
    public int getItemCount() {
        return 100
                ;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.attention_text);
        }
    }
}

package com.example.sebas_pc.formularioandroid.view;

/**
 * Created by aleix on 15/01/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.sebas_pc.formularioandroid.R;

/**
 * Created by dam2a on 11/01/18.
 */

class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView tvContent;
    public TextView ivContent;


    public PostViewHolder(View itemView) {
        super(itemView);
        tvContent = itemView.findViewById(R.id.postText);
        ivContent = itemView.findViewById(R.id.postImage);


    }
}

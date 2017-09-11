package com.admin.claire.lotto.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.model.Betting;

/**
 * Created by claire on 2017/9/3.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView betting_text, datetime_text;
    public ImageView image_edit, image_delete;

    public MyViewHolder(View itemView) {
        super(itemView);
        betting_text = (TextView)itemView.findViewById(R.id.betting_Text);
        datetime_text = (TextView)itemView.findViewById(R.id.datetime_Text);
        image_edit = (ImageView)itemView.findViewById(R.id.image_edit);
        image_delete = (ImageView)itemView.findViewById(R.id.image_delete);
    }

}

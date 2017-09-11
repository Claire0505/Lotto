package com.admin.claire.lotto.swiper;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.adapter.MyRecyclerViewAdapter;
import com.admin.claire.lotto.adapter.MyViewHolder;
import com.admin.claire.lotto.model.Betting;

/**
 * Created by claire on 2017/9/4.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeHelper(MyRecyclerViewAdapter myRecyclerViewAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.myRecyclerViewAdapter = myRecyclerViewAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){

            myRecyclerViewAdapter.removeBettingItem(position);
        }
        //myRecyclerViewAdapter.removeBettingItem(viewHolder.getAdapterPosition());
    }

}

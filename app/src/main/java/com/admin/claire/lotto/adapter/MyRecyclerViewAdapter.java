package com.admin.claire.lotto.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.database.LottoDAO;
import com.admin.claire.lotto.fragment.HistoryFragment;
import com.admin.claire.lotto.model.Betting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by claire on 2017/9/3.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context mContext;
    private List<Betting> bettings;
    private LottoDAO mLottoDAO;
    private List<Betting> filterList;


    public MyRecyclerViewAdapter(Context mContext, List<Betting> bettings) {
        this.mContext = mContext;
        this.bettings = bettings;

        ///我們將原始列表複製到過濾器列表中，並將其用於設置行值
        this.filterList = new ArrayList<Betting>();
        this.filterList.addAll(this.bettings);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.betting_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Betting singleBetting = filterList.get(position); //filter
        holder.betting_text.setText(singleBetting.getBettingNum());
        holder.datetime_text.setText(singleBetting.getLocalDateTime());
        //singleBetting.setDateCreated(new Date().getTime());

       //編輯號碼
        holder.image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, singleBetting.getBettingNum() +
//                                singleBetting.getLocalDateTime(),
//                        Toast.LENGTH_SHORT).show();

                editTaskDialog(singleBetting);

            }
        });

        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("刪除號碼:\n" + singleBetting.getBettingNum());
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mLottoDAO = new LottoDAO(mContext);
                        mLottoDAO.deletedDB(singleBetting.getId());
                        //refresh the activity
                    ((Activity) mContext).finish();
                    mContext.startActivity(((Activity) mContext).getIntent());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "取消刪除", Toast.LENGTH_SHORT).show();
                    }
                });
              builder.show();
                
            }
        });

    }

    @Override
    public int getItemCount() {
       // return bettings.size();
       return (null != filterList ? filterList.size() : bettings.size());

    }

    private void editTaskDialog(final Betting betting) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View subView = inflater.inflate(R.layout.add_betting_layout, null);

        final EditText bettingField = (EditText) subView.findViewById(R.id.edit_betting);
        final TextView modifyDateTime = (TextView) subView.findViewById(R.id.add_dateTime);

        if (betting != null) {
            bettingField.setText(betting.getBettingNum());
            modifyDateTime.setText(betting.getLocalDateTime());
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("修改樂透號碼");
        dialog.setIcon(R.drawable.chinese_ingot);
        dialog.setView(subView);
        dialog.create();

        dialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String bettingNum = bettingField.getText().toString();
                betting.setBettingNum(bettingNum);
               // Log.e(TAG, "onClick: " + bettingNum);

                betting.setDateCreated(new Date().getTime());
                final long modifyTime = new Date().getTime();
                betting.setDateCreated(modifyTime);
                modifyDateTime.setText(betting.getLocalDateTime());

               // Log.e(TAG, "onClickTime: " + betting.getLocalDateTime() );

                if (TextUtils.isEmpty(bettingNum)) {
                    Toast.makeText(mContext, "Something went wrong. Check your input values",
                            Toast.LENGTH_SHORT).show();
                } else {

                    //要先初始化
                    mLottoDAO = new LottoDAO(mContext);
                   // mLottoDAO.updateDB(new Betting(betting.getId(), bettingNum, modifyTime));
                    mLottoDAO.updateDB(betting);

                    //refresh the activity
//                    ((Activity) mContext).finish();
//                    mContext.startActivity(((Activity) mContext).getIntent());

                    // Set on UI Thread
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Task Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    //滑動刪除資料
    public void removeBettingItem(final int position){
        Betting betting = bettings.get(position);
        final long id = betting.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("刪除號碼");
        builder.setMessage(betting.getBettingNum());
        builder.setIcon(R.drawable.chinese_ingot);

        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LottoDAO mLottoDAO = new LottoDAO(mContext);
                if (mLottoDAO.deletedDB(id)){

                    bettings.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,bettings.size());
                    //refresh the activity
                    ((Activity) mContext).finish();
                    mContext.startActivity(((Activity) mContext).getIntent());
                } else {
                    Toast.makeText(mContext, "Unable To Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "取消刪除", Toast.LENGTH_SHORT).show();
                // Set on UI Thread
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        builder.show();

    }

    public void filter(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)){
                    filterList.addAll(bettings);
                }else {
                    // Iterate in the original List and add it to filter list...
                    for (Betting betting : bettings){
                        if (betting.getBettingNum().contains(text)){

                            filterList.add(betting);

                            String bettingStr = betting.getBettingNum();
                           // Log.e(TAG, "run: " + bettingStr);
                        }

                    }
                }
                // Set on UI Thread
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();

                    }
                });

            }
        }).start();
    }

}

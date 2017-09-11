package com.admin.claire.lotto.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.fragment.HistoryFragment;
import com.admin.claire.lotto.fragment.LottoFragment;
import com.admin.claire.lotto.fragment.SupperLottoFragment;

/**
 * Created by claire on 2017/9/2.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"投注紀錄","大樂透","威力彩"};
    private Context context;

    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HistoryFragment();
            case 1:
                return new LottoFragment();
            case 2:
                return new SupperLottoFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    //建立Title
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }

   //自定tab畫面
    public View getTabView(int position){
        View tab = LayoutInflater.from(context)
                .inflate(R.layout.custom_tab,null);
        TextView tv = (TextView)tab.findViewById(R.id.custom_text);
        ImageView image = (ImageView)tab.findViewById(R.id.image_ingot);
        tv.setText(tabTitles[position]);

        return tab;
    }
}

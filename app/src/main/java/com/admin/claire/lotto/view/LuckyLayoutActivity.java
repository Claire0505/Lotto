package com.admin.claire.lotto.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import com.admin.claire.lotto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LuckyLayoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView id_imageView;
    private LuckyTurnTable_SurfaceView luckyTurnTable_surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_layout);

        //放廣告
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
        setListener();
    }

    private void setListener() {
        id_imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!luckyTurnTable_surfaceView.isStart()){
                    id_imageView.setImageResource(R.drawable.stop);
                   Random random = new Random();
                   luckyTurnTable_surfaceView.luckyStart(random.nextInt()%6);

                }else {
                    if (!luckyTurnTable_surfaceView.isShouldEnd()){
                        id_imageView.setImageResource(R.drawable.start);
                        luckyTurnTable_surfaceView.luckyEnd();
                    }
                }

            }
        });
    }


    private void initView() {
        luckyTurnTable_surfaceView =    (LuckyTurnTable_SurfaceView)findViewById(R.id.id_luckyTurnTable);
        id_imageView=(ImageView)findViewById(R.id.id_imageView);
    }
}

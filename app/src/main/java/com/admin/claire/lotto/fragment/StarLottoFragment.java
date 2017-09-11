package com.admin.claire.lotto.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.database.LottoDAO;
import com.admin.claire.lotto.model.Betting;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class StarLottoFragment extends Fragment {
    private static final String TAG = StarLottoFragment.class.getSimpleName();
    private TextView lotte3Star_text, lotte4Star_text;
    private Button btn3Star_Random, btn3Star_Restart_random;
    private Button btn4Star_Random, btn4Star_Restart_random;
    private LottoDAO mLottoDao;
    private Button btn_Save3StarBettingOn, btn_Save4StarBettingOn;
    private Betting betting = new Betting();
    private ImageView btn_BasicPlay;


    public StarLottoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_star_lotto,container,false);
        lotte3Star_text = (TextView)rootView.findViewById(R.id.lotte_3Star_Text);
        btn3Star_Random = (Button)rootView.findViewById(R.id.btn3StarRandom);
        btn3Star_Restart_random = (Button)rootView.findViewById(R.id.btnRe3StarRandom);
        btn_Save3StarBettingOn = (Button)rootView.findViewById(R.id.btn_3StarSaveBettingOn);
        btn_BasicPlay = (ImageView)rootView.findViewById(R.id.basicPlay_image);

        lotte4Star_text = (TextView)rootView.findViewById(R.id.lotte_4Star_Text);
        btn4Star_Random = (Button)rootView.findViewById(R.id.btn4StarRandom);
        btn4Star_Restart_random = (Button)rootView.findViewById(R.id.btnRe4Star_random);
        btn_Save4StarBettingOn = (Button)rootView.findViewById(R.id.btn_4StarSaveBettingOn);

        initLotte3Star();
        initLotte4Star();

        // Inflate the layout for this fragment
        return rootView;
    }

    //三星彩
    private void initLotte3Star() {
        btn3Star_Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLotte3StarRandom();
                btn3Star_Random.setVisibility(View.INVISIBLE);
                btn3Star_Restart_random.setVisibility(View.VISIBLE);

            }
        });

        btn3Star_Restart_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotte3Star_text.setText("");
                btn3Star_Random.setVisibility(View.VISIBLE);
                btn3Star_Restart_random.setVisibility(View.INVISIBLE);

            }
        });

        btn_Save3StarBettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lotteStr = lotte3Star_text.getText().toString();
                betting.setBettingNum("3星彩 " + lotteStr);
                betting.setDateCreated(new Date().getTime());
                mLottoDao = new LottoDAO(getActivity());
                mLottoDao.insertDB(betting);
                Toast.makeText(getActivity(), "已儲存，祝你中獎!!!!", Toast.LENGTH_SHORT).show();

                // openFragment(new HistoryFragment());
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        btn_BasicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.lotto_star_dialog_layout,null);
                dialog.setTitle("3 4星彩玩法說明");
                dialog.setIcon(R.drawable.chinese_ingot);
                dialog.setView(view);
                dialog.create();
                dialog.setPositiveButton("前往台灣彩券-3星彩玩法說明", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://www.taiwanlottery.com.tw/3D/index.asp";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                dialog.setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    private void startLotte3StarRandom() {
        int num[] = new int[3];
        int i;
        int temp;
        for ( i =0; i < 3; i++){
            temp = (int)(Math.random() * 10);
            num[i] = temp;
            String numStr = String.valueOf(num[i]) + " ";
            String lotto3Star = lotte3Star_text.getText().toString() + numStr;
            lotte3Star_text.setText(lotto3Star);
        }
    }

    //四星彩
    private void initLotte4Star() {
        btn4Star_Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLotte4StarRandom();
                btn4Star_Random.setVisibility(View.INVISIBLE);
                btn4Star_Restart_random.setVisibility(View.VISIBLE);
            }
        });

        btn4Star_Restart_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotte4Star_text.setText("");
                btn4Star_Random.setVisibility(View.VISIBLE);
                btn4Star_Restart_random.setVisibility(View.INVISIBLE);

            }
        });

        btn_Save4StarBettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lotteStr = lotte4Star_text.getText().toString();
                betting.setBettingNum("4星彩 " + lotteStr);
                betting.setDateCreated(new Date().getTime());

                mLottoDao = new LottoDAO(getActivity());
                mLottoDao.insertDB(betting);
                Toast.makeText(getActivity(), "已儲存，祝你中獎!!!!", Toast.LENGTH_SHORT).show();

                // openFragment(new HistoryFragment());
                getActivity().finish();
                startActivity(getActivity().getIntent());

            }
        });
    }

    private void startLotte4StarRandom() {
        int num[] = new int[4];
        int i;
        int temp;
        for ( i =0; i < 4; i++){
            temp = (int)(Math.random() * 10);
            num[i] = temp;
            String numStr = String.valueOf(num[i]) + " ";
            String lotto4Star = lotte4Star_text.getText().toString() + numStr;
            lotte4Star_text.setText(lotto4Star);
        }
    }


}

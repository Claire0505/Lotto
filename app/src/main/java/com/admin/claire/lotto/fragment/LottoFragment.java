package com.admin.claire.lotto.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * 大樂透
 */
public class LottoFragment extends Fragment {
    private static final String TAG = LottoFragment.class.getSimpleName();
    private TextView lotte_text, specialNum_text;
    private Button btn_Random, btn_Re_random;
    private LottoDAO mLottoDao;
    private Button btn_SaveBettingOn;
    private Betting betting = new Betting();
    private ImageView btn_BasicPlay;


    public LottoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lotte, container, false);
        lotte_text = (TextView) rootView.findViewById(R.id.lotte_Text);
        specialNum_text = (TextView) rootView.findViewById(R.id.spacial_Text);
        btn_Random = (Button) rootView.findViewById(R.id.btn_random);
        btn_Re_random = (Button) rootView.findViewById(R.id.btn_Re_random);
        btn_SaveBettingOn = (Button)rootView.findViewById(R.id.btn_BettingOn);
        btn_BasicPlay =(ImageView)rootView.findViewById(R.id.basicPlay_image);
        initHandler();
        return rootView;
    }

    private void initHandler() {
        btn_Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLotteRandom();
                // randomLotte();
                btn_Random.setVisibility(View.INVISIBLE);
                btn_Re_random.setVisibility(View.VISIBLE);

            }
        });

        btn_Re_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotte_text.setText("");
                specialNum_text.setText("");

                btn_Random.setVisibility(View.VISIBLE);
                btn_Re_random.setVisibility(View.INVISIBLE);

            }
        });

        //儲存下注號碼
        btn_SaveBettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotteStr = lotte_text.getText().toString();
                String specialSTR = specialNum_text.getText().toString();
                betting.setBettingNum("大樂透 " + lotteStr + " \n" + specialSTR);
                betting.setDateCreated(new Date().getTime());
                mLottoDao = new LottoDAO(getActivity());
                mLottoDao.insertDB(betting);
                Toast.makeText(getActivity(), "已儲存，祝你中獎!!!!", Toast.LENGTH_SHORT).show();

                //openFragment(new HistoryFragment());
                getActivity().finish();
                startActivity(getActivity().getIntent());

            }
        });

        //玩法說明
        btn_BasicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.lotto_dialog_layout,null);
                dialog.setTitle("大樂透玩法說明");
                dialog.setIcon(R.drawable.chinese_ingot);
                dialog.setView(view);
                dialog.create();
                dialog.setPositiveButton("前往台灣彩券-大樂透玩法說明", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://www.taiwanlottery.com.tw/Lotto649/index.asp";
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


    private void openFragment(final Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }

    private void startLotteRandom() {
        //1.宣告一個陣列產生七個號碼(包含一個特別號)
        int num[] = new int[7];
        int temp = 0; //暫存數字
        int i;

        for (i = 0; i < num.length; i++) {

            //把temp跟之前的數字比對，如有有重複就重新產生亂數
            temp = (int) (Math.random() * 49) + 1;
            int j = 0;
            while (j < i) {
                if (num[j] == temp) {
                    //把temp跟之前的數字比對，如有有重複就重新產生亂數
                    temp = (int) (Math.random() * 49) + 1;
                    j = 0;
                } else {
                    j++;
                }
            }

            //沒有重複才把值給陣列
            num[i] = temp;
            // Log.e(TAG, "沒有排序: " + "x[" + i + "]" + "=" + num[i] + " ");

        }

        //先排序
        bubbleSort(num);
        for (i = 0; i < num.length; i++) {
            if (i < 6) {

                String lotteNum = String.valueOf("第" + (i + 1) + "個號碼:") + num[i] + "'\n";
                // Log.e(TAG, "lotteNum: " + lotteNum);

                if (num[i] < 10) {
                    String numStr = "0" + num[i] + "  ";
                    String str = lotte_text.getText().toString() + numStr;
                    lotte_text.setText(str);
                } else {
                    String lotteStr = String.valueOf(num[i]) + "  ";
                    String str = lotte_text.getText().toString() + lotteStr;
                    lotte_text.setText(str);
                }


            } else {

                if (num[i] < 10) {
                    String numStr = "0" + num[i];
                    specialNum_text.setText("特別號:" + numStr);
                    // Log.e(TAG, "onClick: "+numStr );
                } else {
                    String spNumStr = String.valueOf(num[i]);
                    specialNum_text.setText("特別號:" + spNumStr);
                    // Log.e(TAG, "onClick: "+ sixNumStr );
                }

            }

        }

    }

    //由小到大排序
    private void bubbleSort(int[] x) {
        //1.先取得int[]x最大索引值，然後再遞減
        for (int i = x.length - 1; i > 0; i--) {
            //int j 只要小於 i 就然它跑一次
            for (int j = 0; j < i; j++) {

                if (x[j] > x[j + 1]) {
                    int tmp = x[j]; //把x[j]裡的值丟到 tmp，x[j]就空了
                    x[j] = x[j + 1]; //再把x[j+1]的值丟到x[j]裡面去, x[j+1]就空了
                    x[j + 1] = tmp; //最後把先前的tmp的值給 x[j+1], 那x[j]和 x[j+1]的值就交換了
                }
            }
        }

    }

    private void randomLotte() {
        Random rnd = new Random();
        int[] x = new int[7];
        for (int i = 0; i < x.length; i++) {

            int temp = rnd.nextInt(49) + 1;
            int b = 0;
            while (b < i) {
                if (x[b] == temp) {
                    //把temp跟之前的數字比對，如有有重複就重新產生亂數
                    temp = rnd.nextInt(49) + 1;
                    b = 0;
                } else {
                    b++;
                }
            }
            //沒有重複才把值給陣列
            x[i] = temp;

            Log.e(TAG, "randomLotte: " + "x[" + i + "]" + "=" + x[i] + "\t");


        }
        bubbleSort(x);
        for (int i = 0; i < x.length; i++) {
            Log.e(TAG, "randomLotteSort: " + "x[" + i + "]" + "=" + x[i] + "\t");
            if (i < 6) {

                if (x[i] < 10) {
                    String numStr = "0" + x[i] + "   ";
                    String str = lotte_text.getText().toString() + numStr;
                    lotte_text.setText(str);
                } else {
                    String lotteStr = String.valueOf(x[i]) + "   ";
                    String str = lotte_text.getText().toString() + lotteStr;
                    lotte_text.setText(str);
                }


            } else {

                if (x[i] < 10) {
                    String numStr = "0" + x[i];
                    specialNum_text.setText("特別號:" + numStr);
                    // Log.e(TAG, "onClick: "+numStr );
                } else {
                    String spNumStr = String.valueOf(x[i]);
                    specialNum_text.setText("特別號:" + spNumStr);
                    // Log.e(TAG, "onClick: "+ sixNumStr );
                }

            }
        }
    }


}

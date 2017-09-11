package com.admin.claire.lotto.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * 威力彩
 */
public class SupperLottoFragment extends Fragment {
    private static final String TAG = SupperLottoFragment.class.getSimpleName();
    private TextView superLotte_text, superLotte2_text;
    private Button btnSuper_Random, btnSuper_Restart_random;
    private LottoDAO mLottoDao;
    private Button btn_SaveSuperBettingOn;
    private Betting betting = new Betting();
    private ImageView btn_BasicPlay;


    public SupperLottoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_supper_lotto,container,false);
        superLotte_text = (TextView)rootView.findViewById(R.id.superLotte_Text);
        superLotte2_text = (TextView)rootView.findViewById(R.id.superLotte2_Text);
        btnSuper_Random = (Button)rootView.findViewById(R.id.btnRandom);
        btnSuper_Restart_random = (Button)rootView.findViewById(R.id.btnRe_random);
        btn_SaveSuperBettingOn = (Button)rootView.findViewById(R.id.btn_SuperBettingOn);
        btn_BasicPlay =(ImageView)rootView.findViewById(R.id.basicPlay_image);

        initHandler();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void initHandler() {
        btnSuper_Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLotteRandom();
                // randomLotte();
                btnSuper_Random.setVisibility(View.INVISIBLE);
                btnSuper_Restart_random.setVisibility(View.VISIBLE);
            }
        });

        btnSuper_Restart_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               superLotte_text.setText("");
                superLotte2_text.setText("");

               btnSuper_Random.setVisibility(View.VISIBLE);
               btnSuper_Restart_random.setVisibility(View.INVISIBLE);

            }
        });

        btn_SaveSuperBettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotteStr = superLotte_text.getText().toString();
                String lotteStr2 = superLotte2_text.getText().toString();
                betting.setBettingNum("威力彩 " + lotteStr + " \n" + lotteStr2);
                betting.setDateCreated(new Date().getTime());
                mLottoDao = new LottoDAO(getActivity());
                mLottoDao.insertDB(betting);
                Toast.makeText(getActivity(), "已儲存，祝你中獎!!!!", Toast.LENGTH_SHORT).show();

               // openFragment(new HistoryFragment());
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
                View view = inflater.inflate(R.layout.super_lotto_dialog_layout,null);
                dialog.setTitle("威力彩玩法說明");
                dialog.setIcon(R.drawable.chinese_ingot);
                dialog.setView(view);
                dialog.create();
                dialog.setPositiveButton("前往台灣彩券-威力彩玩法說明", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://www.taiwanlottery.com.tw/Superlotto638/index.asp";
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
            temp = (int) (Math.random() * 38) + 1;
            int j = 0;
            while (j < i) {
                if (num[j] == temp) {
                    //把temp跟之前的數字比對，如有有重複就重新產生亂數
                    temp = (int) (Math.random() * 38) + 1;
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
                    String str = superLotte_text.getText().toString() + numStr;
                    superLotte_text.setText(str);
                } else {
                    String lotteStr = String.valueOf(num[i]) + "  ";
                    String str = superLotte_text.getText().toString() + lotteStr;
                    superLotte_text.setText(str);
                }

            }

        }

        int lotto2 = (int)(Math.random() * 8) +1;
        String result = String.valueOf(lotto2);
        superLotte2_text.setText("第二區號碼:" + "0" + result);

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


}

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
 * 今彩539
 * 您必須從01~39的號碼中任選5個號碼進行投注。
 *，五個選號中，如有二個以上（含二個號碼）對中當期開出之五個號碼，
 *  即為中獎。
 */
public class Lotto539Fragment extends Fragment {
    private static final String TAG = Lotto539Fragment.class.getSimpleName();
    private TextView lotte539_text;
    private Button btn539_Random, btn539_Restart_random;
    private LottoDAO mLottoDao;
    private Button btn_Save539BettingOn;
    private Betting betting = new Betting();
    private ImageView btn_BasicPlay;


    public Lotto539Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lotto539,container,false);
        lotte539_text = (TextView)rootView.findViewById(R.id.lotte539_Text);
        btn539_Random = (Button)rootView.findViewById(R.id.btnRandom);
        btn539_Restart_random = (Button)rootView.findViewById(R.id.btnRe_random);
        btn_Save539BettingOn = (Button)rootView.findViewById(R.id.btn_539BettingOn);
        btn_BasicPlay =(ImageView)rootView.findViewById(R.id.basicPlay_image);

        initHandler();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void initHandler() {
        btn539_Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLotteRandom();
                // randomLotte();
                btn539_Random.setVisibility(View.INVISIBLE);
                btn539_Restart_random.setVisibility(View.VISIBLE);
            }
        });

        btn539_Restart_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotte539_text.setText("");

                btn539_Random.setVisibility(View.VISIBLE);
                btn539_Restart_random.setVisibility(View.INVISIBLE);

            }
        });

        btn_Save539BettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotteStr = lotte539_text.getText().toString();
                betting.setBettingNum("今彩539 " + lotteStr );
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
                View view = inflater.inflate(R.layout.lotto539_dialog_layout,null);
                dialog.setTitle("今彩539玩法說明");
                dialog.setIcon(R.drawable.chinese_ingot);
                dialog.setView(view);
                dialog.create();
                dialog.setPositiveButton("前往台灣彩券-今彩539玩法說明", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://www.taiwanlottery.com.tw/DailyCash/index.asp";
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

    private void startLotteRandom() {
        //1.宣告一個陣列產生5個號碼
        int num[] = new int[5];
        int temp = 0; //暫存數字
        int i;

        for (i = 0; i < num.length; i++) {

            //把temp跟之前的數字比對，如有有重複就重新產生亂數
            temp = (int) (Math.random() * 39) + 1;
            int j = 0;
            while (j < i) {
                if (num[j] == temp) {
                    //把temp跟之前的數字比對，如有有重複就重新產生亂數
                    temp = (int) (Math.random() * 39) + 1;
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
            if (i < 5) {

                String lotteNum = String.valueOf("第" + (i + 1) + "個號碼:") + num[i] + "'\n";
                // Log.e(TAG, "lotteNum: " + lotteNum);

                if (num[i] < 10) {
                    String numStr = "0" + num[i] + "  ";
                    String str = lotte539_text.getText().toString() + numStr;
                    lotte539_text.setText(str);
                } else {
                    String lotteStr = String.valueOf(num[i]) + "  ";
                    String str = lotte539_text.getText().toString() + lotteStr;
                    lotte539_text.setText(str);
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

}

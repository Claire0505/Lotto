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
 * 大福彩
 * 大福彩是一種樂透型遊戲。您必須從01~40中任選七個號碼進行投注。
 * 開獎時，開獎單位將從01~40的號碼中隨機開出七個號碼加一個特別號，
 * 這組號碼就是該期大福彩的中獎號碼，也稱為「獎號」。
 * 您的七個選號中，如果有三個以上（含三個號碼）對中當期開出之七個號碼
 * （特別號只適用於大福獎、小福獎），即為中獎(普獎)，並可依規定兌領獎金。
 */
public class BigLottoFragment extends Fragment {

    private static final String TAG = BigLottoFragment.class.getSimpleName();
    private TextView big_lotte_text, big_specialNum_text;
    private Button btn_Random, btn_Re_random;
    private LottoDAO mLottoDao;
    private Button btn_SaveBettingOn;
    private Betting betting = new Betting();
    private ImageView btn_BasicPlay;



    public BigLottoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_big_lotto, container, false);
        big_lotte_text = (TextView) rootView.findViewById(R.id.big_lotte_Text);
        big_specialNum_text = (TextView) rootView.findViewById(R.id.big_spacial_Text);
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
                big_lotte_text.setText("");
                big_specialNum_text.setText("");

                btn_Random.setVisibility(View.VISIBLE);
                btn_Re_random.setVisibility(View.INVISIBLE);

            }
        });

        //儲存下注號碼
        btn_SaveBettingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotteStr = big_lotte_text.getText().toString();
                String specialSTR = big_specialNum_text.getText().toString();
                betting.setBettingNum("大福彩 " + lotteStr + " \n" + specialSTR);
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
                View view = inflater.inflate(R.layout.basic_play_dialog_layout,null);
                dialog.setTitle("大福彩玩法說明");
                dialog.setIcon(R.drawable.chinese_ingot);
                dialog.setView(view);
                dialog.create();
                dialog.setPositiveButton("前往台灣彩券-大福彩玩法說明", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://www.taiwanlottery.com.tw/Lotto740/index.asp";
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
        //宣告一個產生八個號碼的陣列(1~7)(8 為特別號)
        int num[] = new int[8];
        int temp = 0; //暫存數字
        int i;

        for (i = 0; i < num.length; i++){
            //產出1~40的亂數號碼
            temp = (int)(Math.random() * 40) + 1;
            int j = 0;

            //當j的長度小於時，則近入判斷兩者號碼是否相同
            while (j < i){
                //把temp跟之前的數字比對，如有有重複就重新產生亂數
                if (num[j] == temp){
                   temp = (int) (Math.random() * 40) + 1;
                    j = 0;
                } else {
                    j++;
                }
            }

            //沒有重複temp就把i值給陣列
            num[i] = temp;
            // Log.e(TAG, "沒有排序: " + "x[" + i + "]" + "=" + num[i] + " ");
        }

        //先排序
        bubbleSort(num);
        //排完序後，再跑一次迴圈
        for (i = 0; i < num.length; i++){
            //產出七個大福彩號碼
            if (i < 7){
                String lotteNum = String.valueOf("第" + (i + 1) + "個號碼:") + num[i] + "'\n";
                // Log.e(TAG, "lotteNum: " + lotteNum);

                //如果num[i]的數值小於10，則前面補上0
                if (num[i] < 10){
                    String numStr = "0" + num[i] + " "; //就個位數字補上0 8=>08
                    String str = big_lotte_text.getText().toString() + numStr;
                    big_lotte_text.setText(str);
                }else {
                    String lotteStr = String.valueOf(num[i] + " ");
                    String str = big_lotte_text.getText().toString() + lotteStr;
                    big_lotte_text.setText(str);
                }


            } else {
                //特別號
                if (num[i] < 10) {
                    String numStr = "0" + num[i];
                    big_specialNum_text.setText("特別號:" + numStr);
                    // Log.e(TAG, "onClick: "+numStr );
                } else {
                    String spNumStr = String.valueOf(num[i]);
                    big_specialNum_text.setText("特別號:" + spNumStr);
                    // Log.e(TAG, "onClick: "+ sixNumStr );
                }
            }
        }

    }

    //由小到大排列
    private void bubbleSort(int[] x) {
        //1.先取得int[]x最大索引值，然後再遞減
        for (int i = x.length -1; i > 0; i-- ){
            //int j 只要小於i 就讓它跑一次
            for (int j = 0; j < i; j++){
                //如果x[j]索引位置的值大於x[j +1]索引位置的值,就把x[j]值丟到暫存tmp裡
                //最後會把兩數的位置交換
                if (x[j] > x[j + 1]){
                    int tmp = x[j]; //把x[j]裡的值丟到 tmp，x[j]就空了
                    x[j] = x[j + 1]; //再把x[j+1]的值丟到x[j]裡面去, x[j+1]就空了
                    x[j + 1] = tmp; //最後把先前的tmp的值給 x[j+1], 那x[j]和 x[j+1]的值就交換了
                }

            }
        }
    }

}

package com.admin.claire.lotto.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.database.LottoDAO;
import com.admin.claire.lotto.model.Betting;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Star4Fragment extends Fragment {

    private Button btn4Star_Random, btn4Star_Restart_random;
    private TextView lotte4Star_text;
    private LottoDAO mLottoDao;
    private Button btn_Save4StarBettingOn;
    private Betting betting = new Betting();


    public Star4Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_star4,container,false);
        lotte4Star_text = (TextView)rootView.findViewById(R.id.lotte_4Star_Text);
        btn4Star_Random = (Button)rootView.findViewById(R.id.btn4StarRandom);
        btn4Star_Restart_random = (Button)rootView.findViewById(R.id.btnRe4Star_random);
        btn_Save4StarBettingOn = (Button)rootView.findViewById(R.id.btn_4StarSaveBettingOn);

        initLotte4Star();

        return rootView;
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

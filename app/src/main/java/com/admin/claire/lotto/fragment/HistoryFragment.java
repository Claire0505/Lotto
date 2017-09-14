package com.admin.claire.lotto.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.claire.lotto.R;
import com.admin.claire.lotto.adapter.MyRecyclerViewAdapter;
import com.admin.claire.lotto.database.LottoDAO;
import com.admin.claire.lotto.model.Betting;
import com.admin.claire.lotto.swiper.SwipeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = HistoryFragment.class.getSimpleName();

    private LottoDAO lottoDAO;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private View rootView;
    Betting betting = new Betting();
    private Fragment mContext;
    private String spinnerLotte;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //要在片段中使用Menu要先設定它
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        initRecyclerView();
        //滑動刪除 搜尋時刪除資料時會有index 不同的問題暫拿
       // initSwipe();


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new quick Task
                addTaskDialog();
            }
        });

        return rootView;
    }


    private void initSwipe() {
         lottoDAO = new LottoDAO(getActivity());
        List<Betting> allBetting = lottoDAO.getAll();
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getActivity(), allBetting);
        //Swipe
        ItemTouchHelper.Callback callback = new SwipeHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }

    private void initRecyclerView() {

        lottoDAO = new LottoDAO(getActivity());
        List<Betting> allBetting = lottoDAO.getAll();

        if (allBetting.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new MyRecyclerViewAdapter(getActivity(), allBetting);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "目前沒有下注紀綠...", Toast.LENGTH_SHORT).show();
        }

    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.add_betting_layout, null);
        final EditText bettingField = (EditText) subView.findViewById(R.id.edit_betting);
        final TextView dateTimeFiled = (TextView) subView.findViewById(R.id.add_dateTime);

        //選擇樂透類型
        final Spinner spinnerLottoType = (Spinner) subView.findViewById(R.id.spinner_LottoType);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lotto_play_type_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerLottoType.setAdapter(spinnerAdapter);
        spinnerLottoType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), spinnerAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
                spinnerLotte = spinnerAdapter.getItem(position) + ":";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("新增投注號碼");
        dialog.setIcon(R.drawable.chinese_ingot);
        dialog.setView(subView);
        dialog.create();

        dialog.setPositiveButton("新增", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String bettingStr = bettingField.getText().toString();
                //將樂透種類的下拉式選單一併寫入 spinnerLotte
                betting.setBettingNum(spinnerLotte + bettingStr);
                betting.setDateCreated(new Date().getTime());

                if (TextUtils.isEmpty(bettingStr)) {
                    Toast.makeText(getActivity(), "Something went wrong. Check your input values", Toast.LENGTH_SHORT).show();
                } else {

                    lottoDAO.insertDB(betting);

                    /**
                     * 1.refresh the fragment  這是直接開啟另一個fragment
                     *  openFragment(new HistoryFragment());
                     *
                     * 2.getActivity().finish();
                     *   startActivity(getActivity().getIntent());
                     */

                    //3.是新增完直接重新run一次recycler
                    initRecyclerView();

                }

            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "取消投注", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false); //是否要點選搜尋圖示後再打開輸入框
        searchView.setSubmitButtonEnabled(true); //輸入框後是否要加上送出的按鈕
        searchView.setQueryHint("Search Here");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        List<Betting> allBetting = lottoDAO.getAll();
        adapter = new MyRecyclerViewAdapter(getActivity(), allBetting);

        adapter.filter(newText);
        recyclerView.setAdapter(adapter);

        return true;

    }


}

package com.example.jeju_makcha;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class fragment_2 extends Fragment {

    private SQLiteDatabase database;
    private View view;
    private String TAG = "페이지 2";

    private RecyclerView recyclerView;
    private Handler handler;
    private Runnable runnable;
    private BusAdapter busAdapter;
    private BusRoute busRoute;
    private ArrayList<String> itemList;
    private DBHelper dbHelper;
    private List<String> favoritesList;


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "페이지2 OnCreateView");
        view = inflater.inflate(R.layout.fragment_frag2, container, false);
        recyclerView = view.findViewById(R.id.frag2_recycler);

        favoritesList = new ArrayList<>();
        busRoute = new BusRoute(requireContext());
        busAdapter = new BusAdapter(favoritesList, "", getContext());


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(busAdapter);

        if (view != null) {
            updateCurrentTime();
        } else {
            // view가 null인 경우 예외 처리
        }

        return view;
    }

    private void updateCurrentTime() {
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                updateDataAndUI();

                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    private void updateDataAndUI() {
        DBHelper dbHelper = new DBHelper(getActivity());
        List<String> dbDataList = dbHelper.getAllFavorites();

        List<String> displayList = new ArrayList<>();
        for (String item : dbDataList) {
            String[] tokens = item.split("\n");

            if (tokens.length >= 2) {
                String time = tokens[1];
                String remainingTime = TimeUtil.calculateRemainingTime(time);
                String result = formatDataForDisplay(tokens[0], time, remainingTime);
                displayList.add(result);
            } else {
                // 데이터가 올바른 형식이 아닌 경우 처리
            }
        }

        Collections.sort(displayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String[] tokens1 = s1.split("\\n");
                String[] tokens2 = s2.split("\\n");
                String remainingTime1 = tokens1[2];
                String remainingTime2 = tokens2[2];
                return remainingTime1.compareTo(remainingTime2);
            }
        });

        busAdapter.setItemList(displayList);
        busAdapter.notifyDataSetChanged();
    }

    private String formatDataForDisplay(String number, String time, String remainingTime) {
        return number + "\n" + time + "\n" + remainingTime;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}

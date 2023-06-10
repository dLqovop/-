package com.example.jeju_makcha;


import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class fragment_1 extends Fragment {

    private SQLiteDatabase database;

    private Handler handler;
    private Runnable runnable;

    private View view;
    private String TAG = "페이지 1";
    private Button favoriteButton;

    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    ArrayList<String> itemList;

    public void setBusAdapter(BusAdapter adapter) {
        busAdapter = adapter;
    }
    public List<String> getItemList() {
        if (busAdapter != null) {
            return busAdapter.getItemList();
        }
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"페이지1 OnCreateView");
        view = inflater.inflate(R.layout.fragment_frag1, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        favoriteButton = view.findViewById(R.id.favorite);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        itemList = new ArrayList<String>() ; //초기화 1
        busAdapter = new BusAdapter(itemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(busAdapter);

        if (view != null) {
            updateCurrentTime();
        } else {
            // view가 null인 경우 예외 처리
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);

        // 데이터베이스 연결 종료
        if (database != null && database.isOpen()) {
            database.close();
        }

    }

    private void updateCurrentTime() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                // 현재 시간 가져오기
                doGetRemainingTime();

                // 일정한 간격으로 업데이트 실행
                handler.postDelayed(this, 1000); // 1초마다 업데이트
            }
        }, 0); // 처음에는 즉시 업데이트 시작
    }

    private void doGetRemainingTime() {
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("bus.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            itemList.clear(); // itemList 초기화

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens.length >= 3 && tokens[2].equals("휴일")) {
                    continue;
                } else {
                    String time = tokens[1]; // 인덱스 1번의 시간 데이터
                    String remainingTime = TimeUtil.calculateRemainingTime(time); // 현재 시간과의 차이 계산
                    String result = tokens[0] + "\n" + time + "\n" + remainingTime;

                    // 세 줄씩 묶어서 하나의 카드로 출력
                    itemList.add(result);
                    if (itemList.size() % 3 == 0) {
                        busAdapter.notifyItemInserted(itemList.size() - 1);
                    }
                }
            }

            reader.close();

            // 남은 시간 기준으로 오름차순 정렬
            Collections.sort(itemList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    String[] tokens1 = s1.split("\\n");
                    String[] tokens2 = s2.split("\\n");
                    String remainingTime1 = tokens1[2];
                    String remainingTime2 = tokens2[2];
                    return remainingTime1.compareTo(remainingTime2);
                }
            });

            busAdapter.notifyDataSetChanged();

            busAdapter.setOnItemClickListener(new BusAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String selectedItem = busAdapter.getItemList().get(position);
                    String[] tokens = selectedItem.split("\n");
                    String itemToSave = tokens[0] + "\n" + tokens[1]; // tokens[0]와 tokens[1]만 저장하도록 수정
                    DBHelper dbHelper = new DBHelper(requireContext());
                    dbHelper.insertFavorite(itemToSave);
                }
            });

        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
    }
}
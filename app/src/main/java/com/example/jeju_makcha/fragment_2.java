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

public class fragment_2 extends Fragment  {

    private SQLiteDatabase database;
    private View view;
    private String TAG = "페이지 2";

    private RecyclerView recyclerView;
    private Handler handler;
    private Runnable runnable;
    private BusAdapter busAdapter;
    ArrayList<String> itemList;
    private DBHelper dbHelper;

    private List<String> favoritesList;


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"페이지2 OnCreateView");
        view = inflater.inflate(R.layout.fragment_frag2, container, false);
        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.frag2_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        busAdapter = new BusAdapter(favoritesList);
        recyclerView.setAdapter(busAdapter);

        if (view != null) {
            updateCurrentTime();
        } else {
            // view가 null인 경우 예외 처리
        }
        // onResume() 메서드에서 runnable을 실행하도록 처리
//        handler.post(updateCurrentTime().postDelayed);
        if (handler == null) {
            handler = new Handler();
        }

        return view;
    }

    private void updateCurrentTime() {
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                // 현재 시간 가져오기
                updateDataAndUI();

                // 일정한 간격으로 업데이트 실행
                handler.postDelayed(this, 1000); // 1초마다 업데이트
            }
        }, 0); // 처음에는 즉시 업데이트 시작
    }

    private void updateDataAndUI() {
        DBHelper dbHelper = new DBHelper(getActivity());
        List<String> dbDataList = dbHelper.getAllFavorites(); // DB에서 데이터를 가져온다고 가정

        // 시간 계산 및 화면에 출력
        List<String> displayList = new ArrayList<>();
        for (String item : dbDataList) {
            String[] tokens = item.split("\n"); // 데이터를 줄바꿈 문자("\n")로 분리

            String time = tokens[1]; // DB에서 시간 추출하는 부분을 구현해야 함
            String remainingTime = TimeUtil.calculateRemainingTime(time); // 현재 시간과의 차이 계산
            String result = formatDataForDisplay(tokens[0], time, remainingTime); // 화면에 표시할 데이터 형식 지정
            displayList.add(result);
        }

// 남은 시간 기준으로 오름차순 정렬
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
        busAdapter.setItemList(displayList); // 데이터 설정
        busAdapter.notifyDataSetChanged(); // 변경 사항 갱신
    }

    private String formatDataForDisplay(String number, String time, String remainingTime) {
        return number + "\n" + time + "\n" + remainingTime;
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
}

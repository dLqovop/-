package com.example.jeju_makcha;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class fragment_1 extends Fragment {

    private Handler handler;
    private Runnable runnable;

    private View view;
    private String TAG = "페이지 1";

    // TextView 식별자 배열
    int[] textViewIds = {R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7, R.id.text8, R.id.text9, R.id.text10,
            R.id.text11, R.id.text12, R.id.text13, R.id.text14, R.id.text15, R.id.text16, R.id.text17, R.id.text18, R.id.text19, R.id.text20,};

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"페이지1 OnCreateView");
        view = inflater.inflate(R.layout.fragment_frag1, container, false);
        return view;
    }

    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        handler = new Handler();
        updateCurrentTime();
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
            int index = 0;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens.length >= 3 && tokens[2].equals("휴일")) {
                    continue;
                }else {
                    String time = tokens[1]; // 인덱스 1번의 시간 데이터
                    String remainingTime = getRemainingTime(time); // 현재 시간과의 차이 계산
                    String result = tokens[0] + ": " + remainingTime;


                    final int currentIndex = index;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Remaining Time", String.valueOf(result));

                            TextView textView = getView().findViewById(textViewIds[currentIndex]);
                            textView.setText(result);
                        }
                    });
                    index++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
    }
    private String getRemainingTime(String time) {
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 버스 시간 파싱
        String[] tokens = time.split(":");
        int busHour = Integer.parseInt(tokens[0]);
        int busMinute = Integer.parseInt(tokens[1]);

        // 남은 시간 계산
        int remainingHour = busHour - currentHour;
        int remainingMinute = busMinute - currentMinute;

        // 음수인 경우 24시간을 더해줌
        if (remainingHour < 0 || (remainingHour == 0 && remainingMinute < 0)) {
            remainingHour += 24;
        }

        // 분이 음수인 경우 시간에서 1을 빼고 분에 60을 더해줌
        if (remainingMinute < 0) {
            remainingHour--;
            remainingMinute += 60;
        }


//        return remainingHour * 60 + remainingMinute;

        String remainingTime = String.format("%02d:%02d", remainingHour, remainingMinute);

        return remainingTime;

    }
}

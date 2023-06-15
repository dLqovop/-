package com.example.jeju_makcha;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_3 extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private EditText minutesEditText;
    private Button settingsButton;
    private DBHelper dbHelper;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag3, container, false);


        minutesEditText = view.findViewById(R.id.minuteEditText);
        settingsButton = view.findViewById(R.id.settingsButton);
        dbHelper = new DBHelper(getActivity());
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmReceiverIntent = new Intent(requireContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        settingsButton.setOnClickListener(v -> {
            String minutesString = minutesEditText.getText().toString();
            if (!minutesString.isEmpty()) {
                int minutes = Integer.parseInt(minutesString);
                long delayMillis = minutes * 60 * 1000; // 분을 밀리초로 변환

                // 이전에 설정된 알람을 취소
                alarmManager.cancel(alarmIntent);

                // 현재 시간에 delayMillis를 더하여 알람 설정
                long triggerTimeMillis = System.currentTimeMillis() + delayMillis;
                Log.d("시간","시간 : "+triggerTimeMillis);
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, alarmIntent);

                // 사용자 설정 저장
                dbHelper.insertUserSetting(minutes);

                Toast.makeText(getActivity(), "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "분을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

package com.example.jeju_makcha;

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
    private EditText minutesEditText;
    private Button settingsButton;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag3, container, false);

        minutesEditText = view.findViewById(R.id.minuteEditText);
        settingsButton = view.findViewById(R.id.settingsButton);
        dbHelper = new DBHelper(getActivity());


        settingsButton.setOnClickListener(v -> {
            String minutesString = minutesEditText.getText().toString();
            if (!minutesString.isEmpty()) {
                int minutes = Integer.parseInt(minutesString);
                SQLiteDatabase db = dbHelper.getWritableDatabase(); // 또는 getReadableDatabase()
                dbHelper.onCreate(db);
                dbHelper.insertUserSetting(minutes);
                Toast.makeText(getActivity(), "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "분을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}


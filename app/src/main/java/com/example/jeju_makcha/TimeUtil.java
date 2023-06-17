package com.example.jeju_makcha;


import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeUtil {
    // 시간 계산 메서드
    public static String calculateRemainingTime(String time) {
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 버스 시간 파싱
        String[] tokens = time.split(":");
        int busHour = Integer.parseInt(tokens[0]);
        int busMinute = Integer.parseInt(tokens[1].replaceAll(" 출발", ""));

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

        String remainingTime = String.format("%02d시 %02d분 뒤에 출발", remainingHour, remainingMinute);
        return remainingTime;
    }
}


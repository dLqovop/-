package com.example.jeju_makcha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알림 생성 및 표시
        createNotificationChannel(context);
        showNotification(context);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context) {

        // 알림 클릭 시 실행될 액티비티 지정
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // 알림 생성
        Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle("버스 출발하기 전입니다.")
                .setContentText("버스 시간표를 확인하세요.")
                .setSmallIcon(R.drawable.logo_blue)
                .setContentIntent(pendingIntent) // 클릭 이벤트 설정
                .setAutoCancel(true); // 클릭 후 알림이 자동으로 사라지도록 설정

        // 알림 표시
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            Log.d("시발","시발");
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            // 알림이 비활성화되어 있음
            // 사용자에게 알림을 활성화하도록 안내할 수 있음
            Log.d("시발","비활성화");
        }
    }
}

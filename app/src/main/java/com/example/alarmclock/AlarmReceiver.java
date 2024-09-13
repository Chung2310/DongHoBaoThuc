package com.example.alarmclock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    final String Channel_id = "201";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "MyAction".equals(intent.getAction())) {
            String time = intent.getStringExtra("time");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = notificationManager.getNotificationChannel(Channel_id);
                if (channel == null) {
                    channel = new NotificationChannel(Channel_id, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("Miêu tả cho kênh 1");
                    notificationManager.createNotificationChannel(channel);
                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Channel_id)
                    .setContentTitle("Báo Thức " + time)
                    .setContentText("Dậy đi! " + time + " rồi!")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setColor(Color.RED)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setCategory(NotificationCompat.CATEGORY_ALARM);

            notificationManager.notify(getNotificationId(), builder.build());
        }
    }

    private int getNotificationId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}

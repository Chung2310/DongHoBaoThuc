package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    final String Channel_id = "201";
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();  // Khởi tạo Handler

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "MyAction".equals(intent.getAction())) {
            String time = intent.getStringExtra("time");
            Log.d("AlarmReceiver", "Alarm triggered at: " + time);

            mediaPlayer = MediaPlayer.create(context, R.raw.ambao);
            mediaPlayer.start();

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

            // Dừng nhạc sau 1 phút và kiểm tra xem báo thức có còn hoạt động không
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (isAlarmStillActive(context)) {
                        setNextAlarm(context);
                    }
                }
            }, 60000);  // 60000 milliseconds = 1 phút
        }
    }

    private int getNotificationId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    private void setNextAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("MyAction");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 10);  // 10 phút sau

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Log.d("AlarmReceiver", "Alarm rescheduled for 10 minutes later.");
    }

    private boolean isAlarmStillActive(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);

        return pendingIntent != null;
    }
}

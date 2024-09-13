package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TimePicker timePicker;
    Button btnDat,btnDung;
    TextView textView;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        btnDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Dừng đặt giờ!");
                pendingIntent.cancel();
            }
        });
        btnDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                int gio = timePicker.getHour();
                int phut = timePicker.getMinute();
                if(gio>12) {
                    gio = gio - 12;
                }
                textView.setText("Thời gian đặt giờ:"+gio+":"+phut);
                Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
                intent.setAction("MyAction");
                intent.putExtra("time",gio+":"+phut);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

            }
        });
    }
    private void anhXa(){
        timePicker = findViewById(R.id.timePicker);
        btnDat = findViewById(R.id.btnDatGio);
        btnDung = findViewById(R.id.btnDungDatGio);
        textView = findViewById(R.id.textView);
    }
}
package com.example.bonaventurajason.mycataloguemoviebd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonaventurajason.mycataloguemoviebd.Notif.DailyReminderReceiver;
import com.example.bonaventurajason.mycataloguemoviebd.Notif.ReleaseTodayReceiver;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.daily_switch)
    Switch daily_switch;
    @BindView(R.id.release_switch) Switch release_switch;
    @BindView(R.id.languages)
    TextView languages;

    private static final String TAG = SettingsActivity.class.getSimpleName();
    AlarmManager dailyAlarmManager;
    PendingIntent pendingIntent;
    PendingIntent releasePendingIntent;
    AlarmManager releaseManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("com.example.bonaventurajason.mycataloguemoviebd", MODE_PRIVATE);
        editorPreferences = sharedPreferences.edit();
        ButterKnife.bind(this);

        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intentSettings);
            }
        });
        daily_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editorPreferences.putBoolean("dailySwitch", true).apply();
//                    Alarm 1
                    Intent dailyIntent = new Intent(SettingsActivity.this, DailyReminderReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this,
                            0, dailyIntent, 0);
                    dailyAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Calendar dailyCalendar = Calendar.getInstance();
                    dailyCalendar.setTimeInMillis(System.currentTimeMillis());
                    dailyCalendar.set(Calendar.HOUR_OF_DAY, 7);
                    dailyCalendar.set(Calendar.MINUTE, 0);
                    dailyCalendar.set(Calendar.SECOND, 0);
                    if (dailyCalendar.getTime().before(new Date(System.currentTimeMillis()))) {
                        dailyCalendar.add(Calendar.DATE, 1);
                    }
                    dailyAlarmManager.setInexactRepeating(AlarmManager.RTC, dailyCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    Toast.makeText(SettingsActivity.this, "Daily reminder notification will appear every 7 a.m.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Daily reminder disabled", Toast.LENGTH_SHORT).show();
                    editorPreferences.putBoolean("dailySwitch", false).apply();
                    dailyAlarmManager.cancel(pendingIntent);
                }
            }
        });
        release_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editorPreferences.putBoolean("releaseSwitch", true).apply();
//                    Alarm 2
                    Intent releaseIntent = new Intent(SettingsActivity.this, ReleaseTodayReceiver.class);
                    releasePendingIntent = PendingIntent.getBroadcast(SettingsActivity.this,
                            1, releaseIntent, 0);
                    releaseManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar releaseCalendar = Calendar.getInstance();
                    releaseCalendar.setTimeInMillis(System.currentTimeMillis());
                    releaseCalendar.set(Calendar.HOUR_OF_DAY, 8);
                    releaseCalendar.set(Calendar.MINUTE, 0);
                    releaseCalendar.set(Calendar.SECOND, 0);
                    if (releaseCalendar.getTime().before(new Date(System.currentTimeMillis()))) {
                        releaseCalendar.add(Calendar.DATE, 1);
                    }
                    releaseManager.setInexactRepeating(AlarmManager.RTC, releaseCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, releasePendingIntent);
                    Toast.makeText(SettingsActivity.this, R.string.release_enable_text, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, R.string.release_disabled_text, Toast.LENGTH_SHORT).show();
                    editorPreferences.putBoolean("releaseSwitch", false).apply();
                    releaseManager.cancel(releasePendingIntent);
                }
            }
        });
        daily_switch.setChecked(sharedPreferences.getBoolean("dailySwitch", false));
        release_switch.setChecked(sharedPreferences.getBoolean("releaseSwitch", false));
    }
}

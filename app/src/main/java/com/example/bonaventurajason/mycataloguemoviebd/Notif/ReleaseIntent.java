package com.example.bonaventurajason.mycataloguemoviebd.Notif;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.example.bonaventurajason.mycataloguemoviebd.MainActivity;
import com.example.bonaventurajason.mycataloguemoviebd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class ReleaseIntent extends IntentService {

    public static int NOTIFICATION_ID = 2;
    public static final String CHANNEL_ID = "2";
    private static final String TAG = ReleaseIntent.class.getSimpleName();


    public ReleaseIntent() {
        super(ReleaseIntent.class.getSimpleName());
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        Ambil tanggal sekarang
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);


        Date today = calendar.getTime();
        URL endpoint = null;


        try {
            endpoint = new URL("https://api.themoviedb.org/3/movie/now_playing?api_key=72fbac04db947004b7b94fe4b9aa6eb4&language=en-US");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Membuat koneksi
        HttpsURLConnection apiConnection;
        try {
            apiConnection = (HttpsURLConnection) endpoint.openConnection();
            if (apiConnection.getResponseCode() == 200) {
                // Baca dari JSON
                BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();


                //JSON Object
                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray movieArray = jsonObject.getJSONArray("results");

                String inputString;
                DateFormat dateFormat;
                Date inputDate;

                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject movie = movieArray.getJSONObject(i);
                    inputString = movie.getString("release_date");
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    inputDate = dateFormat.parse(inputString);

                    if (today.equals(inputDate)) {
                        pushTodayNotification(movie.getString("title"));
                    }
                }
            }
            //diconnect connection
            apiConnection.disconnect();
        } catch (IOException | JSONException | ParseException e) {

            e.printStackTrace();
        }
    }

    private void pushTodayNotification(String title) {

        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        // Builder for notification
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(title)
                .setContentText(title + getResources().getString(R.string.release_today_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
        NOTIFICATION_ID++;
    }
}

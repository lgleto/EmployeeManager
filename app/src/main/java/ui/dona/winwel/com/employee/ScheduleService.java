package ui.dona.winwel.com.employee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleService extends Service {

    List<Employee> employees = new ArrayList<>();

    public static final String EMPLOYEES = "employees";
    Handler handler;
    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            // Check if sommething happen
            Date dateNow = Calendar.getInstance().getTime();

            for (Employee e : employees){
                if (
                        dateNow.getMonth()      == e.dateOfBirth.getMonth() &&
                        dateNow.getDay()        == e.dateOfBirth.getDay() &&
                        dateNow.getHours()      == 12 &&
                        dateNow.getMinutes()    == 33 &&
                        dateNow.getSeconds()    == 0){
                    sendNotification("It's  "+ e.getFirstName() + " birthday!");
                }
            }


            handler.postDelayed(runnable,500L);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler =  new Handler();
        runnable.run();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String [] empleeysStr = intent.getStringArrayExtra(EMPLOYEES);
        for (int i= 0; i<empleeysStr.length; i++){
            try {
                employees.add(Employee.fromJson(new JSONObject(empleeysStr[i])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_notifications)
                        .setContentTitle(messageBody)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

package com.example.broadcastreceiver;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {
    private static final String PRODUCTS_APP_PACKAGE_NAME = "com.example.miniprojekt1";
    private static final String PRODUCTS_APP_PRODUCT_ACTIVITY_LIST = "com.example.miniprojekt1.ProductListActivity";
    private final String channelID = "channel1";
    private int id = 0;

    private final IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    void sendNotification(Context context, Intent intent) {
        String productName = intent.getStringExtra("productName");

        Intent launchIntent = new Intent();
        launchIntent.setClassName(PRODUCTS_APP_PACKAGE_NAME, PRODUCTS_APP_PRODUCT_ACTIVITY_LIST);

        @SuppressLint("WrongConstant")
        PendingIntent pendint = PendingIntent.getActivity(context, 0, launchIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        createNotificationChannel(context);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dodano nowy produkt: " + productName)
                .setContentText("Kliknij, aby przejść do listy produktów")
                .setContentIntent(pendint)
                .setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id++, notif.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // SDK >= 26
            CharSequence name = String.valueOf(R.string.my_channel);
            String description = String.valueOf(R.string.my_chan_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);

            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }
}

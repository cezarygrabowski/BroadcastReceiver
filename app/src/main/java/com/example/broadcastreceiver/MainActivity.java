package com.example.broadcastreceiver;

import android.content.*;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String PRODUCT_ADDED_ACTION = "com.example.product.added";
    private MyReceiver mbr = new MyReceiver();
    public static NotificationService notificationService;
    private boolean mBound = false;

    private ServiceConnection mcon = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            NotificationService.MyBinder binder = (NotificationService.MyBinder) service;
            notificationService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(PRODUCT_ADDED_ACTION);
        registerReceiver(mbr, filter, "com.example.my_permissions.MY_PERMISSION", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbr);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NotificationService.class);
        bindService(intent, mcon, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mcon);
            mBound = false;
        }
    }
}

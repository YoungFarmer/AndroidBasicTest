package com.example.anotherapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class MyReceiver2 extends BroadcastReceiver {
    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = intent.getExtras().getString("name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("广播2来了！！！");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("电量不足！！！");
        builder.setContentText("电量还剩10%"+"快通知："+name);
        manager.notify(1001, builder.build());

    }
}
package com.example.appaidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver22 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getExtras().getString("name");
        Toast.makeText(context, "Boardcast Two TWO " + name, Toast.LENGTH_SHORT).show();
    }
}
package com.example.anotherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver3 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getExtras().getString("name");
        Toast.makeText(context, "Boardcast Three " + name, Toast.LENGTH_SHORT).show();
    }
}
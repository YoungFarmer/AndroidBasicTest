package com.example.appaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AppService extends Service {
    public AppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        System.out.println("OnBinder!!!");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AppService Start!!");
        Log.i("APPService","AppService Start!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("AppService Destroy!!");
        Log.i("APPService","AppService Destroy!!");
    }

    private final IMyAidlInterface.Stub binder =  new IMyAidlInterface.Stub(){

        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }
    };

}
package com.example.anotherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anotherapp.fragment.ItemViewModel;
import com.example.anotherapp.fragment.TestFragment;
import com.example.appaidl.IMyAidlInterface;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AIDL";
    Button btnStart,btnDestory,btnAIDL, btnUnbindAIDL, btnBoardcast, btnSystemProvider, btnElse;
    private IMyAidlInterface mService = null;
    private ItemViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnDestory = findViewById(R.id.btnDestory);
        btnAIDL = findViewById(R.id.btnAIDL);
        btnUnbindAIDL = findViewById(R.id.btnUnbindAIDL);
        btnBoardcast = findViewById(R.id.btnBoardcast);
        btnSystemProvider = findViewById(R.id.btnSystemProvider);
        btnElse = findViewById(R.id.btnElse);

        btnStart.setOnClickListener(this);
        btnDestory.setOnClickListener(this);
        btnAIDL.setOnClickListener(this);
        btnUnbindAIDL.setOnClickListener(this);
        btnBoardcast.setOnClickListener(this);
        btnSystemProvider.setOnClickListener(this);
        btnElse.setOnClickListener(this);

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this, 2);
        dbOpenHelper.getReadableDatabase();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        TestFragment testFragment = new TestFragment();
        fragmentTransaction.add(R.id.list, testFragment);
        fragmentTransaction.commit();

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, item -> {
            // Perform an action with the latest item data
            CharSequence text = item.getText();
            Log.i(TAG, "text: "+ text);
            System.out.println("text " + text);
        });

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMyAidlInterface.Stub.asInterface(service);
            Log.i(TAG, "onServiceConnected: Success!");
            try {
                int a = 1;
                int b = 2;
                int sum = 0;
                sum = mService.add(a, b);
                Log.i(TAG, "value is " + sum);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected!!");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.appaidl","com.example.appaidl.AppService"));
            startService(intent);
        } else if (v.getId() == R.id.btnDestory) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.appaidl","com.example.appaidl.AppService"));
            stopService(intent);
        } else if (v.getId() == R.id.btnAIDL) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.appaidl", "com.example.appaidl.AppService"));
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        } else if (v.getId() == R.id.btnUnbindAIDL) {
            unbindService(mConnection);
        } else if (v.getId() == R.id.btnBoardcast) {
//            IntentFilter intentFilter = new IntentFilter();
//            MyReceiver1 myReceiver1 = new MyReceiver1();
//            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
//            Log.i(TAG, "btnBoardcast is clicked");
//            localBroadcastManager.registerReceiver(myReceiver1, intentFilter);

            Intent intent = new Intent();
            intent.setAction("myaction");
            intent.setPackage("com.example.appaidl");
            intent.putExtra("name","This is Boardcast!");

//            intent.setComponent(new ComponentName("com.example.anotherapp", "com.example.anotherapp.MyReceiver1"));
//            intent.setPackage("com.example.anotherapp");
            getApplication().sendBroadcast(intent);
        } else if(v.getId() == R.id.btnSystemProvider) {

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            }
        } else {
            System.out.println("11111");
        }

    }

    private void showInContextUI() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                    Toast.makeText(this, "No Permission!!", Toast.LENGTH_SHORT).show();
                }
            }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readContacts() {
        Log.i(TAG, "There is readContacts!!");
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null);
        if (cursor != null) {
            Log.i(TAG, "cursor is not null !!");

            while(cursor.moveToNext()) {
                String name = String.valueOf(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                Log.i(TAG, "name is " + name);
                String num = String.valueOf(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i(TAG, "num is " + num);
                Log.i(TAG, "name is " + name + "num is " + num);
            }
        } else {
            Log.i(TAG, "cursor is null !!");
        }

        cursor.close();
    }
}
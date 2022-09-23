package com.example.appaidl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private EditText nameText;
    private EditText ageText;
    private EditText heightText;
    private Button btnInsert,btnqueryAll;
    private ContentResolver contentResolver;
    private TextView labelView,displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.btnInsert);
        btnqueryAll = findViewById(R.id.btnqueryAll);
        nameText = findViewById(R.id.nameText);
        ageText = findViewById(R.id.ageText);
        heightText = findViewById(R.id.heightText);
        labelView = findViewById(R.id.labelView);
        displayView = findViewById(R.id.displayView);
        contentResolver = this.getContentResolver();


        btnInsert.setOnClickListener(this);
        btnqueryAll.setOnClickListener(this);

    }


    @SuppressLint("Range")
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnInsert) {
            ContentValues values = new ContentValues();
            values.put(People.KEY_NAME, nameText.getText().toString());
            values.put(People.KEY_AGE, ageText.getText().toString());
            values.put(People.KEY_HEIGHT, heightText.getText().toString());
            Uri newUri = contentResolver.insert(People.CONTENT_URI, values);
            labelView.setText("add success!!" + newUri);
        } else if(v.getId() == R.id.btnqueryAll) {

            Cursor cursor = contentResolver.query(People.CONTENT_URI, new String[]{People.KEY_ID, People.KEY_NAME, People.KEY_AGE, People.KEY_HEIGHT},
                    null, null, null);

            if(cursor == null) {
                labelView.setText("database:" + String.valueOf(cursor.getCount()) + "records");
            }
                StringBuilder msg = new StringBuilder();
                if(cursor.moveToFirst()) {
                    do {
                        msg.append("ID: ").append(cursor.getString(cursor.getColumnIndex(People.KEY_ID))).append(",");
                        msg.append("姓名: ").append(cursor.getString(cursor.getColumnIndex(People.KEY_NAME))).append(",");
                        msg.append("年龄: ").append(cursor.getInt(cursor.getColumnIndex(People.KEY_AGE))).append(",");
                        msg.append("身高: ").append(cursor.getFloat(cursor.getColumnIndex(People.KEY_HEIGHT))).append(",");
                    } while (cursor.moveToNext());
                }
                displayView.setText(msg);

        }
    }
}
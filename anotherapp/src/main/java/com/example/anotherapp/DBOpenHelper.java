package com.example.anotherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public  class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_Name = "BookStore.db";
    private static final String TAG = "DB";
    private static String TABLE_BOOK_NAME ="Book";
    private static String COLUMN_NAME = "name";
    private static String COLUMN_AUTHOR = "author";
    private static String COLUMN_PRICE = "price";
    private static String COLUMN_PAGES = "page";

    private static String CREATE_TABLE_SQL = "create table " + TABLE_BOOK_NAME + " ( " +
            "id integer primary key autoincrement, " +
            COLUMN_NAME + "text, " +
            COLUMN_AUTHOR + "text," +
            COLUMN_PRICE + "real," +
            COLUMN_PAGES + "integer)";




    public DBOpenHelper(@Nullable Context context,int version) {
        super(context, DB_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: DB");
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: DB");
    }
}

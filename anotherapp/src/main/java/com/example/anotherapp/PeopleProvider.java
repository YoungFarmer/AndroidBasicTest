package com.example.anotherapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class PeopleProvider extends ContentProvider {
    public PeopleProvider() {
    }

    public static final String DB_NAME = "people.db";
    public static final String DB_TABLE = "peopleinfo";
    public static final int DB_VERSION = 1;

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public static final int MULTIPLE_PEOPLE = 1;
    public static final int SINGLE_PEOPLE = 2;
    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(People.AUTHORITY, People.PATH_MULTIPLE, MULTIPLE_PEOPLE);
        uriMatcher.addURI(People.AUTHORITY, People.PATH_SINGLE, SINGLE_PEOPLE);
    }

    @Override
    public boolean onCreate() {
        Log.i("appApp", "PeopleProvider onCreate: " + getContext().getApplicationContext());
        Context context = getContext();
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        db = dbOpenHelper.getWritableDatabase();

        Log.i("appApp", "PeopleProvider onCreate: " + getContext().getPackageManager().resolveContentProvider("com.example.anotherapp", 0).initOrder);
        if(db == null){
            return false;
        }else{
            return true;
        }

    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MULTIPLE_PEOPLE:
                count = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            case SINGLE_PEOPLE:
                String segment = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE, People.KEY_ID + "=" + segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case MULTIPLE_PEOPLE:
                return People.MIME_TYPE_MULTIPLE;
            case SINGLE_PEOPLE:
                return People.MIME_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkown uro:"+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id =db.insert(DB_TABLE, null, values);
        if(id>0){
            Uri newUri = ContentUris.withAppendedId(People.CONTENT_URI,id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("failed to insert row into " + uri);

    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE);
        switch (uriMatcher.match(uri)){
            case SINGLE_PEOPLE:
                qb.appendWhere(People.KEY_ID+"="+uri.getPathSegments().get(1));
                break;
            default:
                break;
        }
        Cursor cursor = qb.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch(uriMatcher.match(uri)) {
            case MULTIPLE_PEOPLE:
                count = db.update(DB_TABLE, values, selection, selectionArgs);
                break;
            case SINGLE_PEOPLE:
                String segment = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE, values, People.KEY_ID + "=" + segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknow URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final  String DB_CREATE = "create table "+
                DB_TABLE+"("+People.KEY_ID+" integer primary key autoincrement, "+
                People.KEY_NAME+" text not null, "+People.KEY_AGE+" integer, "+
                People.KEY_HEIGHT+" float);";

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DB_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);

        }
    }


}
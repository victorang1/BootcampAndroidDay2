package com.example.latihanvolley;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Bootcamp2.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private Cursor cursor;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE User(" +
                "username TEXT," +
                "password TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean registerUser(User user) {
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            db.insert("User", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(User user) {
        try {
            db = getReadableDatabase();
            String query = String.format("SELECT * FROM User WHERE username = \"%s\" AND password = \"%s\"", user.getUsername(), user.getPassword());
            cursor = db.rawQuery(query, new String[]{});
            if(cursor.moveToFirst()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.example.tomek.passwordgenerator.Helper;

import android.content.ContentValues;
import android.content.Context;

import com.example.tomek.passwordgenerator.Bank;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

//import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tomek on 10.07.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "pss.db";
    public static final String TABLE_NAME = "STORE";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_PASS = "password";
    public static final String PASS_PHARSE = Installation.class.toString();
    private static final int DATABASE_VER = 1;
    private static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_WEBSITE + " TEXT PRIMARY KEY, " + COLUMN_PASS + " TEXT)";
    private static final String SQL_DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static DBHelper instance;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    static public synchronized DBHelper getInstance(Context context) {
        if (instance == null) instance = new DBHelper(context);
        return instance;
    }

    public static String toCopy(String dataTocCopy) {
        String word = "";
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        Cursor cursor = db.rawQuery(String.format(("SELECT * FROM '%s' WHERE " + COLUMN_WEBSITE + "='" + dataTocCopy + "';"), TABLE_NAME), null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                word = cursor.getString((cursor.getColumnIndex(COLUMN_PASS)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return word;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }

    public void insertWebsite(String website) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEBSITE, website);
        values.put(COLUMN_PASS, Bank.addition1());
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void updateWebsite(String oldWebsite, String newwebsite, String newpass) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEBSITE, newwebsite);
        values.put(COLUMN_PASS, newpass);
        db.update(TABLE_NAME, values, COLUMN_WEBSITE + "='" + oldWebsite + "'", null);
        db.close();
    }

    public void deleteWebsite(String website) {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEBSITE, website);
        //values.put(COLUMN_PASS, PASS_PHARSE);
        db.delete(TABLE_NAME, COLUMN_WEBSITE + "='" + website + "'", null);
        db.close();
    }

    public List<String> getAll() {
        SQLiteDatabase db = instance.getWritableDatabase(PASS_PHARSE);
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", TABLE_NAME), null);
        List<String> web = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String website = cursor.getString((cursor.getColumnIndex(COLUMN_WEBSITE)));
                web.add(website);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return web;
    }
    //    public static String secure(){
//        TelephonyManager tm =
//                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//    }
}

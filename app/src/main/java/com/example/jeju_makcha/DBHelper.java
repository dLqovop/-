package com.example.jeju_makcha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FAVORITE_ITEM = "favorite_item";
    private static final String TABLE_NAME_USER_SETTINGS = "user_settings";
    private static final String COLUMN_MINUTES = "minutes";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFavoritesTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FAVORITE_ITEM + " TEXT" +
                ")";
        db.execSQL(createFavoritesTableQuery);

        String createUserSettingsTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER_SETTINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MINUTES + " INTEGER" +
                ")";
        db.execSQL(createUserSettingsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropFavoritesTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITES;
        db.execSQL(dropFavoritesTableQuery);

        String dropUserSettingsTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_USER_SETTINGS;
        db.execSQL(dropUserSettingsTableQuery);

        onCreate(db);
    }

    public void insertFavorite(String item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_ITEM, item);
        long newRowId = db.insert(TABLE_NAME_FAVORITES, null, values);
        if (newRowId == -1) {
            Log.e("DBHelper", "Failed to insert favorite item");
        } else {
            Log.i("DBHelper", "Inserted favorite item: " + item);
        }
    }

    public List<String> getAllFavorites() {
        List<String> favorites = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FAVORITES, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int itemColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (itemColumnIndex != -1) {
                    String item = cursor.getString(itemColumnIndex);
                    favorites.add(item);
                }
            }
            cursor.close();
        }
        return favorites;
    }

    public void insertUserSetting(int minutes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MINUTES, minutes);
        long newRowId = db.insert(TABLE_NAME_USER_SETTINGS, null, values);
        if (newRowId == -1) {
            Log.e("DBHelper", "Failed to insert user setting");
        } else {
            Log.i("DBHelper", "Inserted user setting: " + minutes);
        }
    }
}


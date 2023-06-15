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
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_FAVORITES = "favorites";
    private static final String COLUMN_ID_FAVORITES = "id";
    public static final String COLUMN_ITEM_FAVORITES = "item";

    /////////////////////////////////////////////////////
    //알람 DB
    private static final String TABLE_NAME_USER_SETTINGS = "user_settings";
    private static final String COLUMN_MINUTES = "minutes";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFavoritesTableQuery = "CREATE TABLE " + TABLE_NAME_FAVORITES + " (" +
                COLUMN_ID_FAVORITES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_FAVORITES + " TEXT" +
                ")";
        Log.i("DB", "onCreate 생성 확인");
        db.execSQL(createFavoritesTableQuery);


        String createUserSettingsTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER_SETTINGS + " (" +
                COLUMN_ID_FAVORITES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MINUTES + " INTEGER" +
                ")";
        db.execSQL(createUserSettingsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITES;
        db.execSQL(dropTableQuery);

        //알람
        String dropUserSettingsTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_USER_SETTINGS;
        db.execSQL(dropUserSettingsTableQuery);

        onCreate(db);
    }

    public void insertFavorite(String item) {
        SQLiteDatabase db = getReadableDatabase();
        // 중복 체크
        if (isFavoriteItemExists(item, db)) {
            Log.i("DBHelper", "Favorite item already exists: " + item);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_FAVORITES, item);
        long newRowId = db.insert(TABLE_NAME_FAVORITES, null, values);
        if (newRowId == -1) {
            Log.e("DBHelper", "Failed to insert favorite item");
        } else {
            Log.i("DBHelper", "Inserted favorite item: " + item);
        }

    }
    private boolean isFavoriteItemExists(String item, SQLiteDatabase db) { //확장
        Cursor cursor = db.query(
                TABLE_NAME_FAVORITES, // 테이블 이름
                new String[]{COLUMN_ITEM_FAVORITES}, // 검색할 열
                COLUMN_ITEM_FAVORITES + "=?", // 조건
                new String[]{item}, // 조건 값
                null, null, null
        );
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public List<String> getAllFavorites() {
        List<String> favorites = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FAVORITES, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int itemColumnIndex = cursor.getColumnIndex(COLUMN_ITEM_FAVORITES);
                if (itemColumnIndex != -1) {
                    String item = cursor.getString(itemColumnIndex);
                    favorites.add(item);
                }
            }
            cursor.close();
        }
        return favorites;
    }

//알람
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

    public void deleteFavorite(String item) {
        SQLiteDatabase db = getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME_FAVORITES, COLUMN_ITEM_FAVORITES + "=?", new String[]{item});
        if (deletedRows > 0) {
            Log.i("DBHelper", "Deleted favorite item: " + item);
        } else {
            Log.e("DBHelper", "Failed to delete favorite item");
        }
    }

    public boolean isFavorite(String itemToCheck) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 아이템을 즐겨찾기 테이블에서 조회하여 존재 여부 확인
        Cursor cursor = db.query(TABLE_NAME_FAVORITES, new String[]{COLUMN_ITEM_FAVORITES}, COLUMN_ITEM_FAVORITES + "=?", new String[]{itemToCheck}, null, null, null);
        boolean isFavorite = (cursor != null && cursor.getCount() > 0);

        // 리소스 해제
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return isFavorite;
    }
}

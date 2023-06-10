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
    private static final String TABLE_NAME = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM = "item";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM + " TEXT" +
                ")";
        Log.i("DB", "onCreate 생성 확인");
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public void insertFavorite(String item) {
        SQLiteDatabase db = getReadableDatabase();
//        db = getWritableDatabase();

        // 중복 체크
        if (isFavoriteItemExists(item, db)) {
            Log.i("DBHelper", "Favorite item already exists: " + item);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM, item);
        long newRowId = db.insert(TABLE_NAME, null, values);
        if (newRowId == -1) {
            Log.e("DBHelper", "Failed to insert favorite item");
        } else {
            Log.i("DBHelper", "Inserted favorite item: " + item);
        }
    }
    private boolean isFavoriteItemExists(String item, SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME, // 테이블 이름
                new String[]{COLUMN_ITEM}, // 검색할 열
                COLUMN_ITEM + "=?", // 조건
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
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int itemColumnIndex = cursor.getColumnIndex(COLUMN_ITEM);
                if (itemColumnIndex != -1) {
                    String item = cursor.getString(itemColumnIndex);
                    favorites.add(item);
                }
            }
            cursor.close();
        }
        return favorites;
    }
}

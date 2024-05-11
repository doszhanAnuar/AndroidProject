package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу для пользователей
        String createTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT, " +
                "password TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Здесь можно добавить логику для обновления базы данных
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean authenticate(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Создаем запрос для поиска пользователя с указанными именем и паролем
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isAuthenticated = cursor.getCount() > 0;  // Если хотя бы одна запись найдена, аутентификация прошла успешно
        cursor.close();  // Закрываем курсор

        return isAuthenticated;
    }
}

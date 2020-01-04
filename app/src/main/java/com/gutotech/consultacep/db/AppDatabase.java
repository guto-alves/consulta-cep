package com.gutotech.consultacep.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ZipCodeEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "zip-codes-db";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        return instance;
    }

    public abstract ZipCodeDao getZipCodeDAO();
}

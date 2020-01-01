package com.gutotech.consultacep.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ZipCodes.db";
    public static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ZIPCODES_TABLE = "" +
                "CREATE TABLE " + ZipCodeEntry.TABLE_NAME + " (" +
                ZipCodeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ZipCodeEntry.ZIPCODE + " INTEGER UNIQUE NOT NULL, " +
                ZipCodeEntry.ADDRESS + " TEXT," +
                ZipCodeEntry.DISTRICT + " TEXT, " +
                ZipCodeEntry.CITY + " TEXT, " +
                ZipCodeEntry.STATE + " VARCHAR(2), " +
                ZipCodeEntry.LAT + " INTEGER, " +
                ZipCodeEntry.LNG + " INTEGER)";

        db.execSQL(CREATE_ZIPCODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

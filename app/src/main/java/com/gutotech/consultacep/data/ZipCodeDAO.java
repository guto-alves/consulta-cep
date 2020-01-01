package com.gutotech.consultacep.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gutotech.consultacep.model.ZipCode;

import java.util.ArrayList;
import java.util.List;

public class ZipCodeDAO {
    private Database database;

    public ZipCodeDAO(Context context) {
        database = new Database(context);
    }

    public long insert(ZipCode zipCode) {
        SQLiteDatabase db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ZipCodeEntry.ZIPCODE, zipCode.zipCode);
        values.put(ZipCodeEntry.ADDRESS, zipCode.address);
        values.put(ZipCodeEntry.DISTRICT, zipCode.district);
        values.put(ZipCodeEntry.CITY, zipCode.city);
        values.put(ZipCodeEntry.STATE, zipCode.state);
        values.put(ZipCodeEntry.LAT, zipCode.lat);
        values.put(ZipCodeEntry.LNG, zipCode.lng);

        return db.insert(ZipCodeEntry.TABLE_NAME, null, values);
    }

    public void queryAll(List<ZipCode> zipCodeList) {
        SQLiteDatabase db = database.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ZipCodeEntry.TABLE_NAME, null);

        zipCodeList.clear();

        while (cursor.moveToNext()) {
            zipCodeList.add(0, new ZipCode(
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.ZIPCODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.DISTRICT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.CITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.STATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.LAT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ZipCodeEntry.LNG))
            ));
        }

        cursor.close();
    }

    public void delete(String zipCode) {
        SQLiteDatabase db = database.getWritableDatabase();

        db.delete(ZipCodeEntry.TABLE_NAME, ZipCodeEntry.ZIPCODE + " LIKE ?", new String[]{zipCode});
    }

    public void close() {
        database.close();
    }
}

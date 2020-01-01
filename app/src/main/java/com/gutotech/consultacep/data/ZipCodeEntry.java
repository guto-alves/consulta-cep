package com.gutotech.consultacep.data;

import android.provider.BaseColumns;

public final class ZipCodeEntry implements BaseColumns {
    public static final String TABLE_NAME = "zipcodes";
    public static final String ZIPCODE = "cep";
    public static final String ADDRESS = "address";
    public static final String DISTRICT = "district";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
}


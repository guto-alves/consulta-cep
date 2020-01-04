package com.gutotech.consultacep.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "zipcodes")
public class ZipCodeEntity {
    @PrimaryKey
    @NonNull
    public final String zipCode;

    public final String address;
    public final String district;
    public final String city;
    public final String state;
    public final String lat;
    public final String lng;
    public long dateSearched;

    public ZipCodeEntity(@NonNull String zipCode, String address, String district, String city, String state, String lat, String lng, long dateSearched) {
        this.zipCode = zipCode;
        this.address = address;
        this.district = district;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.dateSearched = dateSearched;
    }
}

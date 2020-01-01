package com.gutotech.consultacep.model;

public class ZipCode {
    public final String zipCode;
    public final String address;
    public final String district;
    public final String city;
    public final String state;
    public final String lat;
    public final String lng;

    public ZipCode(String zipCode, String address, String district, String city, String state, String lat, String lng) {
        this.zipCode = zipCode;
        this.address = address;
        this.district = district;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
    }
}

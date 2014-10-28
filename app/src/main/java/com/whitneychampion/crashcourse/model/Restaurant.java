package com.whitneychampion.crashcourse.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by whitneychampion on 10/25/14.
 */
public class Restaurant implements Parcelable {

    private int id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;

    public Restaurant() {

    }

    public Restaurant(int id, String name, String address, String city, String state, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
            String.valueOf(this.id),
            this.name,
            this.address,
            this.city,
            this.state,
            this.country
        });
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.address = data[2];
        this.city = data[3];
        this.state = data[4];
        this.country = data[5];
    }
}

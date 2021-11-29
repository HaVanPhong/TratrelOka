package com.example.traveloka.Volley;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Account implements Serializable, Parcelable {
    private  int id;
    private String username;
    private String password;

    public Account() {
    }


    public Account(int id, String username, String password) {
        this.id= id;
        this.username = username;
        this.password = password;
    }

    protected Account(Parcel in) {
        id= in.readInt();
        username = in.readString();
        password = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Creator<Account> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
    }
}

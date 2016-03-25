package com.hawk.gshassist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 在此写用途
 * Created by hawk on 2016/3/18.
 */
public class SpinnerInfo implements Parcelable {
    private String name;
    private int flagInt;
    private String flagStr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlagInt() {
        return flagInt;
    }

    public void setFlagInt(int flagInt) {
        this.flagInt = flagInt;
    }

    public String getFlagStr() {
        return flagStr;
    }

    public void setFlagStr(String flagStr) {
        this.flagStr = flagStr;
    }

    @Override
    public String toString() {
        return "SpinnerInfo{" +
                "name='" + name + '\'' +
                ", flagInt=" + flagInt +
                ", flagStr='" + flagStr + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.flagInt);
        dest.writeString(this.flagStr);
    }

    public SpinnerInfo() {
    }

    protected SpinnerInfo(Parcel in) {
        this.name = in.readString();
        this.flagInt = in.readInt();
        this.flagStr = in.readString();
    }

    public static final Parcelable.Creator<SpinnerInfo> CREATOR = new Parcelable.Creator<SpinnerInfo>() {
        @Override
        public SpinnerInfo createFromParcel(Parcel source) {
            return new SpinnerInfo(source);
        }

        @Override
        public SpinnerInfo[] newArray(int size) {
            return new SpinnerInfo[size];
        }
    };
}

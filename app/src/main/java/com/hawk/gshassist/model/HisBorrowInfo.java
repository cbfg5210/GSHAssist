package com.hawk.gshassist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 在此写用途
 * Created by hawk on 2016/3/19.
 */
public class HisBorrowInfo implements Parcelable {
    private String jieqi;
    private String huanqi;
    private String timing;
    private String tushuleixing;
    private String dengluhao;

    public String getDengluhao() {
        return dengluhao;
    }

    public void setDengluhao(String dengluhao) {
        this.dengluhao = dengluhao;
    }

    public String getJieqi() {
        return jieqi;
    }

    public void setJieqi(String jieqi) {
        this.jieqi = jieqi;
    }

    public String getHuanqi() {
        return huanqi;
    }

    public void setHuanqi(String huanqi) {
        this.huanqi = huanqi;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getTushuleixing() {
        return tushuleixing;
    }

    public void setTushuleixing(String tushuleixing) {
        this.tushuleixing = tushuleixing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jieqi);
        dest.writeString(this.huanqi);
        dest.writeString(this.timing);
        dest.writeString(this.tushuleixing);
        dest.writeString(this.dengluhao);
    }

    public HisBorrowInfo() {
    }

    protected HisBorrowInfo(Parcel in) {
        this.jieqi = in.readString();
        this.huanqi = in.readString();
        this.timing = in.readString();
        this.tushuleixing = in.readString();
        this.dengluhao = in.readString();
    }

    public static final Parcelable.Creator<HisBorrowInfo> CREATOR = new Parcelable.Creator<HisBorrowInfo>() {
        @Override
        public HisBorrowInfo createFromParcel(Parcel source) {
            return new HisBorrowInfo(source);
        }

        @Override
        public HisBorrowInfo[] newArray(int size) {
            return new HisBorrowInfo[size];
        }
    };
}

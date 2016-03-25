package com.hawk.gshassist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 在此写用途
 * Created by hawk on 2016/3/19.
 */
public class CurBorrowInfo implements Parcelable {
    private String xujie;
    private String zuichiyinghuanqi;
    private String timing;
    private String juanqi;
    private String tushuleixing;
    private String dengluhao;
    private String jieqi;

    public String getJieqi() {
        return jieqi;
    }

    public void setJieqi(String jieqi) {
        this.jieqi = jieqi;
    }

    public String getXujie() {
        return xujie;
    }

    public void setXujie(String xujie) {
        this.xujie = xujie;
    }

    public String getZuichiyinghuanqi() {
        return zuichiyinghuanqi;
    }

    public void setZuichiyinghuanqi(String zuichiyinghuanqi) {
        this.zuichiyinghuanqi = zuichiyinghuanqi;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getJuanqi() {
        return juanqi;
    }

    public void setJuanqi(String juanqi) {
        this.juanqi = juanqi;
    }

    public String getTushuleixing() {
        return tushuleixing;
    }

    public void setTushuleixing(String tushuleixing) {
        this.tushuleixing = tushuleixing;
    }

    public String getDengluhao() {
        return dengluhao;
    }

    public void setDengluhao(String dengluhao) {
        this.dengluhao = dengluhao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.xujie);
        dest.writeString(this.zuichiyinghuanqi);
        dest.writeString(this.timing);
        dest.writeString(this.juanqi);
        dest.writeString(this.tushuleixing);
        dest.writeString(this.dengluhao);
        dest.writeString(this.jieqi);
    }

    public CurBorrowInfo() {
    }

    protected CurBorrowInfo(Parcel in) {
        this.xujie = in.readString();
        this.zuichiyinghuanqi = in.readString();
        this.timing = in.readString();
        this.juanqi = in.readString();
        this.tushuleixing = in.readString();
        this.dengluhao = in.readString();
        this.jieqi = in.readString();
    }

    public static final Parcelable.Creator<CurBorrowInfo> CREATOR = new Parcelable.Creator<CurBorrowInfo>() {
        @Override
        public CurBorrowInfo createFromParcel(Parcel source) {
            return new CurBorrowInfo(source);
        }

        @Override
        public CurBorrowInfo[] newArray(int size) {
            return new CurBorrowInfo[size];
        }
    };
}

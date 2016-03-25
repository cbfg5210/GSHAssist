package com.hawk.gshassist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 在此写用途
 * Created by hawk on 2016/3/19.
 */
public class BookInfo implements Parcelable {
    private String timing;
    private String zerenzhe;
    private String chubanshe;
    private String chubannian;
    private String suoquhao;
    private String guancang;
    private String kejie;
    private String xiangguanziyuan;

    public String getXiangguanziyuan() {
        return xiangguanziyuan;
    }

    public void setXiangguanziyuan(String xiangguanziyuan) {
        this.xiangguanziyuan = xiangguanziyuan;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getZerenzhe() {
        return zerenzhe;
    }

    public void setZerenzhe(String zerenzhe) {
        this.zerenzhe = zerenzhe;
    }

    public String getChubanshe() {
        return chubanshe;
    }

    public void setChubanshe(String chubanshe) {
        this.chubanshe = chubanshe;
    }

    public String getChubannian() {
        return chubannian;
    }

    public void setChubannian(String chubannian) {
        this.chubannian = chubannian;
    }

    public String getSuoquhao() {
        return suoquhao;
    }

    public void setSuoquhao(String suoquhao) {
        this.suoquhao = suoquhao;
    }

    public String getGuancang() {
        return guancang;
    }

    public void setGuancang(String guancang) {
        this.guancang = guancang;
    }

    public String getKejie() {
        return kejie;
    }

    public void setKejie(String kejie) {
        this.kejie = kejie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timing);
        dest.writeString(this.zerenzhe);
        dest.writeString(this.chubanshe);
        dest.writeString(this.chubannian);
        dest.writeString(this.suoquhao);
        dest.writeString(this.guancang);
        dest.writeString(this.kejie);
        dest.writeString(this.xiangguanziyuan);
    }

    public BookInfo() {
    }

    protected BookInfo(Parcel in) {
        this.timing = in.readString();
        this.zerenzhe = in.readString();
        this.chubanshe = in.readString();
        this.chubannian = in.readString();
        this.suoquhao = in.readString();
        this.guancang = in.readString();
        this.kejie = in.readString();
        this.xiangguanziyuan = in.readString();
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}

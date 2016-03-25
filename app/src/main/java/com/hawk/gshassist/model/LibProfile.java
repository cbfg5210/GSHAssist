package com.hawk.gshassist.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 在此写用途
 * Created by hawk on 2016/3/19.
 */
public class LibProfile implements Parcelable {
    private String zhenghao;
    private String xingming;
    private String leixing;
    private String danwei;
    private String danqianzhuangtai;
    private String dianhua;
    private String shouji;
    private String beizhu;
    private String emaildizhi;
    private String zhuzhi;
    private String qianfei;

    public String getQianfei() {
        return qianfei;
    }

    public void setQianfei(String qianfei) {
        this.qianfei = qianfei;
    }

    public String getZhenghao() {
        return zhenghao;
    }

    public void setZhenghao(String zhenghao) {
        this.zhenghao = zhenghao;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getDanqianzhuangtai() {
        return danqianzhuangtai;
    }

    public void setDanqianzhuangtai(String danqianzhuangtai) {
        this.danqianzhuangtai = danqianzhuangtai;
    }

    public String getDianhua() {
        return dianhua;
    }

    public void setDianhua(String dianhua) {
        this.dianhua = dianhua;
    }

    public String getShouji() {
        return shouji;
    }

    public void setShouji(String shouji) {
        this.shouji = shouji;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getEmaildizhi() {
        return emaildizhi;
    }

    public void setEmaildizhi(String emaildizhi) {
        this.emaildizhi = emaildizhi;
    }

    public String getZhuzhi() {
        return zhuzhi;
    }

    public void setZhuzhi(String zhuzhi) {
        this.zhuzhi = zhuzhi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.zhenghao);
        dest.writeString(this.xingming);
        dest.writeString(this.leixing);
        dest.writeString(this.danwei);
        dest.writeString(this.danqianzhuangtai);
        dest.writeString(this.dianhua);
        dest.writeString(this.shouji);
        dest.writeString(this.beizhu);
        dest.writeString(this.emaildizhi);
        dest.writeString(this.zhuzhi);
        dest.writeString(this.qianfei);
    }

    public LibProfile() {
    }

    protected LibProfile(Parcel in) {
        this.zhenghao = in.readString();
        this.xingming = in.readString();
        this.leixing = in.readString();
        this.danwei = in.readString();
        this.danqianzhuangtai = in.readString();
        this.dianhua = in.readString();
        this.shouji = in.readString();
        this.beizhu = in.readString();
        this.emaildizhi = in.readString();
        this.zhuzhi = in.readString();
        this.qianfei = in.readString();
    }

    public static final Parcelable.Creator<LibProfile> CREATOR = new Parcelable.Creator<LibProfile>() {
        @Override
        public LibProfile createFromParcel(Parcel source) {
            return new LibProfile(source);
        }

        @Override
        public LibProfile[] newArray(int size) {
            return new LibProfile[size];
        }
    };
}

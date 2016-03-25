package com.hawk.gshassist.model;

/**
 * 在此写用途
 * Created by hawk on 2016/3/17.
 */
public class Scores {
    private String xueqi;
    private String kemu;
    private String xuefen;
    private String qimozongping;
    private String bukaochengjiyi;
    private String bukaochengjier;
    private String bukaochengjisan;

    @Override
    public String toString() {
        return "Scores{" +
                "xueqi='" + xueqi + '\'' +
                ", kemu='" + kemu + '\'' +
                ", xuefen='" + xuefen + '\'' +
                ", qimozongping='" + qimozongping + '\'' +
                ", bukaochengjiyi='" + bukaochengjiyi + '\'' +
                ", bukaochengjier='" + bukaochengjier + '\'' +
                ", bukaochengjisan='" + bukaochengjisan + '\'' +
                '}';
    }

    public String getBukaochengjier() {
        return bukaochengjier;
    }

    public void setBukaochengjier(String bukaochengjier) {
        this.bukaochengjier = bukaochengjier;
    }

    public String getXueqi() {
        return xueqi;
    }

    public void setXueqi(String xueqi) {
        this.xueqi = xueqi;
    }

    public String getKemu() {
        return kemu;
    }

    public void setKemu(String kemu) {
        this.kemu = kemu;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getQimozongping() {
        return qimozongping;
    }

    public void setQimozongping(String qimozongping) {
        this.qimozongping = qimozongping;
    }

    public String getBukaochengjiyi() {
        return bukaochengjiyi;
    }

    public void setBukaochengjiyi(String bukaochengjiyi) {
        this.bukaochengjiyi = bukaochengjiyi;
    }

    public String getBukaochengjisan() {
        return bukaochengjisan;
    }

    public void setBukaochengjisan(String bukaochengjisan) {
        this.bukaochengjisan = bukaochengjisan;
    }
}

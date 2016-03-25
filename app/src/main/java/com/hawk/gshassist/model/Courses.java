package com.hawk.gshassist.model;

/**
 * 在此写用途
 * Created by hawk on 2016/3/17.
 */
public class Courses {
    private String kechengmingcheng;
    private String shangkeshijian;
    private String shangkedidian;
    private String renkejiaoshi;

    @Override
    public String toString() {
        return "Courses{" +
                " kechengmingcheng='" + kechengmingcheng + '\'' +
                ", shangkeshijian='" + shangkeshijian + '\'' +
                ", shangkedidian='" + shangkedidian + '\'' +
                ", renkejiaoshi='" + renkejiaoshi + '\'' +
                ", xueyuan='" + xueyuan + '\'' +
                ", banji='" + banji + '\'' +
                '}';
    }

    private String xueyuan;

    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getKechengmingcheng() {
        return kechengmingcheng;
    }

    public void setKechengmingcheng(String kechengmingcheng) {
        this.kechengmingcheng = kechengmingcheng;
    }

    public String getShangkeshijian() {
        return shangkeshijian;
    }

    public void setShangkeshijian(String shangkeshijian) {
        this.shangkeshijian = shangkeshijian;
    }

    public String getShangkedidian() {
        return shangkedidian;
    }

    public void setShangkedidian(String shangkedidian) {
        this.shangkedidian = shangkedidian;
    }

    public String getRenkejiaoshi() {
        return renkejiaoshi;
    }

    public void setRenkejiaoshi(String renkejiaoshi) {
        this.renkejiaoshi = renkejiaoshi;
    }

    public String getXueyuan() {
        return xueyuan;
    }

    public void setXueyuan(String xueyuan) {
        this.xueyuan = xueyuan;
    }

    private String banji;
}

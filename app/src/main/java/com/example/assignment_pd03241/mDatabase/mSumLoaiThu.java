package com.example.assignment_pd03241.mDatabase;

public class mSumLoaiThu {
    private int id;
    private String nameLThu;
    private double sumAmountLThu;

    public mSumLoaiThu() {
    }

    public mSumLoaiThu(double sumAmountLThu) {
        this.sumAmountLThu = sumAmountLThu;
    }

    public mSumLoaiThu(String nameLThu, double sumAmountLThu) {
        this.nameLThu = nameLThu;
        this.sumAmountLThu = sumAmountLThu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameLThu() {
        return nameLThu;
    }

    public void setNameLThu(String nameLThu) {
        this.nameLThu = nameLThu;
    }

    public double getSumAmountLThu() {
        return sumAmountLThu;
    }

    public void setSumAmountLThu(double sumAmountLThu) {
        this.sumAmountLThu = sumAmountLThu;
    }
}

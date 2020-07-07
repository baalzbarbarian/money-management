package com.example.assignment_pd03241.mDatabase;

import java.util.Date;

public class mMoney {

    private int id;
    private String name;
    private double money;
    private String date;

    public mMoney() {
    }

    public mMoney(String name, double money, String date) {
        this.name = name;
        this.money = money;
        this.date = date;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

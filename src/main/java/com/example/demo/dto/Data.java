package com.example.demo.dto;

public class Data {

    private int type;
    private String date;

    public Data(int type, String date) {
        this.type = type;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

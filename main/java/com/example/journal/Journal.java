package com.example.journal;

import java.util.Date;

public class Journal {
    private int id;
    private Date date;
    private String weather;
    private String title;
    private String content;
    private String address;

    public Journal() {
    }

    public Journal(Date date, String weather, String title, String content, String address) {
        this.date = date;
        this.weather = weather;
        this.title = title;
        this.content = content;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

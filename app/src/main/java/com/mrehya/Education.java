package com.mrehya;

/**
 * Created by Rubick on 2/18/2018.
 */

public class Education {
    int id;
    String field,place,from,to;

    public Education(String field, String place, String from, String to) {
        this.field = field;
        this.place = place;
        this.from = from;
        this.to = to;
    }

    public Education(int id, String field, String place, String from, String to) {

        this.id = id;
        this.field = field;
        this.place = place;
        this.from = from;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

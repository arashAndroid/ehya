package com.mrehya;

/**
 * Created by Rubick on 2/17/2018.
 */

public class Job {
    int id;
    String role,company,from,to;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Job(String from, String to, String role, String company) {

        this.from = from;
        this.to = to;
        this.role = role;
        this.company = company;
    }

    public Job(int id,String from, String to, String role, String company) {

        this.id = id;
        this.from = from;
        this.to = to;
        this.role = role;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

package com.mrehya;

/**
 * Created by User on 06/03/2018.
 */

public class User {
    int id,password_change;
    String firstname,lastname;
    String password;
    String image;
    String phone;
    String mobile;
    String address;
    String token;
    String email;
    String resume;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    String zip;

    public User(int id, String firstname,String lastname, String password, String image) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.image = image;
        //this.token = token;
    }

    public User(int id, String firstname,String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public User(int id, int password_change, String firstname,String lastname, String password, String image) {

        this.id = id;
        this.password_change = password_change;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.image = image;
        //this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassword_change() {
        return password_change;
    }

    public void setPassword_change(int password_change) {
        this.password_change = password_change;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }
}

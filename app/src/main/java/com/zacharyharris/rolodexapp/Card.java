package com.zacharyharris.rolodexapp;

import java.io.Serializable;

/**
 * Created by zacharyharris on 12/20/17.
 */

public class Card implements Serializable {
    public String lastName;
    public String firstName;
    public String email;
    public String company;
    public String startDate;
    public String bio;
    public String photoURL;

    public Card() {

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


}

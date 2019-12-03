package com.example.contact;



import java.io.Serializable;


public class Contact implements Serializable {
    String name;
    String phone;
    String uriAvatar;
    int isClick = 0;

    public int getIsClick() {
        return isClick;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Contact() {
    }

    public Contact(String name, String phone, String uriAvatar) {
        this.name = name;
        this.phone = phone;
        this.uriAvatar = uriAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUriAvatar() {
        return uriAvatar;
    }

    public void setUriAvatar(String uriAvatar) {
        this.uriAvatar = uriAvatar;
    }
}



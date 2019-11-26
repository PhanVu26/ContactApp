package com.example.contact;

import java.io.Serializable;
import java.util.Date;

public class ContactRecents implements Serializable {
    String phone;
    String callType;
    Date date;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ContactRecents() {
    }
    public ContactRecents(String phone, String callType, Date date) {
        this.phone = phone;
        this.callType = callType;
        this.date = date;
    }
}

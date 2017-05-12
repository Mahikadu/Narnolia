package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 08/03/2017.
 */

public class GetMessagesModel implements Serializable {
    String date;
    String emp;
    String id;
    String message;
    String result;
    String role;
    String receiver;

    public GetMessagesModel(String date, String emp, String id, String message, String result, String role) {
        this.date = date;
        this.emp = emp;
        this.id = id;
        this.message = message;
        this.result = result;
        this.role = role;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }
}

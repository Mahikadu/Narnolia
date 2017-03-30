package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 23/03/2017.
 */

public class LoginDetailsModel implements Serializable {
    String login_UserID,login_LoginID,login_Email,login_Mobile_no,login_Result,login_Status,login_Username,login_Attendence,login_IsRM;
    public String getLogin_UserID() {
        return login_UserID;
    }

    public void setLogin_UserID(String login_UserID) {
        this.login_UserID = login_UserID;
    }

    public String getLogin_LoginID() {
        return login_LoginID;
    }

    public void setLogin_LoginID(String login_LoginID) {
        this.login_LoginID = login_LoginID;
    }

    public String getLogin_Email() {
        return login_Email;
    }

    public void setLogin_Email(String login_Email) {
        this.login_Email = login_Email;
    }

    public String getLogin_Mobile_no() {
        return login_Mobile_no;
    }

    public void setLogin_Mobile_no(String login_Mobile_no) {
        this.login_Mobile_no = login_Mobile_no;
    }

    public String getLogin_Result() {
        return login_Result;
    }

    public void setLogin_Result(String login_Result) {
        this.login_Result = login_Result;
    }

    public String getLogin_Status() {
        return login_Status;
    }

    public void setLogin_Status(String login_Status) {
        this.login_Status = login_Status;
    }

    public String getLogin_Username() {
        return login_Username;
    }

    public void setLogin_Username(String login_Username) {
        this.login_Username = login_Username;
    }

    public String getLogin_Attendence() {
        return login_Attendence;
    }

    public void setLogin_Attendence(String login_Attendence) {
        this.login_Attendence = login_Attendence;
    }

    public String getLogin_IsRM() {
        return login_IsRM;
    }

    public void setLogin_IsRM(String login_IsRM) {
        this.login_IsRM = login_IsRM;
    }




}

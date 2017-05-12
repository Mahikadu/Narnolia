package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 30/03/2017.
 */

public class AttendenceReportModel implements Serializable {
    String empId1, attendance1, insertdate1, latitude, longitude, location, name1;

    public String getEmpId1() {
        return empId1;
    }

    public void setEmpId1(String empId1) {
        this.empId1 = empId1;
    }

    public String getAttendance() {
        return attendance1;
    }

    public void setAttendance(String attendance) {
        this.attendance1 = attendance;
    }

    public String getInsertdate() {
        return insertdate1;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate1 = insertdate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNameAttendence() {
        return name1;
    }

    public void setNameAttendence(String name) {
        this.name1 = name;
    }


}

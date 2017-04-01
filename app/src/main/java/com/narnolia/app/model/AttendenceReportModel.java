package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 30/03/2017.
 */

public class AttendenceReportModel implements Serializable {
    String attendance1, insertdate1, latitude1, longitude1, name1 ;
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
        return latitude1;
    }

    public void setLatitude(String latitude) {
        this.latitude1 = latitude;
    }

    public String getLongitude() {
        return longitude1;
    }

    public void setLongitude(String longitude) {
        this.longitude1 = longitude;
    }

    public String getNameAttendence() {
        return name1;
    }

    public void setNameAttendence(String name) {
        this.name1 = name;
    }


}

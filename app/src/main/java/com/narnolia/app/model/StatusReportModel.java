package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 23/03/2017.
 */

public class StatusReportModel implements Serializable {
    String t1;
    String t2;
    String t3;
    String t4;
    String t5;
    String t6;
    String t7;
    String t_month;
    String t_quater;
    String today_report;
    String status;

    public StatusReportModel(String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t_month, String t_quater, String today_report, String status) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t_month = t_month;
        this.t_quater = t_quater;
        this.today_report = today_report;
        this.status = status;
    }

    public String getT1_1() {
        return t1;
    }

    public void setT1_1(String t1_1) {
        this.t1 = t1_1;
    }

    public String getT2_1() {
        return t2;
    }

    public void setT2_1(String t2_1) {
        this.t2 = t2_1;
    }

    public String getT3_1() {
        return t3;
    }

    public void setT3_1(String t3_1) {
        this.t3 = t3_1;
    }

    public String getT4_1() {
        return t4;
    }

    public void setT4_1(String t4_1) {
        this.t4 = t4_1;
    }

    public String getT5_1() {
        return t5;
    }

    public void setT5_1(String t5_1) {
        this.t5 = t5_1;
    }

    public String getT6_1() {
        return t6;
    }

    public void setT6_1(String t6_1) {
        this.t6 = t6_1;
    }

    public String getT7_1() {
        return t7;
    }

    public void setT7_1(String t7_1) {
        this.t7 = t7_1;
    }

    public String getT_month_1() {
        return t_month;
    }

    public void setT_month_1(String t_month_1) {
        this.t_month = t_month_1;
    }

    public String getT_quater_1() {
        return t_quater;
    }

    public void setT_quater_1(String t_quater_1) {
        this.t_quater = t_quater_1;
    }

    public String getToday_report_1() {
        return today_report;
    }

    public void setToday_report_1(String today_report_1) {
        this.today_report = today_report_1;
    }

    public String getStatus_1() {
        return status;
    }

    public void setStatus_1(String status_1) {
        this.status = status_1;
    }


}

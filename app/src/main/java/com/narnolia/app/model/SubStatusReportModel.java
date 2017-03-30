package com.narnolia.app.model;

import java.io.Serializable;

/**
 * Created by Sudesi on 24/03/2017.
 */

public class SubStatusReportModel implements Serializable {
    String sub_lead_id,sub_name,sub_mobile_no,sub_city,sub_pincode,sub_last_meeting_date,sub_last_meeting_update,sub_lead_status;
    public SubStatusReportModel(String sub_lead_id,String sub_name,String sub_mobile_no,String sub_city,String sub_pincode,String sub_last_meeting_date,String sub_last_meeting_update,String sub_lead_status){
        this.sub_lead_id=sub_lead_id;
        this.sub_name=sub_name;
        this.sub_mobile_no=sub_mobile_no;
        this.sub_city=sub_city;
        this.sub_pincode=sub_pincode;
        this.sub_last_meeting_date=sub_last_meeting_date;
        this.sub_last_meeting_update=sub_last_meeting_update;
        this.sub_lead_status=sub_lead_status;
    }

    public SubStatusReportModel() {

    }

    public String getSub_lead_id() {
        return sub_lead_id;
    }

    public void setSub_lead_id(String sub_lead_id) {
        this.sub_lead_id = sub_lead_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_mobile_no() {
        return sub_mobile_no;
    }

    public void setSub_mobile_no(String sub_mobile_no) {
        this.sub_mobile_no = sub_mobile_no;
    }

    public String getSub_city() {
        return sub_city;
    }

    public void setSub_city(String sub_city) {
        this.sub_city = sub_city;
    }

    public String getSub_pincode() {
        return sub_pincode;
    }

    public void setSub_pincode(String sub_pincode) {
        this.sub_pincode = sub_pincode;
    }

    public String getSub_last_meeting_date() {
        return sub_last_meeting_date;
    }

    public void setSub_last_meeting_date(String sub_last_meeting_date) {
        this.sub_last_meeting_date = sub_last_meeting_date;
    }

    public String getSub_last_meeting_update() {
        return sub_last_meeting_update;
    }

    public void setSub_last_meeting_update(String sub_last_meeting_update) {
        this.sub_last_meeting_update = sub_last_meeting_update;
    }

    public String getSub_lead_status() {
        return sub_lead_status;
    }

    public void setSub_lead_status(String sub_lead_status) {
        this.sub_lead_status = sub_lead_status;
    }


}

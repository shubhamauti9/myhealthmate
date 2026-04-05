package com.example.myhealthmate;

import java.util.Calendar;

public class PatientMedicationReminder {

    String ms, userId;
    String dt;



    String cal;

    public PatientMedicationReminder() {
    }

    public PatientMedicationReminder(String cal, String ms, String userId, String dt) {
        this.cal = cal;
        this.ms = ms;
        this.userId = userId;
        this.dt = dt;
    }



    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }



}

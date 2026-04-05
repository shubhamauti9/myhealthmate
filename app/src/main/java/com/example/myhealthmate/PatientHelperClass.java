package com.example.myhealthmate;

public class PatientHelperClass {

    String Helper_pName, Helper_pUsername, Helper_pEmail, Helper_pPhoneNo, Helper_pAge, dcode, Helper_pPassword, Helper_pGender;




    public PatientHelperClass(String pName, String pUsername, String pEmail, String pPhoneNo, String
            pAge, String dcode, String pPassword, String pGender) {
        this.Helper_pName = pName;
        this.Helper_pUsername = pUsername;
        this.Helper_pEmail = pEmail;
        this.Helper_pPhoneNo = pPhoneNo;
        this.Helper_pAge = pAge;
        this.dcode = dcode;

        this.Helper_pPassword = pPassword;
        this.Helper_pGender = pGender;

    }

    public String getpAge() {
        return Helper_pAge;
    }

    public void setpAge(String pAge) {
        this.Helper_pAge = pAge;
    }







    public String getpName() {
        return Helper_pName;
    }

    public void setpName(String pName) {
        this.Helper_pName = pName;
    }

    public String getpUsername() {
        return Helper_pUsername;
    }

    public void setpUsername(String pUsername) {
        this.Helper_pUsername = pUsername;
    }

    public String getpEmail() {
        return Helper_pEmail;
    }

    public void setpEmail(String pEmail) {
        this.Helper_pEmail = pEmail;
    }

    public String getpPhoneNo() {
        return Helper_pPhoneNo;
    }

    public void setpPhoneNo(String pPhoneNo) {
        this.Helper_pPhoneNo = pPhoneNo;
    }

    public String getpPassword() {
        return Helper_pPassword;
    }

    public void setpPassword(String pPassword) {
        this.Helper_pPassword = pPassword;
    }

    public String getGender() {
        return Helper_pGender;
    }

    public void setpGender(String pGender) {
        this.Helper_pGender = pGender;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }
}

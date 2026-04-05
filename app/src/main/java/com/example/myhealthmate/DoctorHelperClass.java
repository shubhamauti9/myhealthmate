package com.example.myhealthmate;

public class DoctorHelperClass {

    String Helper_dName, Helper_dUsername, Helper_dEmail, Helper_dPhoneNo, Helper_dRegistrationNo,Helper_dPassword;


    public DoctorHelperClass(String dName, String dUsername, String dEmail, String dPhoneNo, String dRegistrationNo, String dPassword) {
        this.Helper_dName = dName;
        this.Helper_dUsername = dUsername;
        this.Helper_dEmail = dEmail;
        this.Helper_dPhoneNo = dPhoneNo;
        this.Helper_dRegistrationNo = dRegistrationNo;
        this.Helper_dPassword = dPassword;
    }

    public String getdName() {
        return Helper_dName;
    }

    public void setdName(String dName) {
        this.Helper_dName = dName;
    }



    public String getdUsername() {
        return Helper_dUsername;
    }

    public void setdUsername(String dUsername) {
        this.Helper_dUsername = dUsername;
    }



    public String getdEmail() {
        return Helper_dEmail;
    }

    public void setdEmail(String dEmail) {
        this.Helper_dEmail = dEmail;
    }




    public String getdPhoneNo() {
        return Helper_dPhoneNo;
    }

    public void setdPhoneNo(String dPhoneNo) {
        this.Helper_dPhoneNo = dPhoneNo;
    }




    public String getdRegistrationNo() {
        return Helper_dRegistrationNo;
    }

    public void setdRegistrationNo(String dRegistrationNo) {
        this.Helper_dRegistrationNo = dRegistrationNo;
    }



    public String getdPassword() {
        return Helper_dPassword;
    }

    public void setdPassword(String dPassword) {
        this.Helper_dPassword = dPassword;
    }
}

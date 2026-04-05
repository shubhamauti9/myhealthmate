package com.example.myhealthmate;

public class ExpenseData {
    String details_str, date, amount_str, userId;

    public ExpenseData(String details_str, String date, String amount_str, String userId) {
        this.details_str = details_str;
        this.date = date;
        this.amount_str = amount_str;
        this.userId = userId;
    }

    public ExpenseData() {
    }

    public String getDetails_str() {
        return details_str;
    }

    public void setDetails_str(String details_str) {
        this.details_str = details_str;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount_str() {
        return amount_str;
    }

    public void setAmount_str(String amount_str) {
        this.amount_str = amount_str;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

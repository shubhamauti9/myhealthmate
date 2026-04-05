package com.example.myhealthmate;

public class YogaDb {
    String bmi, influenza, covid, stress, diabetes, hypertension, userid;

    public YogaDb(String bmi, String influenza, String covid, String stress, String diabetes, String hypertension, String userid) {
        this.bmi = bmi;
        this.influenza = influenza;
        this.covid = covid;
        this.stress = stress;
        this.diabetes = diabetes;
        this.hypertension = hypertension;
        this.userid = userid;
    }


    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getInfluenza() {
        return influenza;
    }

    public void setInfluenza(String influenza) {
        this.influenza = influenza;
    }

    public String getCovid() {
        return covid;
    }

    public void setCovid(String covid) {
        this.covid = covid;
    }

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getHypertension() {
        return hypertension;
    }

    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TinggiBadanUmur {
    public String tbuId;
    public String tbuScore;
    public String tbuStatus;
    public String tbuHeight;
    public Integer tbuAge;
    public String tbuDateSave;

    public TinggiBadanUmur() {

    }

    public TinggiBadanUmur(String tbuId, String tbuScore, String tbuStatus, String tbuHeight,
                           Integer tbuAge, String tbuDateSave) {
        this.tbuId = tbuId;
        this.tbuScore = tbuScore;
        this.tbuStatus = tbuStatus;
        this.tbuHeight = tbuHeight;
        this.tbuAge = tbuAge;
        this.tbuDateSave = tbuDateSave;
    }

    public String getTbuId() {
        return tbuId;
    }

    public void setTbuId(String tbuId) {
        this.tbuId = tbuId;
    }

    public String getTbuScore() {
        return tbuScore;
    }

    public void setTbuScore(String tbuScore) {
        this.tbuScore = tbuScore;
    }

    public String getTbuStatus() {
        return tbuStatus;
    }

    public void setTbuStatus(String tbuStatus) {
        this.tbuStatus = tbuStatus;
    }

    public String getTbuHeight() {
        return tbuHeight;
    }

    public void setTbuHeight(String tbuHeight) {
        this.tbuHeight = tbuHeight;
    }

    public Integer getTbuAge() {
        return tbuAge;
    }

    public void setTbuAge(Integer tbuAge) {
        this.tbuAge = tbuAge;
    }

    public String getTbuDateSave() {
        return tbuDateSave;
    }

    public void setTbuDateSave(String tbuDateSave) {
        this.tbuDateSave = tbuDateSave;
    }
}

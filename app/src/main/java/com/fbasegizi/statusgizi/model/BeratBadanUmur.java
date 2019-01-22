package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BeratBadanUmur {
    public String bbuId;
    public String bbuScore;
    public String bbuStatus;
    public String bbuWeight;
    public Integer bbuAge;
    public String bbuDateSave;

    public BeratBadanUmur() {

    }

    public BeratBadanUmur(String bbuId, String bbuScore, String bbuStatus, String bbuWeight,
                          Integer bbuAge, String bbuDateSave) {
        this.bbuId = bbuId;
        this.bbuScore = bbuScore;
        this.bbuStatus = bbuStatus;
        this.bbuWeight = bbuWeight;
        this.bbuAge = bbuAge;
        this.bbuDateSave = bbuDateSave;
    }

    public String getBbuId() {
        return bbuId;
    }

    public void setBbuId(String bbuId) {
        this.bbuId = bbuId;
    }

    public String getBbuScore() {
        return bbuScore;
    }

    public void setBbuScore(String bbuScore) {
        this.bbuScore = bbuScore;
    }

    public String getBbuStatus() {
        return bbuStatus;
    }

    public void setBbuStatus(String bbuStatus) {
        this.bbuStatus = bbuStatus;
    }

    public String getBbuWeight() {
        return bbuWeight;
    }

    public void setBbuWeight(String bbuWeight) {
        this.bbuWeight = bbuWeight;
    }

    public Integer getBbuAge() {
        return bbuAge;
    }

    public void setBbuAge(Integer bbuAge) {
        this.bbuAge = bbuAge;
    }

    public String getBbuDateSave() {
        return bbuDateSave;
    }

    public void setBbuDateSave(String bbuDateSave) {
        this.bbuDateSave = bbuDateSave;
    }
}

package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BeratBadanTinggiBadan {
    public String bbtbId;
    public String bbtbScore;
    public String bbtbStatus;
    public String bbtbWeight;
    public String bbtbHeight;
    public Integer bbtbAge;
    public String bbtbDateSave;

    public BeratBadanTinggiBadan() {

    }

    public BeratBadanTinggiBadan(String bbtbId, String bbtbScore, String bbtbStatus,
                                 String bbtbWeight, String bbtbHeight,
                                 Integer bbtbAge, String bbtbDateSave) {
        this.bbtbId = bbtbId;
        this.bbtbScore = bbtbScore;
        this.bbtbStatus = bbtbStatus;
        this.bbtbWeight = bbtbWeight;
        this.bbtbHeight = bbtbHeight;
        this.bbtbAge = bbtbAge;
        this.bbtbDateSave = bbtbDateSave;
    }

    public String getBbtbId() {
        return bbtbId;
    }

    public void setBbtbId(String bbtbId) {
        this.bbtbId = bbtbId;
    }

    public String getBbtbScore() {
        return bbtbScore;
    }

    public void setBbtbScore(String bbtbScore) {
        this.bbtbScore = bbtbScore;
    }

    public String getBbtbStatus() {
        return bbtbStatus;
    }

    public void setBbtbStatus(String bbtbStatus) {
        this.bbtbStatus = bbtbStatus;
    }

    public String getBbtbWeight() {
        return bbtbWeight;
    }

    public void setBbtbWeight(String bbtbWeight) {
        this.bbtbWeight = bbtbWeight;
    }

    public String getBbtbHeight() {
        return bbtbHeight;
    }

    public void setBbtbHeight(String bbtbHeight) {
        this.bbtbHeight = bbtbHeight;
    }

    public Integer getBbtbAge() {
        return bbtbAge;
    }

    public void setBbtbAge(Integer bbtbAge) {
        this.bbtbAge = bbtbAge;
    }

    public String getBbtbDateSave() {
        return bbtbDateSave;
    }

    public void setBbtbDateSave(String bbtbDateSave) {
        this.bbtbDateSave = bbtbDateSave;
    }
}
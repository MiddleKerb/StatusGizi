package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class IndeksMassaTubuhUmur {
    public String imtuId;
    public String imtuScore;
    public String imtuStatus;
    public String imtuWeight;
    public String imtuHeight;
    public Integer imtuAge;
    public String imtuDateSave;

    public IndeksMassaTubuhUmur() {

    }

    public IndeksMassaTubuhUmur(String imtuId, String imtuScore, String imtuStatus,
                                String imtuWeight, String imtuHeight, Integer imtuAge,
                                String imtuDateSavet) {
        this.imtuId = imtuId;
        this.imtuScore = imtuScore;
        this.imtuStatus = imtuStatus;
        this.imtuWeight = imtuWeight;
        this.imtuHeight = imtuHeight;
        this.imtuAge = imtuAge;
        this.imtuDateSave = imtuDateSavet;
    }

    public String getImtuId() {
        return imtuId;
    }

    public void setImtuId(String imtuId) {
        this.imtuId = imtuId;
    }

    public String getImtuScore() {
        return imtuScore;
    }

    public void setImtuScore(String imtuScore) {
        this.imtuScore = imtuScore;
    }

    public String getImtuStatus() {
        return imtuStatus;
    }

    public void setImtuStatus(String imtuStatus) {
        this.imtuStatus = imtuStatus;
    }

    public String getImtuWeight() {
        return imtuWeight;
    }

    public void setImtuWeight(String imtuWeight) {
        this.imtuWeight = imtuWeight;
    }

    public String getImtuHeight() {
        return imtuHeight;
    }

    public void setImtuHeight(String imtuHeight) {
        this.imtuHeight = imtuHeight;
    }

    public Integer getImtuAge() {
        return imtuAge;
    }

    public void setImtuAge(Integer imtuAge) {
        this.imtuAge = imtuAge;
    }

    public String getImtuDateSave() {
        return imtuDateSave;
    }

    public void setImtuDateSave(String imtuDateSave) {
        this.imtuDateSave = imtuDateSave;
    }
}
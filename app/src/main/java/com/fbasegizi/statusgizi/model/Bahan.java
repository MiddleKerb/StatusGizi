package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Bahan {
    public String bahanId;
    public String bahanNama;
    public String bahanKalori;
    public String bahanKarbo;
    public String bahanLemak;
    public String bahanProtein;

    public Bahan() {

    }

    public Bahan(String bahanId, String bahanNama, String bahanKalori, String bahanKarbo, String bahanLemak, String bahanProtein) {
        this.bahanId = bahanId;
        this.bahanNama = bahanNama;
        this.bahanKalori = bahanKalori;
        this.bahanKarbo = bahanKarbo;
        this.bahanLemak = bahanLemak;
        this.bahanProtein = bahanProtein;
    }


    public String getBahanId() {
        return bahanId;
    }

    public void setBahanId(String bahanId) {
        this.bahanId = bahanId;
    }

    public String getBahanNama() {
        return bahanNama;
    }

    public void setBahanNama(String bahanNama) {
        this.bahanNama = bahanNama;
    }

    public String getBahanKalori() {
        return bahanKalori;
    }

    public void setBahanKalori(String bahanKalori) {
        this.bahanKalori = bahanKalori;
    }

    public String getBahanKarbo() {
        return bahanKarbo;
    }

    public void setBahanKarbo(String bahanKarbo) {
        this.bahanKarbo = bahanKarbo;
    }

    public String getBahanLemak() {
        return bahanLemak;
    }

    public void setBahanLemak(String bahanLemak) {
        this.bahanLemak = bahanLemak;
    }

    public String getBahanProtein() {
        return bahanProtein;
    }

    public void setBahanProtein(String bahanProtein) {
        this.bahanProtein = bahanProtein;
    }
}

package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rekomendasi {
    public String rekomendasiId;
    public String rekomendasiBahan;
    public String rekomendasiTanggal;
    public String rekomendasiWaktu;
    public String rekomendasiKode;

    public Rekomendasi() {

    }

    public Rekomendasi(String rekomendasiId, String rekomendasiBahan, String rekomendasiTanggal, String rekomendasiWaktu, String rekomendasiKode) {
        this.rekomendasiBahan = rekomendasiBahan;
        this.rekomendasiTanggal = rekomendasiTanggal;
        this.rekomendasiWaktu = rekomendasiWaktu;
        this.rekomendasiKode = rekomendasiKode;
        this.rekomendasiId = rekomendasiId;
    }

    public String getRekomendasiId() {
        return rekomendasiId;
    }

    public void setRekomendasiId(String rekomendasiId) {
        this.rekomendasiId = rekomendasiId;
    }

    public String getRekomendasiBahan() {
        return rekomendasiBahan;
    }

    public void setRekomendasiBahan(String rekomendasiBahan) {
        this.rekomendasiBahan = rekomendasiBahan;
    }

    public String getRekomendasiTanggal() {
        return rekomendasiTanggal;
    }

    public void setRekomendasiTanggal(String rekomendasiTanggal) {
        this.rekomendasiTanggal = rekomendasiTanggal;
    }

    public String getRekomendasiWaktu() {
        return rekomendasiWaktu;
    }

    public void setRekomendasiWaktu(String rekomendasiWaktu) {
        this.rekomendasiWaktu = rekomendasiWaktu;
    }

    public String getRekomendasiKode() {
        return rekomendasiKode;
    }

    public void setRekomendasiKode(String rekomendasiKode) {
        this.rekomendasiKode = rekomendasiKode;
    }
}

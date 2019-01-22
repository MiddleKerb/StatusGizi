package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bonie on 02-Jul-18.
 */
@IgnoreExtraProperties
public class Anak {

    public String childId;
    public String childname;
    public String gender;
    public String dateborn;

    public Anak() {

    }

    public Anak(String childId, String childname, String gender, String dateborn) {
        this.childId = childId;
        this.childname = childname;
        this.gender = gender;
        this.dateborn = dateborn;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateborn() {
        return dateborn;
    }

    public void setDateborn(String dateborn) {
        this.dateborn = dateborn;
    }
}

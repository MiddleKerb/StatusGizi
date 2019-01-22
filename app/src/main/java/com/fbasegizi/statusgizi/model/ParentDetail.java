package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ParentDetail {
    public String parentId;
    public String parentHeight;
    public String parentWeight;
    public Integer parentAge;
    public Integer parentFamily;

    public ParentDetail() {

    }

    public ParentDetail(String parentId, String parentHeight, String parentWeight,
                        Integer parentAge, Integer parentFamily) {
        this.parentId = parentId;
        this.parentHeight = parentHeight;
        this.parentWeight = parentWeight;
        this.parentAge = parentAge;
        this.parentFamily = parentFamily;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(String parentHeight) {
        this.parentHeight = parentHeight;
    }

    public String getParentWeight() {
        return parentWeight;
    }

    public void setParentWeight(String parentWeight) {
        this.parentWeight = parentWeight;
    }

    public Integer getParentAge() {
        return parentAge;
    }

    public void setParentAge(Integer parentAge) {
        this.parentAge = parentAge;
    }

    public Integer getParentFamily() {
        return parentFamily;
    }

    public void setParentFamily(Integer parentFamily) {
        this.parentFamily = parentFamily;
    }
}

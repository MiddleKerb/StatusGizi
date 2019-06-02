package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MenuMakan {
    public String menuId;
    public String menuJudul;
    public String menuBahan;
    public String menuProses;

    public MenuMakan() {

    }

    public MenuMakan(String menuId, String menuJudul, String menuBahan, String menuProses) {
        this.menuId = menuId;
        this.menuJudul = menuJudul;
        this.menuBahan = menuBahan;
        this.menuProses = menuProses;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuJudul() {
        return menuJudul;
    }

    public void setMenuJudul(String menuJudul) {
        this.menuJudul = menuJudul;
    }

    public String getMenuBahan() {
        return menuBahan;
    }

    public void setMenuBahan(String menuBahan) {
        this.menuBahan = menuBahan;
    }

    public String getMenuProses() {
        return menuProses;
    }

    public void setMenuProses(String menuProses) {
        this.menuProses = menuProses;
    }
}

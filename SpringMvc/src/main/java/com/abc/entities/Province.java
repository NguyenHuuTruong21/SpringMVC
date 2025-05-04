package com.abc.entities;

public class Province {
    private int idProvince;
    private String nameProvince;
    private String note;

    public Province(int idProvince, String nameProvince, String note) {
        this.idProvince = idProvince;
        this.nameProvince = nameProvince;
        this.note = note;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }

    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
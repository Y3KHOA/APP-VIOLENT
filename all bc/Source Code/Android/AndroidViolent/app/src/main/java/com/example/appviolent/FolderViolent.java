package com.example.appviolent;

import java.io.Serializable;

public class FolderViolent implements Serializable {
    String label, name, number ;
    int idImg;

    public FolderViolent(String label, String name, String number, int idImg) {
        this.label = label;
        this.name = name;
        this.number = number;
        this.idImg = idImg;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }
}

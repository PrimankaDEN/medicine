package com.primankaden.medicine.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

@Entity
public class Item implements Serializable {
    @PrimaryKey
    private int id;
    private String title, description;
    private String titleLowReg, descriptionLowReg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleLowReg() {
        return titleLowReg;
    }

    public void setTitleLowReg(String titleLowReg) {
        this.titleLowReg = titleLowReg;
    }

    public String getDescriptionLowReg() {
        return descriptionLowReg;
    }

    public void setDescriptionLowReg(String descriptionLowReg) {
        this.descriptionLowReg = descriptionLowReg;
    }
}

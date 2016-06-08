package com.mirzaaqibbeg.roademergencies.DataStructure;

import java.io.Serializable;

/**
 * Created by Mirzaaqibbeg on 09-03-2016.
 */ public class ContactLocal implements Serializable{
    private String id;
    private String number;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

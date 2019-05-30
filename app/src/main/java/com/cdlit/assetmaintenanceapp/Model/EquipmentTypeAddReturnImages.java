package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 22-06-2017.
 */

public class EquipmentTypeAddReturnImages {

    private String bitmapstring;

    private Date createdDate;

    private int createdUser;

    private int equipmentTypeId;

    private int id;

    private String imagePath;

    private Date modifiedDate;

    private int modifiedUser;

    private int state;

    public void setBitmapstring(String bitmapstring) {
        this.bitmapstring = bitmapstring;
    }

    public String getBitmapstring() {
        return this.bitmapstring;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedUser(int createdUser) {
        this.createdUser = createdUser;
    }

    public int getCreatedUser() {
        return this.createdUser;
    }

    public void setEquipmentTypeId(int equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public int getEquipmentTypeId() {
        return this.equipmentTypeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedUser(int modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public int getModifiedUser() {
        return this.modifiedUser;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

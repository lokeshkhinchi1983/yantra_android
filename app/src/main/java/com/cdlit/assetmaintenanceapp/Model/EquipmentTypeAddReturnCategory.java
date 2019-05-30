package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 22-06-2017.
 */

public class EquipmentTypeAddReturnCategory {
    private String categoryName;

    private Date createdDate;

    private int createdUser;

    private int id;

    private int isActive;

    private Date modifiedDate;

    private int modifiedUser;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return this.isActive;
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
}

package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 16-06-2017.
 */

public class LoationDeactiveResponse {

    private int id;

    private String createdDate;

    private int createdUser;

    private int isActive;

    private String locationName;

    private String modifiedDate;

    private int modifiedUser;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedUser(int createdUser) {
        this.createdUser = createdUser;
    }

    public int getCreatedUser() {
        return this.createdUser;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsActive() {
        return this.isActive;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedUser(int modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public int getModifiedUser() {
        return this.modifiedUser;
    }

}

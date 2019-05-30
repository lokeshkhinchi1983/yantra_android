package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 22-06-2017.
 */

public class EquipmentTypeAddReturn {

    private int catId;

    private EquipmentTypeAddReturnCategory category;

    private Date createdDate;

    private int createdUser;

    private List<EquipmentTypeAddReturnImages> equipmentTypeImages;

    private String equipmentTypeName;

    private String description;

    private int id;

    private int isActive;

    private Date modifiedDate;

    private int modifiedUser;

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getCatId() {
        return this.catId;
    }

    public void setCategory(EquipmentTypeAddReturnCategory category) {
        this.category = category;
    }

    public EquipmentTypeAddReturnCategory getCategory() {
        return this.category;
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

    public void setEquipmentTypeImages(List<EquipmentTypeAddReturnImages> equipmentTypeImages) {
        this.equipmentTypeImages = equipmentTypeImages;
    }

    public List<EquipmentTypeAddReturnImages> getEquipmentTypeImages() {
        return this.equipmentTypeImages;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }

    public String getEquipmentTypeName() {
        return this.equipmentTypeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
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



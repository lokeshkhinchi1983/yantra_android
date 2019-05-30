package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 27-06-2017.
 */

public class EquipmentTypeAddChecklistReturn {


    private String checkListName;

    private Date createdDate;

    private Integer createdUser;

    private String description;

    private Object equipmentType;

    private Integer e_id;


    private Integer id;

    private Integer isActive;

    private Integer loc_id;

    private Object location;

    private Date modifiedDate;

    private Integer modifiedUser;

    private String frequency;

    private String frequencyDay;


    private Integer checklistType;

    public void setCheckListName(String checkListName) {
        this.checkListName = checkListName;
    }

    public String getCheckListName() {
        return this.checkListName;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedUser(Integer createdUser) {
        this.createdUser = createdUser;
    }

    public Integer getCreatedUser() {
        return this.createdUser;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEquipmentType(Object equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Object getEquipmentType() {
        return this.equipmentType;
    }

    /*public void setEtype_id(Integer etype_id) {
        this.etype_id = etype_id;
    }

    public Integer getEtype_id() {
        return this.etype_id;
    }
*/
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public void setLoc_id(Integer loc_id) {
        this.loc_id = loc_id;
    }

    public Integer getLoc_id() {
        return this.loc_id;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getLocation() {
        return this.location;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedUser(Integer modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Integer getModifiedUser() {
        return this.modifiedUser;
    }

    public Integer getE_id() {
        return e_id;
    }

    public void setE_id(Integer e_id) {
        this.e_id = e_id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyDay() {
        return frequencyDay;
    }

    public void setFrequencyDay(String frequencyDay) {
        this.frequencyDay = frequencyDay;
    }

    public Integer getChecklistType() {
        return checklistType;
    }

    public void setChecklistType(Integer checklistType) {
        this.checklistType = checklistType;
    }
}

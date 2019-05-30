package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rakesh on 27-06-2017.
 */

public class EquipmentChecklistResponse implements Parcelable {

    private Integer id;

    private String checkListName;

    private Date createdDate;

    private Integer createdUser;

    private String description;

    private EquipmentChecklistEquipmentType equipmentType;

    private Date modifiedDate;

    private Integer modifiedUser;

    private Integer isActive;

    private String etype_id;

    private String frequency;

    private String frequencyDay;

    private Integer checklistType;

    protected EquipmentChecklistResponse(Parcel in) {
        checkListName = in.readString();
        description = in.readString();
        etype_id = in.readString();
    }

    public static final Creator<EquipmentChecklistResponse> CREATOR = new Creator<EquipmentChecklistResponse>() {
        @Override
        public EquipmentChecklistResponse createFromParcel(Parcel in) {
            return new EquipmentChecklistResponse(in);
        }

        @Override
        public EquipmentChecklistResponse[] newArray(int size) {
            return new EquipmentChecklistResponse[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

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

    public void setEquipmentType(EquipmentChecklistEquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public EquipmentChecklistEquipmentType getEquipmentType() {
        return this.equipmentType;
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

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public void setEtype_id(String etype_id) {
        this.etype_id = etype_id;
    }

    public String getEtype_id() {
        return this.etype_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(checkListName);
        dest.writeString(description);
        dest.writeString(etype_id);
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

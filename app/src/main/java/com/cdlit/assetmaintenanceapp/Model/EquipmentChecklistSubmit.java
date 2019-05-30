package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 29-11-2017.
 */

public class EquipmentChecklistSubmit {

    private List<ChecklistDetails> checklistDetails;

    private String description;

    private Integer equipmentId;

    private String modelNo;

    private Date modified_date;

    private Integer userid;

    private String username;

    public void setChecklistDetails(List<ChecklistDetails> checklistDetails) {
        this.checklistDetails = checklistDetails;
    }

    public List<ChecklistDetails> getChecklistDetails() {
        return this.checklistDetails;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getEquipmentId() {
        return this.equipmentId;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getModelNo() {
        return this.modelNo;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    public Date getModified_date() {
        return this.modified_date;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return this.userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public static class ChecklistDetails {

        public  ChecklistDetails(){

        }

        private Integer booleanAnswer;

        private String checkListDescription;

        private String checkListName;

        private String comment;

        private Integer equipmentCheckListId;

        private List<String> images;

        private Date modifiedDate;

        private Integer modifiedUser;

        private Integer faultType;




        public void setBooleanAnswer(Integer booleanAnswer) {
            this.booleanAnswer = booleanAnswer;
        }

        public Integer getBooleanAnswer() {
            return this.booleanAnswer;
        }

        public void setCheckListDescription(String checkListDescription) {
            this.checkListDescription = checkListDescription;
        }

        public String getCheckListDescription() {
            return this.checkListDescription;
        }

        public void setCheckListName(String checkListName) {
            this.checkListName = checkListName;
        }

        public String getCheckListName() {
            return this.checkListName;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return this.comment;
        }

        public void setEquipmentCheckListId(Integer equipmentCheckListId) {
            this.equipmentCheckListId = equipmentCheckListId;
        }

        public Integer getEquipmentCheckListId() {
            return this.equipmentCheckListId;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImages() {
            return this.images;
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


        public Integer getFaultType() {
            return faultType;
        }

        public void setFaultType(Integer faultType) {
            this.faultType = faultType;
        }
    }
}

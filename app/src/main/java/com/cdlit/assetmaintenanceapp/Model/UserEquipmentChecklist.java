package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 08-09-2017.
 */

public class UserEquipmentChecklist extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public static class Response implements Parcelable {

        private Integer equipmentId;

        private Integer equipmentTypeId;

        private Integer userCheckedAnswerId;

        private Integer equipmentCheckListId;

        private String checkListName;

        private String checkListDescription;

        private Integer booleanAnswer;

        private String comment;

        private List<String> images;

        private List<String> image_path;

        private Integer modifiedUser;

        private Date modifiedDate;

        private Integer faultType;

        private Integer checklistType;

        private String managerComment;

        private String managerName;

        protected Response(Parcel in) {
            checkListName = in.readString();
            checkListDescription = in.readString();
            comment = in.readString();
            images = in.createStringArrayList();
            image_path = in.createStringArrayList();
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel in) {
                return new Response(in);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };

        public void setEquipmentId(Integer equipmentId) {
            this.equipmentId = equipmentId;
        }

        public Integer getEquipmentId() {
            return this.equipmentId;
        }

        public void setEquipmentTypeId(Integer equipmentTypeId) {
            this.equipmentTypeId = equipmentTypeId;
        }

        public Integer getEquipmentTypeId() {
            return this.equipmentTypeId;
        }

        public void setUserCheckedAnswerId(Integer userCheckedAnswerId) {
            this.userCheckedAnswerId = userCheckedAnswerId;
        }

        public Integer getUserCheckedAnswerId() {
            return this.userCheckedAnswerId;
        }

        public void setEquipmentCheckListId(Integer equipmentCheckListId) {
            this.equipmentCheckListId = equipmentCheckListId;
        }

        public Integer getEquipmentCheckListId() {
            return this.equipmentCheckListId;
        }

        public void setCheckListName(String checkListName) {
            this.checkListName = checkListName;
        }

        public String getCheckListName() {
            return this.checkListName;
        }

        public void setCheckListDescription(String checkListDescription) {
            this.checkListDescription = checkListDescription;
        }

        public String getCheckListDescription() {
            return this.checkListDescription;
        }

        public void setBooleanAnswer(Integer booleanAnswer) {
            this.booleanAnswer = booleanAnswer;
        }

        public Integer getBooleanAnswer() {
            return this.booleanAnswer;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return this.comment;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<String> getImages() {
            return this.images;
        }

        public void setModifiedUser(Integer modifiedUser) {
            this.modifiedUser = modifiedUser;
        }

        public Integer getModifiedUser() {
            return this.modifiedUser;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Date getModifiedDate() {
            return this.modifiedDate;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(checkListName);
            dest.writeString(checkListDescription);
            dest.writeString(comment);
            dest.writeStringList(images);
            dest.writeStringList(image_path);
        }

        public List<String> getImage_path() {
            return image_path;
        }

        public void setImage_path(List<String> image_path) {
            this.image_path = image_path;
        }

        public Integer getFaultType() {
            return faultType;
        }

        public void setFaultType(Integer faultType) {
            this.faultType = faultType;
        }

        public Integer getChecklistType() {
            return checklistType;
        }

        public void setChecklistType(Integer checklistType) {
            this.checklistType = checklistType;
        }

        public String getManagerComment() {
            return managerComment;
        }

        public void setManagerComment(String managerComment) {
            this.managerComment = managerComment;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }
    }

}

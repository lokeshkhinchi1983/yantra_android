package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rakesh on 13-12-2017.
 */

public class RepairLog extends RestResponse {

    private List<RepairlogResponse> response;

    public void setResponse(List<RepairlogResponse> response) {
        this.response = response;
    }

    public List<RepairlogResponse> getResponse() {
        return this.response;
    }

    public class RepairlogResponse implements Parcelable {

        private Integer repairLogId;

        private String equipmentId;

        private String modelName;

        private List<String> model_bitmap;

        private List<String> image_path;

        private String cost;

        private String description;

        private String location;

        private String contactNumber;

        public String getRepairLogDate() {
            return repairLogDate;
        }

        public void setRepairLogDate(String repairLogDate) {
            this.repairLogDate = repairLogDate;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        private String repairLogDate;
        private String firstName;
        private String lastName;

        private String noMilesOrHours;
        private String unitMilesOrHours;

     /*   private String milesHoursType;
        private String milesHours;*/

        protected RepairlogResponse(Parcel in) {
            repairLogId = in.readInt();
            equipmentId = in.readString();
            modelName = in.readString();
            model_bitmap = in.createStringArrayList();
            image_path = in.createStringArrayList();
            cost = in.readString();
            description = in.readString();
        }

        public final Creator<RepairlogResponse> CREATOR = new Creator<RepairlogResponse>() {
            @Override
            public RepairlogResponse createFromParcel(Parcel in) {
                return new RepairlogResponse(in);
            }

            @Override
            public RepairlogResponse[] newArray(int size) {
                return new RepairlogResponse[size];
            }
        };

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public String getEquipmentId() {
            return this.equipmentId;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getModelName() {
            return this.modelName;
        }

        public void setModel_bitmap(List<String> model_bitmap) {
            this.model_bitmap = model_bitmap;
        }

        public List<String> getModel_bitmap() {
            return this.model_bitmap;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getCost() {
            return this.cost;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(repairLogId);
            dest.writeString(equipmentId);
            dest.writeString(modelName);
            dest.writeStringList(model_bitmap);
            dest.writeStringList(image_path);
            dest.writeString(cost);
            dest.writeString(description);
        }

        public Integer getRepairLogId() {
            return repairLogId;
        }

        public void setRepairLogId(Integer repairLogId) {
            this.repairLogId = repairLogId;
        }

        public List<String> getImage_path() {
            return image_path;
        }

        public void setImage_path(List<String> image_path) {
            this.image_path = image_path;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }


        public String getNoMilesOrHours() {
            return noMilesOrHours;
        }

        public void setNoMilesOrHours(String noMilesOrHours) {
            this.noMilesOrHours = noMilesOrHours;
        }

        public String getUnitMilesOrHours() {
            return unitMilesOrHours;
        }

        public void setUnitMilesOrHours(String unitMilesOrHours) {
            this.unitMilesOrHours = unitMilesOrHours;
        }
    }
}

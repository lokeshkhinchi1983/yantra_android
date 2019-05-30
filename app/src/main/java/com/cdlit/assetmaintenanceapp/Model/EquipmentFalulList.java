package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EquipmentFalulList extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response implements Parcelable {


        private Integer faultLogId;
        private Location2 location;
        private EquipmentType2 equipmentType;
        private Equipment2 equipment;
        private String userComment;
        private String userFirstName;
        private String logDate;
        private Integer faultType;
        private String loggedDate;
        private List<String> imagePath;
        private String bitmapstring;
        private List<FaultLogComments> faultLogComments;

        protected Response(Parcel in) {


            if (in.readByte() == 0) {
                faultLogId = null;
            } else {
                faultLogId = in.readInt();
            }
            userComment = in.readString();
            userFirstName = in.readString();
            logDate = in.readString();
            if (in.readByte() == 0) {
                faultType = null;
            } else {
                faultType = in.readInt();
            }
            loggedDate = in.readString();
            imagePath = in.createStringArrayList();
            bitmapstring = in.readString();

        }

        public final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel in) {
                return new Response(in);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };

        public void setFaultLogId(Integer faultLogId) {
            this.faultLogId = faultLogId;
        }

        public Integer getFaultLogId() {
            return this.faultLogId;
        }

        public Location2 getLocation() {
            return location;
        }

        public void setLocation(Location2 location) {
            this.location = location;
        }

        public EquipmentType2 getEquipmentType() {
            return equipmentType;
        }

        public void setEquipmentType(EquipmentType2 equipmentType) {
            this.equipmentType = equipmentType;
        }

        public Equipment2 getEquipment() {
            return equipment;
        }

        public void setEquipment(Equipment2 equipment) {
            this.equipment = equipment;
        }

        public String getUserComment() {
            return userComment;
        }

        public void setUserComment(String userComment) {
            this.userComment = userComment;
        }

        public String getUserFirstName() {
            return userFirstName;
        }

        public void setUserFirstName(String userFirstName) {
            this.userFirstName = userFirstName;
        }

        public String getLogDate() {
            return logDate;
        }

        public void setLogDate(String logDate) {
            this.logDate = logDate;
        }

        public Integer getFaultType() {
            return faultType;
        }

        public void setFaultType(Integer faultType) {
            this.faultType = faultType;
        }

        public String getLoggedDate() {
            return loggedDate;
        }

        public void setLoggedDate(String loggedDate) {
            this.loggedDate = loggedDate;
        }

        public List<String> getImagePath() {
            return imagePath;
        }

        public void setImagePath(List<String> imagePath) {
            this.imagePath = imagePath;
        }

        public void setBitmapstring(String bitmapstring) {
            this.bitmapstring = bitmapstring;
        }

        public String getBitmapstring() {
            return this.bitmapstring;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {


            if (faultLogId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(faultLogId);
            }
            dest.writeString(userComment);
            dest.writeString(userFirstName);
            dest.writeString(logDate);
            if (faultType == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(faultType);
            }
            dest.writeString(loggedDate);
            dest.writeStringList(imagePath);
            dest.writeString(bitmapstring);

        }

        public List<FaultLogComments> getFaultLogComments() {
            return faultLogComments;
        }

        public void setFaultLogComments(List<FaultLogComments> faultLogComments) {
            this.faultLogComments = faultLogComments;
        }


    }

    public class Location2 {

        private Integer id;

        private String locationName;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationName() {
            return this.locationName;
        }

    }


    public class FaultLogComments {
        private Integer id;
        private String username;

        private String comment;

        private String faultLogId;

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return this.username;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return this.comment;
        }

        public void setFaultLogId(String faultLogId) {
            this.faultLogId = faultLogId;
        }

        public String getFaultLogId() {
            return this.faultLogId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public class EquipmentType2 {

        private Integer id;

        private String equipmentTypeName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEquipmentTypeName() {
            return equipmentTypeName;
        }

        public void setEquipmentTypeName(String equipmentTypeName) {
            this.equipmentTypeName = equipmentTypeName;
        }

    }

    public class Equipment2 {

        private Integer id;
        private String modelNo;
        private String remarks;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getModelNo() {
            return modelNo;
        }

        public void setModelNo(String modelNo) {
            this.modelNo = modelNo;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

    }

}

package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Notification extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response implements Parcelable {

        private String checklistname;

        private String tasktype;

        private String description;

        private String loggedby;

        private String loggedDate;

        private String status;

        private Integer faulttype;

        private String locationname;

        private String equipmentname;

        private Integer locationid;

        private Integer equipmentid;

        private Integer equipmenttypeid;

        private Integer taskid;

        private Integer faultLogId;

        private String equipmenttypename;

        protected Response(Parcel in) {

            checklistname = in.readString();
            tasktype = in.readString();
            description = in.readString();
            loggedby = in.readString();
            loggedDate = in.readString();
            status = in.readString();
            if (in.readByte() == 0) {
                faulttype = null;
            } else {
                faulttype = in.readInt();
            }
            locationname = in.readString();
            equipmentname = in.readString();
            if (in.readByte() == 0) {
                locationid = null;
            } else {
                locationid = in.readInt();
            }
            if (in.readByte() == 0) {
                equipmentid = null;
            } else {
                equipmentid = in.readInt();
            }
            if (in.readByte() == 0) {
                equipmenttypeid = null;
            } else {
                equipmenttypeid = in.readInt();
            }
            if (in.readByte() == 0) {
                taskid = null;
            } else {
                taskid = in.readInt();
            }
            equipmenttypename = in.readString();

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

        public void setChecklistname(String checklistname) {
            this.checklistname = checklistname;
        }

        public String getChecklistname() {
            return this.checklistname;
        }

        public void setTasktype(String tasktype) {
            this.tasktype = tasktype;
        }

        public String getTasktype() {
            return this.tasktype;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setLoggedby(String loggedby) {
            this.loggedby = loggedby;
        }

        public String getLoggedby() {
            return this.loggedby;
        }

        public void setLoggedDate(String loggedDate) {
            this.loggedDate = loggedDate;
        }

        public String getLoggedDate() {
            return this.loggedDate;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setFaulttype(Integer faulttype) {
            this.faulttype = faulttype;
        }

        public Integer getFaulttype() {
            return this.faulttype;
        }

        public String getLocationname() {
            return locationname;
        }

        public void setLocationname(String locationname) {
            this.locationname = locationname;
        }

        public String getEquipmentname() {
            return equipmentname;
        }

        public void setEquipmentname(String equipmentname) {
            this.equipmentname = equipmentname;
        }

        public Integer getLocationid() {
            return locationid;
        }

        public void setLocationid(Integer locationid) {
            this.locationid = locationid;
        }

        public Integer getEquipmentid() {
            return equipmentid;
        }

        public void setEquipmentid(Integer equipmentid) {
            this.equipmentid = equipmentid;
        }

        public Integer getEquipmenttypeid() {
            return equipmenttypeid;
        }

        public void setEquipmenttypeid(Integer equipmenttypeid) {
            this.equipmenttypeid = equipmenttypeid;
        }

        public String getEquipmenttypename() {
            return equipmenttypename;
        }

        public void setEquipmenttypename(String equipmenttypename) {
            this.equipmenttypename = equipmenttypename;
        }

        public Integer getTaskid() {
            return taskid;
        }

        public void setTaskid(Integer taskid) {
            this.taskid = taskid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(checklistname);
            dest.writeString(tasktype);
            dest.writeString(description);
            dest.writeString(loggedby);
            dest.writeString(loggedDate);
            dest.writeString(status);
            if (faulttype == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(faulttype);
            }
            dest.writeString(locationname);
            dest.writeString(equipmentname);
            if (locationid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(locationid);
            }
            if (equipmentid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(equipmentid);
            }
            if (equipmenttypeid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(equipmenttypeid);
            }
            if (taskid == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(taskid);
            }
            dest.writeString(equipmenttypename);
        }

        public Integer getFaultLogId() {
            return faultLogId;
        }

        public void setFaultLogId(Integer faultLogId) {
            this.faultLogId = faultLogId;
        }
    }


}

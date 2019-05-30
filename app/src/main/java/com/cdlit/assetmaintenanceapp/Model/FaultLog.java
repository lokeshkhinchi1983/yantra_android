package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FaultLog extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response implements Parcelable {

        private Integer faultLogId;
        private String repairLog;
        private String status;
        private String userFirstName;
        private String userLastName;
        private String userComment;
        private String checkListName;

        protected Response(Parcel in) {
            if (in.readByte() == 0) {
                faultLogId = null;
            } else {
                faultLogId = in.readInt();
            }
            repairLog = in.readString();
            status = in.readString();
            userFirstName = in.readString();
            userLastName = in.readString();
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

        public Integer getFaultLogId() {
            return faultLogId;
        }

        public void setFaultLogId(Integer faultLogId) {
            this.faultLogId = faultLogId;
        }

        public String getRepairLog() {
            return repairLog;
        }

        public void setRepairLog(String repairLog) {
            this.repairLog = repairLog;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserFirstName() {
            return userFirstName;
        }

        public void setUserFirstName(String userFirstName) {
            this.userFirstName = userFirstName;
        }

        public String getUserLastName() {
            return userLastName;
        }

        public void setUserLastName(String userLastName) {
            this.userLastName = userLastName;
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
            dest.writeString(repairLog);
            dest.writeString(status);
            dest.writeString(userFirstName);
            dest.writeString(userLastName);
        }

        public String getUserComment() {
            return userComment;
        }

        public void setUserComment(String userComment) {
            this.userComment = userComment;
        }

        public String getCheckListName() {
            return checkListName;
        }

        public void setCheckListName(String checkListName) {
            this.checkListName = checkListName;
        }
    }

}

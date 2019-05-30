package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rakesh on 22-12-2017.
 */

public class EquipmentTimeline extends RestResponse {


    public ArrayList<EquipmentTimelineResponse> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<EquipmentTimelineResponse> response) {
        this.response = response;
    }

    public ArrayList<EquipmentTimelineResponse> response;

    public class EquipmentTimelineResponse implements Parcelable{

        protected EquipmentTimelineResponse(Parcel in) {
            action = in.readString();
            msg = in.readString();
        }

        public  final Creator<EquipmentTimelineResponse> CREATOR = new Creator<EquipmentTimelineResponse>() {
            @Override
            public EquipmentTimelineResponse createFromParcel(Parcel in) {
                return new EquipmentTimelineResponse(in);
            }

            @Override
            public EquipmentTimelineResponse[] newArray(int size) {
                return new EquipmentTimelineResponse[size];
            }
        };

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String action;
        public String msg;
        public Date date;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(action);
            dest.writeString(msg);
        }
    }
}

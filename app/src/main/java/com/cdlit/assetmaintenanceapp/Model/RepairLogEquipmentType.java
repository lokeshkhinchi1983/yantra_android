package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rakesh on 15-12-2017.
 */

public class RepairLogEquipmentType extends RestResponse {

    private List<RepairLogEquipmentTypeResponse> response;

    public List<RepairLogEquipmentTypeResponse> getResponse() {
        return response;
    }

    public void setResponse(List<RepairLogEquipmentTypeResponse> response) {
        this.response = response;
    }

    public class RepairLogEquipmentTypeResponse implements Parcelable {

        private Integer id;
        private String name;
        private String location;

        protected RepairLogEquipmentTypeResponse(Parcel in) {
            name = in.readString();
        }

        public final Creator<RepairLogEquipmentTypeResponse> CREATOR = new Creator<RepairLogEquipmentTypeResponse>() {
            @Override
            public RepairLogEquipmentTypeResponse createFromParcel(Parcel in) {
                return new RepairLogEquipmentTypeResponse(in);
            }

            @Override
            public RepairLogEquipmentTypeResponse[] newArray(int size) {
                return new RepairLogEquipmentTypeResponse[size];
            }
        };

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}

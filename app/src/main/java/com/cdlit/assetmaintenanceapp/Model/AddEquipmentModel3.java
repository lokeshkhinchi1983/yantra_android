package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddEquipmentModel3 implements Parcelable {

    private String serviceName;
    private String serviceFreq;
    private String serviceNo;
    private String lastServiceDate;
    private String nextServiceDate;

    public AddEquipmentModel3() {


    }

    protected AddEquipmentModel3(Parcel in) {
        serviceName = in.readString();
        serviceFreq = in.readString();
        serviceNo = in.readString();
        lastServiceDate = in.readString();
        nextServiceDate = in.readString();
    }

    public static final Creator<AddEquipmentModel3> CREATOR = new Creator<AddEquipmentModel3>() {
        @Override
        public AddEquipmentModel3 createFromParcel(Parcel in) {
            return new AddEquipmentModel3(in);
        }

        @Override
        public AddEquipmentModel3[] newArray(int size) {
            return new AddEquipmentModel3[size];
        }
    };

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceFreq() {
        return serviceFreq;
    }

    public void setServiceFreq(String serviceFreq) {
        this.serviceFreq = serviceFreq;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getLastServiceDate() {
        return lastServiceDate;
    }

    public void setLastServiceDate(String lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public String getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(String nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeString(serviceFreq);
        dest.writeString(serviceNo);
        dest.writeString(lastServiceDate);
        dest.writeString(nextServiceDate);
    }
}

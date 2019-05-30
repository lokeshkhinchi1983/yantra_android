package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rakesh on 15-06-2017.
 */

public class LocationResponse implements Parcelable {

    //  private Integer isActive;

    private Integer id;

    private String locationName;

    private Integer modifiedUser;

    //  private Date createdDate;

    private Date modifiedDate;

    //  private Integer createdUser;

    public LocationResponse() {


    }

    public LocationResponse(Parcel in) {
        //    isActive = in.readInt();
        id = in.readInt();
        locationName = in.readString();
        modifiedUser = in.readInt();
        //   createdUser = in.readInt();
    }

    public static final Creator<LocationResponse> CREATOR = new Creator<LocationResponse>() {
        @Override
        public LocationResponse createFromParcel(Parcel in) {
            return new LocationResponse(in);
        }

        @Override
        public LocationResponse[] newArray(int size) {
            return new LocationResponse[size];
        }
    };

    /* public Integer getIsActive() {
         return isActive;
     }

     public void setIsActive(int isActive) {
         this.isActive = isActive;
     }
 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(Integer modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
/*
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }*/

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

  /*  public Integer getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Integer createdUser) {
        this.createdUser = createdUser;
    }*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //  dest.writeInt(isActive);
        dest.writeInt(id);
        dest.writeString(locationName);
        dest.writeInt(modifiedUser);
        //    dest.writeInt(createdUser);
    }
}



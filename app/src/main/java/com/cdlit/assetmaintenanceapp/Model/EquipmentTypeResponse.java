package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 23-06-2017.
 */

public class EquipmentTypeResponse implements Parcelable {

    private Integer id;
    // private Date createdDate;
    //  private Integer createdUser;
    private String description;
    private String equipmentTypeName;
    // private Integer isActive;
    private Date modifiedDate;
    private Integer modifiedUser;
    //  private EquipmentTypeResponseCategory category;
    private List<EquipmentTypeResponseImages> equipmentTypeImages;
    //  private String catId;
    private Integer isCheckList;

    protected EquipmentTypeResponse(Parcel in) {
        description = in.readString();
        equipmentTypeName = in.readString();
        //  catId = in.readString();
    }

    public static final Creator<EquipmentTypeResponse> CREATOR = new Creator<EquipmentTypeResponse>() {
        @Override
        public EquipmentTypeResponse createFromParcel(Parcel in) {
            return new EquipmentTypeResponse(in);
        }

        @Override
        public EquipmentTypeResponse[] newArray(int size) {
            return new EquipmentTypeResponse[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    /*  public void setCreatedDate(Date createdDate) {
          this.createdDate = createdDate;
      }

      public Date getCreatedDate() {
          return this.createdDate;
      }

      public void setCreatedUser(Integer createdUser) {
          this.createdUser = createdUser;
      }

      public Integer getCreatedUser() {
          return this.createdUser;
      }
  */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }

    public String getEquipmentTypeName() {
        return this.equipmentTypeName;
    }

    /*  public void setIsActive(Integer isActive) {
          this.isActive = isActive;
      }

      public Integer getIsActive() {
          return this.isActive;
      }
  */
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

   /* public void setCategory(EquipmentTypeResponseCategory category) {
        this.category = category;
    }

    public EquipmentTypeResponseCategory getCategory() {
        return this.category;
    }*/

    public void setEquipmentTypeImages(List<EquipmentTypeResponseImages> equipmentTypeImages) {
        this.equipmentTypeImages = equipmentTypeImages;
    }

    public List<EquipmentTypeResponseImages> getEquipmentTypeImages() {
        return this.equipmentTypeImages;
    }

   /* public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatId() {
        return this.catId;
    }*/

    public Integer getIsCheckList() {
        return isCheckList;
    }

    public void setIsCheckList(Integer isCheckList) {
        this.isCheckList = isCheckList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(equipmentTypeName);
        //    dest.writeString(catId);
    }
}

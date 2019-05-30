package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rakesh on 16-06-2017.
 */

public class CategoryResponse implements Parcelable {

    private Integer id;
    private String categoryName;
    private Date modifiedDate;
    private Integer modifiedUser;

    protected CategoryResponse(Parcel in) {
        id = in.readInt();
        categoryName = in.readString();
        //  createdUser = in.readInt();
        // isActive = in.readInt();
        modifiedUser = in.readInt();
    }

    public static final Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {
        @Override
        public CategoryResponse createFromParcel(Parcel in) {
            return new CategoryResponse(in);
        }

        @Override
        public CategoryResponse[] newArray(int size) {
            return new CategoryResponse[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /*  public Date getCreatedDate() {
          return createdDate;
      }

      public void setCreatedDate(Date createdDate) {
          this.createdDate = createdDate;
      }

      public int getCreatedUser() {
          return createdUser;
      }

      public void setCreatedUser(int createdUser) {
          this.createdUser = createdUser;
      }

      public int getIsActive() {
          return isActive;
      }

      public void setIsActive(int isActive) {
          this.isActive = isActive;
      }
  */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(Integer modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(categoryName);
        //  dest.writeInt(createdUser);
        //    dest.writeInt(isActive);
        dest.writeInt(modifiedUser);
    }
}

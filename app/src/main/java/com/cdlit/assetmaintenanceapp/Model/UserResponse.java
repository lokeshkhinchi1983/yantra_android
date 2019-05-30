
package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.DrawableRequestBuilder;

import java.util.Date;

public class UserResponse implements Parcelable {

    private Integer id;

    private String contactNo;

    private Date createdDate;

    private Integer createdUser;

    private String emailId;

    private String firstName;

    private String imagePath;

    private Integer isActive;

    private String lastName;

    private Date modifiedDate;

    private Integer modifiedUser;

    private String password;

    private String employee_no;

    private UserLocation location;

    private Integer passwordChange;

    private String userType;

    private String userName;

    //  private String role;

    //  private String temp_role_id;

    private Integer temp_location_id;

    private String image_bitmap;

    private String original_password;

    private String image_path;

    private DrawableRequestBuilder<String> imag;

    protected UserResponse(Parcel in) {
        contactNo = in.readString();
        //createdDate = in.readString();
        emailId = in.readString();
        firstName = in.readString();
        imagePath = in.readString();
        lastName = in.readString();
        //  modifiedDate = in.readString();
        password = in.readString();
        employee_no = in.readString();
        userType = in.readString();
        userName = in.readString();
        //  role = in.readString();
        //  temp_role_id = in.readString();
        temp_location_id = in.readInt();
        image_bitmap = in.readString();
        original_password = in.readString();
    }

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setCreatedDate(Date createdDate) {
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

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getEmployee_no() {
        return this.employee_no;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public UserLocation getLocation() {
        return this.location;
    }

    public void setPasswordChange(Integer passwordChange) {
        this.passwordChange = passwordChange;
    }

    public Integer getPasswordChange() {
        return this.passwordChange;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

   /* public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public void setTemp_role_id(String temp_role_id) {
        this.temp_role_id = temp_role_id;
    }

    public String getTemp_role_id() {
        return this.temp_role_id;
    }*/

    public void setTemp_location_id(Integer temp_location_id) {
        this.temp_location_id = temp_location_id;
    }

    public Integer getTemp_location_id() {
        return this.temp_location_id;
    }

    public void setImage_bitmap(String image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    public String getImage_bitmap() {
        return this.image_bitmap;
    }

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }

    public String getOriginal_password() {
        return this.original_password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactNo);
        //  dest.writeString(createdDate);
        dest.writeString(emailId);
        dest.writeString(firstName);
        dest.writeString(imagePath);
        dest.writeString(lastName);
        //    dest.writeString(modifiedDate);
        dest.writeString(password);
        dest.writeString(employee_no);
        dest.writeString(userType);
        dest.writeString(userName);
        //    dest.writeString(role);
        //    dest.writeString(temp_role_id);
        //   dest.writeString(temp_location_id);
        dest.writeString(image_bitmap);
        dest.writeString(original_password);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String user_type) {
        this.userType = user_type;
    }

    public DrawableRequestBuilder<String> getImag() {
        return imag;
    }

    public void setImag(DrawableRequestBuilder<String> imag) {
        this.imag = imag;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    public class UserLocation {

        private Integer id;

        private String locationName;

        private String modifiedDate;

        private Integer modifiedUser;

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

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getModifiedDate() {
            return this.modifiedDate;
        }

        public void setModifiedUser(Integer modifiedUser) {
            this.modifiedUser = modifiedUser;
        }

        public Integer getModifiedUser() {
            return this.modifiedUser;
        }

    }

/*
    private Integer id;

    private String contactNo;

    private Date createdDate;

    private Integer createdUser;

    private String emailId;

    private String firstName;

    private String imagePath;

    private Integer isActive;

    private String lastName;

    private Date modifiedDate;

    private Integer modifiedUser;

    private String password;

    private String employee_no;

    private Integer passwordChange;

    private String userName;

    private UserResponseUsertype usertype;

    private String temp_user_type;

    private String image_bitmap = "";

    protected UserResponse(Parcel in) {

        contactNo = in.readString();
        emailId = in.readString();
        firstName = in.readString();
        imagePath = in.readString();
        lastName = in.readString();
        password = in.readString();
        employee_no = in.readString();
        userName = in.readString();
        temp_user_type = in.readString();
        image_bitmap = in.readString();
    }

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setCreatedDate(Date createdDate) {
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

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getEmployee_no() {
        return this.employee_no;
    }

    public void setPasswordChange(Integer passwordChange) {
        this.passwordChange = passwordChange;
    }

    public Integer getPasswordChange() {
        return this.passwordChange;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUsertype(UserResponseUsertype usertype) {
        this.usertype = usertype;
    }

    public UserResponseUsertype getUsertype() {
        return this.usertype;
    }

    public void setTemp_user_type(String temp_user_type) {
        this.temp_user_type = temp_user_type;
    }

    public String getTemp_user_type() {
        return this.temp_user_type;
    }

    public void setImage_bitmap(String image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    public String getImage_bitmap() {
        return this.image_bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactNo);
        dest.writeString(emailId);
        dest.writeString(firstName);
        dest.writeString(imagePath);
        dest.writeString(lastName);
        dest.writeString(password);
        dest.writeString(employee_no);
        dest.writeString(userName);
        dest.writeString(temp_user_type);
        dest.writeString(image_bitmap);
    }*/
}

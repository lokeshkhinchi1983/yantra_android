package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 03-07-2017.
 */

public class UserAddReturn {
/*
    private Integer id;

    private String contactNo;

    private Date createdDate;

    private Integer createdUser;

    private String emailId;

    private String firstName;

    private String imagePath;

    private String image_bitmap;

    private Integer isActive;

    private String lastName;

    private Date modifiedDate;

    private Integer modifiedUser;

    private String password;

    private String employee_no;

    private Integer passwordChange;

    private String userName;

    private Integer temp_user_type;

    private String usertype;

    private String original_password;




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

    public void setImage_bitmap(String image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    public String getImage_bitmap() {
        return this.image_bitmap;
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

    public void setTemp_user_type(Integer temp_user_type) {
        this.temp_user_type = temp_user_type;
    }

    public Integer getTemp_user_type() {
        return this.temp_user_type;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUsertype() {
        return this.usertype;
    }

    public String getOriginal_password() {
        return original_password;
    }

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }*/


    private String contactNo;

    private Date createdDate;

    private Integer createdUser;

    private String emailId;

    private String employee_no;

    private String firstName;

    private Integer id;

    private String imagePath;

    private String image_bitmap;

    private Integer isActive;

    private String lastName;

    private UserAddLocation location;

    private Date modifiedDate;

    private Integer modifiedUser;

    private String original_password;

    private String password;

    private Integer passwordChange;

    private Integer temp_location_id;

    private String userName;

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

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getEmployee_no() {
        return this.employee_no;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImage_bitmap(String image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    public String getImage_bitmap() {
        return this.image_bitmap;
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

    public void setLocation(UserAddLocation location) {
        this.location = location;
    }

    public UserAddLocation getLocation() {
        return this.location;
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

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }

    public String getOriginal_password() {
        return this.original_password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPasswordChange(Integer passwordChange) {
        this.passwordChange = passwordChange;
    }

    public Integer getPasswordChange() {
        return this.passwordChange;
    }

    public void setTemp_location_id(Integer temp_location_id) {
        this.temp_location_id = temp_location_id;
    }

    public Integer getTemp_location_id() {
        return this.temp_location_id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }


    public class UserAddLocation {

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
}

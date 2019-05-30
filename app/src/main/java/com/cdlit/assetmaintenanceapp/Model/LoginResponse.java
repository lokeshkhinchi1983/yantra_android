package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 08-06-2017.
 */

public class LoginResponse {

    public String userid;
    public String username;
    public String firstname;
    public String lastname;
    public String emailid;
    public int active;
    public String createddate;
    public String modifieddate;
    public String usertype;
    public int createduser;
    public int modifieduser;
    public String bitmap;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String imagePath;

    private List<LoginPrivilege> privilege;
    private LoginLocation location;

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCreateduser() {
        return createduser;
    }

    public void setCreateduser(int createduser) {
        this.createduser = createduser;
    }

    public int getModifieduser() {
        return modifieduser;
    }

    public void setModifieduser(int modifieduser) {
        this.modifieduser = modifieduser;
    }

    public List<LoginPrivilege> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<LoginPrivilege> privilege) {
        this.privilege = privilege;
    }

    public LoginLocation getLocation() {
        return location;
    }

    public void setLocation(LoginLocation location) {
        this.location = location;
    }

    public class LoginLocation {

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

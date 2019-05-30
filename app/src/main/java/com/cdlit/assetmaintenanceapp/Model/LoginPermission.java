package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 09-06-2017.
 */

public class LoginPermission {

    public int id;
    public String createdDate;
    public int createdUser;
    public String description;
    public String modifiedDate;
    public int modifiedUser;
    public int roleId;
    public LoginPermissionUser user;
    public int uId;
    public String role_name;

    public LoginPermissionUser getUser() {
        return user;
    }

    public void setUser(LoginPermissionUser user) {
        this.user = user;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }


    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(int createdUser) {
        this.createdUser = createdUser;
    }

    public int getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(int modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }


}

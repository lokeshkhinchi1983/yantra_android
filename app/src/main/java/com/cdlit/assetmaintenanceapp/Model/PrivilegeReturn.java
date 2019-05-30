package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 07-07-2017.
 */

public class PrivilegeReturn {

    private Object action;
    private Date createdDate;
    private Integer createdUser;
    private Integer id;
    private Date modifiedDate;
    private Integer modifiedUser;
    private Integer state;
    private Integer temp_action_id;
    private Integer temp_user_id;
    private String temp_user_type;
    private Object user;

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Integer createdUser) {
        this.createdUser = createdUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTemp_action_id() {
        return temp_action_id;
    }

    public void setTemp_action_id(Integer temp_action_id) {
        this.temp_action_id = temp_action_id;
    }

    public Integer getTemp_user_id() {
        return temp_user_id;
    }

    public void setTemp_user_id(Integer temp_user_id) {
        this.temp_user_id = temp_user_id;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }


    public String getTemp_user_type() {
        return temp_user_type;
    }

    public void setTemp_user_type(String temp_user_type) {
        this.temp_user_type = temp_user_type;
    }
}

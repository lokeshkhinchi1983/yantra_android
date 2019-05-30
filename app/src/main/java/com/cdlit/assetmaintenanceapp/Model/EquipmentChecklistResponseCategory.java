package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 27-06-2017.
 */

public class EquipmentChecklistResponseCategory {


    private Integer id;

    private String categoryName;

    private Date createdDate;

    private Integer createdUser;

    private Integer isActive;

    private Date modifiedDate;

    private Integer modifiedUser;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
    public String getCategoryName(){
        return this.categoryName;
    }
    public void setCreatedDate(Date createdDate){
        this.createdDate = createdDate;
    }
    public Date getCreatedDate(){
        return this.createdDate;
    }
    public void setCreatedUser(Integer createdUser){
        this.createdUser = createdUser;
    }
    public Integer getCreatedUser(){
        return this.createdUser;
    }
    public void setIsActive(Integer isActive){
        this.isActive = isActive;
    }
    public Integer getIsActive(){
        return this.isActive;
    }
    public void setModifiedDate(Date modifiedDate){
        this.modifiedDate = modifiedDate;
    }
    public Date getModifiedDate(){
        return this.modifiedDate;
    }
    public void setModifiedUser(Integer modifiedUser){
        this.modifiedUser = modifiedUser;
    }
    public Integer getModifiedUser(){
        return this.modifiedUser;
    }



}

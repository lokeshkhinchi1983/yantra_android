package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;

/**
 * Created by rakesh on 28-06-2017.
 */

public class EquipmentAddReturnEquipmentImages {

    private String bitmapstring;

    //  private Date createdDate;

    //  private Integer createdUser;

    //   private Integer equipmentId;

    private Integer equipmentTypeId;

    private Integer id;

    private String imagePath;

    private Date modifiedDate;

    private Integer modifiedUser;

    private Integer state;

    public void setBitmapstring(String bitmapstring) {
        this.bitmapstring = bitmapstring;
    }

    public String getBitmapstring() {
        return this.bitmapstring;
    }

 /*   public void setCreatedDate(Date createdDate){
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
    */

   /* public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getEquipmentId() {
        return this.equipmentId;
    }
*/
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

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public Integer getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Integer equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }
}

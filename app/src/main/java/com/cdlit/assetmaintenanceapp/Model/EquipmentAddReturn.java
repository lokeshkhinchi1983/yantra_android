package com.cdlit.assetmaintenanceapp.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 28-06-2017.
 */

public class EquipmentAddReturn {

    // private Date createdDate;

    // private Integer createdUser;

    private Integer equipment_modle_id;
    private Date annualServiceDate;
    private String description;
    private Date nextServiceDate;
    private String due_service_interval;
    private Date last_service_date;
    private Date expiry_date;
    // private String inspectionDuration;
    private String serviceFrequency;
    private Integer eid;
    private List<EquipmentAddReturnEquipmentImages> equipmentImages;
    private Object equipmentType;
    private Integer id;
    //  private Integer isActive;
    private Integer loc_id;
    private Object location;
    private Date manufacturerDate;
    private String modelNo;
    private Date modifiedDate;
    private Integer modifiedUser;
    private String serialNo;
    private String service_type;
    private List<String> emailId;
    private Integer remainderDuration;
    private String remarks;
    private String frequency;
    private String frequencyDay;
    private Integer equipmentCloneId;


    /*
        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }
inspectionDuration
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
  /*  public Date getDue_service_date() {
        return due_service_date;
    }

    public void setDue_service_date(Date due_service_date) {
        this.due_service_date = due_service_date;
    }*/

    public String getDue_service_interval() {
        return due_service_interval;
    }

    public void setDue_service_interval(String due_service_interval) {
        this.due_service_interval = due_service_interval;
    }

    public Date getLast_service_date() {
        return last_service_date;
    }

    public void setLast_service_date(Date last_service_date) {
        this.last_service_date = last_service_date;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

   /* public String getInspection_duration() {
        return inspection_duration;
    }

    public void setInspection_duration(String inspection_duration) {
        this.inspection_duration = inspection_duration;
    }*/

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getEid() {
        return this.eid;
    }

    public void setEquipmentImages(List<EquipmentAddReturnEquipmentImages> equipmentImages) {
        this.equipmentImages = equipmentImages;
    }

    public List<EquipmentAddReturnEquipmentImages> getEquipmentImages() {
        return this.equipmentImages;
    }

    public void setEquipmentType(Object equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Object getEquipmentType() {
        return this.equipmentType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    /*public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return this.isActive;
    }
*/
    public void setLoc_id(Integer loc_id) {
        this.loc_id = loc_id;
    }

    public Integer getLoc_id() {
        return this.loc_id;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getLocation() {
        return this.location;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getModelNo() {
        return this.modelNo;
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

    public Date getAnnualServiceDate() {
        return annualServiceDate;
    }

    public void setAnnualServiceDate(Date annualServiceDate) {
        this.annualServiceDate = annualServiceDate;
    }

    public Date getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(Date manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

   /* public String getInspectionDuration() {
        return inspectionDuration;
    }

    public void setInspectionDuration(String inspectionDuration) {
        this.inspectionDuration = inspectionDuration;
    }*/

    public Integer getEquipment_modle_id() {
        return equipment_modle_id;
    }

    public void setEquipment_modle_id(Integer equipment_modle_id) {
        this.equipment_modle_id = equipment_modle_id;
    }

    public void setEmailId(ArrayList<String> emailId) {
        this.emailId = emailId;
    }

    public List<String> getEmailId() {
        return emailId;
    }

    public Integer getRemainderDuration() {
        return remainderDuration;
    }

    public void setRemainderDuration(Integer remainderDuration) {
        this.remainderDuration = remainderDuration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(Date nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public String getServiceFrequency() {
        return serviceFrequency;
    }

    public void setServiceFrequency(String serviceFrequency) {
        this.serviceFrequency = serviceFrequency;
    }

    public Integer getEquipmentCloneId() {
        return equipmentCloneId;
    }

    public void setEquipmentCloneId(Integer equipmentCloneId) {
        this.equipmentCloneId = equipmentCloneId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyDay() {
        return frequencyDay;
    }

    public void setFrequencyDay(String frequencyDay) {
        this.frequencyDay = frequencyDay;
    }
}

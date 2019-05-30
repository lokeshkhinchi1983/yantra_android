package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rakesh on 22-06-2017.
 */

public class EquipmentResponse implements Parcelable {

    private Integer id;

    private EquipmentResponseEquipmentType equipmentType;

    private EquipmentResponseLocation location;

    private String description;

    private String modelNo;

    private String serialNo;

    private String annualServiceDate;

    private String manufacturerDate;

    private String expiry_date;

    private String last_service_date;

    private String nextServiceDate;

    private String due_service_interval;

    //   private String inspection_duration;

    // private String inspectionDuration;
    private String serviceFrequency;

    private String service_type;

    private String modifiedDate;

    private Integer modifiedUser;

    private String loc_id;

    private List<EquipmentResponseImages> equipmentImages;

    private String eid;

    private List<String> emailId;

    private Integer remainderDuration;

    private String remarks;

    private Integer isCheckList;

    private List<EquipmentServiceTypes> equipmentServiceTypes;

    protected EquipmentResponse(Parcel in) {
        description = in.readString();
        modelNo = in.readString();
        serialNo = in.readString();
        annualServiceDate = in.readString();
        manufacturerDate = in.readString();
        expiry_date = in.readString();
        last_service_date = in.readString();
        nextServiceDate = in.readString();
        due_service_interval = in.readString();
        serviceFrequency = in.readString();
        service_type = in.readString();
        modifiedDate = in.readString();
        loc_id = in.readString();
        eid = in.readString();
    }

    public static final Creator<EquipmentResponse> CREATOR = new Creator<EquipmentResponse>() {
        @Override
        public EquipmentResponse createFromParcel(Parcel in) {
            return new EquipmentResponse(in);
        }

        @Override
        public EquipmentResponse[] newArray(int size) {
            return new EquipmentResponse[size];
        }
    };

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setEquipmentType(EquipmentResponseEquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public EquipmentResponseEquipmentType getEquipmentType() {
        return this.equipmentType;
    }

    public void setLocation(EquipmentResponseLocation location) {
        this.location = location;
    }

    public EquipmentResponseLocation getLocation() {
        return this.location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getModelNo() {
        return this.modelNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setAnnualServiceDate(String annualServiceDate) {
        this.annualServiceDate = annualServiceDate;
    }

    public String getAnnualServiceDate() {
        return this.annualServiceDate;
    }

    public void setManufacturerDate(String manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getManufacturerDate() {
        return this.manufacturerDate;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getExpiry_date() {
        return this.expiry_date;
    }

    public void setLast_service_date(String last_service_date) {
        this.last_service_date = last_service_date;
    }

    public String getLast_service_date() {
        return this.last_service_date;
    }

    /* public void setDue_service_date(String due_service_date) {
         this.due_service_date = due_service_date;
     }

     public String getDue_service_date() {
         return this.due_service_date;
     }
 */
    public void setDue_service_interval(String due_service_interval) {
        this.due_service_interval = due_service_interval;
    }

    public String getDue_service_interval() {
        return this.due_service_interval;
    }

   /* public void setInspection_duration(String inspection_duration) {
        this.inspection_duration = inspection_duration;
    }

    public String getInspection_duration() {
        return this.inspection_duration;
    }*/

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getService_type() {
        return this.service_type;
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

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getLoc_id() {
        return this.loc_id;
    }

    public void setEquipmentImages(List<EquipmentResponseImages> equipmentImages) {
        this.equipmentImages = equipmentImages;
    }

    public List<EquipmentResponseImages> getEquipmentImages() {
        return this.equipmentImages;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return this.eid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(modelNo);
        dest.writeString(serialNo);
        dest.writeString(annualServiceDate);
        dest.writeString(manufacturerDate);
        dest.writeString(expiry_date);
        dest.writeString(last_service_date);
        dest.writeString(nextServiceDate);
        dest.writeString(due_service_interval);
        dest.writeString(serviceFrequency);
        dest.writeString(service_type);
        dest.writeString(modifiedDate);
        dest.writeString(loc_id);
        dest.writeString(eid);
    }

  /*  public String getInspectionDuration() {
        return inspectionDuration;
    }

    public void setInspectionDuration(String inspectionDuration) {
        this.inspectionDuration = inspectionDuration;
    }*/

    public List<String> getEmailId() {
        return emailId;
    }

    public void setEmailId(List<String> emailId) {
        this.emailId = emailId;
    }

    public String getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(String nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public String getServiceFrequency() {
        return serviceFrequency;
    }

    public void setServiceFrequency(String serviceFrequency) {
        this.serviceFrequency = serviceFrequency;
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

    public Integer getIsCheckList() {
        return isCheckList;
    }

    public void setIsCheckList(Integer isCheckList) {
        this.isCheckList = isCheckList;
    }

    public List<EquipmentServiceTypes> getEquipmentServiceTypes() {
        return equipmentServiceTypes;
    }

    public void setEquipmentServiceTypes(List<EquipmentServiceTypes> equipmentServiceTypes) {
        this.equipmentServiceTypes = equipmentServiceTypes;
    }

    public class EquipmentResponseEquipmentType {
        private Integer id;

        private String description;

        private String equipmentTypeName;

        private String modifiedDate;

        private Integer modifiedUser;

        private String equipmentTypeImages;

        private String catId;

        private String isCheckList;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

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

        public void setEquipmentTypeImages(String equipmentTypeImages) {
            this.equipmentTypeImages = equipmentTypeImages;
        }

        public String getEquipmentTypeImages() {
            return this.equipmentTypeImages;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getCatId() {
            return this.catId;
        }

        public void setIsCheckList(String isCheckList) {
            this.isCheckList = isCheckList;
        }

        public String getIsCheckList() {
            return this.isCheckList;
        }

    }

    public class EquipmentResponseLocation {

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

    public class EquipmentResponseImages {
        private Integer id;

        private Integer equipmentId;

        private String imagePath;

        private String modifiedDate;

        private Integer modifiedUser;

        private String bitmapstring;

        private Integer state;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setEquipmentId(Integer equipmentId) {
            this.equipmentId = equipmentId;
        }

        public Integer getEquipmentId() {
            return this.equipmentId;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return this.imagePath;
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

        public void setBitmapstring(String bitmapstring) {
            this.bitmapstring = bitmapstring;
        }

        public String getBitmapstring() {
            return this.bitmapstring;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public Integer getState() {
            return this.state;
        }

    }

    public class EquipmentServiceTypes {

        private Integer id;

        private String serviceName;

        private String frequency;

        private Integer frequencyNo;

        private String lastCheckDate;

        private String nextCheckDate;

        private String status;

        private Integer createdBy;

        private String createdDate;

        private Integer modifiedBy;

        private String modifiedDate;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceName() {
            return this.serviceName;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getFrequency() {
            return this.frequency;
        }

        public void setFrequencyNo(Integer frequencyNo) {
            this.frequencyNo = frequencyNo;
        }

        public Integer getFrequencyNo() {
            return this.frequencyNo;
        }

        public void setLastCheckDate(String lastCheckDate) {
            this.lastCheckDate = lastCheckDate;
        }

        public String getLastCheckDate() {
            return this.lastCheckDate;
        }

        public void setNextCheckDate(String nextCheckDate) {
            this.nextCheckDate = nextCheckDate;
        }

        public String getNextCheckDate() {
            return this.nextCheckDate;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getCreatedBy() {
            return this.createdBy;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedDate() {
            return this.createdDate;
        }

        public void setModifiedBy(int modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public Integer getModifiedBy() {
            return this.modifiedBy;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getModifiedDate() {
            return this.modifiedDate;
        }
    }
}

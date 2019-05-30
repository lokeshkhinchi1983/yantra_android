package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 14-12-2017.
 */

public class RepairLogDetail extends RestResponse {

    private RepairLogDetailResponse response;

    public void setResponse(RepairLogDetailResponse response) {
        this.response = response;
    }

    public RepairLogDetailResponse getResponse() {
        return this.response;
    }

    public class RepairLogDetailResponse implements Parcelable {

        private Integer id;

        private RepairLogDetailEquipment equipment;

        private Integer cost;

        private String description;

        private String location;
        private Integer location_id;

        private String agencyName;
        private String contactPersonName;
        private String repairLogDate;
        private String contactNumber;

        private String noMilesOrHours;
        private String unitMilesOrHours;



        private List<RepairLogDetailRepairLogImages> repairLogImages;

        //    private String temp_equipment_id;

        protected RepairLogDetailResponse(Parcel in) {
            description = in.readString();
            // temp_equipment_id = in.readString();
        }

        public final Creator<RepairLogDetailResponse> CREATOR = new Creator<RepairLogDetailResponse>() {
            @Override
            public RepairLogDetailResponse createFromParcel(Parcel in) {
                return new RepairLogDetailResponse(in);
            }

            @Override
            public RepairLogDetailResponse[] newArray(int size) {
                return new RepairLogDetailResponse[size];
            }
        };

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setEquipment(RepairLogDetailEquipment equipment) {
            this.equipment = equipment;
        }

        public RepairLogDetailEquipment getEquipment() {
            return this.equipment;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public Integer getCost() {
            return this.cost;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        /*  public void setModifiedDate(Date modifiedDate) {
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
  */
        public void setRepairLogImages(List<RepairLogDetailRepairLogImages> repairLogImages) {
            this.repairLogImages = repairLogImages;
        }

        public List<RepairLogDetailRepairLogImages> getRepairLogImages() {
            return this.repairLogImages;
        }

    /*    public void setTemp_equipment_id(String temp_equipment_id) {
            this.temp_equipment_id = temp_equipment_id;
        }

        public String getTemp_equipment_id() {
            return this.temp_equipment_id;
        }
*/

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(description);
            //    dest.writeString(temp_equipment_id);
        }

        public Integer getLocation_id() {
            return location_id;
        }

        public void setLocation_id(Integer location_id) {
            this.location_id = location_id;
        }

        public String getContactPersonName() {
            return contactPersonName;
        }

        public void setContactPersonName(String contactPersonName) {
            this.contactPersonName = contactPersonName;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getRepairLogDate() {
            return repairLogDate;
        }

        public void setRepairLogDate(String repairLogDate) {
            this.repairLogDate = repairLogDate;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNoMilesOrHours() {
            return noMilesOrHours;
        }

        public void setNoMilesOrHours(String noMilesOrHours) {
            this.noMilesOrHours = noMilesOrHours;
        }

        public String getUnitMilesOrHours() {
            return unitMilesOrHours;
        }

        public void setUnitMilesOrHours(String unitMilesOrHours) {
            this.unitMilesOrHours = unitMilesOrHours;
        }
    }

    public class RepairLogDetailRepairLogImages {

        private Integer id;

        private Integer repairLogId;

        private String imagePath;

        private Date modifiedDate;

        private Integer modifiedUser;

        private String bitmapstring;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setRepairLogId(Integer repairLogId) {
            this.repairLogId = repairLogId;
        }

        public Integer getRepairLogId() {
            return this.repairLogId;
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

        public void setBitmapstring(String bitmapstring) {
            this.bitmapstring = bitmapstring;
        }

        public String getBitmapstring() {
            return this.bitmapstring;
        }


    }

    public class RepairLogDetailEquipment {

        private Integer id;

        private RepairLogDetailEquipmentType equipmentType;

        private RepairLogDetailLocation location;

        // private RepairLogDetailLocation location;

        //   private String description;

        private String modelNo;

     /*   private String serialNo;

        private String annualServiceDate;

        private String manufacturerDate;

        private String expiry_date;

        private String last_service_date;

        private String due_service_date;

        private String due_service_interval;

        private String inspectionDuration;

        private String service_type;

        private String modifiedDate;

        private Integer modifiedUser;

        private String loc_id;

        private String equipmentImages;

        private String eid;*/

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setEquipmentType(RepairLogDetailEquipmentType equipmentType) {
            this.equipmentType = equipmentType;
        }

        public RepairLogDetailEquipmentType getEquipmentType() {
            return this.equipmentType;
        }

       /* public void setLocation(RepairLogDetailLocation location) {
            this.location = location;
        }

        public RepairLogDetailLocation getLocation() {
            return this.location;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }*/

        public void setModelNo(String modelNo) {
            this.modelNo = modelNo;
        }

        public String getModelNo() {
            return this.modelNo;
        }

        public RepairLogDetailLocation getLocation3() {
            return location;
        }

        public void setLocation3(RepairLogDetailLocation location) {
            this.location = location;
        }

       /* public void setSerialNo(String serialNo) {
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

        public void setDue_service_date(String due_service_date) {
            this.due_service_date = due_service_date;
        }

        public String getDue_service_date() {
            return this.due_service_date;
        }

        public void setDue_service_interval(String due_service_interval) {
            this.due_service_interval = due_service_interval;
        }

        public String getDue_service_interval() {
            return this.due_service_interval;
        }

        public void setInspectionDuration(String inspectionDuration) {
            this.inspectionDuration = inspectionDuration;
        }

        public String getInspectionDuration() {
            return this.inspectionDuration;
        }

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

        public void setEquipmentImages(String equipmentImages) {
            this.equipmentImages = equipmentImages;
        }

        public String getEquipmentImages() {
            return this.equipmentImages;
        }

        public void setEid(String eid) {
            this.eid = eid;
        }

        public String getEid() {
            return this.eid;
        }*/

    }

    public class RepairLogDetailEquipmentType {
        //  private Integer id;

        //  private String description;

        private String equipmentTypeName;
/*

        private Date modifiedDate;

        private Integer modifiedUser;

        private String equipmentTypeImages;

        private String catId;

        private String isCheckList;
*/

        /*  public void setId(Integer id) {
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
  */
        public void setEquipmentTypeName(String equipmentTypeName) {
            this.equipmentTypeName = equipmentTypeName;
        }

        public String getEquipmentTypeName() {
            return this.equipmentTypeName;
        }

        /*public void setModifiedDate(Date modifiedDate) {
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
        */

    }

    public class RepairLogDetailLocation {

        private Integer id;

        private String locationName;

        /*  private String modifiedDate;

          private Integer modifiedUser;
  */
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

        /*public void setModifiedDate(String modifiedDate) {
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
*/
    }


}

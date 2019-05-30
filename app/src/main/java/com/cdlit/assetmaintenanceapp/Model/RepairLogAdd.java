package com.cdlit.assetmaintenanceapp.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rakesh on 12-12-2017.
 */

public class RepairLogAdd {

    private Integer cost;

    private String description;

    private String equipment;

    private Integer id;

    private Date modifiedDate;

    private Integer modifiedUser;

    private List<RepairLogImages> repairLogImages;

    private List<Integer> notificationId;
    private List<Faults> faults;
    private List<Tasks> tasks;

    private String logType;
    private Integer temp_equipment_id;

    private ArrayList<FaultLog> faultLogList;

    private ArrayList<NotificationLog> notificationLogList;

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getRepairLogDate() {
        return repairLogDate;
    }

    public void setRepairLogDate(String repairLogDate) {
        this.repairLogDate = repairLogDate;
    }

    private String agencyName;
    private String contactPersonName;
    private String repairLogDate;
    private String contactNumber;
    private String noMilesOrHours;
    private String unitMilesOrHours;

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

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipment() {
        return this.equipment;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
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

    public void setRepairLogImages(List<RepairLogImages> repairLogImages) {
        this.repairLogImages = repairLogImages;
    }

    public List<RepairLogImages> getRepairLogImages() {
        return this.repairLogImages;
    }

    public void setTemp_equipment_id(Integer temp_equipment_id) {
        this.temp_equipment_id = temp_equipment_id;
    }

    public Integer getTemp_equipment_id() {
        return this.temp_equipment_id;
    }

    public List<Integer> getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(List<Integer> notificationId) {
        this.notificationId = notificationId;
    }

    public void setFaultLogList(ArrayList<FaultLog> faultLogList) {
        this.faultLogList = faultLogList;
    }

    public void setNotificationLogList(ArrayList<NotificationLog> notificationLogList) {
        this.notificationLogList = notificationLogList;
    }

    public ArrayList<NotificationLog> getNotificationLogList() {
        return notificationLogList;
    }

    public ArrayList<FaultLog> getFaultLogList() {
        return faultLogList;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<RepairLogAdd.Faults> getFaults() {
        return faults;
    }

    public void setFaults(List<RepairLogAdd.Faults> faults) {
        this.faults = faults;
    }

    public List<RepairLogAdd.Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<RepairLogAdd.Tasks> tasks) {
        this.tasks = tasks;
    }

    public String getLogType() {
        return logType;
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

   /* public void setFaultLogId(ArrayList<Integer> faultLogId) {
        this.faultLogId = faultLogId;
    }

    public List<Integer> getFaultLogId() {
        return faultLogId;
    }

    public List<Integer> getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(List<Integer> taskLogId) {
        this.taskLogId = taskLogId;
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    public List<Integer> getFaults() {
        return faults;
    }

    public void setFaults(List<Faults> faults) {
        this.faults = faults;
    }
*/

    public static class RepairLogImages {

        public RepairLogImages() {

        }


        private String bitmapstring;

        private Integer id;

        private String imagePath;

        private Date modifiedDate;

        private Integer modifiedUser;

        public void setBitmapstring(String bitmapstring) {
            this.bitmapstring = bitmapstring;
        }

        public String getBitmapstring() {
            return this.bitmapstring;
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
    }

    public static class FaultLog {

        private Integer faultLogId;
        private String status;

        public Integer getFaultLogId() {
            return faultLogId;
        }

        public void setFaultLogId(Integer faultLogId) {
            this.faultLogId = faultLogId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class NotificationLog {

        private Integer id;
        private String status;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public static class Faults {
        private Integer faultLogId;
        private String status;


        public Integer getFaultLogId() {
            return faultLogId;
        }

        public void setFaultLogId(Integer faultLogId) {
            this.faultLogId = faultLogId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static  class Tasks {

        private Integer id;
        private String status;
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}

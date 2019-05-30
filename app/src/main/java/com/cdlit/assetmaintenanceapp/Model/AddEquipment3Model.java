package com.cdlit.assetmaintenanceapp.Model;

import java.util.Date;
import java.util.List;

public class AddEquipment3Model {

    private List<ServiceTypes> serviceTypes;

    public void setServiceTypes(List<ServiceTypes> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<ServiceTypes> getServiceTypes() {
        return this.serviceTypes;
    }


    public static class ServiceTypes {

        private Integer equipmentId;

        private String frequency;

        private Integer frequency_no;

        private Integer id;

        private Date last_check_date;

        private Date next_check_date;

        private String service_name;

        private Integer userid;

        public void setEquipmentId(Integer equipmentId) {
            this.equipmentId = equipmentId;
        }

        public Integer getEquipmentId() {
            return this.equipmentId;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getFrequency() {
            return this.frequency;
        }

        public void setFrequency_no(Integer frequency_no) {
            this.frequency_no = frequency_no;
        }

        public Integer getFrequency_no() {
            return this.frequency_no;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setLast_check_date(Date last_check_date) {
            this.last_check_date = last_check_date;
        }

        public Date getLast_check_date() {
            return this.last_check_date;
        }

        public void setNext_check_date(Date next_check_date) {
            this.next_check_date = next_check_date;
        }

        public Date getNext_check_date() {
            return this.next_check_date;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_name() {
            return this.service_name;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public Integer getUserid() {
            return this.userid;
        }

    }
}

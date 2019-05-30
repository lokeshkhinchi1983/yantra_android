package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 14-12-2017.
 */

public class RepairLogEquipment extends RestResponse {

    private List<RepairLogEquipmentResponse> response;

    public void setResponse(List<RepairLogEquipmentResponse> response) {
        this.response = response;
    }

    public List<RepairLogEquipmentResponse> getResponse() {
        return this.response;
    }

    public class RepairLogEquipmentResponse {

        private Integer id;

        private String modalname;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setModalname(String modalname) {
            this.modalname = modalname;
        }

        public String getModalname() {
            return this.modalname;
        }

    }
}

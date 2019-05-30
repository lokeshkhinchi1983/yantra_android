package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 05-09-2017.
 */

public class EquipmentList extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response {

        private int equipment_id;

        private String model_no;

        private int equipment_type_id;

        private String location_name;

        private int location_id;

        private List<String> image_bitmap;

        private String message;

        private String description;

        public void setEquipment_id(int equipment_id) {
            this.equipment_id = equipment_id;
        }

        public int getEquipment_id() {
            return this.equipment_id;
        }

        public void setModel_no(String model_no) {
            this.model_no = model_no;
        }

        public String getModel_no() {
            return this.model_no;
        }

        public void setEquipment_type_id(int equipment_type_id) {
            this.equipment_type_id = equipment_type_id;
        }

        public int getEquipment_type_id() {
            return this.equipment_type_id;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }

        public String getLocation_name() {
            return this.location_name;
        }

        public void setLocation_id(int location_id) {
            this.location_id = location_id;
        }

        public int getLocation_id() {
            return this.location_id;
        }

        public void setImage_bitmap(List<String> image_bitmap) {
            this.image_bitmap = image_bitmap;
        }

        public List<String> getImage_bitmap() {
            return this.image_bitmap;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 17-10-2017.
 */

public class UserChecked extends RestResponse {

    private List<UserCheckedResponse> response;

    public void setResponse(List<UserCheckedResponse> response) {
        this.response = response;
    }

    public List<UserCheckedResponse> getResponse() {
        return this.response;
    }

    public class UserCheckedResponse {

        private Integer user_id;

        private String user_bitmap;
        private String image_path;
        private String msg;

        private String created_time_stamp;

        private Integer equipment_id;

        private Integer equipment_type_id;

        private Integer user_checked_id;

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public Integer getUser_id() {
            return this.user_id;
        }

        public void setUser_bitmap(String user_bitmap) {
            this.user_bitmap = user_bitmap;
        }

        public String getUser_bitmap() {
            return this.user_bitmap;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setCreated_time_stamp(String created_time_stamp) {
            this.created_time_stamp = created_time_stamp;
        }

        public String getCreated_time_stamp() {
            return this.created_time_stamp;
        }

        public void setEquipment_id(Integer equipment_id) {
            this.equipment_id = equipment_id;
        }

        public Integer getEquipment_id() {
            return this.equipment_id;
        }

        public void setEquipment_type_id(Integer equipment_type_id) {
            this.equipment_type_id = equipment_type_id;
        }

        public Integer getEquipment_type_id() {
            return this.equipment_type_id;
        }

        public Integer getUser_checked_id() {
            return user_checked_id;
        }

        public void setUser_checked_id(Integer user_checked_id) {
            this.user_checked_id = user_checked_id;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }


}

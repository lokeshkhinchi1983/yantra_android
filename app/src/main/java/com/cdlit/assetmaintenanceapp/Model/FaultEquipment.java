package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

public class FaultEquipment extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response {

        private Integer id;

        private Location1 location;

        private String modelNo;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public void setModelNo(String modelNo) {
            this.modelNo = modelNo;
        }

        public String getModelNo() {
            return this.modelNo;
        }

        public Location1 getLocation() {
            return location;
        }

        public void setLocation(Location1 location) {
            this.location = location;
        }

    }

    public class Location1 {

        private Integer id;

        private String locationName;

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

    }
}

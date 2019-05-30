package com.cdlit.assetmaintenanceapp.Model;

public class EquipmentModelAddReturn extends RestResponse {

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public class Response {
        private int id;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }
}

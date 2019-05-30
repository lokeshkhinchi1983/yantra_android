package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 16-06-2017.
 */

public class LocationDeactive extends RestResponse {

    private LoationDeactiveResponse response;

    public void setResponse(LoationDeactiveResponse response) {
        this.response = response;
    }

    public LoationDeactiveResponse getResponse() {
        return this.response;
    }

}

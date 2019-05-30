package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 15-06-2017.
 */

public class Location extends RestResponse {

    private List<LocationResponse> response;

    public List<LocationResponse> getResponse() {
        return response;
    }

    public void setResponse(List<LocationResponse> response) {
        this.response = response;
    }

}

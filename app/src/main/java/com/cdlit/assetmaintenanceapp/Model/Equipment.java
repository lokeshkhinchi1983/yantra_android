package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 22-06-2017.
 */

public class Equipment extends RestResponse {

    private List<EquipmentResponse> response;

    public List<EquipmentResponse> getResponse() {
        return response;
    }

    public void setResponse(List<EquipmentResponse> response) {
        this.response = response;
    }

}

package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 23-06-2017.
 */

public class EquipmentType extends RestResponse {

    private List<EquipmentTypeResponse> response;

    public List<EquipmentTypeResponse> getResponse() {
        return response;
    }

    public void setResponse(List<EquipmentTypeResponse> response) {
        this.response = response;
    }

}

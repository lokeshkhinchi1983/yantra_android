package com.cdlit.assetmaintenanceapp.Model;

import java.util.ArrayList;

/**
 * Created by rakesh on 27-06-2017.
 */

public class EquipmentChecklist extends RestResponse {

    private ArrayList<EquipmentChecklistResponse> response;

    public ArrayList<EquipmentChecklistResponse> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<EquipmentChecklistResponse> response) {
        this.response = response;
    }
}

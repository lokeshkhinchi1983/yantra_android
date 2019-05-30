package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 07-07-2017.
 */

public class Privilege extends RestResponse {

    private List<PrivilegeResponse> response;

    public List<PrivilegeResponse> getResponse() {
        return response;
    }

    public void setResponse(List<PrivilegeResponse> response) {
        this.response = response;
    }


}

package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 10-07-2017.
 */

public class PrivilegeAll extends RestResponse {

    private List<PrivilegeAllResponse> response;

    public List<PrivilegeAllResponse> getResponse() {
        return response;
    }

    public void setResponse(List<PrivilegeAllResponse> response) {
        this.response = response;
    }
}

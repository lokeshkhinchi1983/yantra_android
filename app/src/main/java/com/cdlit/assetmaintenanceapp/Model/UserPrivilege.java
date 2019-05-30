package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 12-07-2017.
 */

public class UserPrivilege extends RestResponse {

    private List<UserPrivilegeResponse> response;

    public List<UserPrivilegeResponse> getResponse() {
        return response;
    }

    public void setResponse(List<UserPrivilegeResponse> response) {
        this.response = response;
    }
}

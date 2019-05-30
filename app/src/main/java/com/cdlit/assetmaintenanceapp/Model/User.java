package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 04-07-2017.
 */

public class User extends RestResponse {

    private List<UserResponse> response;

    public List<UserResponse> getResponse() {
        return response;
    }

    public void setResponse(List<UserResponse> response) {
        this.response = response;
    }

}

package com.cdlit.assetmaintenanceapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakesh on 08-06-2017.
 */


public class Login extends RestResponse {

    @SerializedName("response")
    private LoginResponse loginResponse;

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

}

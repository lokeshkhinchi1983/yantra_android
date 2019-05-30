package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 08-06-2017.
 */


public class RestResponse {

    private String status;
    private String message;
    private Integer code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}

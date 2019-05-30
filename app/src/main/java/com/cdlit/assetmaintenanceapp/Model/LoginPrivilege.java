package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 06-07-2017.
 */

public class LoginPrivilege {

    private Integer id;

    private String action;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}

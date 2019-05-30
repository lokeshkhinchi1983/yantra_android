package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 07-07-2017.
 */

public class PrivilegeResponse {

    private Integer id;

    private String action;

    private String type;

    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setAction(String action){
        this.action = action;
    }
    public String getAction(){
        return this.action;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

}

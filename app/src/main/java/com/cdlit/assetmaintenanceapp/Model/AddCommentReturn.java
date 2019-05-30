package com.cdlit.assetmaintenanceapp.Model;

public class AddCommentReturn {

    private Integer id;

    private String comment;

    private Integer faultLogId;

    private String username;

    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return this.comment;
    }
    public void setFaultLogId(Integer faultLogId){
        this.faultLogId = faultLogId;
    }
    public Integer getFaultLogId(){
        return this.faultLogId;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

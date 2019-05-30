package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 12-07-2017.
 */

public class UserPrivilegeResponse {

    private Integer id;
    private UserPrivilegeResponseAction action;
    private UserPrivilegeResponseUser user;
    private String temp_user_type;

    public UserPrivilegeResponseAction getAction() {
        return action;
    }

    public void setAction(UserPrivilegeResponseAction action) {
        this.action = action;
    }

    public UserPrivilegeResponseUser getUser() {
        return user;
    }

    public void setUser(UserPrivilegeResponseUser user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemp_user_type() {
        return temp_user_type;
    }

    public void setTemp_user_type(String temp_user_type) {
        this.temp_user_type = temp_user_type;
    }

    public class UserPrivilegeResponseUser {

        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public class UserPrivilegeResponseAction {

        private Integer id;

        private String action;

        private String type;

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

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }


    }

}

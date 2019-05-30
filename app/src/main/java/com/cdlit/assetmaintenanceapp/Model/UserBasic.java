package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 08-11-2017.
 */

public class UserBasic extends RestResponse {

 /*   private String message;

    private int code;

    private String status;*/

    private List<UserBasicResponse> response;

    /*public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
*/
    /*public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
*/
    public void setResponse(List<UserBasicResponse> response) {
        this.response = response;
    }

    public List<UserBasicResponse> getResponse() {
        return this.response;
    }


    public class UserBasicResponse {

        private int userid;

        private String username;

        private String name;

        private String userType;

        private String locationName;

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getUserid() {
            return this.userid;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return this.username;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}

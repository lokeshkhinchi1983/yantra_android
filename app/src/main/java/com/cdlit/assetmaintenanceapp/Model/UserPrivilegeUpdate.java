package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 11-10-2017.
 */

public class UserPrivilegeUpdate extends RestResponse {

    private UserPrivilegeUpdateResponse response;

    public void setResponse(UserPrivilegeUpdateResponse response) {
        this.response = response;
    }

    public UserPrivilegeUpdateResponse getResponse() {
        return this.response;
    }


    public class PrivilegeUpdate {

        private int id;

        private String action;

        private String type;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
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

    public class UserPrivilegeUpdateResponse {

        public int userid;

        public List<PrivilegeUpdate> privilege;

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getUserid() {
            return this.userid;
        }

        public void setPrivilege(List<PrivilegeUpdate> privilege) {
            this.privilege = privilege;
        }

        public List<PrivilegeUpdate> getPrivilege() {
            return this.privilege;
        }

    }
}

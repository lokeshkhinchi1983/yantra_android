package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 13-10-2017.
 */

public class UserUpdate extends RestResponse {

    private UserUpdateResponse response;

    public UserUpdateResponse getResponse() {
        return response;
    }

    public void setResponse(UserUpdateResponse response) {
        this.response = response;
    }

    public class UserUpdateResponse {

        private Integer id;

       /* private String contactNo;

        private String createdDate;

        private Integer createdUser;*/


        private String emailId;
        private String firstName;
        private String lastName;
        private Integer locationId;
        private String locationName;
       /* private String firstName;

        private String imagePath;

        private Integer isActive;

        private String lastName;

        private String modifiedDate;

        private Integer modifiedUser;

        private String password;

        private String employee_no;

        private Integer passwordChange;*/


        private String userName;

      /*  private String role;

        private String temp_role_id;*/


        private String image_bitmap;

        private String image_path;

        //  private String original_password;

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        /*    public void setContactNo(String contactNo) {
                this.contactNo = contactNo;
            }

            public String getContactNo() {
                return this.contactNo;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getCreatedDate() {
                return this.createdDate;
            }

            public void setCreatedUser(Integer createdUser) {
                this.createdUser = createdUser;
            }

            public Integer getCreatedUser() {
                return this.createdUser;
            }
    */
        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getEmailId() {
            return this.emailId;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstName() {
            return this.firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastName() {
            return this.lastName;
        }

        /*   public void setFirstName(String firstName) {
               this.firstName = firstName;
           }

           public String getFirstName() {
               return this.firstName;
           }

           public void setImagePath(String imagePath) {
               this.imagePath = imagePath;
           }

           public String getImagePath() {
               return this.imagePath;
           }

           public void setIsActive(Integer isActive) {
               this.isActive = isActive;
           }

           public Integer getIsActive() {
               return this.isActive;
           }

           public void setLastName(String lastName) {
               this.lastName = lastName;
           }

           public String getLastName() {
               return this.lastName;
           }

           public void setModifiedDate(String modifiedDate) {
               this.modifiedDate = modifiedDate;
           }

           public String getModifiedDate() {
               return this.modifiedDate;
           }

           public void setModifiedUser(Integer modifiedUser) {
               this.modifiedUser = modifiedUser;
           }

           public Integer getModifiedUser() {
               return this.modifiedUser;
           }

           public void setPassword(String password) {
               this.password = password;
           }

           public String getPassword() {
               return this.password;
           }

           public void setEmployee_no(String employee_no) {
               this.employee_no = employee_no;
           }

           public String getEmployee_no() {
               return this.employee_no;
           }

           public void setPasswordChange(Integer passwordChange) {
               this.passwordChange = passwordChange;
           }

           public Integer getPasswordChange() {
               return this.passwordChange;
           }
   */
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return this.userName;
        }

        /* public void setRole(String role) {
             this.role = role;
         }

         public String getRole() {
             return this.role;
         }

         public void setTemp_role_id(String temp_role_id) {
             this.temp_role_id = temp_role_id;
         }

         public String getTemp_role_id() {
             return this.temp_role_id;
         }
 */
        public void setImage_bitmap(String image_bitmap) {
            this.image_bitmap = image_bitmap;
        }

        public String getImage_bitmap() {
            return this.image_bitmap;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        /*public void setOriginal_password(String original_password) {
            this.original_password = original_password;
        }

        public String getOriginal_password() {
            return this.original_password;
        }*/
    }
}

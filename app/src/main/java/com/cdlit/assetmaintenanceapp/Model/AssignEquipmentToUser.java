package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AssignEquipmentToUser extends RestResponse {

    private List<Response> response;

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response implements Parcelable {
        private Integer isCheckList;

        private String description;

        private String equipmentName;

        private List<EquipmentImages> equipmentImages;

        private Integer id;

        protected Response(Parcel in) {
            if (in.readByte() == 0) {
                isCheckList = null;
            } else {
                isCheckList = in.readInt();
            }
            description = in.readString();
            equipmentName = in.readString();
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
        }

        public final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel in) {
                return new Response(in);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };

        public void setIsCheckList(Integer isCheckList) {
            this.isCheckList = isCheckList;
        }

        public Integer getIsCheckList() {
            return this.isCheckList;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public String getEquipmentName() {
            return this.equipmentName;
        }

        public void setEquipmentImages(List<EquipmentImages> equipmentImages) {
            this.equipmentImages = equipmentImages;
        }

        public List<EquipmentImages> getEquipmentImages() {
            return this.equipmentImages;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (isCheckList == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(isCheckList);
            }
            dest.writeString(description);
            dest.writeString(equipmentName);
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
        }

        public class EquipmentImages {

            private Integer id;

            private Integer equipmentId;

            private String imagePath;

            private String modifiedDate;

            private Integer modifiedUser;

            private String bitmapstring;

            private Integer state;

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getId() {
                return this.id;
            }

            public void setEquipmentId(Integer equipmentId) {
                this.equipmentId = equipmentId;
            }

            public Integer getEquipmentId() {
                return this.equipmentId;
            }

            public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
            }

            public String getImagePath() {
                return this.imagePath;
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

            public void setBitmapstring(String bitmapstring) {
                this.bitmapstring = bitmapstring;
            }

            public String getBitmapstring() {
                return this.bitmapstring;
            }

            public void setState(Integer state) {
                this.state = state;
            }

            public Integer getState() {
                return this.state;
            }


        }
    }
}



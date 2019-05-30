package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AssignedEquipment extends RestResponse {

    private List<Response> response;


    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public List<Response> getResponse() {
        return this.response;
    }

    public class Response implements Parcelable {

        private String equipment_name;

        private Image_path image_path;

        private String image_bitmap;

        private String equipment_description;

        private Integer assigned_to_user_id;

        private Integer equipment_id;

        protected Response(Parcel in) {
            equipment_name = in.readString();
            image_bitmap = in.readString();
            equipment_description = in.readString();
            if (in.readByte() == 0) {
                assigned_to_user_id = null;
            } else {
                assigned_to_user_id = in.readInt();
            }
            if (in.readByte() == 0) {
                equipment_id = null;
            } else {
                equipment_id = in.readInt();
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

        public void setEquipment_name(String equipment_name) {
            this.equipment_name = equipment_name;
        }

        public String getEquipment_name() {
            return this.equipment_name;
        }

        public void setImage_path(Image_path image_path) {
            this.image_path = image_path;
        }

        public Image_path getImage_path() {
            return this.image_path;
        }

        public void setImage_bitmap(String image_bitmap) {
            this.image_bitmap = image_bitmap;
        }

        public String getImage_bitmap() {
            return this.image_bitmap;
        }

        public void setEquipment_description(String equipment_description) {
            this.equipment_description = equipment_description;
        }

        public String getEquipment_description() {
            return this.equipment_description;
        }

        public void setAssigned_to_user_id(Integer assigned_to_user_id) {
            this.assigned_to_user_id = assigned_to_user_id;
        }

        public Integer getAssigned_to_user_id() {
            return this.assigned_to_user_id;
        }

        public void setEquipment_id(Integer equipment_id) {
            this.equipment_id = equipment_id;
        }

        public Integer getEquipment_id() {
            return this.equipment_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(equipment_name);
            dest.writeString(image_bitmap);
            dest.writeString(equipment_description);
            if (assigned_to_user_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(assigned_to_user_id);
            }
            if (equipment_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(equipment_id);
            }
        }

        public class Image_path {

            private List<String> image_path;

            private List<String> image_bitmap;

            public void setImage_path(List<String> image_path) {
                this.image_path = image_path;
            }

            public List<String> getImage_path() {
                return this.image_path;
            }

            public void setImage_bitmap(List<String> image_bitmap) {
                this.image_bitmap = image_bitmap;
            }

            public List<String> getImage_bitmap() {
                return this.image_bitmap;
            }
        }
    }
}

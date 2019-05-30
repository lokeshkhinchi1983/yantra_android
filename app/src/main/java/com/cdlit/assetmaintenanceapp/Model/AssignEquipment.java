package com.cdlit.assetmaintenanceapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rakesh on 27-07-2017.
 */

public class AssignEquipment extends RestResponse {


    private List<AssignEquipmentResponse> response;

    public void setResponse(List<AssignEquipmentResponse> response) {
        this.response = response;
    }

    public List<AssignEquipmentResponse> getResponse() {
        return this.response;
    }


    public class AssignEquipmentResponse implements Parcelable {

        private Integer equipment_type_id;

        private List<String> image_bitmap;

        private List<String> image_path;

        private String equipment_type_name;

        private String equipment_type_description;

        private List<AssignEquipments> equipments;

        private String assigned_to_user_id;

        protected AssignEquipmentResponse(Parcel in) {

            equipment_type_id = in.readInt();
            image_bitmap = in.createStringArrayList();
            image_path = in.createStringArrayList();
            equipment_type_name = in.readString();
            equipment_type_description = in.readString();
            assigned_to_user_id = in.readString();

        }

        public final Creator<AssignEquipmentResponse> CREATOR = new Creator<AssignEquipmentResponse>() {
            @Override
            public AssignEquipmentResponse createFromParcel(Parcel in) {
                return new AssignEquipmentResponse(in);
            }

            @Override
            public AssignEquipmentResponse[] newArray(int size) {
                return new AssignEquipmentResponse[size];
            }
        };

        public List<String> getImage_path() {
            return image_path;
        }

        public void setImage_path(List<String> image_path) {
            this.image_path = image_path;
        }

        public void setEquipment_type_id(Integer equipment_type_id) {
            this.equipment_type_id = equipment_type_id;
        }

        public Integer getEquipment_type_id() {
            return this.equipment_type_id;
        }

        public void setImage_bitmap(List<String> image_bitmap) {
            this.image_bitmap = image_bitmap;
        }

        public List<String> getImage_bitmap() {
            return this.image_bitmap;
        }

        public void setEquipment_type_name(String equipment_type_name) {
            this.equipment_type_name = equipment_type_name;
        }

        public String getEquipment_type_name() {
            return this.equipment_type_name;
        }

        public void setEquipment_type_description(String equipment_type_description) {
            this.equipment_type_description = equipment_type_description;
        }

        public String getEquipment_type_description() {
            return this.equipment_type_description;
        }

        public void setEquipments(List<AssignEquipments> equipments) {
            this.equipments = equipments;
        }

        public List<AssignEquipments> getEquipments() {
            return this.equipments;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(equipment_type_id);
            dest.writeStringList(image_bitmap);
            dest.writeStringList(image_path);
            dest.writeString(equipment_type_name);
            dest.writeString(equipment_type_description);
            dest.writeString(assigned_to_user_id);

        }

        public String getAssigned_to_user_id() {
            return assigned_to_user_id;
        }

        public void setAssigned_to_user_id(String assigned_to_user_id) {
            this.assigned_to_user_id = assigned_to_user_id;
        }
    }

    public class AssignEquipments implements Parcelable {

        private Integer equipment_id;

        private Integer equipment_type_id;

        //   private Integer location_id;

        // private String location_name;

        //  private String message;
        private String last_checked_user;
        private String description;

        private List<String> image_bitmap;
        private List<String> image_path;

        //   private String service_type;

        private String model_no;

        //   private String serial_no;

        //     private String annual_service_date;

        //    private String manufacturer_date;

        protected AssignEquipments(Parcel in) {
            //      location_name = in.readString();
            //      message = in.readString();
            description = in.readString();
            image_bitmap = in.createStringArrayList();
            image_path = in.createStringArrayList();

            //      service_type = in.readString();
            model_no = in.readString();
            //      serial_no = in.readString();
            //    annual_service_date = in.readString();
            //    manufacturer_date = in.readString();
        }

        public final Creator<AssignEquipments> CREATOR = new Creator<AssignEquipments>() {
            @Override
            public AssignEquipments createFromParcel(Parcel in) {
                return new AssignEquipments(in);
            }

            @Override
            public AssignEquipments[] newArray(int size) {
                return new AssignEquipments[size];
            }
        };

        public void setEquipment_id(Integer equipment_id) {
            this.equipment_id = equipment_id;
        }

        public Integer getEquipment_id() {
            return this.equipment_id;
        }

        public void setEquipment_type_id(Integer equipment_type_id) {
            this.equipment_type_id = equipment_type_id;
        }

        public Integer getEquipment_type_id() {
            return this.equipment_type_id;
        }

        /*   public void setLocation_id(Integer location_id) {
               this.location_id = location_id;
           }

           public Integer getLocation_id() {
               return this.location_id;
           }

           public void setLocation_name(String location_name) {
               this.location_name = location_name;
           }

           public String getLocation_name() {
               return this.location_name;
           }

           public void setMessage(String message) {
               this.message = message;
           }

           public String getMessage() {
               return this.message;
           }
   */
        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setImage_bitmap(List<String> image_bitmap) {
            this.image_bitmap = image_bitmap;
        }

        public List<String> getImage_bitmap() {
            return this.image_bitmap;
        }

        /*   public void setService_type(String service_type) {
               this.service_type = service_type;
           }

           public String getService_type() {
               return this.service_type;
           }
   */
        public void setModel_no(String model_no) {
            this.model_no = model_no;
        }

        public String getModel_no() {
            return this.model_no;
        }

        /*   public void setSerial_no(String serial_no) {
               this.serial_no = serial_no;
           }

           public String getSerial_no() {
               return this.serial_no;
           }

           public void setAnnual_service_date(String annual_service_date) {
               this.annual_service_date = annual_service_date;
           }

           public String getAnnual_service_date() {
               return this.annual_service_date;
           }

           public void setManufacturer_date(String manufacturer_date) {
               this.manufacturer_date = manufacturer_date;
           }

           public String getManufacturer_date() {
               return this.manufacturer_date;
           }
   */
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            //   dest.writeString(location_name);
            //    dest.writeString(message);
            dest.writeString(description);
            dest.writeStringList(image_bitmap);
            dest.writeStringList(image_path);
            //      dest.writeString(service_type);
            dest.writeString(model_no);
            //    dest.writeString(serial_no);
            //   dest.writeString(annual_service_date);
            //    dest.writeString(manufacturer_date);
        }

        public List<String> getImage_path() {
            return image_path;
        }

        public void setImage_path(List<String> image_path) {
            this.image_path = image_path;
        }

        public String getLast_checked_user() {
            return last_checked_user;
        }

        public void setLast_checked_user(String last_checked_user) {
            this.last_checked_user = last_checked_user;
        }
    }


}

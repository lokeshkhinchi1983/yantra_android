package com.cdlit.assetmaintenanceapp.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rakesh on 01-08-2017.
 */

public class AssignEquipmentReturn {

    private Date created_date;

    private Integer created_user_id;

    //  private ArrayList<Integer> equipment_type_id;

    private ArrayList<Integer> e_id;


    private Integer user_id;

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getCreated_date() {
        return this.created_date;
    }

    public void setCreated_user_id(Integer created_user_id) {
        this.created_user_id = created_user_id;
    }

    public Integer getCreated_user_id() {
        return this.created_user_id;
    }


    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return this.user_id;
    }


   /* public ArrayList<Integer> getEquipment_type_id() {
        return equipment_type_id;
    }

    public void setEquipment_type_id(ArrayList<Integer> equipment_type_id) {
        this.equipment_type_id = equipment_type_id;
    }*/

    public ArrayList<Integer> getE_id() {
        return e_id;
    }

    public void setE_id(ArrayList<Integer> e_id) {
        this.e_id = e_id;
    }
}

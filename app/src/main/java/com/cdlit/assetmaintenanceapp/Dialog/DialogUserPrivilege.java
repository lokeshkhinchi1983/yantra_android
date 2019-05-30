package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.Privilege;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeAll;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeAllResponse;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeResponse;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeReturn;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentManageUser;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogUserPrivilege extends DialogFragment {

    private static final String TAG = DialogUserPrivilege.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;
    private Spinner spinnerUserType;
    private String okString;
    private Handler handler;
    private ArrayList<String> listPrivilege;
    private ArrayList<PrivilegeResponse> listPrivilegeResponse;

    private CheckBox cbLocationR;
    private CheckBox cbLocationW;
    private CheckBox cbLocationD;
    //  private CheckBox cbCategoryR;
    //  private CheckBox cbCategoryW;
    //  private CheckBox cbCategoryD;
    private CheckBox cbEquipmentR;
    private CheckBox cbEquipmentW;
    private CheckBox cbEquipmentD;
    private CheckBox cbEquipmentTypeR;
    private CheckBox cbEquipmentTypeW;
    private CheckBox cbEquipmentTypeD;
    private CheckBox cbManageUserR;
    private CheckBox cbManageUserW;
    private CheckBox cbManageUserD;
    private CheckBox cbEquipmentchecklistR;
    private CheckBox cbEquipmentchecklistW;
    private CheckBox cbEquipmentchecklistD;
    private CheckBox cbAssigntouserR;
    private CheckBox cbAssigntouserW;
    private CheckBox cbAssigntouserD;
    private ArrayList<String> listAllPrivilege;
    private ArrayList<PrivilegeAllResponse> listAllPrivilegeResponse;
    private String userType;
    private HashMap<String, Integer> mapAllPrivilege;
    private TextView tvUserName;
    private CheckBox cbUserR;
    private CheckBox cbUserW;
    private CheckBox cbUserD;
    private Integer userTempId;
    private ArrayList<String> listUserPrivileges;
    private ArrayList<Integer> listPrivilegeId;
    private HashMap<String, Integer> mapUserPrivileges;
    private String title;
    private CheckBox cbRepairlogR;
    private CheckBox cbRepairlogW;
    private CheckBox cbRepairlogD;
    private CheckBox cbEquipmentTimelineR;
    private CheckBox cbEquipmentTimelineW;
    private CheckBox cbEquipmentTimelineD;
    private String user_Type;
    private CheckBox cbFaultlogR;
    private CheckBox cbFaultlogW;
    private CheckBox cbFaultlogD;
    // private int setSelection;
    // private String tempUserType;

    public static DialogUserPrivilege newInstance() {

        DialogUserPrivilege dialogAddEquipmentModel = new DialogUserPrivilege();
        return dialogAddEquipmentModel;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        GetAllPrivilege getAllPrivilege = new GetAllPrivilege();

        Bundle args = getArguments();
        title = args.getString("title");
        final String userName = args.getString("user_name");
        userTempId = args.getInt("user_temp_id");

        Log.e("user_temp_id", "" + userTempId);
        Log.e("userName", "" + userName);

        final List<String> listUserType = new ArrayList<String>();

        listUserType.add("Select Personnel Type");

        user_Type = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

        if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(user_Type)) {

            listUserType.add(getActivity().getResources().getString(R.string.Supervisor));
            listUserType.add(getActivity().getResources().getString(R.string.Operator));

        } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(user_Type)) {

            listUserType.add(getActivity().getResources().getString(R.string.Administrator));
            listUserType.add(getActivity().getResources().getString(R.string.Supervisor));
            listUserType.add(getActivity().getResources().getString(R.string.Operator));
        }


       /* listUserType.add(getActivity().getResources().getString(R.string.Administrator));
        listUserType.add(getActivity().getResources().getString(R.string.Supervisor));
        listUserType.add(getActivity().getResources().getString(R.string.Operator));*/

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_user_privilege_dialog_title))) {

            okString = getResources().getString(R.string.update_bt_string);

            listPrivilege = args.getStringArrayList("list_user_privileges");
            listPrivilegeId = args.getIntegerArrayList("list_user_privileges_id");

            mapUserPrivileges = new HashMap<String, Integer>();

            for (int i = 0; i < listPrivilege.size(); i++) {

                mapUserPrivileges.put(listPrivilege.get(i), listPrivilegeId.get(i));

            }

            //      tempUserType = args.getString("temp_user_type");
            userType = args.getString("temp_user_type");

            Log.e("userType", "" + userType);

            //     spinnerUserType.setSelection(1);

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    Log.e("setPrivileges", "setPrivileges");

                    setPrivileges();

                } else if (msg.what == 101) {

                    // GetPrivilege getPrivilege = new GetPrivilege(userType);

                }

                return false;
            }
        });

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //  alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        //     customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_user_privilege, null);

        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        spinnerUserType = (Spinner) view.findViewById(R.id.spinner_user_type);

        cbLocationR = (CheckBox) view.findViewById(R.id.cb_location_R);
        cbLocationW = (CheckBox) view.findViewById(R.id.cb_location_W);
        cbLocationD = (CheckBox) view.findViewById(R.id.cb_location_D);

        //  cbCategoryR = (CheckBox) view.findViewById(R.id.cb_category_R);
        //  cbCategoryW = (CheckBox) view.findViewById(R.id.cb_category_W);
        // cbCategoryD = (CheckBox) view.findViewById(R.id.cb_category_D);

        cbEquipmentR = (CheckBox) view.findViewById(R.id.cb_equipment_R);
        cbEquipmentW = (CheckBox) view.findViewById(R.id.cb_equipment_W);
        cbEquipmentD = (CheckBox) view.findViewById(R.id.cb_equipment_D);

        cbEquipmentTypeR = (CheckBox) view.findViewById(R.id.cb_equipment_type_R);
        cbEquipmentTypeW = (CheckBox) view.findViewById(R.id.cb_equipment_type_W);
        cbEquipmentTypeD = (CheckBox) view.findViewById(R.id.cb_equipment_type_D);

        cbManageUserR = (CheckBox) view.findViewById(R.id.cb_manage_user_R);
        cbManageUserW = (CheckBox) view.findViewById(R.id.cb_manage_user_W);
        cbManageUserD = (CheckBox) view.findViewById(R.id.cb_manage_user_D);

        cbEquipmentchecklistR = (CheckBox) view.findViewById(R.id.cb_equipment_checklist_R);
        cbEquipmentchecklistW = (CheckBox) view.findViewById(R.id.cb_equipment_checklist_W);
        cbEquipmentchecklistD = (CheckBox) view.findViewById(R.id.cb_equipment_checklist_D);

        cbAssigntouserR = (CheckBox) view.findViewById(R.id.cb_assign_to_user_R);
        cbAssigntouserW = (CheckBox) view.findViewById(R.id.cb_assign_to_user_W);
        cbAssigntouserD = (CheckBox) view.findViewById(R.id.cb_assign_to_user_D);

        cbUserR = (CheckBox) view.findViewById(R.id.cb_user_R);
        cbUserW = (CheckBox) view.findViewById(R.id.cb_user_W);
        cbUserD = (CheckBox) view.findViewById(R.id.cb_user_D);

        cbRepairlogR = (CheckBox) view.findViewById(R.id.cb_repairlog_R);
        cbRepairlogW = (CheckBox) view.findViewById(R.id.cb_repairlog_W);
        cbRepairlogD = (CheckBox) view.findViewById(R.id.cb_repairlog_D);

        cbEquipmentTimelineR = (CheckBox) view.findViewById(R.id.cb_equipment_timeline_R);
        cbEquipmentTimelineW = (CheckBox) view.findViewById(R.id.cb_equipment_timeline_W);
        cbEquipmentTimelineD = (CheckBox) view.findViewById(R.id.cb_equipment_timeline_D);


        cbFaultlogR = (CheckBox) view.findViewById(R.id.cb_faultlog_R);
        cbFaultlogW = (CheckBox) view.findViewById(R.id.cb_faultlog_W);
        cbFaultlogD = (CheckBox) view.findViewById(R.id.cb_faultlog_D);

        tvUserName.setText(userName);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listUserType);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUserType.setAdapter(dataAdapter);

        listAllPrivilege = new ArrayList<String>();

        mapAllPrivilege = new HashMap<String, Integer>();

        listAllPrivilegeResponse = new ArrayList<PrivilegeAllResponse>();

        //   GetAllPrivilege getAllPrivilege = new GetAllPrivilege();


        if (title.equalsIgnoreCase(getResources().getString(R.string.update_user_privilege_dialog_title))) {

            int pos = 0;

            for (int i = 0; i < listUserType.size(); i++) {

                if (userType.equalsIgnoreCase(listUserType.get(i))) {

                    pos = i;

                }

            }

            Log.e("pos", "" + pos);
            //   setSelection = 0;

            //   Log.e("setSelection",""+setSelection);

            spinnerUserType.setSelection(pos);

            //   spinnerUserType.setSelection(0);

            //    setPrivileges();

            //     setSelection++;

        } else {

            //   setSelection++;

            //    Log.e("setSelection",""+setSelection);

            spinnerUserType.setSelection(0);

        }


        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {


                } else {

                    // Log.e("setSelection",""+setSelection);

                    //if (setSelection != 0) {

                    String userType = listUserType.get(position);

                    GetPrivilege getPrivilege = new GetPrivilege(userType);

                    //  }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_user_privilege_dialog_title))) {

            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

        }


        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_user_privilege_dialog_title))) {

                    if (spinnerUserType.getSelectedItem().toString().equalsIgnoreCase("Select Personnel Type")) {

                        Utilities.showSnackbar(v, "Select personnel type");

                    } else {

                        List<PrivilegeReturn> listPrivilegeReturn = getPrivilegeReturn();

                        if (listPrivilegeReturn.size() == 0) {

                            Utilities.showSnackbar(v, "Please Select Any Privilege");

                        } else {

                            ((FragmentManageUser) getTargetFragment()).addUserPrivilegePositiveClick(listPrivilegeReturn);

                        }
                    }

                } else {

                    if (userType.equalsIgnoreCase(spinnerUserType.getSelectedItem().toString())) {

                        Utilities.showSnackbar(v, getResources().getString(R.string.not_any_change));

                    } else if (spinnerUserType.getSelectedItem().toString().equalsIgnoreCase("Select Personnel Type")) {

                        Utilities.showSnackbar(v, "Select personnel type");

                    } else {

                        List<PrivilegeReturn> listPrivilegeReturn = getPrivilegeReturn();

                        if (listPrivilegeReturn.size() == 0) {

                            Utilities.showSnackbar(v, "Please , select any privilege");

                        } else {

                            ((FragmentManageUser) getTargetFragment()).addUserPrivilegePositiveClick(listPrivilegeReturn);

                        }

                    }

                }

            }
        });

        return dialog;
    }

    private void setPrivileges() {

        //location
        if (listPrivilege.contains(Utilities.LOCATION_MASTER_READ))
            cbLocationR.setChecked(true);
        else
            cbLocationR.setChecked(false);

        if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE))
            cbLocationW.setChecked(true);
        else
            cbLocationW.setChecked(false);

        if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE))
            cbLocationD.setChecked(true);
        else
            cbLocationD.setChecked(false);


       /* //category
        if (listPrivilege.contains(Utilities.CATEGORY_MASTER_READ))
            cbCategoryR.setChecked(true);
        else
            cbCategoryR.setChecked(false);

        if (listPrivilege.contains(Utilities.CATEGORY_MASTER_WRITE))
            cbCategoryW.setChecked(true);
        else
            cbCategoryW.setChecked(false);

        if (listPrivilege.contains(Utilities.CATEGORY_MASTER_DELETE))
            cbCategoryD.setChecked(true);
        else
            cbCategoryD.setChecked(false);*/


        //equipment

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_READ))
            cbEquipmentR.setChecked(true);
        else
            cbEquipmentR.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE))
            cbEquipmentW.setChecked(true);
        else
            cbEquipmentW.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE))
            cbEquipmentD.setChecked(true);
        else
            cbEquipmentD.setChecked(false);


        //equipment type

        if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_READ))
            cbEquipmentTypeR.setChecked(true);
        else
            cbEquipmentTypeR.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE))
            cbEquipmentTypeW.setChecked(true);
        else
            cbEquipmentTypeW.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_DELETE))
            cbEquipmentTypeD.setChecked(true);
        else
            cbEquipmentTypeD.setChecked(false);


        //manage user

        if (listPrivilege.contains(Utilities.USER_MASTER_READ))
            cbManageUserR.setChecked(true);
        else
            cbManageUserR.setChecked(false);

        if (listPrivilege.contains(Utilities.USER_MASTER_WRITE))
            cbManageUserW.setChecked(true);
        else
            cbManageUserW.setChecked(false);

        if (listPrivilege.contains(Utilities.USER_MASTER_DELETE))
            cbManageUserD.setChecked(true);
        else
            cbManageUserD.setChecked(false);


        //equipment checklist

        if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_READ))
            cbEquipmentchecklistR.setChecked(true);
        else
            cbEquipmentchecklistR.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE))
            cbEquipmentchecklistW.setChecked(true);
        else
            cbEquipmentchecklistW.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE))
            cbEquipmentchecklistD.setChecked(true);
        else
            cbEquipmentchecklistD.setChecked(false);


        //assign to user

        if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_READ))
            cbAssigntouserR.setChecked(true);
        else
            cbAssigntouserR.setChecked(false);

        if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE))
            cbAssigntouserW.setChecked(true);
        else
            cbAssigntouserW.setChecked(false);

        if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE))
            cbAssigntouserD.setChecked(true);
        else
            cbAssigntouserD.setChecked(false);

        // user

        if (listPrivilege.contains(Utilities.MYCHECKLIST_READ))
            cbUserR.setChecked(true);
        else
            cbUserR.setChecked(false);

        if (listPrivilege.contains(Utilities.MYCHECKLIST_WRITE))
            cbUserW.setChecked(true);
        else
            cbUserW.setChecked(false);

        if (listPrivilege.contains(Utilities.MYCHECKLIST_DELETE))
            cbUserD.setChecked(true);
        else
            cbUserD.setChecked(false);


        //Repair log
        if (listPrivilege.contains(Utilities.REPAIRLOG_READ))
            cbRepairlogR.setChecked(true);
        else
            cbRepairlogR.setChecked(false);

        if (listPrivilege.contains(Utilities.REPAIRLOG_WRITE))
            cbRepairlogW.setChecked(true);
        else
            cbRepairlogW.setChecked(false);

        if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE))
            cbRepairlogD.setChecked(true);
        else
            cbRepairlogD.setChecked(false);


        //equipment

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_READ))
            cbEquipmentTimelineR.setChecked(true);
        else
            cbEquipmentTimelineR.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE))
            cbEquipmentTimelineW.setChecked(true);
        else
            cbEquipmentTimelineW.setChecked(false);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE))
            cbEquipmentTimelineD.setChecked(true);
        else
            cbEquipmentTimelineD.setChecked(false);


        //Fault log
        if (listPrivilege.contains(Utilities.FAULTLOG_READ))
            cbFaultlogR.setChecked(true);
        else
            cbFaultlogR.setChecked(false);

        if (listPrivilege.contains(Utilities.FAULTLOG_WRITE))
            cbFaultlogW.setChecked(true);
        else
            cbFaultlogW.setChecked(false);

        if (listPrivilege.contains(Utilities.FAULTLOG_DELETE))
            cbFaultlogD.setChecked(true);
        else
            cbFaultlogD.setChecked(false);


        // set disable checkboxes...

        cbLocationR.setClickable(false);
        cbLocationW.setClickable(false);
        cbLocationD.setClickable(false);

      /*  cbCategoryR.setClickable(false);
        cbCategoryW.setClickable(false);
        cbCategoryD.setClickable(false);*/

        cbEquipmentR.setClickable(false);
        cbEquipmentW.setClickable(false);
        cbEquipmentD.setClickable(false);

        cbEquipmentTypeR.setClickable(false);
        cbEquipmentTypeW.setClickable(false);
        cbEquipmentTypeD.setClickable(false);

        cbManageUserR.setClickable(false);
        cbManageUserW.setClickable(false);
        cbManageUserD.setClickable(false);

        cbEquipmentchecklistR.setClickable(false);
        cbEquipmentchecklistW.setClickable(false);
        cbEquipmentchecklistD.setClickable(false);

        cbAssigntouserR.setClickable(false);
        cbAssigntouserW.setClickable(false);
        cbAssigntouserD.setClickable(false);

        cbUserR.setClickable(false);
        cbUserW.setClickable(false);
        cbUserD.setClickable(false);

        cbRepairlogR.setClickable(false);
        cbRepairlogW.setClickable(false);
        cbRepairlogD.setClickable(false);

        cbEquipmentTimelineR.setClickable(false);
        cbEquipmentTimelineW.setClickable(false);
        cbEquipmentTimelineD.setClickable(false);

        cbFaultlogR.setClickable(false);
        cbFaultlogW.setClickable(false);
        cbFaultlogD.setClickable(false);

    }

    private List<PrivilegeReturn> getPrivilegeReturn() {

        String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

        Set<String> keys = mapAllPrivilege.keySet();

        /*for (String key : keys) {

            Integer value = mapAllPrivilege.get(key);

            Log.e("key-value", "" + key + " - " + value);
        }*/

        List<PrivilegeReturn> listPrivilegeReturn = new ArrayList<PrivilegeReturn>();

        PrivilegeReturn privilegeReturn0 = new PrivilegeReturn();
        privilegeReturn0.setAction(null);
        privilegeReturn0.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn0.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn0.setId(0);
        privilegeReturn0.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn0.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn0.setTemp_user_id(userTempId);
        privilegeReturn0.setUser(null);
        privilegeReturn0.setTemp_action_id(mapAllPrivilege.get(Utilities.LOCATION_MASTER_READ));
        privilegeReturn0.setState(cbLocationR.isChecked() == true ? 1 : 0);
        privilegeReturn0.setTemp_user_type(spinnerUserType.getSelectedItem().toString());


        if (cbLocationR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn0);
        }

        PrivilegeReturn privilegeReturn1 = new PrivilegeReturn();
        privilegeReturn1.setAction(null);
        privilegeReturn1.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn1.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn1.setId(0);
        privilegeReturn1.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn1.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn1.setTemp_user_id(userTempId);
        privilegeReturn1.setUser(null);
        privilegeReturn1.setTemp_action_id(mapAllPrivilege.get(Utilities.LOCATION_MASTER_WRITE));
        privilegeReturn1.setState(cbLocationW.isChecked() == true ? 1 : 0);
        privilegeReturn1.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbLocationW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn1);
        }


        PrivilegeReturn privilegeReturn2 = new PrivilegeReturn();
        privilegeReturn2.setAction(null);
        privilegeReturn2.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn2.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn2.setId(0);
        privilegeReturn2.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn2.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn2.setTemp_user_id(userTempId);
        privilegeReturn2.setUser(null);
        privilegeReturn2.setTemp_action_id(mapAllPrivilege.get(Utilities.LOCATION_MASTER_DELETE));
        privilegeReturn2.setState(cbLocationD.isChecked() == true ? 1 : 0);
        privilegeReturn2.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbLocationD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn2);
        }

/*
        PrivilegeReturn privilegeReturn3 = new PrivilegeReturn();
        privilegeReturn3.setAction(null);
        privilegeReturn3.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn3.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn3.setId(0);
        privilegeReturn3.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn3.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn3.setTemp_user_id(userTempId);
        privilegeReturn3.setUser(null);
        privilegeReturn3.setTemp_action_id(mapAllPrivilege.get(Utilities.CATEGORY_MASTER_READ));
        privilegeReturn3.setState(cbCategoryR.isChecked() == true ? 1 : 0);
        privilegeReturn3.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbCategoryR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn3);
        }


        PrivilegeReturn privilegeReturn4 = new PrivilegeReturn();
        privilegeReturn4.setAction(null);
        privilegeReturn4.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn4.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn4.setId(0);
        privilegeReturn4.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn4.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn4.setTemp_user_id(userTempId);
        privilegeReturn4.setUser(null);
        privilegeReturn4.setTemp_action_id(mapAllPrivilege.get(Utilities.CATEGORY_MASTER_WRITE));
        privilegeReturn4.setState(cbCategoryW.isChecked() == true ? 1 : 0);
        privilegeReturn4.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbCategoryW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn4);
        }


        PrivilegeReturn privilegeReturn5 = new PrivilegeReturn();
        privilegeReturn5.setAction(null);
        privilegeReturn5.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn5.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn5.setId(0);
        privilegeReturn5.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn5.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn5.setTemp_user_id(userTempId);
        privilegeReturn5.setUser(null);
        privilegeReturn5.setTemp_action_id(mapAllPrivilege.get(Utilities.CATEGORY_MASTER_DELETE));
        privilegeReturn5.setState(cbCategoryD.isChecked() == true ? 1 : 0);
        privilegeReturn5.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbCategoryD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn5);
        }*/


        PrivilegeReturn privilegeReturn6 = new PrivilegeReturn();
        privilegeReturn6.setAction(null);
        privilegeReturn6.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn6.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn6.setId(0);
        privilegeReturn6.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn6.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn6.setTemp_user_id(userTempId);
        privilegeReturn6.setUser(null);
        privilegeReturn6.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_MASTER_READ));
        privilegeReturn6.setState(cbEquipmentR.isChecked() == true ? 1 : 0);
        privilegeReturn6.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn6);
        }


        PrivilegeReturn privilegeReturn7 = new PrivilegeReturn();
        privilegeReturn7.setAction(null);
        privilegeReturn7.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn7.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn7.setId(0);
        privilegeReturn7.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn7.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn7.setTemp_user_id(userTempId);
        privilegeReturn7.setUser(null);
        privilegeReturn7.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_MASTER_WRITE));
        privilegeReturn7.setState(cbEquipmentW.isChecked() == true ? 1 : 0);
        privilegeReturn7.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn7);
        }


        PrivilegeReturn privilegeReturn8 = new PrivilegeReturn();
        privilegeReturn8.setAction(null);
        privilegeReturn8.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn8.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn8.setId(0);
        privilegeReturn8.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn8.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn8.setTemp_user_id(userTempId);
        privilegeReturn8.setUser(null);
        privilegeReturn8.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_MASTER_DELETE));
        privilegeReturn8.setState(cbEquipmentD.isChecked() == true ? 1 : 0);
        privilegeReturn8.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn8);
        }


        PrivilegeReturn privilegeReturn9 = new PrivilegeReturn();
        privilegeReturn9.setAction(null);
        privilegeReturn9.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn9.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn9.setId(0);
        privilegeReturn9.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn9.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn9.setTemp_user_id(userTempId);
        privilegeReturn9.setUser(null);
        privilegeReturn9.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TYPE_MASTER_READ));
        privilegeReturn9.setState(cbEquipmentTypeR.isChecked() == true ? 1 : 0);
        privilegeReturn9.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTypeR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn9);
        }


        PrivilegeReturn privilegeReturn10 = new PrivilegeReturn();
        privilegeReturn10.setAction(null);
        privilegeReturn10.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn10.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn10.setId(0);
        privilegeReturn10.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn10.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn10.setTemp_user_id(userTempId);
        privilegeReturn10.setUser(null);
        privilegeReturn10.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TYPE_MASTER_WRITE));
        privilegeReturn10.setState(cbEquipmentTypeW.isChecked() == true ? 1 : 0);
        privilegeReturn10.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTypeW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn10);
        }


        PrivilegeReturn privilegeReturn11 = new PrivilegeReturn();
        privilegeReturn11.setAction(null);
        privilegeReturn11.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn11.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn11.setId(0);
        privilegeReturn11.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn11.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn11.setTemp_user_id(userTempId);
        privilegeReturn11.setUser(null);
        privilegeReturn11.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TYPE_MASTER_DELETE));
        privilegeReturn11.setState(cbEquipmentTypeD.isChecked() == true ? 1 : 0);
        privilegeReturn11.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTypeD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn11);
        }


        PrivilegeReturn privilegeReturn12 = new PrivilegeReturn();
        privilegeReturn12.setAction(null);
        privilegeReturn12.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn12.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn12.setId(0);
        privilegeReturn12.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn12.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn12.setTemp_user_id(userTempId);
        privilegeReturn12.setUser(null);
        privilegeReturn12.setTemp_action_id(mapAllPrivilege.get(Utilities.USER_MASTER_READ));
        privilegeReturn12.setState(cbManageUserR.isChecked() == true ? 1 : 0);
        privilegeReturn12.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbManageUserR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn12);
        }


        PrivilegeReturn privilegeReturn13 = new PrivilegeReturn();
        privilegeReturn13.setAction(null);
        privilegeReturn13.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn13.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn13.setId(0);
        privilegeReturn13.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn13.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn13.setTemp_user_id(userTempId);
        privilegeReturn13.setUser(null);
        privilegeReturn13.setTemp_action_id(mapAllPrivilege.get(Utilities.USER_MASTER_WRITE));
        privilegeReturn13.setState(cbManageUserW.isChecked() == true ? 1 : 0);
        privilegeReturn13.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbManageUserW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn13);
        }


        PrivilegeReturn privilegeReturn14 = new PrivilegeReturn();
        privilegeReturn14.setAction(null);
        privilegeReturn14.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn14.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn14.setId(0);
        privilegeReturn14.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn14.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn14.setTemp_user_id(userTempId);
        privilegeReturn14.setUser(null);
        privilegeReturn14.setTemp_action_id(mapAllPrivilege.get(Utilities.USER_MASTER_DELETE));
        privilegeReturn14.setState(cbManageUserD.isChecked() == true ? 1 : 0);
        privilegeReturn14.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbManageUserD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn14);
        }


        PrivilegeReturn privilegeReturn15 = new PrivilegeReturn();
        privilegeReturn15.setAction(null);
        privilegeReturn15.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn15.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn15.setId(0);
        privilegeReturn15.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn15.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn15.setTemp_user_id(userTempId);
        privilegeReturn15.setUser(null);
        privilegeReturn15.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_CHECKLIST_MASTER_READ));
        privilegeReturn15.setState(cbEquipmentchecklistR.isChecked() == true ? 1 : 0);
        privilegeReturn15.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentchecklistR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn15);
        }


        PrivilegeReturn privilegeReturn16 = new PrivilegeReturn();
        privilegeReturn16.setAction(null);
        privilegeReturn16.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn16.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn16.setId(0);
        privilegeReturn16.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn16.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn16.setTemp_user_id(userTempId);
        privilegeReturn16.setUser(null);
        privilegeReturn16.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE));
        privilegeReturn16.setState(cbEquipmentchecklistW.isChecked() == true ? 1 : 0);
        privilegeReturn16.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentchecklistW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn16);
        }


        PrivilegeReturn privilegeReturn17 = new PrivilegeReturn();
        privilegeReturn17.setAction(null);
        privilegeReturn17.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn17.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn17.setId(0);
        privilegeReturn17.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn17.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn17.setTemp_user_id(userTempId);
        privilegeReturn17.setUser(null);
        privilegeReturn17.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE));
        privilegeReturn17.setState(cbEquipmentchecklistD.isChecked() == true ? 1 : 0);
        privilegeReturn17.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentchecklistD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn17);
        }


        PrivilegeReturn privilegeReturn18 = new PrivilegeReturn();
        privilegeReturn18.setAction(null);
        privilegeReturn18.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn18.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn18.setId(0);
        privilegeReturn18.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn18.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn18.setTemp_user_id(userTempId);
        privilegeReturn18.setUser(null);
        privilegeReturn18.setTemp_action_id(mapAllPrivilege.get(Utilities.ASSIGN_TO_USER_READ));
        privilegeReturn18.setState(cbAssigntouserR.isChecked() == true ? 1 : 0);
        privilegeReturn18.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbAssigntouserR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn18);
        }


        PrivilegeReturn privilegeReturn19 = new PrivilegeReturn();
        privilegeReturn19.setAction(null);
        privilegeReturn19.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn19.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn19.setId(0);
        privilegeReturn19.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn19.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn19.setTemp_user_id(userTempId);
        privilegeReturn19.setUser(null);
        privilegeReturn19.setTemp_action_id(mapAllPrivilege.get(Utilities.ASSIGN_TO_USER_WRITE));
        privilegeReturn19.setState(cbAssigntouserW.isChecked() == true ? 1 : 0);
        privilegeReturn19.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbAssigntouserW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn19);
        }


        PrivilegeReturn privilegeReturn20 = new PrivilegeReturn();
        privilegeReturn20.setAction(null);
        privilegeReturn20.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn20.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn20.setId(0);
        privilegeReturn20.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn20.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn20.setTemp_user_id(userTempId);
        privilegeReturn20.setUser(null);
        privilegeReturn20.setTemp_action_id(mapAllPrivilege.get(Utilities.ASSIGN_TO_USER_DELETE));
        privilegeReturn20.setState(cbAssigntouserD.isChecked() == true ? 1 : 0);
        privilegeReturn20.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbAssigntouserD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn20);
        }


        PrivilegeReturn privilegeReturn21 = new PrivilegeReturn();
        privilegeReturn21.setAction(null);
        privilegeReturn21.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn21.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn21.setId(0);
        privilegeReturn21.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn21.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn21.setTemp_user_id(userTempId);
        privilegeReturn21.setUser(null);
        privilegeReturn21.setTemp_action_id(mapAllPrivilege.get(Utilities.MYCHECKLIST_READ));
        privilegeReturn21.setState(cbUserR.isChecked() == true ? 1 : 0);
        privilegeReturn21.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbUserR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn21);
        }

        PrivilegeReturn privilegeReturn22 = new PrivilegeReturn();
        privilegeReturn22.setAction(null);
        privilegeReturn22.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn22.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn22.setId(0);
        privilegeReturn22.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn22.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn22.setTemp_user_id(userTempId);
        privilegeReturn22.setUser(null);
        privilegeReturn22.setTemp_action_id(mapAllPrivilege.get(Utilities.MYCHECKLIST_WRITE));
        privilegeReturn22.setState(cbUserW.isChecked() == true ? 1 : 0);
        privilegeReturn22.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbUserW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn22);
        }

        PrivilegeReturn privilegeReturn23 = new PrivilegeReturn();
        privilegeReturn23.setAction(null);
        privilegeReturn23.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn23.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn23.setId(0);
        privilegeReturn23.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn23.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn23.setTemp_user_id(userTempId);
        privilegeReturn23.setUser(null);
        privilegeReturn23.setTemp_action_id(mapAllPrivilege.get(Utilities.MYCHECKLIST_DELETE));
        privilegeReturn23.setState(cbUserD.isChecked() == true ? 1 : 0);
        privilegeReturn23.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbUserD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn23);
        }


        PrivilegeReturn privilegeReturn24 = new PrivilegeReturn();
        privilegeReturn24.setAction(null);
        privilegeReturn24.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn24.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn24.setId(0);
        privilegeReturn24.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn24.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn24.setTemp_user_id(userTempId);
        privilegeReturn24.setUser(null);
        privilegeReturn24.setTemp_action_id(mapAllPrivilege.get(Utilities.REPAIRLOG_READ));
        privilegeReturn24.setState(cbRepairlogR.isChecked() == true ? 1 : 0);
        privilegeReturn24.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbRepairlogR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn24);
        }

        PrivilegeReturn privilegeReturn25 = new PrivilegeReturn();
        privilegeReturn25.setAction(null);
        privilegeReturn25.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn25.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn25.setId(0);
        privilegeReturn25.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn25.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn25.setTemp_user_id(userTempId);
        privilegeReturn25.setUser(null);
        privilegeReturn25.setTemp_action_id(mapAllPrivilege.get(Utilities.REPAIRLOG_WRITE));
        privilegeReturn25.setState(cbRepairlogW.isChecked() == true ? 1 : 0);
        privilegeReturn25.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbRepairlogW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn25);
        }

        PrivilegeReturn privilegeReturn26 = new PrivilegeReturn();
        privilegeReturn26.setAction(null);
        privilegeReturn26.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn26.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn26.setId(0);
        privilegeReturn26.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn26.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn26.setTemp_user_id(userTempId);
        privilegeReturn26.setUser(null);
        privilegeReturn26.setTemp_action_id(mapAllPrivilege.get(Utilities.REPAIRLOG_DELETE));
        privilegeReturn26.setState(cbRepairlogD.isChecked() == true ? 1 : 0);
        privilegeReturn26.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbRepairlogD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn26);
        }


        PrivilegeReturn privilegeReturn27 = new PrivilegeReturn();
        privilegeReturn27.setAction(null);
        privilegeReturn27.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn27.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn27.setId(0);
        privilegeReturn27.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn27.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn27.setTemp_user_id(userTempId);
        privilegeReturn27.setUser(null);
        privilegeReturn27.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TIMELINE_READ));
        privilegeReturn27.setState(cbEquipmentTimelineR.isChecked() == true ? 1 : 0);
        privilegeReturn27.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTimelineR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn27);
        }


        PrivilegeReturn privilegeReturn28 = new PrivilegeReturn();
        privilegeReturn28.setAction(null);
        privilegeReturn28.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn28.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn28.setId(0);
        privilegeReturn28.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn28.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn28.setTemp_user_id(userTempId);
        privilegeReturn28.setUser(null);
        privilegeReturn28.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TIMELINE_WRITE));
        privilegeReturn28.setState(cbEquipmentTimelineW.isChecked() == true ? 1 : 0);
        privilegeReturn28.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTimelineW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn28);
        }


        PrivilegeReturn privilegeReturn29 = new PrivilegeReturn();
        privilegeReturn29.setAction(null);
        privilegeReturn29.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn29.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn29.setId(0);
        privilegeReturn29.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn29.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn29.setTemp_user_id(userTempId);
        privilegeReturn29.setUser(null);
        privilegeReturn29.setTemp_action_id(mapAllPrivilege.get(Utilities.EQUIPMENT_TIMELINE_DELETE));
        privilegeReturn29.setState(cbEquipmentTimelineD.isChecked() == true ? 1 : 0);
        privilegeReturn29.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbEquipmentTimelineD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn29);
        }


        PrivilegeReturn privilegeReturn30 = new PrivilegeReturn();
        privilegeReturn30.setAction(null);
        privilegeReturn30.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn30.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn30.setId(0);
        privilegeReturn30.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn30.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn30.setTemp_user_id(userTempId);
        privilegeReturn30.setUser(null);
        privilegeReturn30.setTemp_action_id(mapAllPrivilege.get(Utilities.FAULTLOG_READ));
        privilegeReturn30.setState(cbFaultlogR.isChecked() == true ? 1 : 0);
        privilegeReturn30.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbFaultlogR.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn30);
        }


        PrivilegeReturn privilegeReturn31 = new PrivilegeReturn();
        privilegeReturn31.setAction(null);
        privilegeReturn31.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn31.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn31.setId(0);
        privilegeReturn31.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn31.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn31.setTemp_user_id(userTempId);
        privilegeReturn31.setUser(null);
        privilegeReturn31.setTemp_action_id(mapAllPrivilege.get(Utilities.FAULTLOG_WRITE));
        privilegeReturn31.setState(cbFaultlogW.isChecked() == true ? 1 : 0);
        privilegeReturn31.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbFaultlogW.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn31);
        }


        PrivilegeReturn privilegeReturn32 = new PrivilegeReturn();
        privilegeReturn32.setAction(null);
        privilegeReturn32.setCreatedDate(Utilities.currentDateTime());
        privilegeReturn32.setCreatedUser(Integer.parseInt(userid));
        privilegeReturn32.setId(0);
        privilegeReturn32.setModifiedDate(Utilities.currentDateTime());
        privilegeReturn32.setModifiedUser(Integer.parseInt(userid));
        privilegeReturn32.setTemp_user_id(userTempId);
        privilegeReturn32.setUser(null);
        privilegeReturn32.setTemp_action_id(mapAllPrivilege.get(Utilities.FAULTLOG_DELETE));
        privilegeReturn32.setState(cbFaultlogD.isChecked() == true ? 1 : 0);
        privilegeReturn32.setTemp_user_type(spinnerUserType.getSelectedItem().toString());
        if (cbFaultlogD.isChecked()) {
            listPrivilegeReturn.add(privilegeReturn32);
        }

        return listPrivilegeReturn;

    }

    private class GetAllPrivilege implements LoaderManager.LoaderCallbacks<PrivilegeAll> {

        private static final int LOADER_ALL_PRIVILEGE = 1;
        private ProgressDialog progressDialog;

        public GetAllPrivilege() {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "action";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL_PRIVILEGE, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<PrivilegeAll> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {
                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<PrivilegeAll>(getActivity(), httpWrapper, PrivilegeAll.class);

                }

            } else {

                progressDialog.dismiss();
                // Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //  .show();
                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<PrivilegeAll> loader, PrivilegeAll data) {

            progressDialog.dismiss();

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //     progressDialog.dismiss();

                // listAllPrivilege = new ArrayList<String>();
                listAllPrivilege.clear();

                // mapAllPrivilege = new HashMap<String, Integer>();
                mapAllPrivilege.clear();

                // listAllPrivilegeResponse = new ArrayList<PrivilegeAllResponse>();
                listAllPrivilegeResponse.clear();

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listAllPrivilegeResponse.add(data.getResponse().get(i));

                }

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listAllPrivilege.add(data.getResponse().get(i).getAction());

                    Log.e("key-value", "" + data.getResponse().get(i).getAction() + "-" + data.getResponse().get(i).getId());

                    mapAllPrivilege.put(data.getResponse().get(i).getAction(), data.getResponse().get(i).getId());

                }

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(getView(), "" + data.getMessage());
            }

            //   progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<PrivilegeAll> loader) {

        }
    }

    private class GetPrivilege implements LoaderManager.LoaderCallbacks<Privilege> {

        private final String type;
        private static final int LOADER_USER_PRIVILEGE = 1;
        private ProgressDialog progressDialog;

        public GetPrivilege(String type) {

            this.type = type;

            if (type.equalsIgnoreCase("super user")) {
                type = "superuser";
            }

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "action/" + URLEncoder.encode(type.toString(), "UTF-8").replace("+", "%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_USER_PRIVILEGE, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }


        @Override
        public Loader<Privilege> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {
                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<Privilege>(getActivity(), httpWrapper, Privilege.class);

                }

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Privilege> loader, Privilege data) {
            progressDialog.dismiss();
            if (null != data && data.getStatus().equalsIgnoreCase("success")) {


                //     progressDialog.dismiss();

                listPrivilege = new ArrayList<String>();

                //   listPrivilege.clear();

                listPrivilegeResponse = new ArrayList<PrivilegeResponse>();

                //    listPrivilegeResponse.clear();

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listPrivilegeResponse.add(data.getResponse().get(i));

                }

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listPrivilege.add(data.getResponse().get(i).getAction());

                }


                //  progressDialog.dismiss();

                handler.sendEmptyMessage(100);


            } else {

                progressDialog.dismiss();

                //   Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(getView(), "Please enter personnel name");

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Privilege> loader) {


        }
    }


}

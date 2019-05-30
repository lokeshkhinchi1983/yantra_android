
package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.RepairLogEquipmentType;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rakesh on 13062017.
 */

public class DialogAddEquipmentModelRequired extends DialogFragment {

    private static final String TAG = DialogAddEquipmentModelRequired.class.getSimpleName();

    private AlertDialog.Builder alertDialogBuilder;
    private Spinner spinnerLocation;
    private Spinner spinnerEquipmentType;
    private EditText etAddEquipmentModel;
    private ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse> listEquipmentType;
    private ArrayList<LocationResponse> listLocation;
    private ArrayList<String> listLocationName;
    private ArrayList<String> listEquipmentTypeName;
    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> equipmentTypeAdapter;

    private EquipmentResponse equipmentResponse;
    private String locationName;
    private String equipmentTypeName;
    private int locationPos;
    private int equipmentTypePos;
    private String okString;
    // private EditText etDueServiceInterval;
    //   private Spinner spinnerInspectionDuration;
    private ArrayList<String> listInspectionDuration;
    // private ArrayAdapter<String> indpectionDurationAdapter;
    private String inspectionDuration;
    private int inspectionDurationPos;
    private TextView tvEmailId;
    private DialogAddEmail dialogAddEmail;
    private ArrayList<String> listEmail;
    private ArrayList<String> listEmail_1;
    // private TextView tvDueServiceDate;
    private String dueServiceDate;
    private EquipmentResponse equipmentClone;
    //  private TextView tvDueServiceDateTitle;

    // private TextView tvWeekDay;
//    private LinearLayout llWeekDay;
    //  private Spinner spinnerWeekDay;
    //  private TextView tvMonthDay;
    private TextView tvOtherDay;
    //  private EditText etMonthDay;
    private ArrayList<String> listDayFreq;
    private ArrayAdapter<String> checkDayAdapter;

    public DialogAddEquipmentModelRequired() {

    }

    public static DialogAddEquipmentModelRequired newInstance() {

        DialogAddEquipmentModelRequired dialogAddEquipmentModelRequired = new DialogAddEquipmentModelRequired();
        return dialogAddEquipmentModelRequired;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        listLocation = args.getParcelableArrayList("list_location");
        listEquipmentType = args.getParcelableArrayList("list_equipment_type");

        equipmentClone = args.getParcelable("equipment_clone");

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {


            equipmentResponse = (EquipmentResponse) args.getParcelable("equipment_response");
            locationName = equipmentResponse.getLocation().getLocationName();
            equipmentTypeName = equipmentResponse.getEquipmentType().getEquipmentTypeName();
            inspectionDuration = equipmentResponse.getServiceFrequency();
            okString = getResources().getString(R.string.update_bt_string);
            listEmail = (ArrayList<String>) equipmentResponse.getEmailId();

            listEmail_1 = args.getStringArrayList("email_list");

            Log.e("listEmail_1", "" + listEmail_1);

        } else {

            okString = getResources().getString(R.string.add_bt_string);

            listEmail = new ArrayList<String>();

        }

        listLocationName = new ArrayList<String>();

        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

        if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType) && equipmentClone == null) {
            listLocationName.add("Select Location");
        }

        for (LocationResponse locationResponse : listLocation) {
            listLocationName.add(locationResponse.getLocationName());
        }

        listEquipmentTypeName = new ArrayList<String>();

        if (equipmentClone == null) {
            listEquipmentTypeName.add("Select Asset Category");
        }

        for (RepairLogEquipmentType.RepairLogEquipmentTypeResponse equipmentTypeResponse : listEquipmentType) {
            listEquipmentTypeName.add(equipmentTypeResponse.getName());
        }

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        String title1 = null;

        if (equipmentClone != null) {

            if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {


            } else {

                title1 = "Clone asset";

            }

        } else {

            title1 = title;

        }

        // alertDialogBuilder.setTitle("" + title);
        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title1);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_equipment_model_required, null);

        spinnerLocation = (Spinner) view.findViewById(R.id.spinner_location);
        spinnerEquipmentType = (Spinner) view.findViewById(R.id.spinner_equipment_type);
        etAddEquipmentModel = (EditText) view.findViewById(R.id.et_add_equipment_model);
        //   tvDueServiceDateTitle = (TextView) view.findViewById(R.id.tv_due_service_date_title);

        //  etDueServiceInterval = (EditText) view.findViewById(R.id.et_due_service_interval);
        //  spinnerInspectionDuration = (Spinner) view.findViewById(R.id.spinner_inspection_duration);

        tvEmailId = (TextView) view.findViewById(R.id.tv_email_id);
        //  tvDueServiceDate = (TextView) view.findViewById(R.id.tv_due_service_date);




/*
        tvWeekDay = (TextView) view.findViewById(R.id.tv_week_day);
        llWeekDay = (LinearLayout) view.findViewById(R.id.ll_week_day);
        spinnerWeekDay = (Spinner) view.findViewById(R.id.spinner_week_day);
        tvMonthDay = (TextView) view.findViewById(R.id.tv_month_day);
        etMonthDay = (EditText) view.findViewById(R.id.et_month_day);
        */


        tvOtherDay = (TextView) view.findViewById(R.id.tv_other_day);
        locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocationName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        equipmentTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEquipmentTypeName);
        equipmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEquipmentType.setAdapter(equipmentTypeAdapter);

        listInspectionDuration = new ArrayList<String>();
        listInspectionDuration.add("Select Service Frequency");
        listInspectionDuration.add("Daily");
        listInspectionDuration.add("Weekly");
        listInspectionDuration.add("Monthly");
        listInspectionDuration.add("Quarterly");
        listInspectionDuration.add("Half Yearly");
        listInspectionDuration.add("Yearly");

        listDayFreq = new ArrayList<String>();
        listDayFreq.add("Select Day");

        listDayFreq.add("Monday");
        listDayFreq.add("Tuesday");
        listDayFreq.add("Wednesday");
        listDayFreq.add("Thursday");
        listDayFreq.add("Friday");
        //  listDayFreq.add("Saturday");
        //  listDayFreq.add("Sunday");

        checkDayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listDayFreq);
        checkDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinnerWeekDay.setAdapter(checkDayAdapter);

/*

        indpectionDurationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listInspectionDuration);
        indpectionDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInspectionDuration.setAdapter(indpectionDurationAdapter);

        spinnerInspectionDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                   */
/* tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);
                    spinnerWeekDay.setVisibility(View.GONE);
                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);*//*

                    //  tvOtherDay.setVisibility(View.GONE);

                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");

                } else if (position == 1) {

                   */
/* tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);
                    spinnerWeekDay.setVisibility(View.GONE);
                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);*//*

                    // tvOtherDay.setVisibility(View.GONE);

                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");

                } else if (position == 2) {

                   */
/* spinnerWeekDay.setVisibility(View.VISIBLE);
                    tvWeekDay.setVisibility(View.VISIBLE);
                    llWeekDay.setVisibility(View.VISIBLE);

                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);*//*

                    //   tvOtherDay.setVisibility(View.GONE);
                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");


                } else if (position == 3) {

                   */
/* spinnerWeekDay.setVisibility(View.GONE);
                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);

                    tvMonthDay.setVisibility(View.VISIBLE);
                    etMonthDay.setVisibility(View.VISIBLE);*//*

                    //    tvOtherDay.setVisibility(View.GONE);

                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");


                } else if (position == 4 || position == 5 || position == 6) {

                   */
/* spinnerWeekDay.setVisibility(View.GONE);
                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);

                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);*//*

                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/

        tvEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAddEmail = DialogAddEmail.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.dialog_add_email_title));

                bundle.putStringArrayList("list_email", listEmail);

                dialogAddEmail.setArguments(bundle);
                dialogAddEmail.setTargetFragment(DialogAddEquipmentModelRequired.this, 0);
                dialogAddEmail.show(getFragmentManager(), "dialog");

            }
        });


        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

            etAddEquipmentModel.setHint("Update equipment model");
            //  etDueServiceInterval.setHint("Update due service interval");

            for (int i = 0; i < listLocationName.size(); i++) {
                if (listLocationName.get(i).equalsIgnoreCase(locationName)) {
                    locationPos = i;
                }
            }

            for (int i = 0; i < listEquipmentTypeName.size(); i++) {
                if (listEquipmentTypeName.get(i).equalsIgnoreCase(equipmentTypeName)) {
                    equipmentTypePos = i;
                }
            }

            for (int i = 0; i < listInspectionDuration.size(); i++) {
                if (listInspectionDuration.get(i).equalsIgnoreCase(inspectionDuration)) {
                    inspectionDurationPos = i;
                }
            }

            etAddEquipmentModel.setText(equipmentResponse.getModelNo());
            //  etDueServiceInterval.setText(equipmentResponse.getDue_service_interval());

            spinnerLocation.setSelection(locationPos);
            spinnerEquipmentType.setSelection(equipmentTypePos);
            // spinnerInspectionDuration.setSelection(inspectionDurationPos);

            String email = "";

            for (int i = 0; i < listEmail.size(); i++) {

                if (i == (listEmail.size() - 1)) {
                    email = email + listEmail.get(i);
                } else {
                    email = email + listEmail.get(i) + " , ";
                }

            }

            tvEmailId.setText(email);

          /*  dueServiceDate = convertDateFormate(equipmentResponse.getNextServiceDate());
            if (dueServiceDate == null || dueServiceDate.equalsIgnoreCase("")) {
                tvDueServiceDate.setText("Select Next Service Date");
            } else {
                tvDueServiceDate.setText("" + dueServiceDate);
            }*/


        }

       /* tvDueServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
*/
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((FragmentEquipment) getTargetFragment()).addEquipmentNegativeClick();
            }
        });

        alertDialogBuilder.setNeutralButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

                    ((FragmentEquipment) getTargetFragment()).updateEquipmentNegativeClick();

                }

            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.show();
        //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);

        } else {

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        }


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_equipment_model_dialog_title))) {

                    int posLocation = 0;

                    for (int i = 0; i < listLocation.size(); i++) {
                        if (listLocation.get(i).getLocationName().equalsIgnoreCase(spinnerLocation.getSelectedItem().toString())) {
                            posLocation = i;
                        }
                    }

                    int posEquipment = 0;

                    for (int i = 0; i < listEquipmentType.size(); i++) {
                        if (listEquipmentType.get(i).getName().equalsIgnoreCase(spinnerEquipmentType.getSelectedItem().toString())) {
                            posEquipment = i;
                        }
                    }

                    String freqDay = "";

                    if (spinnerLocation.getSelectedItem().toString().equalsIgnoreCase("Select location")) {

                        Utilities.showSnackbar(v, "Please select any location");

                    } else if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("Select Asset Category")) {

                        Utilities.showSnackbar(v, "Please select any asset category");

                    } else if (etAddEquipmentModel.getText().toString().equalsIgnoreCase("")) {

                        Utilities.showSnackbar(v, "Please add asset model");

                    } /*else if (etDueServiceInterval.getText().toString().equalsIgnoreCase("")) {

                        Snackbar.make(v, "Please add due service interval", Snackbar.LENGTH_LONG).show();

                    }*/ else if (listEmail == null || listEmail.size() == 0) {

                        //Snackbar.make(v, "Please add any email id", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add any email id");

                    }


                    /*else if (tvDueServiceDate.getText().toString().equalsIgnoreCase("Select Next Service Date")) {

                        //  Snackbar.make(v, "Please select next service date", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select next service date");

                    } else if (spinnerInspectionDuration.getSelectedItemPosition() == 0) {

                        //Snackbar.make(v, "Please select any service frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select any service frequency");

                    }*/


                    else /*if (spinnerInspectionDuration.getSelectedItemPosition() == 1)*/ {

                        ((FragmentEquipment) getTargetFragment()).addEquipmentPositiveClick(listLocation.get(posLocation).getId(), listEquipmentType.get(posEquipment).getId(), etAddEquipmentModel.getText().toString(),

                                /*spinnerInspectionDuration.getSelectedItem().toString(),*/ listEmail/*, tvDueServiceDate.getText().toString(), spinnerInspectionDuration.getSelectedItem().toString(), freqDay*/);

                    }


                } else {

                    Log.e("listEmail", "" + listEmail);

                    Log.e("equipmentResponse", "" + listEmail_1);

                    //List<String> list = equipmentResponse.getEmailId();

                    boolean email = listEmail.containsAll(listEmail_1);

                    Log.e("email", "" + email);

                    if (spinnerLocation.getSelectedItem().toString().equalsIgnoreCase(equipmentResponse.getLocation().getLocationName()) &&
                            spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase(equipmentResponse.getEquipmentType().getEquipmentTypeName()) &&
                            etAddEquipmentModel.getText().toString().equalsIgnoreCase(equipmentResponse.getModelNo()) /*&&
                            etDueServiceInterval.getText().toString().equalsIgnoreCase(equipmentResponse.getDue_service_interval())*/

                          /*  && tvDueServiceDate.getText().toString().equalsIgnoreCase(dueServiceDate) &&
                            spinnerInspectionDuration.getSelectedItem().toString().equalsIgnoreCase(equipmentResponse.getServiceFrequency())
                            */
                            ) {

                        Utilities.showSnackbar(v, getResources().getString(R.string.not_any_change));

                    } else if (spinnerLocation.getSelectedItem().toString().equalsIgnoreCase("Select location")) {

                        Utilities.showSnackbar(v, "Please select any location");

                    } else if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("Select Asset Category")) {

                        Utilities.showSnackbar(v, "Please select any asset category");

                    } else if (etAddEquipmentModel.getText().toString().equalsIgnoreCase("")) {

                        Utilities.showSnackbar(v, "Please update asset model");

                    }


                    /*else if (etDueServiceInterval.getText().toString().equalsIgnoreCase("")) {

                        Snackbar.make(v, "Please add due service interval", Snackbar.LENGTH_LONG).show();

                    }*/


                   /* else if (spinnerInspectionDuration.getSelectedItem().toString().equalsIgnoreCase("Select Service Frequency")) {

                        //     Snackbar.make(v, "Please select any service frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select any service frequency");

                    } */


                    else if (listEmail == null || listEmail.size() == 0) {

                        //   Snackbar.make(v, "Please add any email id", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add any email id");

                    } /*else if (tvDueServiceDate.getText().toString().equalsIgnoreCase("Select Next Service Date")) {

                        Snackbar.make(v, "Please select next service date", Snackbar.LENGTH_LONG).show();


                    } */ else {

                        int posLocation = 0;

                        for (int i = 0; i < listLocation.size(); i++) {
                            if (listLocation.get(i).getLocationName().equalsIgnoreCase(spinnerLocation.getSelectedItem().toString())) {
                                posLocation = i;
                            }
                        }

                        int posEquipment = 0;

                        for (int i = 0; i < listEquipmentType.size(); i++) {
                            if (listEquipmentType.get(i).getName().equalsIgnoreCase(spinnerEquipmentType.getSelectedItem().toString())) {
                                posEquipment = i;
                            }
                        }

                        ((FragmentEquipment) getTargetFragment()).editEquipmentPositiveClick(listLocation.get(posLocation).getId(), listEquipmentType.get(posEquipment).getId(), etAddEquipmentModel.getText().toString()

                                /*   , spinnerInspectionDuration.getSelectedItem().toString() */, listEmail /* , tvDueServiceDate.getText().toString() */);

                    }


                }


            }
        });

        return dialog;
    }

    private void showDatePickerDialog() {


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        Log.e("mYear", "" + mYear);
        Log.e("mMonth", "" + mMonth);
        Log.e("mDay", "" + mDay);



      /*  DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        tvDueServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();*/


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), null, mYear, mMonth, mDay) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {

                dismiss();

                //       tvDueServiceDate.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);

            }
        };

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);


    }

    private String convertDateFormate(String stringDate) {
        String date = null;
        Date convertedDate = null;

        if (stringDate == null || stringDate.equals("")) {
            date = "";
            return date;
        } else {

            String oldFormat = "yyyy-MM-dd";

            //  String oldFormat = "yyyy-MM-dd'T'HH:mm:ss";
            String newFormat = "dd-MM-yyyy";

            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);


            try {
                date = sdf2.format(sdf1.parse(stringDate));
            } catch (ParseException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }

            try {
                convertedDate = (Date) sdf2.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
            return date;

        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void addEmail(ArrayList<String> listEmail) {

        this.listEmail = listEmail;

        dialogAddEmail.dismiss();

        String email = "";

        for (int i = 0; i < listEmail.size(); i++) {

            if (i == (listEmail.size() - 1)) {
                email = email + listEmail.get(i);
            } else {
                email = email + listEmail.get(i) + " , ";
            }

        }

        tvEmailId.setText(email);

    }
}
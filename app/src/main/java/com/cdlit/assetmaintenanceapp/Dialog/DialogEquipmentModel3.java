package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Model.AddEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DialogEquipmentModel3 extends DialogFragment {

    private ArrayList<Bitmap> listBitmap;
    private ArrayList<String> listImagePath;
    private boolean imageChanges;
    private EquipmentResponse equipmentResponse;
    private List<EquipmentResponse.EquipmentResponseImages> listEquipmentImages;
    private String okString;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvExpiryDate;
    private EditText etServiceName;
    private TextView tvOtherDay;
    private TextView tvLastServiceDate;
    private TextView tvNextServiceDate;
    private Spinner spinnerInspectionDuration;
    private ArrayList<String> listInspectionDuration;
    private ArrayAdapter<String> indpectionDurationAdapter;
    private RecyclerView recyclerAsset;
    private FloatingActionButton fabAddAsset;
    private EditText etServiceNo;
    private ArrayList<AddEquipmentModel3> listEquipmentModel3;
    private AdapterEquipmentModel3 adapterEquipmentModel3;
    private View view;

    public static DialogEquipmentModel3 newInstance() {

        DialogEquipmentModel3 dialogAddEquipmentModel3 = new DialogEquipmentModel3();

        return dialogAddEquipmentModel3;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_equipment_model_3_dialog_title))) {

            okString = getResources().getString(R.string.update_bt_string);
            equipmentResponse = (EquipmentResponse) args.getParcelable("equipment_response");

        } else {

            okString = getResources().getString(R.string.add_bt_string);


        }


        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));


        alertDialogBuilder.setCustomTitle(customTitle);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_equipment_model_3, null);

        etServiceName = (EditText) view.findViewById(R.id.et_service_name);
        tvOtherDay = (TextView) view.findViewById(R.id.tv_other_day);
        tvLastServiceDate = (TextView) view.findViewById(R.id.tv_last_service_date);
        tvNextServiceDate = (TextView) view.findViewById(R.id.tv_next_service_date);
        spinnerInspectionDuration = (Spinner) view.findViewById(R.id.spinner_inspection_duration);

        etServiceNo = (EditText) view.findViewById(R.id.et_service_no);
        fabAddAsset = (FloatingActionButton) view.findViewById(R.id.fab_add_asset);
        recyclerAsset = (RecyclerView) view.findViewById(R.id.recycler_asset);

        listInspectionDuration = new ArrayList<String>();

        listInspectionDuration.add("Select Service Frequency");
        listInspectionDuration.add("Daily");
        listInspectionDuration.add("Weekly");
        listInspectionDuration.add("Monthly");
        listInspectionDuration.add("Quarterly");
        listInspectionDuration.add("Half Yearly");
        listInspectionDuration.add("Yearly");

        indpectionDurationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listInspectionDuration);
        indpectionDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInspectionDuration.setAdapter(indpectionDurationAdapter);

        listEquipmentModel3 = new ArrayList<AddEquipmentModel3>();

        adapterEquipmentModel3 = new AdapterEquipmentModel3(getActivity(), DialogEquipmentModel3.this, listEquipmentModel3);

        recyclerAsset.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerAsset.setLayoutManager(mLayoutManager);

        //   recyclerAsset.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //   recyclerAsset.setItemAnimator(new DefaultItemAnimator());

        recyclerAsset.setAdapter(adapterEquipmentModel3);


        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_equipment_model_3_dialog_title))) {

            List<EquipmentResponse.EquipmentServiceTypes> listEquipmentServiceType = equipmentResponse.getEquipmentServiceTypes();

            for (int i = 0; i < listEquipmentServiceType.size(); i++) {

                AddEquipmentModel3 addEquipmentModel3 = new AddEquipmentModel3();
                addEquipmentModel3.setServiceName(listEquipmentServiceType.get(i).getServiceName());
                addEquipmentModel3.setServiceFreq(listEquipmentServiceType.get(i).getFrequency());
                addEquipmentModel3.setServiceNo(listEquipmentServiceType.get(i).getFrequencyNo().toString());

                String lastServiceDate;

                if (listEquipmentServiceType.get(i).getLastCheckDate() == null) {
                    lastServiceDate = "";
                } else {
                    lastServiceDate = convertDateFormate(listEquipmentServiceType.get(i).getLastCheckDate().toString());
                }

                addEquipmentModel3.setLastServiceDate(lastServiceDate);

                addEquipmentModel3.setNextServiceDate(convertDateFormate(listEquipmentServiceType.get(i).getNextCheckDate().toString()));

                listEquipmentModel3.add(addEquipmentModel3);

            }

            adapterEquipmentModel3.notifyDataSetChanged();

        }


        spinnerInspectionDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    tvOtherDay.setVisibility(View.GONE);

                } else {

                    tvOtherDay.setVisibility(View.GONE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last service date.");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }

        });


        tvLastServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(1);

            }
        });

        tvNextServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(2);

            }
        });


        fabAddAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etServiceName.getText().toString().equalsIgnoreCase("")) {

                    Utilities.showSnackbarView(view, "Enter service name");

                } else if (spinnerInspectionDuration.getSelectedItemPosition() == 0) {

                    Utilities.showSnackbarView(view, "Select service frequency");

                } else if (etServiceNo.getText().toString().equalsIgnoreCase("")) {

                    Utilities.showSnackbarView(view, "Enter frequency number");

                } /*else if (tvLastServiceDate.getText().toString().equalsIgnoreCase("Select last service date")) {

                    Utilities.showSnackbarView(view, "Select last service date");

                }*/ else if (tvNextServiceDate.getText().toString().equalsIgnoreCase("Select next service date")) {

                    Utilities.showSnackbarView(view, "Select next service date");

                } else {

                    boolean flag = false;

                    for (int i = 0; i < listEquipmentModel3.size(); i++) {

                        String serviceName = listEquipmentModel3.get(i).getServiceName();
                        String serviceName1 = etServiceName.getText().toString();

                        String serviceFreq = listEquipmentModel3.get(i).getServiceFreq();
                        String serviceFreq1 = spinnerInspectionDuration.getSelectedItem().toString();

                        String serviceNo = listEquipmentModel3.get(i).getServiceNo();
                        String serviceNo1 = etServiceNo.getText().toString();

                        String lastServiceDate = listEquipmentModel3.get(i).getLastServiceDate();

                        String lastServiceDate1;

                        if (tvLastServiceDate.getText().toString().equalsIgnoreCase("Select Last Service Date")) {

                            lastServiceDate1 = "";

                        } else {

                            lastServiceDate1 = tvLastServiceDate.getText().toString();

                        }

                        String nextServiceDate = listEquipmentModel3.get(i).getNextServiceDate();
                        String nextServiceDate1 = tvNextServiceDate.getText().toString();

                        if (serviceName.equalsIgnoreCase(serviceName1) &&
                                serviceFreq.equalsIgnoreCase(serviceFreq1) &&
                                serviceNo.equalsIgnoreCase(serviceNo1) &&
                                lastServiceDate.equalsIgnoreCase(lastServiceDate1) &&
                                nextServiceDate.equalsIgnoreCase(nextServiceDate1)) {

                            flag = true;

                            Log.e("flag", "" + flag);

                        }

                    }


                    if (flag) {

                        Utilities.showSnackbarView(view, "This combination is already available in the list, please try with different combination ");

                    } else {

                        String lastServiceDate1;

                        if (tvLastServiceDate.getText().toString().equalsIgnoreCase("Select Last Service Date")) {

                            lastServiceDate1 = "";

                        } else {

                            lastServiceDate1 = tvLastServiceDate.getText().toString();

                        }


                        AddEquipmentModel3 addEquipmentModel3 = new AddEquipmentModel3();

                        addEquipmentModel3.setServiceName(etServiceName.getText().toString());
                        addEquipmentModel3.setServiceFreq(spinnerInspectionDuration.getSelectedItem().toString());
                        addEquipmentModel3.setServiceNo(etServiceNo.getText().toString());
                        addEquipmentModel3.setLastServiceDate(lastServiceDate1);
                        addEquipmentModel3.setNextServiceDate(tvNextServiceDate.getText().toString());

                        adapterEquipmentModel3.notityAdapter(addEquipmentModel3);

                        etServiceName.setText("");
                        spinnerInspectionDuration.setSelection(0);
                        etServiceNo.setText("");
                        tvLastServiceDate.setText("Select Last Service Date");
                        tvNextServiceDate.setText("Select Next Service Date");

                    }

                }


            }
        });

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.edit_equipment_model_3_dialog_title))) {

                    if (listEquipmentModel3 == null || listEquipmentModel3.size() == 0) {

                        Utilities.showSnackbarView(view, "Please enter all service fields ");

                    } else {

                        dialog.dismiss();

                        ((FragmentEquipment) getTargetFragment()).editEquipment3PositiveClick(listEquipmentModel3, title, equipmentResponse);

                    }

                } else {

                    if (listEquipmentModel3 == null || listEquipmentModel3.size() == 0) {

                        Utilities.showSnackbarView(view, "Please enter all service fields ");

                    } else {

                        dialog.dismiss();

                        ((FragmentEquipment) getTargetFragment()).addEquipment3PositiveClick(listEquipmentModel3, title);

                    }

                }

            }
        });

        return dialog;


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


    private String convertDateFormate1(String stringDate) {

        String date = null;
        Date convertedDate = null;

        if (stringDate == null || stringDate.equals("")) {

            date = "";
            return date;

        } else {

            String oldFormat = "dd-MM-yyyy";
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

    private void showDatePickerDialog(final int i) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        Log.e("mYear", "" + mYear);
        Log.e("mMonth", "" + mMonth);
        Log.e("mDay", "" + mDay);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), null, mYear, mMonth, mDay) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                dismiss();

                if (i == 1) {

                    tvLastServiceDate.setText(convertDateFormate1(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                } else if (i == 2) {

                    tvNextServiceDate.setText(convertDateFormate1(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));

                }


            }
        };

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);

    }


    public void deleteClick(AddEquipmentModel3 addEquipmentModel3) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", addEquipmentModel3);

      /*  if (locationResponse == null) {
            bundle.putString("item", deleteLocationName);
        } else {
            bundle.putString("item", locationResponse.getLocationName());
        }
*/

        dialogDeleteConfirm.setArguments(bundle);
        dialogDeleteConfirm.setTargetFragment(DialogEquipmentModel3.this, 8);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    public void deletePositiveClick(AddEquipmentModel3 response) {

        adapterEquipmentModel3.listEquipmentModel3.remove(response);

        adapterEquipmentModel3.notifyDataSetChanged();

    }
}

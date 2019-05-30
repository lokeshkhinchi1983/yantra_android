package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterMultiImages;
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.FaultLog;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.Notification;
import com.cdlit.assetmaintenanceapp.Model.RepairLogDetail;
import com.cdlit.assetmaintenanceapp.Model.RepairLogEquipment;
import com.cdlit.assetmaintenanceapp.Model.RepairLogEquipmentType;
import com.cdlit.assetmaintenanceapp.Model.TaskLog;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;
import com.cdlit.assetmaintenanceapp.Ui.FragmentRepairLog;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rakesh on 12-12-2017.
 */

public class DialogRepairLog extends DialogFragment {

    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = DialogAddEquipmentType.class.getSimpleName();
    private static View view2 = null;
    private static DialogAddEquipmentType dialogAddEquipmentType;
    private static DialogRepairLog dialogRepairLog;
    private AlertDialog.Builder alertDialogBuilder;
    //   private Spinner spinnerCategory;
    //   private EditText etAddEquipmentType;
    //   private EditText etAddEquipmentDes;
    //   private ArrayList<CategoryResponse> listCategory;
    //  private ArrayList<String> listCategoryName;
    private FloatingActionButton fabSelectCamera;
    private Uri fileUri;
    private String filePath;
    private RepairLogDetail.RepairLogDetailResponse repairLogResponse;
    //   private String categoryName;
    private int categoryPos;
    private Bitmap decodedByte;
    private TextView tvAddImage;
    private List<RepairLogDetail.RepairLogDetailRepairLogImages> listRepairLogImages;
    private String okString;
    private ArrayList<Bitmap> listBitmap;
    private AdapterMultiImages adapterMultiImages;
    //  private RecyclerView recyclerEquipmentType;
    public boolean imageChanges;
    private Spinner spinnerEquipmentType;
    private Spinner spinnerEquipment;
    private EditText etAddRepairLogDes;
    private ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse> listEquipmentType;
    //    private ArrayList<EquipmentResponse> listEquipment;
    private ArrayList<String> listEquipmentTypeName;
    //   private ArrayList<String> listEquipmentName;
    public int equipmentPos;
    public int equipmentTypePos;
    private EditText etAddRepairLogCost;
    private ArrayList<RepairLogEquipment.RepairLogEquipmentResponse> listEquipment;
    private RecyclerView recyclerRepairLog;
    private ArrayList<String> listEquipmentName;
    private Handler handler;
    private ArrayAdapter<String> equipmentAdapter;
    private String equipmentTypeName;
    //  private Integer equipmentTypeId;
    private String equipmentName;
    private ArrayList<String> listImagePath;
    private Spinner spinnerLocation;
    private ArrayList<String> listLocationName;
    private ArrayAdapter<String> locationAdapter;
    //   private ArrayList<LocationResponse> listLocation;
    private Integer locationId;
    private String title;
    private ArrayAdapter<String> equipmentTypeAdapter;
    private ArrayList<LocationResponse> listLocation;
    private String userType;
    private int equipmentTypePossition;
    private EditText etAgencyName;
    private EditText etContactPerson;
    private TextView tvRepairlogDate;
    private String agencyName;
    private String contactPerson;
    private String repairlogDate;
    private EquipmentFalulList.Response equipmentFaultResponce;
    private EquipmentResponse equipmentResponce;
    private Notification.Response equipmentNotificationResponce;
    private ArrayList<EquipmentFalulList.Response> equipmentFaultlistResponce;
    private ArrayList<Notification.Response> equipmentNotificationlistResponce;
    private EditText etContactPersonNumber;
    private String locationName;
    private Button btFaultLog;
    private Button btTaskLog;
    private LinearLayout llFaultTaskLog;
    private TextView tvSelectFaultLog;
    private ArrayList<FaultLog.Response> listFaultLog;
    private View view;
    private ArrayList<TaskLog.Response> listTaskLog;
    private DialogFaultLog dialogFaultLog;
    private DialogTaskLog dialogTaskLog;
    private ArrayList<Integer> listFaultLogId;
    private ArrayList<Integer> listTaskLogId;
    private EditText etMilesHours;
    private Spinner spinnerMilesHours;
    private ArrayList<String> listMilesHours;
    private ArrayAdapter<String> milesHoursAdapter;
    private TextView tvMilesHours;
    private String noMilesOrHours;
    private String unitMilesOrHours;


    public static DialogRepairLog newInstance() {

        dialogRepairLog = new DialogRepairLog();
        return dialogRepairLog;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Bitmap bitmap = null;

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, fileUri.getPath());

                } catch (IOException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);

                }

                listBitmap.add(bitmap);

                adapterMultiImages.notifyDataSetChanged();

                imageChanges = true;

                Log.e("filePath", "" + fileUri.getPath());

                File file = new File(fileUri.getPath());

                if (file.delete()) {
                    Log.e("filePath", "" + file.getName() + "is deleted");
                } else {
                    Log.e("filePath", "" + file.getName() + "is not deleted");
                }

            } else if (resultCode == RESULT_CANCELED) {


            } else {


            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("DialogUserImage", "onCreateDialog");

        ActivityNavigationDrawerAdmin.backroundflag = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        title = args.getString("title");

        userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

        listEquipmentType = new ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse>();

        listLocation = args.getParcelableArrayList("list_location");

        listLocationName = args.getStringArrayList("list_location_name");

        equipmentResponce = args.getParcelable("equipment_response");

        equipmentFaultResponce = args.getParcelable("equipment_faultlog");

        equipmentNotificationResponce = args.getParcelable("equipment_notificationlog");

        equipmentFaultlistResponce = args.getParcelableArrayList("equipment_faultlog_list");

        equipmentNotificationlistResponce = args.getParcelableArrayList("equipment_notificationlog_list");

        listBitmap = new ArrayList<Bitmap>();

        listImagePath = new ArrayList<String>();

        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

            imageChanges = false;

            repairLogResponse = (RepairLogDetail.RepairLogDetailResponse) args.getParcelable("repair_log_response");

            for (int i = 0; i < repairLogResponse.getRepairLogImages().size(); i++) {

                listBitmap.add(null);

                String imagePath = repairLogResponse.getRepairLogImages().get(i).getImagePath();

                Log.d("imagePath", "" + imagePath);

                listImagePath.add(imagePath);

            }

            listRepairLogImages = repairLogResponse.getRepairLogImages();

            //   location = repairLogResponse.getLocation();

            equipmentTypeName = repairLogResponse.getEquipment().getEquipmentType().getEquipmentTypeName();
            //       equipmentTypeId = repairLogResponse.getEquipment().getEquipmentType().getId();

            Log.e("equipmentTypeName", "" + equipmentTypeName);
            //   Log.e("equipmentTypeId", "" + equipmentTypeId);

            equipmentName = repairLogResponse.getEquipment().getModelNo();
            //      equipmentId = repairLogResponse.getId();

            Log.e("equipmentName", "" + equipmentName);
            //   Log.e("equipmentId", "" + equipmentId);

            locationId = repairLogResponse.getLocation_id();

            Log.e("locationId", "" + locationId);

            agencyName = repairLogResponse.getAgencyName();

            contactPerson = repairLogResponse.getContactPersonName();

            repairlogDate = convertDate(repairLogResponse.getRepairLogDate());

            locationName = repairLogResponse.getEquipment().getLocation3().getLocationName();

            Log.e("locationName", "" + locationName);

            noMilesOrHours = repairLogResponse.getNoMilesOrHours();

            unitMilesOrHours = repairLogResponse.getUnitMilesOrHours();


            okString = getResources().getString(R.string.update_bt_string);

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 101) {

                    if (listEquipment.size() == 0) {

                        //    Snackbar.make(view2, "There is no equipment available for this current location ", Snackbar.LENGTH_LONG).show();

                        //   Toast.makeText(getActivity(), "There is no asset available for this current location", Toast.LENGTH_SHORT).show();

                        Log.e("not available ", "There is no asset available for this current location ");

                        equipmentAdapter.notifyDataSetChanged();

                    } else {

                        listEquipmentName.add("Select Asset");

                        for (RepairLogEquipment.RepairLogEquipmentResponse equipmentResponse : listEquipment) {

                            listEquipmentName.add(equipmentResponse.getModalname());

                        }

                        equipmentAdapter.notifyDataSetChanged();


                        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

                            int equipmentPossition = 0;

                            for (int i = 0; i < listEquipmentName.size(); i++) {
                                if (listEquipmentName.get(i).equalsIgnoreCase(equipmentName)) {
                                    equipmentPossition = i;
                                    Log.e("equipmentPossition", "" + equipmentPossition);
                                }
                            }

                            Log.e("equipmentPossition", "" + equipmentPossition);

                            spinnerEquipment.setSelection(equipmentPossition);

                        } else {
                            spinnerEquipment.setSelection(0);
                        }

                    }

                    loadImages();
                }

                if (msg.what == 102) {

                    if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

                        int locationPos = 0;

                        for (int i = 0; i < listLocation.size(); i++) {

                            if (listLocation.get(i).getId() == locationId) {
                                locationPos = i;
                            }

                        }

                        spinnerLocation.setSelection(locationPos + 1);

                    }

                    locationAdapter.notifyDataSetChanged();

                }

                if (msg.what == 103) {

                    equipmentTypeAdapter.notifyDataSetChanged();

                    if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

                        equipmentTypePossition = 0;

                        for (int i = 0; i < listEquipmentTypeName.size(); i++) {

                            if (listEquipmentTypeName.get(i).equalsIgnoreCase(equipmentTypeName)) {
                                equipmentTypePossition = i;
                            }

                        }

                        spinnerEquipmentType.setSelection(equipmentTypePossition);

                    } else {
                        spinnerEquipmentType.setSelection(0);
                    }

                }


                if (msg.what == 104) {


                    if (listFaultLog.size() == 0) {

                        Utilities.showSnackbarView(view, "No fault log available for this asset");

                    } else {

                        dialogFaultLog = DialogFaultLog.newInstance();

                        Bundle bundle = new Bundle();
                        bundle.putString("title", getResources().getString(R.string.add_fault_log_dialog_title));
                        bundle.putParcelableArrayList("list_fault_log", listFaultLog);
                        bundle.putIntegerArrayList("list_fault_log_id", listFaultLogId);
                        dialogFaultLog.setArguments(bundle);
                        dialogFaultLog.setTargetFragment(DialogRepairLog.this, 0);

                        dialogFaultLog.show(getActivity().getSupportFragmentManager(), "dialog");

                    }


                }

                if (msg.what == 105) {


                    if (listTaskLog.size() == 0) {

                        Utilities.showSnackbarView(view, "No task log available for this asset");

                    } else {

                        dialogTaskLog = DialogTaskLog.newInstance();

                        Bundle bundle = new Bundle();
                        bundle.putString("title", getResources().getString(R.string.add_task_log_dialog_title));
                        bundle.putParcelableArrayList("list_task_log", listTaskLog);

                        dialogTaskLog.setArguments(bundle);
                        dialogTaskLog.show(getActivity().getSupportFragmentManager(), "dialog");

                    }


                }

                return false;
            }


        });


        listEquipmentTypeName = new ArrayList<String>();

      /*  listEquipmentTypeName.add("Select Equipment Type");

        for (RepairLogEquipmentType.RepairLogEquipmentTypeResponse equipmentTypeResponse : listEquipmentType) {

            listEquipmentTypeName.add(equipmentTypeResponse.getName());

        }*/

        listEquipmentName = new ArrayList<String>();

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

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_repair_log, null);

        spinnerEquipmentType = (Spinner) view.findViewById(R.id.spinner_equipment_type);
        spinnerEquipment = (Spinner) view.findViewById(R.id.spinner_equipment);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinner_location);

        etAddRepairLogDes = (EditText) view.findViewById(R.id.et_add_repair_log_des);
        etAddRepairLogCost = (EditText) view.findViewById(R.id.et_add_repair_log_cost);

        etAgencyName = (EditText) view.findViewById(R.id.et_agency_name);
        etContactPerson = (EditText) view.findViewById(R.id.et_contact_person);
        tvRepairlogDate = (TextView) view.findViewById(R.id.tv_repairlog_date);
        etContactPersonNumber = (EditText) view.findViewById(R.id.et_contact_person_number);
        llFaultTaskLog = (LinearLayout) view.findViewById(R.id.ll_fault_task_log);

        tvSelectFaultLog = (TextView) view.findViewById(R.id.tv_select_fault_log);

        btFaultLog = (Button) view.findViewById(R.id.bt_fault_log);
        btTaskLog = (Button) view.findViewById(R.id.bt_task_log);

        spinnerMilesHours = (Spinner) view.findViewById(R.id.spinner_miles_hours);
        etMilesHours = (EditText) view.findViewById(R.id.et_miles_hours);

        tvAddImage = (TextView) view.findViewById(R.id.tv_add_image);

        tvMilesHours = (TextView) view.findViewById(R.id.tv_miles_hours);

        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {
            //   etAddRepairLogCost.setHint("Update equipment type");
            //     etAddRepairLogDes.setHint("Update equipment description");
            tvAddImage.setText("Update Image");
        }

        fabSelectCamera = (FloatingActionButton) view.findViewById(R.id.fab_select_camera);

        if (equipmentResponce != null) {

            listEquipmentTypeName.add(equipmentResponce.getEquipmentType().getEquipmentTypeName());

        } else if (equipmentFaultResponce != null) {

            listEquipmentTypeName.add(equipmentFaultResponce.getEquipmentType().getEquipmentTypeName());

        } else if (equipmentNotificationResponce != null) {

            listEquipmentTypeName.add(equipmentNotificationResponce.getEquipmenttypename());

        }


        // Creating adapter for spinner
        equipmentTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEquipmentTypeName);

        // Drop down layout style - list view with radio button
        equipmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerEquipmentType.setAdapter(equipmentTypeAdapter);

        if (equipmentResponce != null) {

            listEquipmentName.add(equipmentResponce.getModelNo());

        } else if (equipmentFaultResponce != null) {

            listEquipmentName.add(equipmentFaultResponce.getEquipment().getModelNo());

        } else if (equipmentNotificationResponce != null) {

            listEquipmentName.add(equipmentNotificationResponce.getEquipmentname());

        }

        // Creating adapter for spinner
        equipmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEquipmentName);

        // Drop down layout style - list view with radio button
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerEquipment.setAdapter(equipmentAdapter);

        listEquipment = new ArrayList<RepairLogEquipment.RepairLogEquipmentResponse>();

        listFaultLog = new ArrayList<FaultLog.Response>();
        listTaskLog = new ArrayList<TaskLog.Response>();

        if (equipmentResponce != null) {

            listLocationName.add(equipmentResponce.getLocation().getLocationName());

        } else if (equipmentFaultResponce != null) {

            listLocationName.add(equipmentFaultResponce.getLocation().getLocationName());

        } else if (equipmentNotificationResponce != null) {

            listLocationName.add(equipmentNotificationResponce.getLocationname());

        }

        locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocationName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        listMilesHours = new ArrayList<String>();

        listMilesHours.add("Select Miles / Hours");
        listMilesHours.add("Miles");
        listMilesHours.add("Hours");

        milesHoursAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listMilesHours);
        milesHoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMilesHours.setAdapter(milesHoursAdapter);


        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

            int milesPos = 0;

            for (int i = 0; i < listMilesHours.size(); i++) {

                if (listMilesHours.get(i).equalsIgnoreCase(unitMilesOrHours)) {
                    milesPos = i;
                    Log.e("milesPos", "" + milesPos);
                }

            }

            Log.e("milesPos", "" + milesPos);

            spinnerMilesHours.setSelection(milesPos);

        } else {

            spinnerMilesHours.setSelection(0);

        }


        spinnerMilesHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    tvMilesHours.setVisibility(View.GONE);
                    etMilesHours.setVisibility(View.GONE);

                } else if (position == 1) {

                    tvMilesHours.setVisibility(View.VISIBLE);
                    etMilesHours.setVisibility(View.VISIBLE);
                    tvMilesHours.setText("Enter Miles ");
                    etMilesHours.setHint("Enter Miles");

                } else if (position == 2) {

                    tvMilesHours.setVisibility(View.VISIBLE);
                    etMilesHours.setVisibility(View.VISIBLE);
                    tvMilesHours.setText("Enter Hours ");
                    etMilesHours.setHint("Enter Hours");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btFaultLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFaultLog();

            }
        });


        btTaskLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTaskLog();

            }
        });


        spinnerEquipmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                equipmentTypePos = position;

                if (position == 0) {

                    if (equipmentResponce == null && equipmentFaultResponce == null && equipmentNotificationResponce == null) {
                        listEquipment.clear();
                        listEquipmentName.clear();
                        equipmentAdapter.notifyDataSetChanged();
                    }

                } else {

                    if (spinnerLocation.getSelectedItem().equals("Select location")) {

                        Utilities.showSnackbar(view, "Please select any location");

                    } else {

                        Integer ID = listEquipmentType.get((position - 1)).getId();

                        GetEuipmentRequest getEuipmentRequest = new GetEuipmentRequest(ID, view);

                    }

                }

                llFaultTaskLog.setVisibility(View.GONE);
                tvSelectFaultLog.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinnerEquipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                equipmentPos = position;

                if (position == 0) {
                    llFaultTaskLog.setVisibility(View.GONE);
                    tvSelectFaultLog.setVisibility(View.GONE);
                } else {
                    llFaultTaskLog.setVisibility(View.GONE);
                    tvSelectFaultLog.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (listLocationName.get(position).equalsIgnoreCase("Select location")) {

                    listEquipmentType.clear();
                    listEquipmentTypeName.clear();
                    equipmentTypeAdapter.notifyDataSetChanged();

                    listEquipment.clear();
                    listEquipmentName.clear();
                    equipmentAdapter.notifyDataSetChanged();

                } else {

                    if (equipmentResponce == null && equipmentFaultResponce == null && equipmentNotificationResponce == null) {

                        EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest(view);

                    }

                }

                llFaultTaskLog.setVisibility(View.GONE);
                tvSelectFaultLog.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        tvRepairlogDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });


        adapterMultiImages = new AdapterMultiImages(getActivity(), DialogRepairLog.this, listBitmap);

        recyclerRepairLog = (RecyclerView) view.findViewById(R.id.recycler_repair_log);
        recyclerRepairLog.setHasFixedSize(true);

        int numberOfColumns = 3;

        recyclerRepairLog.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        recyclerRepairLog.setAdapter(adapterMultiImages);

        //  loadImages();

        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_repair_log_dialog_title))) {

            int locationPos = 0;

            for (int i = 0; i < listLocation.size(); i++) {

                if (listLocation.get(i).getId() == locationId) {
                    locationPos = i;
                }

            }

            if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                spinnerLocation.setSelection(locationPos);

            } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                spinnerLocation.setSelection(locationPos + 1);

            }


            fabSelectCamera.setVisibility(View.VISIBLE);

            etAddRepairLogDes.setText("" + repairLogResponse.getDescription());
            etAddRepairLogCost.setText("" + repairLogResponse.getCost());

            etAgencyName.setText(repairLogResponse.getAgencyName());
            etContactPerson.setText(repairLogResponse.getContactPersonName());
            tvRepairlogDate.setText(convertDate(repairLogResponse.getRepairLogDate()));
            etContactPersonNumber.setText(repairLogResponse.getContactNumber());

            etMilesHours.setText(repairLogResponse.getNoMilesOrHours());

            adapterMultiImages.notifyDataSetChanged();

        } else {

            fabSelectCamera.setVisibility(View.VISIBLE);

        }


        fabSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityNavigationDrawerAdmin.backroundflag = false;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            }

        });


        if (title.equalsIgnoreCase(getResources().getString(R.string.add_repair_log_dialog_title))) {

            spinnerLocation.setEnabled(true);
            spinnerEquipmentType.setEnabled(true);
            spinnerEquipment.setEnabled(true);

            spinnerLocation.setClickable(true);
            spinnerEquipmentType.setClickable(true);
            spinnerEquipment.setClickable(true);

        } else {

            spinnerLocation.setEnabled(false);
            spinnerEquipmentType.setEnabled(false);
            spinnerEquipment.setEnabled(false);

            spinnerLocation.setClickable(false);
            spinnerEquipmentType.setClickable(false);
            spinnerEquipment.setClickable(false);

        }


        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((FragmentRepairLog) getTargetFragment()).addRepairLogNegativeClick();

            }
        });


        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_repair_log_dialog_title))) {

                    if (spinnerLocation.getSelectedItem().toString().equalsIgnoreCase("Select Location")) {

                        //Snackbar.make(v, "Please select any location", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select any location");

                    } else if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("Select asset category")) {

                        //Snackbar.make(v, "Please select any asset category", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select any asset category");

                    } /*else if (equipmentResponce == null) {

                        if (listEquipment.size() == 0) {

                            Snackbar.make(v, "Please select any asset type", Snackbar.LENGTH_LONG).show();

                        }

                    }*/ else if (spinnerEquipment.getSelectedItem().toString().equalsIgnoreCase("Select asset")) {

                        //      Snackbar.make(v, "Please select any aaset", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select any aaset");

                    } else if (etAddRepairLogCost.getText().toString().equalsIgnoreCase("")) {

                        // Snackbar.make(v, "Please add maintenance log cost", Snackbar.LENGTH_LONG).show();
                        Utilities.showSnackbar(v, "Please add maintenance log cost");

                    } else if (etAddRepairLogDes.getText().toString().equalsIgnoreCase("")) {

                        //   Snackbar.make(v, "Please add maintenance log description", Snackbar.LENGTH_LONG).show();
                        Utilities.showSnackbar(v, "Please add maintenance log description");

                    } else if (etContactPerson.getText().toString().equalsIgnoreCase("")) {

                        //  Snackbar.make(v, "Please add contact person name", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add contact person name");

                    } else if (etContactPersonNumber.getText().toString().equalsIgnoreCase("")) {

                        //     Snackbar.make(v, "Please add contact number", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add contact number");

                    } else if (tvRepairlogDate.getText().toString().equalsIgnoreCase("Select maintenance log date")) {

                        //  Snackbar.make(v, "Please select maintenance log date", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select maintenance log date");

                    }


                   /* else if (spinnerMilesHours.getSelectedItem().toString().equalsIgnoreCase("Select Miles / Hours")) {

                        Utilities.showSnackbar(v, "Please select Miles / Hours");

                    } else if (etMilesHours.getText().toString().equalsIgnoreCase("")) {

                        if (spinnerMilesHours.getSelectedItemPosition() == 1) {

                            Utilities.showSnackbar(v, "Please enter Miles");

                        } else if (spinnerMilesHours.getSelectedItemPosition() == 2) {

                            Utilities.showSnackbar(v, "Please enter Hours");

                        }

                    }*/


                    else {

                        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                        Integer locationId = null;

                        String milesHours;

                        if (spinnerMilesHours.getSelectedItemPosition() == 0) {

                            milesHours = null;

                        } else {
                            milesHours = spinnerMilesHours.getSelectedItem().toString();
                        }

                        String milesHoursValue;

                        if (etMilesHours.getText().toString().equalsIgnoreCase("")) {
                            milesHoursValue = null;
                        } else {
                            milesHoursValue = null;
                        }

                        if (equipmentResponce == null && equipmentFaultResponce == null && equipmentNotificationResponce == null) {

                            if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                                locationId = listLocation.get(spinnerLocation.getSelectedItemPosition()).getId();

                            } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                                locationId = listLocation.get((spinnerLocation.getSelectedItemPosition() - 1)).getId();

                            }

                        }

                        if (equipmentResponce == null && equipmentFaultResponce == null && equipmentNotificationResponce == null) {

                            ((FragmentRepairLog) getTargetFragment()).addRepairLogPositiveClick(locationId, listEquipmentType.get(equipmentTypePos - 1).getId(), listEquipment.get(equipmentPos - 1).getId(), etAddRepairLogDes.getText().toString(), etAddRepairLogCost.getText().toString(),
                                    listBitmap, etAgencyName.getText().toString(), etContactPerson.getText().toString(), tvRepairlogDate.getText().toString(), equipmentFaultlistResponce, equipmentNotificationlistResponce, etContactPersonNumber.getText().toString(), listFaultLogId, listTaskLogId, milesHours, milesHoursValue);

                        } else if (equipmentResponce != null) {

                            ((FragmentRepairLog) getTargetFragment()).addRepairLogPositiveClick(equipmentResponce.getLocation().getId(), equipmentResponce.getEquipmentType().getId(), equipmentResponce.getId(), etAddRepairLogDes.getText().toString(), etAddRepairLogCost.getText().toString(),
                                    listBitmap, etAgencyName.getText().toString(), etContactPerson.getText().toString(), tvRepairlogDate.getText().toString(), equipmentFaultlistResponce, equipmentNotificationlistResponce, etContactPersonNumber.getText().toString(), listFaultLogId, listTaskLogId, milesHours, milesHoursValue);

                        } else if (equipmentFaultResponce != null) {

                            ((FragmentRepairLog) getTargetFragment()).addRepairLogPositiveClick(equipmentFaultResponce.getLocation().getId(), equipmentFaultResponce.getEquipmentType().getId(), equipmentFaultResponce.getEquipment().getId(), etAddRepairLogDes.getText().toString(), etAddRepairLogCost.getText().toString(),
                                    listBitmap, etAgencyName.getText().toString(), etContactPerson.getText().toString(), tvRepairlogDate.getText().toString(), equipmentFaultlistResponce, equipmentNotificationlistResponce, etContactPersonNumber.getText().toString(), listFaultLogId, listTaskLogId, milesHours, milesHoursValue);

                        } else if (equipmentNotificationResponce != null) {

                            ((FragmentRepairLog) getTargetFragment()).addRepairLogPositiveClick(equipmentNotificationResponce.getLocationid(), equipmentNotificationResponce.getEquipmenttypeid(), equipmentNotificationResponce.getEquipmentid(), etAddRepairLogDes.getText().toString(), etAddRepairLogCost.getText().toString(),
                                    listBitmap, etAgencyName.getText().toString(), etContactPerson.getText().toString(), tvRepairlogDate.getText().toString(), equipmentFaultlistResponce, equipmentNotificationlistResponce, etContactPersonNumber.getText().toString(), listFaultLogId, listTaskLogId, milesHours, milesHoursValue);

                        }

                    }

                } else {

                    String location = repairLogResponse.getEquipment().getLocation3().getLocationName();
                    String category = spinnerEquipmentType.getSelectedItem().toString();
                    String asset = spinnerEquipment.getSelectedItem().toString();

                    if (spinnerLocation.getSelectedItem().toString().equalsIgnoreCase(repairLogResponse.getEquipment().getLocation3().getLocationName()) && spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase(equipmentTypeName)
                            && spinnerEquipment.getSelectedItem().toString().equalsIgnoreCase(equipmentName)
                            &&
                            etAddRepairLogCost.getText().toString().equalsIgnoreCase(repairLogResponse.getCost().toString()) &&
                            etAddRepairLogDes.getText().toString().equalsIgnoreCase(repairLogResponse.getDescription())
                            && etAgencyName.getText().toString().equalsIgnoreCase(repairLogResponse.getAgencyName())
                            && etContactPerson.getText().toString().equalsIgnoreCase(repairLogResponse.getContactPersonName())
                            && etContactPersonNumber.getText().toString().equalsIgnoreCase(repairLogResponse.getContactNumber())
                            && tvRepairlogDate.getText().toString().equalsIgnoreCase(repairlogDate)
                            && spinnerMilesHours.getSelectedItem().toString().equalsIgnoreCase(repairLogResponse.getUnitMilesOrHours())
                            && !imageChanges
                            ) {

                        Utilities.showSnackbar(v, getResources().getString(R.string.not_any_change));

                    } else if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("Select asset category")) {

                        Utilities.showSnackbar(v, "Please select any asset category");

                    } /*else if (listEquipment.size() == 0) {

                        Snackbar.make(v, "Please select any asset type", Snackbar.LENGTH_LONG).show();

                    }*/ else if (spinnerEquipment.getSelectedItem().toString().equalsIgnoreCase("Select asset")) {

                        Utilities.showSnackbar(v, "Please select any asset");

                    } else if (etAddRepairLogCost.getText().toString().equalsIgnoreCase("")) {

                        Utilities.showSnackbar(v, "Please add maintenance log cost");

                    } else if (etAddRepairLogDes.getText().toString().equalsIgnoreCase("")) {

                        Utilities.showSnackbar(v, "Please add maintenance log description");

                    } else if (etContactPerson.getText().toString().equalsIgnoreCase("")) {

                        //      Snackbar.make(v, "Please add contact person name", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add contact person name");

                    } else if (etContactPersonNumber.getText().toString().equalsIgnoreCase("")) {

                        //   Snackbar.make(v, "Please add contact number", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add contact number");

                    } else if (tvRepairlogDate.getText().toString().equalsIgnoreCase("Select maintenance log date")) {

                        //Snackbar.make(v, "Please select maintenance log date", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select maintenance log date");

                    }


                    /*else if (listBitmap.size() == 0) {

                        Snackbar.make(v, "Please capture and add maintenance log image", Snackbar.LENGTH_LONG).show();

                    }*/


                    else {

                        //     Log.e("equipment name", "" + listEquipment.get(equipmentPos - 1).getModalname());
                        //     Log.e("equipment id", "" + listEquipment.get(equipmentPos - 1).getId());
                        //    Log.e("equipment type id", "" + listEquipmentType.get(equipmentTypePos).getId());

                        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);


                        String milesHours;

                        if (spinnerMilesHours.getSelectedItemPosition() == 0) {
                            milesHours = null;
                        } else {
                            milesHours = spinnerMilesHours.getSelectedItem().toString();
                        }

                        String milesHoursValue;

                        if (etMilesHours.getText().toString().equalsIgnoreCase("")) {
                            milesHoursValue = null;
                        } else {
                            milesHoursValue = etMilesHours.getText().toString();
                        }

                        Integer locationId = null;

                        if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                            locationId = listLocation.get(spinnerLocation.getSelectedItemPosition()).getId();

                        } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                            locationId = listLocation.get((spinnerLocation.getSelectedItemPosition() - 1)).getId();

                        }

                        ((FragmentRepairLog) getTargetFragment()).updateRepairLogPositiveClick(locationId, listEquipmentType.get(equipmentTypePos - 1).getId(), listEquipment.get(equipmentPos - 1).getId(), etAddRepairLogDes.getText().toString(), etAddRepairLogCost.getText().toString(),
                                listBitmap, repairLogResponse, etAgencyName.getText().toString(), etContactPerson.getText().toString(), tvRepairlogDate.getText().toString(), etContactPersonNumber.getText().toString(), milesHours, milesHoursValue);

                    }


                }

            }
        });

        return dialog;
    }

    private void getTaskLog() {

        GetTaskLogRequest getTaskLogRequest = new GetTaskLogRequest(listEquipment.get(equipmentPos - 1).getId());

    }

    private void getFaultLog() {

        GetFaultLogRequest getFaultLogRequest = new GetFaultLogRequest(listEquipment.get(equipmentPos - 1).getId());

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

       /*  datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvRepairlogDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }

                }, mYear, mMonth, mDay);*/

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), null, mYear, mMonth, mDay) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {

                dismiss();

                tvRepairlogDate.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);

            }
        };

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);

    }


    private void loadImages() {

        HashMap<Integer, String> mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterMultiImages.getItemCount(); i++) {

            String imagePath = listImagePath.get(i);

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    Log.e("imagePath---", "" + listImagePath.get(i));

                    ImageRequest imageRequest = new ImageRequest(i, listImagePath.get(i));

                }

            }

        }

    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void assignFaultLog(ArrayList<Integer> listFaultLogId) {

        this.listFaultLogId = listFaultLogId;

    }

    public void assignTaskLog(ArrayList<Integer> listTaskLogId) {
        this.listTaskLogId = listTaskLogId;

    }

    private class GetEuipmentRequest implements LoaderManager.LoaderCallbacks<RepairLogEquipment> {

        private static final int LOADER_GET_EQUIPMENT = 1;

        private ProgressDialog progressDialog;

        public GetEuipmentRequest(Integer equipmentTypeId, View view) {

            Log.e("equipmentTypeId", "" + equipmentTypeId);

            DialogRepairLog.view2 = view;

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_repair_log_dialog_title))) {

                    String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                    if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {
                        locationId = listLocation.get(spinnerLocation.getSelectedItemPosition()).getId();
                    } else {
                        locationId = listLocation.get(spinnerLocation.getSelectedItemPosition() - 1).getId();
                    }
                }

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/getListUsingEquipmentType/" + URLEncoder.encode(equipmentTypeId.toString(), "UTF-8") + "/" + URLEncoder.encode(locationId.toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<RepairLogEquipment> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<RepairLogEquipment>(getActivity(), httpWrapper, RepairLogEquipment.class);

                }

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RepairLogEquipment> loader, RepairLogEquipment data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentName.clear();

                listEquipment.clear();

                for (RepairLogEquipment.RepairLogEquipmentResponse equipmentResponse : data.getResponse()) {

                    listEquipment.add(equipmentResponse);

                }

                progressDialog.dismiss();

                handler.sendEmptyMessage(101);

            } else {

                progressDialog.dismiss();
                // Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(getView(), "" + data.getMessage());

            }

        }

        @Override
        public void onLoaderReset(Loader<RepairLogEquipment> loader) {

        }
    }

    private class ImageRequest implements LoaderManager.LoaderCallbacks<ImageUrl> {

        private final int position;
        private String imagePath = null;
        private ProgressDialog progressDialog;
        //   private static final int LOADER_ADD_USER = 1;
        //    private int pos;

        public ImageRequest(int position, String imagePath) {

            this.position = position;
            this.imagePath = imagePath;

            String stringToken = null;
            String imageUrlPath = null;

            Log.e("imagePath---", "" + imagePath);

            StringTokenizer stt = new StringTokenizer(imagePath, "/\\");

            while (stt.hasMoreTokens()) {
                String token = stt.nextToken();
                if (token.contains(".PNG")) {
                    //       Log.e("token---", "" + token);
                    stringToken = token;
                }
            }


            imageUrlPath = stringToken.toString().replace(".PNG", "");


            Log.e("imageUrlPath---", "" + imageUrlPath);

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.REPAIR_LOG_EQUIPMENTS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                //     Log.e("position:---", "" + position);

                getActivity().getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(getActivity(), httpWrapper, ImageUrl.class);

                }

            } else {

                //   Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<ImageUrl> loader, ImageUrl data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                String imageBitmap = data.getResponse().getBitmap();

                String path = data.getResponse().getPath();

                int loaderId = loader.getId();

                Log.e("loaderId", "" + loaderId);

                Log.e("path", "" + path);

                Log.e("imageBitmap", "" + imageBitmap);

                adapterMultiImages.updateImage(loaderId, path, imageBitmap);

            } else {

                //     Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

    private class EquipmentTypeRequest implements LoaderManager.LoaderCallbacks<RepairLogEquipmentType> {

        private static final int LOADER_GET_EQUIPMENT_TYPE = 1;
        private ProgressDialog progressDialog;
        private View view;

        public EquipmentTypeRequest(View view) {

            this.view = view;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {
                    locationId = listLocation.get(spinnerLocation.getSelectedItemPosition()).getId();
                } else {
                    locationId = listLocation.get(spinnerLocation.getSelectedItemPosition() - 1).getId();
                }

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/getListofEquipmentTypeAssignedToUser/" + URLEncoder.encode(userid.toString(), "UTF-8") + "/" + URLEncoder.encode(locationId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(view, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(view, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RepairLogEquipmentType> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<RepairLogEquipmentType>(getActivity(), httpWrapper, RepairLogEquipmentType.class);

                }

            } else {
                progressDialog.dismiss();
                //  Snackbar.make(view, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(view, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RepairLogEquipmentType> loader, RepairLogEquipmentType data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentType.clear();
                listEquipmentTypeName.clear();

                listEquipmentTypeName.add("Select Asset Category");

                for (RepairLogEquipmentType.RepairLogEquipmentTypeResponse equipmentTypeResponse : data.getResponse()) {

                    listEquipmentType.add(equipmentTypeResponse);
                    listEquipmentTypeName.add(equipmentTypeResponse.getName());

                }

                progressDialog.dismiss();

                handler.sendEmptyMessage(103);

            } else {

                progressDialog.dismiss();

                //  Snackbar.make(view, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(view, "" + data.getMessage());

            }

        }

        @Override
        public void onLoaderReset(Loader<RepairLogEquipmentType> loader) {

        }
    }

    public static String convertDate(String repairlogDate) {

        Date convertedDate = null;

        String newFormat = "dd-MM-yyyy";
        String oldFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

        String date = null;
        try {
            date = sdf2.format(sdf1.parse(repairlogDate));
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

    private class GetFaultLogRequest implements LoaderManager.LoaderCallbacks<FaultLog> {

        private static final int LOADER_GET_EQUIPMENT = 1;

        private ProgressDialog progressDialog;

        public GetFaultLogRequest(Integer id) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "faultLog/getPendingFaultLog/" + URLEncoder.encode(id.toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);

                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<FaultLog> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<FaultLog>(getActivity(), httpWrapper, FaultLog.class);

                }

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<FaultLog> loader, FaultLog data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                // listEquipmentName.clear();

                listFaultLog.clear();

                for (FaultLog.Response response : data.getResponse()) {

                    listFaultLog.add(response);

                }

                progressDialog.dismiss();

                handler.sendEmptyMessage(104);

            } else {

                progressDialog.dismiss();

                // Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(getView(), "" + data.getMessage());

            }

        }

        @Override
        public void onLoaderReset(Loader<FaultLog> loader) {

        }

    }

    private class GetTaskLogRequest implements LoaderManager.LoaderCallbacks<TaskLog> {

        private static final int LOADER_GET_EQUIPMENT = 1;

        private ProgressDialog progressDialog;

        public GetTaskLogRequest(Integer id) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "faultLog/getPendingTaskLog/" + URLEncoder.encode(id.toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);

                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }

        @Override
        public Loader<TaskLog> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<TaskLog>(getActivity(), httpWrapper, TaskLog.class);

                }

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<TaskLog> loader, TaskLog data) {
            if (null != data && data.getStatus().equalsIgnoreCase("success")) {


                listTaskLog.clear();

                for (TaskLog.Response response : data.getResponse()) {

                    listTaskLog.add(response);

                }

                progressDialog.dismiss();

                handler.sendEmptyMessage(105);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(getView(), "" + data.getMessage());

            }

        }

        @Override
        public void onLoaderReset(Loader<TaskLog> loader) {

        }
    }
}

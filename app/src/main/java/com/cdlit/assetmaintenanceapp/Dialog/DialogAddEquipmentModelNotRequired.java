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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterMultiImages;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
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

public class DialogAddEquipmentModelNotRequired extends DialogFragment {

    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final String TAG = DialogAddEquipmentModelNotRequired.class.getSimpleName();

    private AlertDialog.Builder alertDialogBuilder;

    //  private Spinner spinnerLocation;
    //  private Spinner spinnerEquipmentType;
    //  private EditText etAddEquipmentModel;
    // private ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse> listEquipmentType;
    //private ArrayList<LocationResponse> listLocation;
    // private ArrayList<String> listLocationName;
    // private ArrayList<String> listEquipmentTypeName;
    private TextView tvAddImage;
    private FloatingActionButton fabSelectCamera;

    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> equipmentTypeAdapter;
    private Uri fileUri;
    private String filePath;
    private EditText etAddEquipmentModelDes;
    private EquipmentResponse equipmentResponse;
    private Bitmap decodedByte;
    //  private String locationName;
    private String equipmentTypeName;
    private int locationPos;
    private int equipmentTypePos;
    private List<EquipmentResponse.EquipmentResponseImages> listEquipmentImages;
    private String okString;
    private ArrayList<Bitmap> listBitmap;
    private AdapterMultiImages adapterMultiImages;
    private RecyclerView recyclerEquipmentType;
    public boolean imageChanges;
    //  private TextView tvAnnualServiceDate;
    private TextView tvManufactureDate;
    // private EditText etAddEquipmentModelServicetype;
    private EditText etAddEquipmentModelSerialno;
    private TextView tvExpiryDate;
    // private TextView tvLastServiceDate;
    //  private TextView tvDueServiceDate;
    //   private EditText etDueServiceInterval;
    //   private EditText etInspectionDuration;
    private String annualServiceDate;
    private String manufacturerDate;
    private String expiryDate;
    //    private String lastServiceDate;
    //  private String dueServiceDate;
    //  private Spinner spinnerInspectionDuration;
    private ArrayAdapter<String> indpectionDurationAdapter;
    //   private ArrayList<String> listInspectionDuration;
    private String inspectionDuration;
    private int inspectionDurationPos;
    private ArrayList<String> listImagePath;
    private HashMap<Integer, String> mapImageResponse;
    private EditText etReminderDuration;
    private EditText etRemarks;
    private String reminderDuration;

    public DialogAddEquipmentModelNotRequired() {

    }


    public static DialogAddEquipmentModelNotRequired newInstance() {

        DialogAddEquipmentModelNotRequired dialogAddEquipmentModelRequired = new DialogAddEquipmentModelNotRequired();
        return dialogAddEquipmentModelRequired;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

              /*  // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                filePath = fileUri.getPath();
                final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);*/


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
        final String title = args.getString("title");

        //   listLocation = args.getParcelableArrayList("list_location");
        //   listEquipmentType = args.getParcelableArrayList("list_equipment_type");

        listBitmap = new ArrayList<Bitmap>();
        listImagePath = new ArrayList<String>();

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

            imageChanges = false;

            equipmentResponse = (EquipmentResponse) args.getParcelable("equipment_response");

            //  locationName = equipmentResponse.getLocation().getLocationName();

            //   Log.d("locationName", "" + locationName);

            // locationName = equipmentResponse.getLocation().getLocationName();

            //    inspectionDuration = equipmentResponse.getInspectionDuration();

            Log.d("inspectionDuration", "" + inspectionDuration);

            // equipmentTypeName = equipmentResponse.getEquipmentType().getEquipmentTypeName();

            listEquipmentImages = equipmentResponse.getEquipmentImages();

            for (int i = 0; i < listEquipmentImages.size(); i++) {

                listBitmap.add(null);

                String imagePath = equipmentResponse.getEquipmentImages().get(i).getImagePath();

                Log.d("imagePath", "" + imagePath);

                listImagePath.add(imagePath);

            }

            okString = getResources().getString(R.string.update_bt_string);

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        //  listLocationName = new ArrayList<String>();

        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);


        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //  alertDialogBuilder.setTitle("" + title);


        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_equipment_model_not_required, null);

        tvExpiryDate = (TextView) view.findViewById(R.id.tv_expiry_date);

        etAddEquipmentModelDes = (EditText) view.findViewById(R.id.et_add_equipment_model_des);

        //   tvAnnualServiceDate = (TextView) view.findViewById(R.id.tv_annual_service_date);
        tvManufactureDate = (TextView) view.findViewById(R.id.tv_manufacture_date);
        // etAddEquipmentModelServicetype = (EditText) view.findViewById(R.id.et_add_equipment_model_service_type);
        etAddEquipmentModelSerialno = (EditText) view.findViewById(R.id.et_add_equipment_model_serial_no);

        etReminderDuration = (EditText) view.findViewById(R.id.et_add_reminder_duration);
        etRemarks = (EditText) view.findViewById(R.id.et_remarks);

        tvAddImage = (TextView) view.findViewById(R.id.tv_add_image);
        fabSelectCamera = (FloatingActionButton) view.findViewById(R.id.fab_select_camera);


        adapterMultiImages = new AdapterMultiImages(getActivity(), DialogAddEquipmentModelNotRequired.this, listBitmap);

        recyclerEquipmentType = (RecyclerView) view.findViewById(R.id.recycler_equipment_type);
        recyclerEquipmentType.setHasFixedSize(true);

        int numberOfColumns = 3;

        recyclerEquipmentType.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        recyclerEquipmentType.setAdapter(adapterMultiImages);

        loadImages();

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

            //    etAddEquipmentModel.setHint("Update equipment model");
            //   etAddEquipmentModelDes.setHint("Update equipment model description");
            tvAddImage.setText("Update Image");

           /* for (int i = 0; i < listLocationName.size(); i++) {
                if (listLocationName.get(i).equalsIgnoreCase(locationName)) {
                    locationPos = i;
                }
            }

            for (int i = 0; i < listInspectionDuration.size(); i++) {
                if (listInspectionDuration.get(i).equalsIgnoreCase(inspectionDuration)) {
                    inspectionDurationPos = i;
                }
            }

            for (int i = 0; i < listEquipmentTypeName.size(); i++) {
                if (listEquipmentTypeName.get(i).equalsIgnoreCase(equipmentTypeName)) {
                    equipmentTypePos = i;
                }
            }*/

            //    etAddEquipmentModel.setText(equipmentResponse.getModelNo());
            etAddEquipmentModelDes.setText(equipmentResponse.getDescription());

            if (equipmentResponse.getRemainderDuration() == null) {
                reminderDuration = "";
            } else {
                reminderDuration = equipmentResponse.getRemainderDuration().toString();
            }

            etReminderDuration.setText("" + reminderDuration);

            etRemarks.setText(equipmentResponse.getRemarks());

            //   annualServiceDate = convertDateFormate(equipmentResponse.getAnnualServiceDate());

            /*if (annualServiceDate == null || annualServiceDate.equalsIgnoreCase("")) {
                //  tvAnnualServiceDate.setText("Select Annual Service Date");

            } else {
                // tvAnnualServiceDate.setText("" + annualServiceDate);
            }*/


            manufacturerDate = convertDateFormate(equipmentResponse.getManufacturerDate());

            if (manufacturerDate == null || manufacturerDate.equalsIgnoreCase("")) {
                tvManufactureDate.setText("Select Manufacture Date");
            } else {
                tvManufactureDate.setText("" + manufacturerDate);
            }


            expiryDate = convertDateFormate(equipmentResponse.getExpiry_date());
            if (expiryDate == null || expiryDate.equalsIgnoreCase("")) {
                tvExpiryDate.setText("Select Expiry Date");
            } else {
                tvExpiryDate.setText("" + expiryDate);
            }


           /* lastServiceDate = convertDateFormate(equipmentResponse.getLast_service_date());
            if (lastServiceDate == null || lastServiceDate.equalsIgnoreCase("")) {
                tvLastServiceDate.setText("Select Last Service Date");
            } else {
                tvLastServiceDate.setText("" + lastServiceDate);
            }
*/

            /*dueServiceDate = convertDateFormate(equipmentResponse.getNextServiceDate());
            if (dueServiceDate == null || dueServiceDate.equalsIgnoreCase("")) {
                tvDueServiceDate.setText("Select Next Service Date");
            } else {
                tvDueServiceDate.setText("" + dueServiceDate);
            }
*/

            //   etDueServiceInterval.setText("" + equipmentResponse.getDue_service_interval());
            //   etInspectionDuration.setText("" + equipmentResponse.getInspection_duration());
            etAddEquipmentModelSerialno.setText(equipmentResponse.getSerialNo());
            //     etAddEquipmentModelServicetype.setText(equipmentResponse.getService_type());

            //  spinnerLocation.setSelection(locationPos);
            //  spinnerInspectionDuration.setSelection(inspectionDurationPos);


            //  spinnerEquipmentType.setSelection(equipmentTypePos);
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


       /* tvAnnualServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(0);

            }
        });*/

        tvManufactureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(1);

            }
        });


        tvExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(2);
            }
        });

        /*tvLastServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(3);
            }
        });*/

       /* tvDueServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(4);
            }
        });*/

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((FragmentEquipment) getTargetFragment()).addEquipmentNotRequiredNegativeClick();

            }
        });


        alertDialogBuilder.setNeutralButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

                    ((FragmentEquipment) getTargetFragment()).updateEquipment3NextClick();

                }

            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (title.equalsIgnoreCase(getResources().getString(R.string.update_equipment_model_dialog_title))) {

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);

        } else {

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_equipment_model_dialog_title))) {

                   /* if (tvAnnualServiceDate.getText().toString().equalsIgnoreCase("Select Annual Service Date")) {

                        annualServiceDate = null;

                    } else {

                        annualServiceDate = tvAnnualServiceDate.getText().toString();
                    }
*/
                    if (tvManufactureDate.getText().toString().equalsIgnoreCase("Select Manufacture Date")) {

                        manufacturerDate = null;

                    } else {

                        manufacturerDate = tvManufactureDate.getText().toString();

                    }

                    if (tvExpiryDate.getText().toString().equalsIgnoreCase("Select Expiry Date")) {

                        expiryDate = null;

                    } else {

                        expiryDate = tvExpiryDate.getText().toString();

                    }


                   /* if (tvLastServiceDate.getText().toString().equalsIgnoreCase("Select Last Service Date")) {

                        lastServiceDate = null;

                    } else {
                        lastServiceDate = tvLastServiceDate.getText().toString();
                    }
*/


/*
                    if (tvDueServiceDate.getText().toString().equalsIgnoreCase("Select Next Service Date")) {
                        dueServiceDate = null;

                    } else {
                        dueServiceDate = tvDueServiceDate.getText().toString();
                    }*/

                    ((FragmentEquipment) getTargetFragment()).addEquipmentNotRequiredPositiveClick(
                            etAddEquipmentModelDes.getText().toString(),
                            listBitmap, manufacturerDate, etAddEquipmentModelSerialno.getText().toString()
                            , expiryDate, /*lastServiceDate, */etReminderDuration.getText().toString(), etRemarks.getText().toString());


                } else {

                    /*  if (
                     *//*tvAnnualServiceDate.getText().toString().equalsIgnoreCase(annualServiceDate) &&*//*
                            tvManufactureDate.getText().toString().equalsIgnoreCase(manufacturerDate) &&
                                    tvExpiryDate.getText().toString().equalsIgnoreCase(expiryDate) &&
                                    tvLastServiceDate.getText().toString().equalsIgnoreCase(lastServiceDate) &&
                                 *//*   tvDueServiceDate.getText().toString().equalsIgnoreCase(dueServiceDate) &&*//*
                                    etAddEquipmentModelDes.getText().toString().equalsIgnoreCase(equipmentResponse.getDescription()) &&
*//*
                                    etAddEquipmentModelServicetype.getText().toString().equalsIgnoreCase(equipmentResponse.getService_type()) &&
*//*
                                    etAddEquipmentModelSerialno.getText().toString().equalsIgnoreCase(equipmentResponse.getSerialNo())
                                    &&
                                    etReminderDuration.getText().toString().equalsIgnoreCase(reminderDuration) &&
                                    etRemarks.getText().toString().equalsIgnoreCase(equipmentResponse.getRemarks())

                                    && !imageChanges
                            ) {

                        Snackbar.make(v, getResources().getString(R.string.not_any_change), Snackbar.LENGTH_LONG).show();

                    } */


                    // else {

                       /* if (tvAnnualServiceDate.getText().toString().equalsIgnoreCase("Select Annual Service Date")) {
                            annualServiceDate = null;
                        } else {
                            annualServiceDate = tvAnnualServiceDate.getText().toString();

                        }*/


                    if (tvManufactureDate.getText().toString().equalsIgnoreCase("Select Manufacture Date")) {
                        manufacturerDate = null;
                    } else {
                        manufacturerDate = tvManufactureDate.getText().toString();

                    }


                    if (tvExpiryDate.getText().toString().equalsIgnoreCase("Select Expiry Date")) {
                        expiryDate = null;

                    } else {
                        expiryDate = tvExpiryDate.getText().toString();

                    }
/*
                    if (tvLastServiceDate.getText().toString().equalsIgnoreCase("Select Last Service Date")) {
                        lastServiceDate = null;
                    } else {
                        lastServiceDate = tvLastServiceDate.getText().toString();
                    }

                    */

                       /* if (tvDueServiceDate.getText().toString().equalsIgnoreCase("Select Next Service Date")) {
                            dueServiceDate = null;
                        } else {
                            dueServiceDate = tvDueServiceDate.getText().toString();
                        }
*/


                    ((FragmentEquipment) getTargetFragment()).editEquipmentNotRequiredPositiveClick(
                            etAddEquipmentModelDes.getText().toString(),
                            listBitmap, manufacturerDate, etAddEquipmentModelSerialno.getText().toString(),
                            expiryDate,/* lastServiceDate, */etReminderDuration.getText().toString(), etRemarks.getText().toString());

                }

                //  }


            }
        });

        return dialog;
    }

    private void loadImages() {

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterMultiImages.getItemCount(); i++) {

            String imagePath = listImagePath.get(i);

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    DialogAddEquipmentModelNotRequired.ImageRequest imageRequest = new DialogAddEquipmentModelNotRequired.ImageRequest(i, listImagePath.get(i));

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
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
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

    private void showDatePickerDialog(final int i) {

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

                        if (i == 0) {

                            // tvAnnualServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        } else if (i == 1) {

                            tvManufactureDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        } else if (i == 2) {

                            tvExpiryDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        } else if (i == 3) {

                            tvLastServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        } else if (i == 4) {

                         //   tvDueServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();*/


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), null, mYear, mMonth, mDay) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dismiss();

                if (i == 0) {

                    // tvAnnualServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                } else if (i == 1) {

                    tvManufactureDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                } else if (i == 2) {

                    tvExpiryDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                } else if (i == 3) {

                    //      tvLastServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                } else if (i == 4) {

                    //   tvDueServiceDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                }

            }
        };

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);

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
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.EQUIPMENTS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                getActivity().getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
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

                // Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();

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

                //  Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }


}

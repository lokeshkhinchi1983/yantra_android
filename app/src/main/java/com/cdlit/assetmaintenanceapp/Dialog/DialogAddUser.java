package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.Location;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.Privilege;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeAllResponse;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeResponse;
import com.cdlit.assetmaintenanceapp.Model.UserResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;
import com.cdlit.assetmaintenanceapp.Ui.FragmentManageUser;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogAddUser extends DialogFragment {

    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2;
    private static final String TAG = DialogAddUser.class.getSimpleName();
    private static final int RESULT_LOAD = 3;
    private static final int PICK_IMAGE_REQUEST_CODE = 4;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddCategory;
    private Spinner spinnerCategory;
    private EditText etAddEquipmentType;
    private EditText etAddEquipmentDes;
    private Spinner spinnerLocation;
    private Spinner spinnerEquipmentType;
    private EditText etAddEquipmentModel;
    private ImageView imageUser;
    //  private Spinner spinnerUserType;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etEmailId;
    private EditText etEmployeeId;
    private FloatingActionButton fabAddImage;
    private AlertDialog.Builder alertDialogImage;
    private Uri fileUri;
    private String filePath;
    private String encodedString = "";
    private String img_Decodable_Str;
    private String okString;
    private UserResponse response;
    private Bitmap decodedByte;
    // private TextView tvPrivilege;
    private Handler handler;
    private List<PrivilegeResponse> privilegeResponse;
    private ArrayList<String> listPrivilege;
    private ArrayList<PrivilegeResponse> listPrivilegeResponse;
    private CheckBox cbLocationR;
    private CheckBox cbLocationW;
    private CheckBox cbLocationD;
    private CheckBox cbCategoryR;
    private CheckBox cbCategoryW;
    private CheckBox cbCategoryD;
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
    private Bitmap bitmap = null;
    private String encryptedFileName = "Enc_File.txt";
    private static String algorithm = "AES";
    static SecretKey yourKey = null;
    //   private FloatingActionButton fabRemoveImage;
    private boolean firstName;
    private boolean userName;
    private boolean password;
    private boolean confirmPassword;
    private boolean emailId;
    private Spinner spUserLocation;
    private ArrayList<LocationResponse> listLocation;
    private ArrayList<String> listLocationName;
    private ArrayAdapter<String> adapter;
    private Integer locationId;
    private String locationName;
    private TextView tvPassword;
    private TextView tvConfirmPassword;
    private ImageView imageRemove;
    private TextView tvAddImage;
    //private String imagePath;

    public static DialogAddUser newInstance() {

        DialogAddUser dialogAddEquipmentModel = new DialogAddUser();
        return dialogAddEquipmentModel;

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("DialogUserImage", "onCreateDialog");

        ActivityNavigationDrawerAdmin.backroundflag = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                imageUser.setImageResource(R.drawable.ic_no_img);

                Bitmap bitmap = null;

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, fileUri.getPath());

                } catch (IOException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                imageUser.setImageBitmap(bitmap);

                encodedString = Utilities.getEncodedString(bitmap);

                Log.e("filePath", "" + fileUri.getPath());

                File file = new File(fileUri.getPath());

                if (file.delete()) {
                    Log.e("filePath", "" + file.getName() + "is deleted");
                } else {
                    Log.e("filePath", "" + file.getName() + "is not deleted");
                }

                //  Log.e("filePath", "" + filePath);

            } else if (resultCode == RESULT_CANCELED) {


            } else {


            }

        }
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                imageUser.setImageResource(R.drawable.ic_no_img);
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                Log.e("filePath", "" + filePath.toString());
                try {
                    bitmap = BitmapFactory
                            .decodeFile(filePath);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                // Set the Image in ImageView after decoding the String
                imageUser.setImageBitmap(bitmap);

                encodedString = Utilities.getEncodedString(bitmap);

            } else if (requestCode == RESULT_CANCELED) {


            } else {


            }


        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        final String title = args.getString("title");

        if (title.equalsIgnoreCase(getResources().getString(R.string.updateUser_dialog_title))) {

            okString = getResources().getString(R.string.update_bt_string);

            response = (UserResponse) args.getParcelable("response");

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    String privilege = "";

                    for (int i = 0; i < listPrivilege.size(); i++) {

                        if (i == (listPrivilege.size() - 1)) {

                            privilege = privilege + listPrivilege.get(i);

                        } else {

                            privilege = privilege + listPrivilege.get(i) + " , ";

                        }

                    }

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


                    //category
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
                        cbCategoryD.setChecked(false);


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


                } else if (msg.what == 101) {

                    GetPrivilege getPrivilege = new GetPrivilege(userType);

                } else if (msg.what == 102) {


                    if (title.equalsIgnoreCase(getResources().getString(R.string.updateUser_dialog_title))) {

                        adapter.notifyDataSetChanged();

                        int pos = 0;

                        for (int i = 0; i < listLocation.size(); i++) {
                            if (listLocation.get(i).getId() == locationId) {
                                pos = i;
                            }
                        }

                        Log.e("pos", "" + pos);

                        spUserLocation.setSelection((pos + 1));


                    } else {

                        adapter.notifyDataSetChanged();

                    }


                }


                return false;
            }
        });

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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_user, null);

        spinnerEquipmentType = (Spinner) view.findViewById(R.id.spinner_equipment_type);

        imageUser = (ImageView) view.findViewById(R.id.image_user);

        fabAddImage = (FloatingActionButton) view.findViewById(R.id.fab_add_image);
        //  fabRemoveImage = (FloatingActionButton) view.findViewById(R.id.fab_remove_image);
        imageRemove = (ImageView) view.findViewById(R.id.image_remove);

        tvAddImage = (TextView) view.findViewById(R.id.tv_add_image);

        tvPassword = (TextView) view.findViewById(R.id.tv_password);
        tvConfirmPassword = (TextView) view.findViewById(R.id.tv_confirm_password);

        etFirstName = (EditText) view.findViewById(R.id.et_first_name);
        etLastName = (EditText) view.findViewById(R.id.et_last_name);
        etUsername = (EditText) view.findViewById(R.id.et_username);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);
        etEmailId = (EditText) view.findViewById(R.id.et_email_id);
        etEmployeeId = (EditText) view.findViewById(R.id.et_employee_id);

        cbLocationR = (CheckBox) view.findViewById(R.id.cb_location_R);
        cbLocationW = (CheckBox) view.findViewById(R.id.cb_location_W);
        cbLocationD = (CheckBox) view.findViewById(R.id.cb_location_D);

      /*  cbCategoryR = (CheckBox) view.findViewById(R.id.cb_category_R);
        cbCategoryW = (CheckBox) view.findViewById(R.id.cb_category_W);
        cbCategoryD = (CheckBox) view.findViewById(R.id.cb_category_D);*/

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

        spUserLocation = (Spinner) view.findViewById(R.id.sp_user_location);

        if (title.equalsIgnoreCase(getResources().getString(R.string.updateUser_dialog_title))) {

            //    fabRemoveImage.setVisibility(View.VISIBLE);
            //    fabAddImage.setVisibility(View.INVISIBLE);


            tvPassword.setVisibility(View.GONE);
            tvConfirmPassword.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);

            etUsername.setEnabled(false);

        } else {

            //   fabRemoveImage.setVisibility(View.INVISIBLE);
            //     fabAddImage.setVisibility(View.VISIBLE);

            etPassword.setEnabled(true);
            etConfirmPassword.setEnabled(true);

        }

      /*  fabRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                encodedString = "";
                bitmap = null;
                imageUser.setImageBitmap(bitmap);

                fabRemoveImage.setVisibility(View.INVISIBLE);
                fabAddImage.setVisibility(View.VISIBLE);

            }
        });*/


        imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                encodedString = "";
                bitmap = null;
                //  imageUser.setImageBitmap(bitmap);

                //     imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
                imageUser.setImageResource(R.drawable.ic_no_img);


                // fabRemoveImage.setVisibility(View.INVISIBLE);
                // fabAddImage.setVisibility(View.VISIBLE);

            }
        });


        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageSelectionDialog();

            }
        });


        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (encodedString != null && !encodedString.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", encodedString);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(getActivity().getSupportFragmentManager(), "dialog");

                }

            }
        });


        final List<String> listUserType = new ArrayList<String>();
        listUserType.add("Select User Type");
        listUserType.add("Admin");
        listUserType.add("User");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listUserType);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (title.equalsIgnoreCase(getResources().getString(R.string.updateUser_dialog_title))) {

            tvAddImage.setText("Update Image");

            etFirstName.setText(response.getFirstName());
            etLastName.setText(response.getLastName());
            etUsername.setText(response.getUserName());
            etPassword.setText(response.getPassword());
            etConfirmPassword.setText(response.getPassword());
            etEmailId.setText(response.getEmailId());
            etEmployeeId.setText(response.getEmployee_no());

            encodedString = response.getImage_bitmap();

           // imagePath = response.getImage_path();

          //  Log.e("imagePath", imagePath);

            if (encodedString == null || encodedString.equalsIgnoreCase("")) {

                encodedString = "";

                bitmap = null;

                //  imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
                imageUser.setImageResource(R.drawable.ic_no_img);

            } else {

                byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                imageUser.setImageBitmap(bitmap);

               // Glide.with(getActivity()).load(response.getImage_path()).into(imageUser);

            }


          // rakesh glide

           /* if (imagePath == null || imagePath.equalsIgnoreCase("")) {

                //  encodedString = "";

                //  bitmap = null;

                //  imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
                imageUser.setImageResource(R.drawable.ic_no_img);

            } else {

                //  byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

                //   bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                //  imageUser.setImageBitmap(bitmap);

                Glide.with(getContext()).load(imagePath).into(imageUser);

            }*/

            // rakesh glide

            // imageUser.setImageBitmap(bitmap);

            locationId = response.getLocation().getId();

            locationName = response.getLocation().getLocationName();

            Log.e("locationId", "" + locationId);

            Log.e("locationName", "" + locationName);


        } else {

            tvAddImage.setText("Add Image");

            // imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
            imageUser.setImageResource(R.drawable.ic_no_img);

        }


        listLocation = new ArrayList<LocationResponse>();

        listLocationName = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocationName);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spUserLocation.setAdapter(adapter);


        spUserLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {


                } else if (!hasFocus) {

                    if (etFirstName.getText().toString().equalsIgnoreCase("")) {
                        etFirstName.setError("Please enter first name");
                        //  Snackbar.make(v, "Please enter first name", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter first name");
                        firstName = false;
                    } else {
                        firstName = true;
                    }

                }

            }
        });


        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    }

                } else if (!hasFocus) {


                }

            }
        });

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    }

                } else if (!hasFocus) {

                    if (etUsername.getText().toString().equalsIgnoreCase("")) {
                        etUsername.setError("Please enter personnel name");
                        //   Snackbar.make(v, "Please enter personnel name", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter personnel name");
                        userName = false;
                    } else {
                        userName = true;

                    }

                }

            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    } else if (!userName) {

                        etUsername.requestFocus();

                    }

                } else if (!hasFocus) {

                    if (etPassword.getText().toString().equalsIgnoreCase("")) {

                        etPassword.setError("Please enter password");
                        //     Snackbar.make(v, "Please enter password", Snackbar.LENGTH_SHORT).show();
                        Utilities.showSnackbar(v, "Please enter password");

                        password = false;

                    } else {

                        password = true;
                    }

                }

            }
        });


        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    } else if (!userName) {

                        etUsername.requestFocus();

                    } else if (!password) {

                        etPassword.requestFocus();

                    }

                } else if (!hasFocus) {

                    if (etConfirmPassword.getText().toString().equalsIgnoreCase("")) {

                        etConfirmPassword.setError("Please enter confirm password");
                        //   Snackbar.make(v, "Please enter confirm password", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter confirm password");

                        confirmPassword = false;

                    } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                        etPassword.setError("Password and confirm password must be same");
                        etConfirmPassword.setError("Password and confirm password must be same");
                        //  Snackbar.make(v, "Password and confirm password must be same", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Password and confirm password must be same");
                        //    password = false;
                        //   confirmPassword = false;

                    } else if (!isValidPasswor(etPassword.getText().toString())) {



                        etPassword.setError("Password contains mim 4 character");
                        etConfirmPassword.setError("Password contains mim 4 character");
                        //      Snackbar.make(v, "Password contains mim 4 character", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Password contains mim 4 character");
                        confirmPassword = false;

                    } else {

                        // password = true;
                        confirmPassword = true;

                    }


                }

            }
        });

        etEmailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    } else if (!userName) {

                        etUsername.requestFocus();

                    } else if (!password) {

                        etPassword.requestFocus();

                    } else if (!confirmPassword) {

                        etConfirmPassword.requestFocus();

                    }

                } else if (!hasFocus) {

                    if (!etEmailId.getText().toString().equals("")) {

                        if (!isValidEmail(etEmailId.getText().toString())) {

                            etEmailId.setError("Please enter valid email id (eg: example@email.com)");
                            //Snackbar.make(v, "Please enter valid email id (eg: example@email.com)", Snackbar.LENGTH_SHORT).show();
                            Utilities.showSnackbar(v, "Please enter valid email id (eg: example@email.com)");

                            emailId = false;
                        } else {

                            emailId = true;

                        }

                    }

                }

            }
        });

        etEmployeeId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    if (!firstName) {

                        etFirstName.requestFocus();

                    } else if (!userName) {

                        etUsername.requestFocus();

                    } else if (!password) {

                        etPassword.requestFocus();

                    } else if (!confirmPassword) {

                        etConfirmPassword.requestFocus();

                    }


                } else if (!hasFocus) {


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

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //   Window window = dialog.getWindow();
        //   window.setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, 800);

        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.addUser_dialog_title))) {

                   /* if (bitmap == null) {

                        Snackbar.make(v, "Please capture and add user image", Snackbar.LENGTH_LONG).show();

                    }*/

                    if (spUserLocation.getSelectedItem().toString().equalsIgnoreCase("Select location")) {
                        // etFirstName.setError("Enter first name");
                        // Snackbar.make(v, "Select location", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Select location");

                    } else if (etFirstName.getText().toString().equalsIgnoreCase("")) {

                        etFirstName.setError("Please enter first name");
                        // Snackbar.make(v, "Please enter first name", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter first name");

                    } else if (etUsername.getText().toString().equalsIgnoreCase("")) {

                        etUsername.setError("Please enter personnel name");
                        //  Snackbar.make(v, "Please enter personnel name", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter personnel name");

                    } else if (etPassword.getText().toString().equalsIgnoreCase("")) {

                        etPassword.setError("Please enter password");
                        //  Snackbar.make(v, "Please enter password", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter password");


                    } else if (etConfirmPassword.getText().toString().equalsIgnoreCase("")) {

                        etConfirmPassword.setError("Please enter confirm password");
                        //   Snackbar.make(v, "Please enter confirm password", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter confirm password");

                    } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                        etPassword.setError("Password and confirm password must be same");
                        etConfirmPassword.setError("Password and confirm password must be same");

                        //    Snackbar.make(v, "Password and confirm password must be same", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Password and confirm password must be same");

                    } else if (!isValidPasswor(etPassword.getText().toString())) {

                        etPassword.setError("Password contains min 4 character");
                        etConfirmPassword.setError("Password contains min 4 character");

//                        Snackbar.make(v, "Password contains min 4 character", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Password contains min 4 character");

                    }


                    /*else if (etEmailId.getText().toString().equalsIgnoreCase("")) {

                        etEmailId.setError("Enter email id");
                        Snackbar.make(v, "Enter email id", Snackbar.LENGTH_SHORT).show();

                                        Please enter

                    } */


                    else if (!etEmailId.getText().toString().equals("") && !isValidEmail(etEmailId.getText().toString())) {

                        etEmailId.setError("Please enter valid email id (eg: example@email.com)");
                        //  Snackbar.make(v, "Please enter valid email id (eg: example@email.com)", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter valid email id (eg: example@email.com)");

                    } else {

                        String encryptedPassword = getEncryptedPassword(etPassword.getText().toString());

                        Log.e("encryptedPassword", "" + encryptedPassword);

                        int pos = spUserLocation.getSelectedItemPosition();

                        Integer loactionId;

                        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                        if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {
                            loactionId = listLocation.get((pos - 1)).getId();
                        } else {
                            loactionId = listLocation.get(pos).getId();
                        }

                        Log.e("loactionId", "" + loactionId);

                        Log.e("loactionNamme", "" + spUserLocation.getSelectedItem().toString());

                       /* if (bitmap == null) {
                            encodedString = "";
                        } else {
                            encodedString = getEncodedString(bitmap);
                        }*/

                        ((FragmentManageUser) getTargetFragment()).addUserPositiveClick(encodedString, etFirstName.getText().toString(), etLastName.getText().toString(),
                                etUsername.getText().toString(), encryptedPassword, encryptedPassword, etEmailId.getText().toString(), etEmployeeId.getText().toString(), etPassword.getText().toString(), response, loactionId);

                    }


                } else {

                    String encodedString1 = response.getImage_bitmap();

                    if (encodedString1 == null) {
                        encodedString1 = "";
                    }

                    /*if (bitmap == null) {
                        encodedString = "";
                    } else {
                        encodedString = getEncodedString(bitmap);
                    }*/

                    if (encodedString.equals(encodedString1) &&
                            locationName.equalsIgnoreCase(spUserLocation.getSelectedItem().toString())
                            && etFirstName.getText().toString().equals(response.getFirstName()) &&
                            etLastName.getText().toString().equals(response.getLastName()) &&
                            etEmailId.getText().toString().equals(response.getEmailId()) &&
                            etEmployeeId.getText().toString().equals(response.getEmployee_no())) {

                        //Snackbar.make(v, getResources().getString(R.string.not_any_change), Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, getResources().getString(R.string.not_any_change));

                    } /*else if (bitmap == null) {

                        Snackbar.make(v, "Please capture and add user image", Snackbar.LENGTH_LONG).show();

                    }*/ else if (spUserLocation.getSelectedItem().toString().equalsIgnoreCase("Select location")) {
                        // etFirstName.setError("Enter first name");
                        //  Snackbar.make(v, "Select location", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Select location");

                    } else if (etFirstName.getText().toString().equalsIgnoreCase("")) {

                        etFirstName.setError("Please enter first name");

                    } else if (etUsername.getText().toString().equalsIgnoreCase("")) {

                        etUsername.setError("Please enter personnel name");

                    } /*else if (etEmailId.getText().toString().equalsIgnoreCase("")) {

                        etEmailId.setError("Enter email id");

                    }*/ else if (!etEmailId.getText().toString().equals("") && !isValidEmail(etEmailId.getText().toString())) {

                        //     Snackbar.make(v, "Please enter valid email id (eg: example@email.com)", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please enter valid email id (eg: example@email.com)");

                    } else {

                       /* if (bitmap == null) {
                            encodedString = "";
                        } else {
                            encodedString = getEncodedString(bitmap);
                        }*/


                        int pos = spUserLocation.getSelectedItemPosition();

                        Integer loactionId;

                        String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                        if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {
                            loactionId = listLocation.get((pos - 1)).getId();
                        } else {
                            loactionId = listLocation.get(pos).getId();
                        }


                        Log.e("loactionId", "" + loactionId);

                        Log.e("loactionNamme", "" + spUserLocation.getSelectedItem().toString());

                        ((FragmentManageUser) getTargetFragment()).updateUserPositiveClick(encodedString, etFirstName.getText().toString(), etLastName.getText().toString(),
                                etUsername.getText().toString(), etEmailId.getText().toString(), etEmployeeId.getText().toString(),
                                response, loactionId);

                    }

                }

            }
        });


       /* listLocation = new ArrayList<LocationResponse>();

        listLocationName = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocationName);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spUserLocation.setAdapter(adapter);*/


        GetLocationRequest getLocationRequest = new GetLocationRequest();
        return dialog;
    }

    /*private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        encodedString = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedString;
    }*/


    private String getEncryptedPassword(String password) {

        // Create MD5 Hash
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        digest.update(password.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer MD5Hash = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String h = Integer.toHexString(0xFF & messageDigest[i]);
            while (h.length() < 2)
                h = "0" + h;
            MD5Hash.append(h);
        }

        return MD5Hash.toString();

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public boolean isValidPasswor(CharSequence password) {

        if (password.length() >= 4) {

            return true;

        } else {
            return false;

        }

    }

    public boolean isValidPasswor1(CharSequence password) {

/*

        (			# Start of group
                (?=.*\d)		#   must contains one digit from 0-9
        (?=.*[a-z])		#   must contains one lowercase characters
                (?=.*[A-Z])		#   must contains one uppercase characters
                (?=.*[@#$%])		#   must contains one special symbols in the list "@#$%"
                .		#     match anything with previous condition checking
        {6,20}	#        length at least 6 characters and maximum of 20
)			# End of group
*/


        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        pattern = Pattern.compile(PASSWORD_PATTERN);

        /**
         * Validate password with regular expression
         * @param password password for validation
         * @return true valid password, false invalid password
         */
        matcher = pattern.matcher(password);
        return matcher.matches();

    }


    private void openImageSelectionDialog() {

        alertDialogImage = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        alertDialogImage.setTitle("" + "Select Image");

        String[] listImageSelection = {"Select From Camera", "Select From Gallary"};

        alertDialogImage.setItems(listImageSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {

                    dialog.dismiss();
                    ActivityNavigationDrawerAdmin.backroundflag = false;
                    openCamera();

                } else {

                    dialog.dismiss();
                    ActivityNavigationDrawerAdmin.backroundflag = false;
                    openGallary();

                }

            }

        });

        final AlertDialog dialog = alertDialogImage.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;
        dialog.show();

    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    private void openGallary() {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);

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
        } /*else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }*/ else {
            return null;
        }

        return mediaFile;

    }


    private class GetPrivilege implements LoaderManager.LoaderCallbacks<Privilege> {

        private final String type;
        private static final int LOADER_USER_PRIVILEGE = 1;
        private ProgressDialog progressDialog;


        public GetPrivilege(String type) {

            this.type = type;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "action/" + URLEncoder.encode(type.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_USER_PRIVILEGE, args, this);

            } else {

                progressDialog.dismiss();

                //    Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();


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
                //   .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Privilege> loader, Privilege data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listPrivilege = new ArrayList<String>();
                listPrivilege.clear();

                listPrivilegeResponse = new ArrayList<PrivilegeResponse>();
                listPrivilegeResponse.clear();


                for (int i = 0; i < data.getResponse().size(); i++) {

                    listPrivilegeResponse.add(data.getResponse().get(i));

                }

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listPrivilege.add(data.getResponse().get(i).getAction());

                }

                handler.sendEmptyMessage(100);

            } else {

                //  Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(getView(), "" + data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Privilege> loader) {

        }
    }


    private class GetLocationRequest implements LoaderManager.LoaderCallbacks<Location> {

        private static final int LOADER_GET_LOCATION = 1;

        private ProgressDialog progressDialog;

        public GetLocationRequest() {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);

            } else {

                progressDialog.dismiss();

                //  Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<Location>(getActivity(), httpWrapper, Location.class);

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
        public void onLoadFinished(Loader<Location> loader, Location data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listLocation.clear();
                listLocationName.clear();

                String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);
                String location_Id = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);

                if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {
                    listLocationName.add("Select Location");
                }

                for (LocationResponse location : data.getResponse()) {

                    if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                        if (location.getId() == Integer.parseInt(location_Id)) {

                            listLocation.add(location);
                            listLocationName.add(location.getLocationName());

                        }

                    } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                        listLocation.add(location);
                        listLocationName.add(location.getLocationName());

                    }

                }

                handler.sendEmptyMessage(102);

            } else {

                //    Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(getView(), "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {


        }


    }


}

package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cdlit.assetmaintenanceapp.Dialog.DialogExit;
import com.cdlit.assetmaintenanceapp.Model.Login;
import com.cdlit.assetmaintenanceapp.Model.LoginPrivilege;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A login screen that offers login via email/password.
 */


public class ActivityLogin extends AppCompatActivity {

    private static final int LOADER_LOGIN_DETAIL = 1;
    private Button btLogIn;
    private EditText etUsername;
    private EditText etPassword;
    private Handler handler;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = ActivityLogin.class.getSimpleName();
    private String userName;
    private String userEmail;
    //    private String userType;
    private String userid;
    private List<LoginPrivilege> listPrivilege;
    private ArrayList<String> listPrivilegeItem;
    private String bitmap;
    private String userType;
    private String userImagePath;
    private String userfistlastName;
    private Button btChangePass;

    @Override
    protected void onRestart() {
        super.onRestart();
        //  etUsername.setFocusableInTouchMode(true);
        //  etUsername.setFocusable(true);
        //  etUsername.requestFocus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Crashlytics.log("" + this.getLocalClassName());

        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.et_username);

        etPassword = (EditText) findViewById(R.id.et_password);

        etUsername.setFocusable(true);

        btLogIn = (Button) findViewById(R.id.bt_log_in);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        //   btChangePass = (Button) findViewById(R.id.bt_change_pass);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 100) {

                    etPassword.setText("");
                    etUsername.setText("");

                    Intent intent = new Intent(getBaseContext(), ActivityNavigationDrawerAdmin.class);
                    intent.putExtra("username", "" + userName);
                    intent.putExtra("useremail", "" + userEmail);
                    //       intent.putExtra("user_image_path", "" + userImagePath);
                    startActivity(intent);

                }

            }
        };

        userName = Utilities.getStringFromSharedPreferances(ActivityLogin.this, Global.KEY_USER_NAME, null);

        userfistlastName = Utilities.getStringFromSharedPreferances(ActivityLogin.this, Global.KEY_USER_FIRST_LAST_NAME, null);

        userEmail = Utilities.getStringFromSharedPreferances(ActivityLogin.this, Global.KEY_EMAIL, null);

        bitmap = Utilities.getStringFromSharedPreferances(ActivityLogin.this, Global.KEY_USER_IMAGE, null);


        if (userName != null) {

            Intent intent = new Intent(this, ActivityNavigationDrawerAdmin.class);
            intent.putExtra("username", "" + userName);
            intent.putExtra("useremail", "" + userEmail);

            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);

        }


        btLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = false;

                if (etUsername.getText().toString().equalsIgnoreCase("")) {
                    flag = true;
                    etUsername.setError("Enter username");
                }
                if (etPassword.getText().toString().equalsIgnoreCase("")) {
                    flag = true;
                    etPassword.setError("Enter password");
                }

                if (!flag) {

                    userLogin(etUsername.getText().toString(), etPassword.getText().toString());

                }

            }
        });



      /*  btChangePass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ActivityChangePassword.class);

                startActivity(intent);


            }
        });*/


    }

    @AddTrace(name = "UserLogin", enabled = true)
    private void userLogin(String s, String s1) {

        new UserLogin(s, s1);

    }

    @Override
    public void onBackPressed() {
        //     super.onBackPressed();

        DialogExit dialogExit = DialogExit.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.exit_dialog_title));
        bundle.putString("msg", getResources().getString(R.string.exit_dialog_msg));

        dialogExit.setArguments(bundle);
        dialogExit.show(getSupportFragmentManager(), "dialog");
    }

    public void exitPositiveClick() {


        finishAffinity();

    }

    private class UserLogin implements LoaderManager.LoaderCallbacks<Login> {

        private ProgressDialog progressDialog;

        public UserLogin(String userid, String password) {

            progressDialog = Utilities.startProgressDialog(ActivityLogin.this);

            if (Utilities.isNetworkAvailable(ActivityLogin.this)) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityLogin.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "login";
                args.putString(Global.ARGS_URI, url);
                args.putString(Global.ARGS_USER_NAME, userid);
                args.putString(Global.ARGS_PASSWORD, password);

                getSupportLoaderManager().restartLoader(LOADER_LOGIN_DETAIL, args, this);

            } else {

                progressDialog.dismiss();
                //        Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //           .show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));
            }

        }


        @Override
        public Loader<Login> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(ActivityLogin.this)) {
                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("username", "" + args.get(Global.ARGS_USER_NAME));
                    hashMap.put("password", "" + args.get(Global.ARGS_PASSWORD));
                    hashMap.put("deviceid", "" + Utilities.getDeviceSerialNumber());

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(hashMap);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<Login>(ActivityLogin.this, httpWrapper, Login.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Login> loader, Login login) {

            if (null != login && login.getStatus().equalsIgnoreCase("success")) {

                userName = login.getLoginResponse().getUsername();
                userfistlastName = login.getLoginResponse().getFirstname() + " " + login.getLoginResponse().getLastname();
                userEmail = login.getLoginResponse().getEmailid();
                userid = login.getLoginResponse().getUserid();
                bitmap = login.getLoginResponse().getBitmap();
                userType = login.getLoginResponse().getUsertype();
                userImagePath = login.getLoginResponse().getImagePath();

                Log.e("userType", "" + userType);

                listPrivilege = login.getLoginResponse().getPrivilege();

                listPrivilegeItem = new ArrayList<String>();

                for (LoginPrivilege privilege : listPrivilege) {
                    listPrivilegeItem.add(privilege.getAction());
                }

                Integer userLocationId = login.getLoginResponse().getLocation().getId();

                String locatinName = login.getLoginResponse().getLocation().getLocationName();

                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_NAME, userName);
                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_FIRST_LAST_NAME, userfistlastName);

                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_EMAIL, userEmail);
                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_ID, userid);

                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_IMAGE, bitmap);
                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_IMAGE_PATH, userImagePath);

                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_TYPE, userType);
                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_LOCATION_ID, userLocationId.toString());

                Utilities.setStringInSharedPreferances(ActivityLogin.this, Global.KEY_USER_LOCATION_NAME, locatinName);


                Set<String> setPrivilegeItem = new HashSet<String>(listPrivilegeItem);

                Utilities.setStringArrayInSharedPreferances(ActivityLogin.this, Global.KEY_PRIVILEGE_ID, setPrivilegeItem);

                handler.sendEmptyMessage(100);

            } else {

                //    Snackbar.make(coordinatorLayout, "" + login.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, login.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Login> loader) {

        }


    }


}


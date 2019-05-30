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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class ActivityChangePassword extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private Button btSubmit;
    //  private EditText etUsername;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = ActivityChangePassword.class.getSimpleName();
    private Handler handler;
    private EditText etConfirmPassword;
    private static final int LOADER_LOGOUT_DETAIL = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        finish();

        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Crashlytics.log("" + this.getLocalClassName());

        setContentView(R.layout.activity_change_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etOldPassword = (EditText) findViewById(R.id.et_old_password);

        etNewPassword = (EditText) findViewById(R.id.et_new_password);

        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);

        btSubmit = (Button) findViewById(R.id.bt_submit);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 100) {

                    //   Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                    //   startActivity(intent);


                    new UserLogOut();

                }

                if (msg.what == 101) {

                    Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                    startActivity(intent);


                    //   new UserLogOut();

                }
            }
        };


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etOldPassword.getText().toString().equalsIgnoreCase("")) {

                    etOldPassword.setError("Please enter currrent password");
                    Utilities.showSnackbar(v, "Please enter currrent password");

                } else if (etNewPassword.getText().toString().equalsIgnoreCase("")) {

                    etNewPassword.setError("Please enter new password");
                    Utilities.showSnackbar(v, "Please enter new password");

                } else if (!isValidPasswor(etNewPassword.getText().toString())) {

                    etNewPassword.setError("New Password contains mim 4 character");
                    Utilities.showSnackbar(v, "New Password contains mim 4 character");

                } else if (etConfirmPassword.getText().toString().equalsIgnoreCase("")) {

                    etConfirmPassword.setError("Please enter confirm password");
                    Utilities.showSnackbar(v, "Please enter confirm password");

                } else if (!isValidPasswor(etConfirmPassword.getText().toString())) {

                    etNewPassword.setError("Confirm Password contains mim 4 character");
                    Utilities.showSnackbar(v, "Confirm Password contains mim 4 character");

                } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                    etConfirmPassword.setError("New password and confirm password must be same");
                    Utilities.showSnackbar(v, "New password and confirm password must be same");

                } else if (etOldPassword.getText().toString().equals(etNewPassword.getText().toString())) {

                    etConfirmPassword.setError("Old password and new password should not be same");
                    Utilities.showSnackbar(v, "Old password and new password should not be same");

                } else {

                    changePassWord();

                }

            }
        });

    }

    public boolean isValidPasswor(CharSequence password) {

        if (password.length() >= 4) {

            return true;

        } else {
            return false;

        }

    }

    private void changePassWord() {

        String userName = Utilities.getStringFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_NAME, null);

        new UserChangePassword(userName, etOldPassword.getText().toString(), etNewPassword.getText().toString());

    }

    private class UserChangePassword implements LoaderManager.LoaderCallbacks<RestResponse> {

        private ProgressDialog progressDialog;
        private static final int LOADER_LOGIN_DETAIL = 1;

        public UserChangePassword(String userid, String oldPassword, String newPassword) {

            progressDialog = Utilities.startProgressDialog(ActivityChangePassword.this);

            if (Utilities.isNetworkAvailable(ActivityChangePassword.this)) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityChangePassword.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/changePassword";
                args.putString(Global.ARGS_URI, url);
                args.putString(Global.ARGS_USER_NAME, userid);
                args.putString(Global.ARGS_OLD_PASSWORD, oldPassword);
                args.putString(Global.ARGS_NEW_PASSWORD, newPassword);

                getSupportLoaderManager().restartLoader(LOADER_LOGIN_DETAIL, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityChangePassword.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("username", "" + args.get(Global.ARGS_USER_NAME));
                    hashMap.put("oldPassword", "" + args.get(Global.ARGS_OLD_PASSWORD));
                    hashMap.put("newPassword", "" + args.get(Global.ARGS_NEW_PASSWORD));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(hashMap);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(ActivityChangePassword.this, httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse response) {

            if (null != response && response.getStatus().equalsIgnoreCase("success")) {

                Utilities.showSnackbar(coordinatorLayout, response.getMessage());

                handler.sendEmptyMessageDelayed(100, 3000);

            } else {

                Utilities.showSnackbar(coordinatorLayout, response.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {


        }


    }

    private class UserLogOut implements LoaderManager.LoaderCallbacks<RestResponse> {

        public UserLogOut() {

            progressDialog = Utilities.startProgressDialog(ActivityChangePassword.this);

            if (Utilities.isNetworkAvailable(ActivityChangePassword.this)) {

                String userId = Utilities.getStringFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_NAME, null);

                progressDialog.setMessage("Please wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityChangePassword.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "logout";
                args.putString(Global.ARGS_URI, url);
                args.putString(Global.ARGS_USER_NAME, userId);

                getSupportLoaderManager().restartLoader(LOADER_LOGOUT_DETAIL, args, this);

            } else {

                progressDialog.dismiss();

                handler.sendEmptyMessage(100);

            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityChangePassword.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("deviceid", "" + Utilities.getDeviceSerialNumber());
                    hashMap.put("username", "" + args.get(Global.ARGS_USER_NAME));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(hashMap);
                    Log.i(TAG, "json String : " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(ActivityChangePassword.this, httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse loginResponse) {

            if (null != loginResponse && loginResponse.getStatus().equalsIgnoreCase("success")) {

                Utilities.showSnackbar(coordinatorLayout, loginResponse.getMessage());

                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_NAME);
                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_FIRST_LAST_NAME);

                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_EMAIL);
                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_ID);

                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_IMAGE);
                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_IMAGE_PATH);

                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_TYPE);
                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_LOCATION_ID);

                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_USER_LOCATION_NAME);
                Utilities.removeObjectFromSharedPreferances(ActivityChangePassword.this, Global.KEY_PRIVILEGE_ID);

                handler.sendEmptyMessage(101);

            } else {

                Utilities.showSnackbar(coordinatorLayout, "" + loginResponse.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }

    }

}

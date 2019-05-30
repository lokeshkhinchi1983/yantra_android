package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterMenuItem;
import com.cdlit.assetmaintenanceapp.Dialog.DialogExit;
import com.cdlit.assetmaintenanceapp.Dialog.DialogLogout;
import com.cdlit.assetmaintenanceapp.Dialog.DialogUserImage;
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.Notification;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.NotificationUtils;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.RefWatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.StringTokenizer;

public class ActivityNavigationDrawerAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // To support vector drawable to lower version of andorid
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int LOADER_LOGOUT_DETAIL = 1;
    private static final String TAG = ActivityNavigationDrawerAdmin.class.getSimpleName();
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    // private TextView tvUsername;
    private TextView tvUseremail;
    private String userName;
    private String userEmail;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navView;
    //  private String userType;
    private Handler handler;
    private Set<String> listPrivilege;
    private ImageView imgUser;
    private String encodedString;
    private DialogUserImage dialogUserImage;
    // private TextView tv_user_name;
    private RecyclerView recyclerMenuItem;
    private LinkedHashMap<String, Integer> mapMenu;
    private AdapterMenuItem adapterMenuItem;
    private LinearLayout llMenuItem;
    private ArrayList<String> listKey;
    private TextView tvUsertype;
    private String userType;
    private String user_image_path;
    private FirebaseAnalytics firebaseAnalytics;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout drawer;
    private TextView tvName;
    private String firstLastName;
    public static boolean backroundflag;
    private String userLocationName;
    //creating fragment object
    Fragment fragment = null;
    private ImageView imgNotification;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onDestroy() {

        Log.e("ActivityNavigationDrawerAdmin:", "onDestroy");
        super.onDestroy();

        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        Log.e("ActivityNavigationDrawerAdmin: ", "onSaveInstanceState");

        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onStop() {

        Log.e("ActivityNavigationDrawer", "onStop");

        super.onStop();

        if (backroundflag == true) {

/*

            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_FIRST_LAST_NAME);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_EMAIL);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_TYPE);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_PRIVILEGE_ID);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE);

            finishAffinity();

*/

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.e("ActivityNavigationDrawerAdmin: ", "onRestart");

        if (fragment instanceof FragmentEquipmentTypeList) {

            ((FragmentEquipmentTypeList) fragment).onRestart();

        }
    }


    @Override
    protected void onResume() {

        Log.e("ActivityNavigationDrawerAdmin: ", "onResume");

        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Global.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Global.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {

        Log.e("ActivityNavigationDrawerAdmin: ", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

     /*   if (isFinishing()) {

            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_FIRST_LAST_NAME);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_EMAIL);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_TYPE);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_PRIVILEGE_ID);
            Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE);

            finishAffinity();

        }*/

        super.onPause();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_navigation_drawer_admin);

        Crashlytics.log("" + this.getLocalClassName());

        setFireBaseAnalytics();

        setFireBasePushNotification();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        navView = (NavigationView) findViewById(R.id.nav_view);

        llMenuItem = (LinearLayout) findViewById(R.id.ll_menu_item);

        imgNotification = (ImageView) findViewById(R.id.img_notification);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Home clicked", "Home clicked");

            }
        });

        backroundflag = true;

        // tv_user_name = (TextView) findViewById(R.id.tv_user_name);

        //  View view = navView.getHeaderView(0);

        //  navView.inflateMenu(R.menu.menu_admin_drawer);

        recyclerMenuItem = (RecyclerView) findViewById(R.id.recycler_menu_item);

       /* imgUser = (ImageView) view.findViewById(R.id.img_user);

        tvUsername = (TextView) view.findViewById(R.id.tv_username);

        tvUseremail = (TextView) view.findViewById(R.id.tv_useremail);*/

        imgUser = (ImageView) findViewById(R.id.img_user);

        //  tvUsername = (TextView) findViewById(R.id.tv_username);

        tvName = (TextView) findViewById(R.id.tv_name);

        tvUseremail = (TextView) findViewById(R.id.tv_useremail);

        tvUsertype = (TextView) findViewById(R.id.tv_usertype);

        //    user_image_path = getIntent().getStringExtra("user_image_path");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if (fragment instanceof FragmentLocation) {

                    if (((FragmentLocation) fragment).actionMode != null) {
                        ((FragmentLocation) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentEquipment) {

                    if (((FragmentEquipment) fragment).actionMode != null) {
                        ((FragmentEquipment) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentManageUser) {

                    if (((FragmentManageUser) fragment).actionMode != null) {
                        ((FragmentManageUser) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentRepairLog) {

                    if (((FragmentRepairLog) fragment).actionMode != null) {
                        ((FragmentRepairLog) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentEquipmentType) {

                    if (((FragmentEquipmentType) fragment).actionMode != null) {
                        ((FragmentEquipmentType) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentEquipmentCheckList) {

                    if (((FragmentEquipmentCheckList) fragment).actionMode != null) {
                        ((FragmentEquipmentCheckList) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentEquipmentToUser) {

                    if (((FragmentEquipmentToUser) fragment).actionMode != null) {
                        ((FragmentEquipmentToUser) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentFaultLog) {

                    if (((FragmentFaultLog) fragment).actionMode != null) {
                        ((FragmentFaultLog) fragment).actionMode.finish();
                    }

                } else if (fragment instanceof FragmentNotification) {

                    if (((FragmentNotification) fragment).actionMode != null) {
                        ((FragmentNotification) fragment).actionMode.finish();
                    }

                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                //  getSupportActionBar().setDisplayShowHomeEnabled(true);

                if (fragment instanceof FragmentLocation) {

                    if (((FragmentLocation) fragment).searchItem != null) {
                        ((FragmentLocation) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentEquipment) {

                    if (((FragmentEquipment) fragment).searchItem != null) {
                        ((FragmentEquipment) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentManageUser) {

                    if (((FragmentManageUser) fragment).searchItem != null) {
                        ((FragmentManageUser) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentRepairLog) {

                    if (((FragmentRepairLog) fragment).searchItem != null) {
                        ((FragmentRepairLog) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentEquipmentType) {

                    if (((FragmentEquipmentType) fragment).searchItem != null) {
                        ((FragmentEquipmentType) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentEquipmentCheckList) {

                    if (((FragmentEquipmentCheckList) fragment).searchItem != null) {
                        ((FragmentEquipmentCheckList) fragment).searchItem.setVisible(false);
                    }

                } else if (fragment instanceof FragmentEquipmentToUser) {

                    if (((FragmentEquipmentToUser) fragment).searchItem != null) {
                        ((FragmentEquipmentToUser) fragment).searchItem.setVisible(false);
                    }

                }

                /*else if (fragment instanceof FragmentFaultLog) {

                        if (((FragmentFaultLog) fragment).searchItem != null) {
                            ((FragmentFaultLog) fragment).searchItem.setVisible(false);
                        }

                    }*/

                else if (fragment instanceof FragmentNotification) {

                    if (((FragmentNotification) fragment).searchItem != null) {
                        ((FragmentNotification) fragment).searchItem.setVisible(false);
                    }

                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                // getSupportActionBar().setDisplayShowHomeEnabled(false);

                if (fragment instanceof FragmentLocation) {

                    if (((FragmentLocation) fragment).searchItem != null) {
                        ((FragmentLocation) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentEquipment) {

                    if (((FragmentEquipment) fragment).searchItem != null) {
                        ((FragmentEquipment) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentManageUser) {

                    if (((FragmentManageUser) fragment).searchItem != null) {
                        ((FragmentManageUser) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentRepairLog) {

                    if (((FragmentRepairLog) fragment).searchItem != null) {
                        ((FragmentRepairLog) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentEquipmentType) {

                    if (((FragmentEquipmentType) fragment).searchItem != null) {
                        ((FragmentEquipmentType) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentEquipmentCheckList) {

                    if (((FragmentEquipmentCheckList) fragment).searchItem != null) {
                        ((FragmentEquipmentCheckList) fragment).searchItem.setVisible(true);
                    }

                } else if (fragment instanceof FragmentEquipmentToUser) {

                    if (((FragmentEquipmentToUser) fragment).searchItem != null) {
                        ((FragmentEquipmentToUser) fragment).searchItem.setVisible(true);
                    }

                }


                /*else if (fragment instanceof FragmentFaultLog) {

                        if (((FragmentFaultLog) fragment).searchItem != null) {
                            ((FragmentFaultLog) fragment).searchItem.setVisible(false);
                        }

                    }*/


                else if (fragment instanceof FragmentNotification) {

                    if (((FragmentNotification) fragment).searchItem != null) {
                        ((FragmentNotification) fragment).searchItem.setVisible(true);
                    }

                }


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        //   setNavigationMenu();

        //   navView.set
        navView.setNavigationItemSelectedListener(this);

        //   navView.setItemBackground(getResources().getDrawable(R.drawable.edittext_background));

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateImageDialog(encodedString);

            }
        });

        recyclerMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("recyclerMenuItem", "recyclerMenuItem");


            }
        });


        llMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("llMenuItem", "llMenuItem");

            }
        });


        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuClick("Notification", null, null, null, null, null);

            }
        });


        addMenutItem(true);

        drawer.openDrawer(GravityCompat.START);

        //    listKey = new ArrayList<>(mapMenu.keySet());

        //   menuClick(listKey.get(0));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 100) {


                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME);
                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_FIRST_LAST_NAME);

                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_EMAIL);
                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID);

                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE);
                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE_PATH);

                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_TYPE);
                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_LOCATION_ID);
                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_LOCATION_NAME);

                    Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_PRIVILEGE_ID);

                    finishAffinity();

                }

                if (msg.what == 101) {

                    dialogUserImage.dismiss();

                    if (encodedString == null || encodedString.equalsIgnoreCase("")) {

                        //    imgUser.setImageResource(R.mipmap.ic_default_rounded_img);

                        imgUser.
                                setImageBitmap(null);


                        imgUser.setBackgroundResource(R.drawable.ic_no_img);

                    } else {

                        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        imgUser.
                                setImageBitmap(decodedByte);
                    }

                    Utilities.setStringInSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE, encodedString);

                }

                if (msg.what == 102) {

                    //    listKey = new ArrayList<>(mapMenu.keySet());

                    //  menuClick(listKey.get(0));

                }

                if (msg.what == 103) {

                    //   listKey = new ArrayList<>(mapMenu.keySet());

                    //   menuClick("Manage Users");

                }


            }
        };

    }

    private void setFireBasePushNotification() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Global.REGISTRATION_COMPLETE)) {

                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications

                    FirebaseMessaging.getInstance().subscribeToTopic(Global.TOPIC_GLOBAL);

                    //       Toast.makeText(getApplicationContext(), "registrationComplete", Toast.LENGTH_LONG).show();

                    //      Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Global.PUSH_NOTIFICATION)) {

                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //   Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.e("Push notification: ", "" + message);

                }
            }
        };

        displayFirebaseRegId();

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {

        String regId = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_REGID, null);

        /*SharedPreferences pref = getApplicationContext().getSharedPreferences(Global.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);*/

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e("Firebase Reg Id:  ", "" + regId);
            //    txtRegId.setText("Firebase Reg Id: " + regId);
        else
            Log.e("Firebase Reg Id:  ", "Firebase Reg Id is not received yet!");

        // txtRegId.setText("Firebase Reg Id is not received yet!");

    }

    private void setFireBaseAnalytics() {

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Set whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Set the minimum engagement time required before starting a session.
        firebaseAnalytics.setMinimumSessionDuration(2000);

        //Set the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(300000);

    }

    public void addMenutItem(boolean flag) {

        mapMenu = new LinkedHashMap<String, Integer>();

        userName = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME, null);

        firstLastName = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_FIRST_LAST_NAME, null);

        Log.e("menu userName", "" + userName);

        userEmail = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_EMAIL, null);

        encodedString = Utilities.getStringFromSharedPreferances(this, Global.KEY_USER_IMAGE, null);

        userType = Utilities.getStringFromSharedPreferances(this, Global.KEY_USER_TYPE, null);

        userLocationName = Utilities.getStringFromSharedPreferances(this, Global.KEY_USER_LOCATION_NAME, null);

        userName = Utilities.capitalizeString(userName);

        userEmail = Utilities.capitalizeString(userEmail);

        tvName.setText(firstLastName);

        //  tvUsername.setText("" + userName);

        tvUsertype.setText(userType + " ( " + userLocationName + " )");

        //  tvUseremail.setText("" + userEmail);

        Log.e("encodedString", "" + encodedString);

        if (encodedString == null || encodedString.equalsIgnoreCase("")) {

            imgUser.
                    setImageBitmap(null);

            imgUser.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imgUser.
                    setImageBitmap(decodedByte);

        }


        listPrivilege = Utilities.getSetFromSharedPreferances(this, Global.KEY_PRIVILEGE_ID, null);

        if (listPrivilege.contains(Utilities.LOCATION_MASTER_READ)) {

            //   navView.getMenu().getItem(0).setVisible(true);

            mapMenu.put("Location", R.drawable.ic_location);

        } else {

            //  navView.getMenu().getItem(0).setVisible(false);

            mapMenu.remove("Location");
        }


        if (listPrivilege.contains(Utilities.CATEGORY_MASTER_READ)) {
//            navView.getMenu().getItem(1).setVisible(false);

            mapMenu.put("Category", R.drawable.ic_category_v1);

        } else {

            mapMenu.remove("Category");
            // navView.getMenu().getItem(1).setVisible(false);
        }

        if (listPrivilege.contains(Utilities.USER_MASTER_READ)) {
            // navView.getMenu().getItem(6).setVisible(true);

            mapMenu.put("Personnel Setup", R.drawable.ic_add_user_v1);

        } else {

            mapMenu.remove("Personnel Setup");

            // navView.getMenu().getItem(6).setVisible(false);
        }

        if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_READ)) {
            //  navView.getMenu().getItem(2).setVisible(true);

            mapMenu.put("Asset Category", R.drawable.ic_equipment_type_v1);

        } else {

            mapMenu.remove("Asset Category");
            //navView.getMenu().getItem(2).setVisible(false);
        }

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_READ)) {
            //  navView.getMenu().getItem(4).setVisible(true);

            mapMenu.put("Asset Setup", R.drawable.ic_equipment_v2);

        } else {

            mapMenu.remove("Asset Setup");

            //navView.getMenu().getItem(4).setVisible(false);
        }

        if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_READ)) {
//            navView.getMenu().getItem(3).setVisible(true);

            mapMenu.put("Asset CheckList", R.drawable.ic_equipment_checklist);

        } else {
            //  navView.getMenu().getItem(3).setVisible(false);
            mapMenu.remove("Asset CheckList");

        }


        if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_READ)) {
            //  navView.getMenu().getItem(5).setVisible(true);

            mapMenu.put("Assign Asset To Personnel", R.drawable.ic_assign_equipment_v3);

        } else {

            mapMenu.remove("Assign Asset To Personnel");
            //   navView.getMenu().getItem(5).setVisible(false);
        }


        if (listPrivilege.contains(Utilities.REPAIRLOG_READ)) {
            // navView.getMenu().getItem(7).setVisible(true);

            mapMenu.put("Maintenance Log", R.drawable.ic_reapir_log_1);

        } else {

            mapMenu.remove("Maintenance Log");

            //navView.getMenu().getItem(7).setVisible(false);
        }


        if (listPrivilege.contains(Utilities.REPAIRLOG_READ)) {
            // navView.getMenu().getItem(7).setVisible(true);

            mapMenu.put("Fault Log", R.drawable.ic_fault_log);

        } else {

            mapMenu.remove("Fault Log");

            //navView.getMenu().getItem(7).setVisible(false);
        }


        if (listPrivilege.contains(Utilities.MYCHECKLIST_READ)) {
            // navView.getMenu().getItem(7).setVisible(true);

            mapMenu.put("My Checklist", R.drawable.ic_my_equipment_v3);

        } else {

            mapMenu.remove("My Checklist");

            //navView.getMenu().getItem(7).setVisible(false);
        }

        mapMenu.put("Change Password", R.drawable.ic_change_password);

        mapMenu.put("Log Out", R.drawable.ic_log_out);

        if (userType.equalsIgnoreCase(getResources().getString(R.string.Operator))) {

            imgNotification.setVisibility(View.GONE);

        } else {

            imgNotification.setVisibility(View.VISIBLE);

        }


        adapterMenuItem = new AdapterMenuItem(this, mapMenu);

        recyclerMenuItem.setHasFixedSize(true);

        //   RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        //   recyclerMenuItem.setLayoutManager(mLayoutManager);

        int numberOfColumns = 3;

        recyclerMenuItem.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        recyclerMenuItem.setAdapter(adapterMenuItem);

        user_image_path = Utilities.getStringFromSharedPreferances(this, Global.KEY_USER_IMAGE_PATH, null);

        if (user_image_path == null || user_image_path.equalsIgnoreCase("")) {

            //     ImageUpdateRequest imageRequest = new ImageUpdateRequest(user_image_path);

        } else {

            //   ImageUpdateRequest imageRequest = new ImageUpdateRequest(user_image_path);
            imageUpdateRequest(user_image_path, flag);

        }

    }

    @AddTrace(name = "ImageUpdateRequest", enabled = true)
    private void imageUpdateRequest(String userimagepath, boolean flag) {

        ImageUpdateRequest imageRequest = new ImageUpdateRequest(userimagepath, flag);

    }


    private void updateImageDialog(String encodedString) {

        dialogUserImage = DialogUserImage.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.update_user_image_dialog_title));
        bundle.putString("encodedString", encodedString);

        dialogUserImage.setArguments(bundle);
        dialogUserImage.show(getSupportFragmentManager(), "dialog");

    }

    @Override
    public void onBackPressed() {

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

       /* if (drawer.isDrawerOpen(drawer)) {

            //   drawer.closeDrawer(GravityCompat.START);

            drawer.closeDrawers();
        }*/

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            /* drawer.closeDrawer(GravityCompat.START);*/

            DialogExit dialogExit = DialogExit.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("title", getResources().getString(R.string.exit_dialog_title));
            bundle.putString("msg", getResources().getString(R.string.exit_dialog_msg));

            dialogExit.setArguments(bundle);
            dialogExit.show(getSupportFragmentManager(), "dialog");

        } else {

            drawer.openDrawer(GravityCompat.START);

        /*    DialogExit dialogExit = DialogExit.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("title", getResources().getString(R.string.exit_dialog_title));
            bundle.putString("msg", getResources().getString(R.string.exit_dialog_msg));

            dialogExit.setArguments(bundle);
            dialogExit.show(getSupportFragmentManager(), "dialog");
*/

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_search, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

       /* if (id == android.R.id.home) {


            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();

        }*/

        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        CharSequence title = item.getTitle();
        //navView.setItemBackground(getResources().getDrawable(R.drawable.edittext_background));

        //creating fragment object
        Fragment fragment = null;

       /* if (id == R.id.menu_item_equipment_list) {
            toolbar.setTitle("" + title);
        } else*/

        if (id == R.id.menu_item_location) {
            toolbar.setTitle("" + title);
            fragment = new FragmentLocation();
        } /*else if (id == R.id.menu_item_category) {
            toolbar.setTitle("" + title);
            fragment = new FragmentCategory();
        }*/ else if (id == R.id.menu_item_equipment_type) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentType();
        } else if (id == R.id.menu_item_equipment_checklist) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentCheckList();
        } else if (id == R.id.menu_item_equipment) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipment();
        } else if (id == R.id.menu_item_assign_equipment) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentToUser();
        } else if (id == R.id.menu_item_add_user) {
            toolbar.setTitle("" + title);
            fragment = new FragmentManageUser();
        } else if (id == R.id.menu_item_equipment_list) {

            toolbar.setTitle("" + title + " / " + "Equipment Type");
            fragment = new FragmentEquipmentTypeList();

        } else if (id == R.id.menu_item_settings) {

            Intent intent = new Intent(this, ActivitySettings.class);
            startActivity(intent);

        } else if (id == R.id.menu_item_abuot) {


        } else if (id == R.id.menu_item_logout) {

            DialogLogout dialogLogout = DialogLogout.newInstance();

            Bundle args = new Bundle();
            args.putString("title", getResources().getString(R.string.logout_title));
            args.putString("msg", getResources().getString(R.string.logout_msg));

            dialogLogout.setArguments(args);
            dialogLogout.show(getSupportFragmentManager(), "dialog");

        }

        //replacing the fragment
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //   ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //   ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);

/*
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);*/

            ft.replace(R.id.content_frame, fragment);

            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //  drawer.closeDrawers();
        return true;
    }

    @AddTrace(name = "UserLogOut", enabled = true)
    public void logoutPositiveClick() {

        new UserLogOut();

    }

    public void exitPositiveClick() {

        logoutPositiveClick();

    }

    @AddTrace(name = "UpdateUserImage", enabled = true)
    public void updateUserImage(String encodedString) {

        UpdateUserImage updateUserImage = new UpdateUserImage(encodedString);

    }

    public void menuClick(String title, EquipmentResponse response, EquipmentFalulList.Response response1, ArrayList<EquipmentFalulList.Response> listResponse1, Notification.Response response2, ArrayList<Notification.Response> listResponse2) {

        if (title.equalsIgnoreCase("Location")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentLocation();
        } else if (title.equalsIgnoreCase("Category")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentCategory();
        } else if (title.equalsIgnoreCase("Asset Category")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentType();
        } else if (title.equalsIgnoreCase("Asset CheckList")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentCheckList();
        } else if (title.equalsIgnoreCase("Asset Setup")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipment();
        } else if (title.equalsIgnoreCase("Assign Asset To Personnel")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentEquipmentToUser();
        } else if (title.equalsIgnoreCase("Personnel Setup")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentManageUser();
        } else if (title.equalsIgnoreCase("My Checklist")) {
            toolbar.setTitle("" + title + " / " + "Assigned Asset list");
            fragment = new FragmentEquipmentTypeList();

        } else if (title.equalsIgnoreCase("Maintenance Log")) {

            toolbar.setTitle("" + title);
            fragment = new FragmentRepairLog();
            Bundle bundle = new Bundle();
            if (response != null) {
                bundle.putParcelable("equipment_repairlog", response);
            } else if (response1 != null) {
                bundle.putParcelable("equipment_faultlog", response1);
                bundle.putParcelableArrayList("equipment_faultlog_list", listResponse1);
            } else if (response2 != null) {
                bundle.putParcelable("equipment_notificationlog", response2);
                bundle.putParcelableArrayList("equipment_notificationlog_list", listResponse2);
            }

            fragment.setArguments(bundle);

        } else if (title.equalsIgnoreCase("Fault Log")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentFaultLog();
        } else if (title.equalsIgnoreCase("Notification")) {
            toolbar.setTitle("" + title);
            fragment = new FragmentNotification();

        } else if (title.equalsIgnoreCase("Log Out")) {

            DialogLogout dialogLogout = DialogLogout.newInstance();

            Bundle args = new Bundle();
            args.putString("title", getResources().getString(R.string.logout_title));
            args.putString("msg", getResources().getString(R.string.logout_msg));

            dialogLogout.setArguments(args);
            dialogLogout.show(getSupportFragmentManager(), "dialog");

        } else if (title.equalsIgnoreCase("Change Password")) {

            Intent intent = new Intent(getBaseContext(), ActivityChangePassword.class);
               /* intent.putExtra("username", "" + userName);
                intent.putExtra("useremail", "" + userEmail);
                //       intent.putExtra("user_image_path", "" + userImagePath);*/
            startActivity(intent);



           /* DialogLogout dialogLogout = DialogLogout.newInstance();

            Bundle args = new Bundle();
            args.putString("title", getResources().getString(R.string.logout_title));
            args.putString("msg", getResources().getString(R.string.logout_msg));

            dialogLogout.setArguments(args);
            dialogLogout.show(getSupportFragmentManager(), "dialog");*/

        }

        //replacing the fragment
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //   ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //   ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);

/*
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);*/

            ft.replace(R.id.content_frame, fragment);

            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    private class UserLogOut implements LoaderManager.LoaderCallbacks<RestResponse> {

        public UserLogOut() {

            progressDialog = Utilities.startProgressDialog(ActivityNavigationDrawerAdmin.this);

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

                String userId = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME, null);

                progressDialog.setMessage("Please wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "logout";
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

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

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
                    return new RestLoader<RestResponse>(ActivityNavigationDrawerAdmin.this, httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //.show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse loginResponse) {

            if (null != loginResponse && loginResponse.getStatus().equalsIgnoreCase("success")) {

                //   Snackbar.make(coordinatorLayout, "" + loginResponse.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, loginResponse.getMessage());

                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_NAME);
                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_FIRST_LAST_NAME);

                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_EMAIL);
                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID);

                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE);
                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE_PATH);

                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_TYPE);
                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_LOCATION_ID);

                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_LOCATION_NAME);
                Utilities.removeObjectFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_PRIVILEGE_ID);


                finishAffinity();


            } else {

                //     Snackbar.make(coordinatorLayout, "" + loginResponse.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, "" + loginResponse.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }

    }

    private class UpdateUserImage implements LoaderManager.LoaderCallbacks<RestResponse> {

        public UpdateUserImage(String encodedString) {

            ActivityNavigationDrawerAdmin.this.encodedString = encodedString;

            progressDialog = Utilities.startProgressDialog(ActivityNavigationDrawerAdmin.this);

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

                String userId = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID, null);

             /*   progressDialog = new ProgressDialog(ActivityNavigationDrawerAdmin.this);
                progressDialog.setMessage("Please wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                progressDialog.setCancelable(false);*/

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/updateImage";
                args.putString(Global.ARGS_URI, url);
                //   args.putString(Global.ARGS_USER_NAME, userId);

                getSupportLoaderManager().restartLoader(LOADER_LOGOUT_DETAIL, args, this);

            } else {

                progressDialog.dismiss();

            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            String userId = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_ID, null);

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    UserImage userImage = new UserImage();
                    userImage.setUserid(Integer.parseInt(userId));
                    userImage.setPath("");
                    userImage.setBitmap(encodedString);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(userImage);
                    Log.i(TAG, "json String : " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(ActivityNavigationDrawerAdmin.this, httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                // Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                Utilities.showSnackbar(coordinatorLayout, "" + data.getMessage());

                handler.sendEmptyMessage(101);

            } else {

                Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class UserImage {

        private String bitmap;

        private String path;

        private int userid;

        public void setBitmap(String bitmap) {
            this.bitmap = bitmap;
        }

        public String getBitmap() {
            return this.bitmap;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getUserid() {
            return this.userid;
        }


        public UserImage() {

        }
    }

    private class ImageUpdateRequest implements LoaderManager.LoaderCallbacks<ImageUrl> {

        private final int position = 100000;
        private String imagePath = null;
        private ProgressDialog progressDialog;
        //   private static final int LOADER_ADD_USER = 1;
        //    private int pos;
        private boolean flag;

        public ImageUpdateRequest(String imagePath, boolean flag) {

            this.imagePath = imagePath;
            this.flag = flag;
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

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.USERS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                Log.e("position:---", "" + position);

                getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                //   progressDialog.dismiss();
                //               Snackbar.make(coordinatorLayout, getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                     .show();
                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));
            }

        }


        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityNavigationDrawerAdmin.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(ActivityNavigationDrawerAdmin.this, httpWrapper, ImageUrl.class);

                }

            } else {

                // Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();
                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

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

                byte[] decodedString = Base64.decode(imageBitmap, Base64.DEFAULT);

                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                imgUser.
                        setImageBitmap(decodedByte);

                Utilities.setStringInSharedPreferances(ActivityNavigationDrawerAdmin.this, Global.KEY_USER_IMAGE, imageBitmap);


                //    handler.sendEmptyMessage(102);


            } else {

                //     Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            if (flag) {

                handler.sendEmptyMessage(102);

            } else {

                handler.sendEmptyMessage(103);

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

}

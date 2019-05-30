package com.cdlit.assetmaintenanceapp.Ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterUserChecked;
import com.cdlit.assetmaintenanceapp.Adapter.AdapterUserEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.UserChecked;
import com.cdlit.assetmaintenanceapp.Model.UserEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.squareup.leakcanary.RefWatcher;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by rakesh on 06-09-2017.
 */

public class ActivityEquipmentChecklist extends AppCompatActivity {

    private int equipmetTypeId;
    private CoordinatorLayout coordinatorLayout;
    //  private SwipeRefreshLayout swipeRefreshListEquipmentChecklist;
    private RecyclerView recyclerListEquipmentChecklist;
    private Handler handler;
    private ArrayList<UserEquipmentChecklist.Response> listEquipmentChecklist;
    private AdapterUserEquipmentChecklist adapterEquipmentChecklist;
    private int equipmetId;
    //    private Button btEquipmentChecklistSubmit;
    private String TAG = ActivityEquipmentChecklist.class.toString();
    private ImageView imageBack;
    private ImageView imageForward;
    private Button btDate;
    public boolean todayDate = true;
    public String selectedDate;
    //   private ArrayList<String> weekDays;
    private TextView tvSelectedDay;
    //  private TextView tvNa;
    private String equipmetModel;
    private String equipmetModelDes;
    private ArrayList<UserChecked.UserCheckedResponse> listCheckedUsers;
    private AdapterUserChecked adapterUserChecked;
    private SwipeRefreshLayout swipeRefreshListEquipmentChecklist;
    private FloatingActionButton fabAddChecklist;
    private LinearLayout llDate;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;

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
    protected void onRestart() {
        super.onRestart();

        String btDateString = btDate.getText().toString();

        String currentDate = getCurrenDate();

        Log.d("btDateString", "" + btDateString);
        Log.d("currentDate", "" + currentDate);

        if (btDateString.equalsIgnoreCase(currentDate)) {
            //  GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(getCurrenDate());
            getEuipmentCheckedUser(getCurrenDate());

        }

    }

    @AddTrace(name = "GetEuipmentCheckedUser", enabled = true)
    private void getEuipmentCheckedUser(String currenDate) {

        GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(currenDate);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Asset Checklist Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onBackPressed() {

        //  super.onBackPressed();
        finish();
        //push from top to bottom
        //   overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity);
        //   overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);

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

        setContentView(R.layout.activity_equipment_checklist);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        equipmetTypeId = getIntent().getIntExtra("equipmet_type_id", 0);
        equipmetId = getIntent().getIntExtra("equipmet_id", 0);

        Log.e("equipmetId", "" + equipmetId);

        equipmetModel = getIntent().getStringExtra("equipmet_model");
        equipmetModelDes = getIntent().getStringExtra("equipmet_model_des");

        getSupportActionBar().setTitle(equipmetModel + " / " + equipmetModelDes);

        imageBack = (ImageView) findViewById(R.id.image_back);

        imageForward = (ImageView) findViewById(R.id.image_forward);

        //    tvNa = (TextView) findViewById(R.id.tv_na);
        //  tvNa.setVisibility(View.INVISIBLE);

        emptyView = (ViewStub) findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        fabAddChecklist = (FloatingActionButton) findViewById(R.id.fab_add_checklist);

        llDate = (LinearLayout) findViewById(R.id.ll_date);

        btDate = (Button) findViewById(R.id.bt_date);

        //    btEquipmentChecklistSubmit = (Button) findViewById(R.id.bt_equipment_checklist_submit);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        tvSelectedDay = (TextView) findViewById(R.id.tv_selected_day);

        swipeRefreshListEquipmentChecklist = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_list_equipment_checklist);

        recyclerListEquipmentChecklist = (RecyclerView) findViewById(R.id.recycler_list_equipment_checklist);

        swipeRefreshListEquipmentChecklist.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshListEquipmentChecklist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                String selectedDate = btDate.getText().toString();

                //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = sdf.parse(selectedDate);
                } catch (ParseException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }

             /*   Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                Date dt = c.getTime();

                Log.e("yesterday", sdf.format(dt));*/

                //  GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(sdf.format(date));

                getEuipmentCheckedUser(sdf.format(date));
                swipeRefreshListEquipmentChecklist.setRefreshing(false);

            }
        });


        handler = new Handler(new Handler.Callback() {

            public Date date1;
            public Date date2;

            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        date1 = sdf.parse(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                    try {
                        date2 = sdf.parse(getCurrenDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                    Log.e("date1", sdf.format(date1));

                    Log.e("date2", sdf.format(date2));

                    if (date1.equals(date2)) {

                        Log.e("date1.equals(date2)", "Date1 is equal Date2");

                        todayDate = true;
                    }

                    if (date1.after(date2)) {

                        Log.e("date1.after(date2)", "Date1 is after Date2");

                        todayDate = false;

                    }

                    if (date1.before(date2)) {

                        Log.e("date1.before(date2)", "Date1 is before Date2");

                        todayDate = false;
                    }

                    adapterEquipmentChecklist.notifyDataSetChanged();

                    btDate.setText(ActivityEquipmentChecklist.this.selectedDate);

                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
                    System.out.println(simpleDateformat.format(date1));

                    tvSelectedDay.setText("( " + simpleDateformat.format(date1) + " )");

                    //     Log.e("simpleDateformat.format(date1)", "" + simpleDateformat.format(date1));

                    if (todayDate) {

                        imageForward.setVisibility(View.GONE);

                /*        btEquipmentChecklistSubmit.setClickable(true);
                        btEquipmentChecklistSubmit.setEnabled(true);
                        btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);*/

                    } else {

                        imageForward.setVisibility(View.VISIBLE);

                      /*  btEquipmentChecklistSubmit.setClickable(false);
                        btEquipmentChecklistSubmit.setEnabled(false);
                        btEquipmentChecklistSubmit.setVisibility(View.GONE);*/

                    }


                    if (listEquipmentChecklist.size() == 0) {

                        recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                        //   tvNa.setVisibility(View.VISIBLE);
                        //    btEquipmentChecklistSubmit.setVisibility(View.GONE);

                    } else {

                        recyclerListEquipmentChecklist.setVisibility(View.VISIBLE);
                        //   tvNa.setVisibility(View.INVISIBLE);
                        //   btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

                    }

                }

                if (msg.what == 101) {

                    onBackPressed();

                    //GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(getCurrenDate());

                }

                if (msg.what == 102) {

                  /*  recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                    tvNa.setVisibility(View.VISIBLE);

                    btEquipmentChecklistSubmit.setVisibility(View.INVISIBLE);*/

                }

                if (msg.what == 103) {

                  /*  recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                    tvNa.setVisibility(View.VISIBLE);

                    btEquipmentChecklistSubmit.setVisibility(View.INVISIBLE);*/


                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        date1 = sdf.parse(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                    try {
                        date2 = sdf.parse(getCurrenDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }


                    Log.e("date1", sdf.format(date1));

                    Log.e("date2", sdf.format(date2));

                    if (date1.equals(date2)) {

                        Log.e("date1.equals(date2)", "Date1 is equal Date2");

                        todayDate = true;
                    }

                    if (date1.after(date2)) {

                        Log.e("date1.after(date2)", "Date1 is after Date2");

                        todayDate = false;

                    }

                    if (date1.before(date2)) {

                        Log.e("date1.before(date2)", "Date1 is before Date2");

                        todayDate = false;
                    }

                    //    adapterEquipmentChecklist.notifyDataSetChanged();

                    btDate.setText(ActivityEquipmentChecklist.this.selectedDate);

                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
                    System.out.println(simpleDateformat.format(date1));

                    tvSelectedDay.setText("( " + simpleDateformat.format(date1) + " )");

                    //     Log.e("simpleDateformat.format(date1)", "" + simpleDateformat.format(date1));

                    if (todayDate) {

                        imageForward.setVisibility(View.GONE);
                        fabAddChecklist.setVisibility(View.VISIBLE);


                /*        btEquipmentChecklistSubmit.setClickable(true);
                        btEquipmentChecklistSubmit.setEnabled(true);
                        btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);*/

                    } else {

                        imageForward.setVisibility(View.VISIBLE);
                        fabAddChecklist.setVisibility(View.GONE);



                      /*  btEquipmentChecklistSubmit.setClickable(false);
                        btEquipmentChecklistSubmit.setEnabled(false);
                        btEquipmentChecklistSubmit.setVisibility(View.GONE);*/

                    }


                  /*  if (listEquipmentChecklist.size() == 0) {

                        recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                        //   tvNa.setVisibility(View.VISIBLE);
                        //    btEquipmentChecklistSubmit.setVisibility(View.GONE);

                    } else {

                        recyclerListEquipmentChecklist.setVisibility(View.VISIBLE);
                        //   tvNa.setVisibility(View.INVISIBLE);
                        //   btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

                    }*/

                    recyclerListEquipmentChecklist.setVisibility(View.VISIBLE);

                    //   tvNa.setVisibility(View.INVISIBLE);

                    adapterUserChecked.notifyDataSetChanged();

                    checkEmptyView(adapterUserChecked);

                    loadImages();

                }

                if (msg.what == 104) {

                  /*  recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                    tvNa.setVisibility(View.VISIBLE);

                    btEquipmentChecklistSubmit.setVisibility(View.INVISIBLE);*/


                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        date1 = sdf.parse(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                    try {
                        date2 = sdf.parse(getCurrenDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }


                    Log.e("date1", sdf.format(date1));

                    Log.e("date2", sdf.format(date2));

                    if (date1.equals(date2)) {

                        Log.e("date1.equals(date2)", "Date1 is equal Date2");

                        todayDate = true;
                    }

                    if (date1.after(date2)) {

                        Log.e("date1.after(date2)", "Date1 is after Date2");

                        todayDate = false;

                    }

                    if (date1.before(date2)) {

                        Log.e("date1.before(date2)", "Date1 is before Date2");

                        todayDate = false;
                    }

                    //    adapterEquipmentChecklist.notifyDataSetChanged();

                    btDate.setText(ActivityEquipmentChecklist.this.selectedDate);

                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
                    System.out.println(simpleDateformat.format(date1));

                    tvSelectedDay.setText("( " + simpleDateformat.format(date1) + " )");

                    //     Log.e("simpleDateformat.format(date1)", "" + simpleDateformat.format(date1));

                    if (todayDate) {

                        imageForward.setVisibility(View.GONE);
                        fabAddChecklist.setVisibility(View.VISIBLE);

                /*        btEquipmentChecklistSubmit.setClickable(true);
                        btEquipmentChecklistSubmit.setEnabled(true);
                        btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);*/

                    } else {

                        imageForward.setVisibility(View.VISIBLE);
                        fabAddChecklist.setVisibility(View.GONE);

                      /*  btEquipmentChecklistSubmit.setClickable(false);
                        btEquipmentChecklistSubmit.setEnabled(false);
                        btEquipmentChecklistSubmit.setVisibility(View.GONE);*/

                    }


                  /*  if (listEquipmentChecklist.size() == 0) {

                        recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);
                        //   tvNa.setVisibility(View.VISIBLE);
                        //    btEquipmentChecklistSubmit.setVisibility(View.GONE);

                    } else {

                        recyclerListEquipmentChecklist.setVisibility(View.VISIBLE);
                        //   tvNa.setVisibility(View.INVISIBLE);
                        //   btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

                    }*/

                    recyclerListEquipmentChecklist.setVisibility(View.INVISIBLE);

                    //      tvNa.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);

                    //    btEquipmentChecklistSubmit.setVisibility(View.GONE);


                    adapterUserChecked.notifyDataSetChanged();
                    checkEmptyView(adapterUserChecked);


                    // loadImages();
                }


                return false;
            }
        });


        listCheckedUsers = new ArrayList<UserChecked.UserCheckedResponse>();

        adapterUserChecked = new AdapterUserChecked(this, listCheckedUsers);

        recyclerListEquipmentChecklist.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerListEquipmentChecklist.setLayoutManager(mLayoutManager);
        //  recyclerListEquipmentChecklist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //   recyclerListEquipmentChecklist.setItemAnimator(new DefaultItemAnimator());
        recyclerListEquipmentChecklist.setAdapter(adapterUserChecked);

        checkEmptyView(adapterUserChecked);

        getEuipmentCheckedUser(getCurrenDate());


      /*  btEquipmentChecklistSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EquipmentChecklistRequest equipmentChecklistRequest = new EquipmentChecklistRequest();

            }
        });
*/

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });

        tvSelectedDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedDate = btDate.getText().toString();

                //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = sdf.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -1);
                Date dt = c.getTime();

                Log.e("yesterday", sdf.format(dt));

                //  GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(sdf.format(dt));

                //   GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(sdf.format(dt));
                getEuipmentCheckedUser(sdf.format(dt));
            }
        });

        imageForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedDate = btDate.getText().toString();

                //     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = sdf.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                Date dt = c.getTime();

                Log.e("tomorrow", sdf.format(dt));

                // GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(sdf.format(dt));
                //   GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(sdf.format(dt));

                getEuipmentCheckedUser(sdf.format(dt));
            }
        });


        fabAddChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ActivityEquipmentChecklistDetail.class);
                intent.putExtra("equipmet_id", equipmetId);
                intent.putExtra("equipmet_type_id", equipmetTypeId);
                intent.putExtra("time_stamp", "");
                startActivity(intent);


                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);


            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = sdf.parse(getCurrenDate());
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }


        Log.e("date1", sdf.format(date));

        btDate.setText(getCurrenDate());

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated

        //System.out.println(simpleDateformat.format(date));

        tvSelectedDay.setText("( " + simpleDateformat.format(date) + " )");

        //     Log.e("simpleDateformat.format(date1)", "" + simpleDateformat.format(date1));

        imageForward.setVisibility(View.GONE);

        fabAddChecklist.setVisibility(View.VISIBLE);

    }

    private void loadImages() {

        for (int i = 0; i < adapterUserChecked.getItemCount(); i++) {

            String imagePath = listCheckedUsers.get(i).getImage_path();

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    //   ImageRequest imageRequest = new ImageRequest(i, listCheckedUsers.get(i).getImage_path());

                    getImageRequest(i, listCheckedUsers.get(i).getImage_path());

                }

            }

        }


    }

    @AddTrace(name = "EquipmentChecklistImageRequest", enabled = true)
    private void getImageRequest(int i, String image_path) {

        ImageRequest imageRequest = new ImageRequest(i, image_path);

    }

    private String getCurrenDate() {

        Date date = new Date();

        //  String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        return currentDate;

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

        /*DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        getEuipmentCheckedUser(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //   GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
*/


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, null, mYear, mMonth, mDay) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {

                dismiss();
                // tvDueServiceDate.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);

                // GetEuipmentCheckedUser getEuipmentRequest = new GetEuipmentCheckedUser(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                getEuipmentCheckedUser(dayOfMonth + "-" + (month + 1) + "-" + year);
                //   GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

            }
        };

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);


    }

    public void addEquipmentTypePositiveClick(ArrayList<Bitmap> listBitmap, int position) {

        ArrayList<String> listStringBitmap = new ArrayList<>();

        for (int i = 0; i < listBitmap.size(); i++) {

            Bitmap bitmap = listBitmap.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

            listStringBitmap.add(encodedString);

        }

        listEquipmentChecklist.get(position).setImages(listStringBitmap);

        adapterEquipmentChecklist.notifyDataSetChanged();

    }

    private class GetEuipmentCheckedUser implements LoaderManager.LoaderCallbacks<UserChecked> {

        private static final int LOADER_CREATED_USER = 1;
        private ProgressDialog progressDialog;

        public GetEuipmentCheckedUser(String selectedDate) {
            progressDialog = Utilities.startProgressDialog(ActivityEquipmentChecklist.this);

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklist.this)) {

                ActivityEquipmentChecklist.this.selectedDate = selectedDate;

                Date convertedDate = null;

                String oldFormat = "dd-MM-yyyy";
                String newFormat = "yyyy-MM-dd";

                SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
                SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

                String date = null;
                try {
                    date = sdf2.format(sdf1.parse(selectedDate));
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


                Bundle args = new Bundle();
                String url = null;


                try {
                    url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklist.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userCheckedAnswer/masterScreen/" + URLEncoder.encode(String.valueOf(equipmetId), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(date), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }


                args.putString(Global.ARGS_URI, url);

                getSupportLoaderManager().restartLoader(LOADER_CREATED_USER, args, this);

            } else {

                progressDialog.dismiss();
                // Snackbar.make(coordinatorLayout, ActivityEquipmentChecklist.this.getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();


                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<UserChecked> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklist.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<UserChecked>(ActivityEquipmentChecklist.this, httpWrapper, UserChecked.class);

                }

            } else {

                progressDialog.dismiss();

                // Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //    .show();


                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));
            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<UserChecked> loader, UserChecked data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listCheckedUsers.clear();

                for (UserChecked.UserCheckedResponse response : data.getResponse()) {

                    listCheckedUsers.add(response);

                }

                // Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                handler.sendEmptyMessage(103);

            } else {

                listCheckedUsers.clear();

                //    Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, data.getMessage());

                handler.sendEmptyMessage(104);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<UserChecked> loader) {

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

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklist.this)) {

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklist.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.USERS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                //     Log.e("position:---", "" + position);

                getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                //   progressDialog.dismiss();
                //  Snackbar.make(coordinatorLayout, getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklist.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(ActivityEquipmentChecklist.this, httpWrapper, ImageUrl.class);

                }

            } else {

                //   Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
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

                adapterUserChecked.updateImage(loaderId, path, imageBitmap);

            } else {

                //   Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

}

package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterUserEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklistSubmit;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.Model.UserEquipmentChecklist;
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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by rakesh on 18-10-2017.
 */

public class ActivityEquipmentChecklistDetail extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    //  private SwipeRefreshLayout swipeRefreshChecklistDetail;
    private RecyclerView recyclerChecklistDetail;
    private Button btEquipmentChecklistSubmit;
    private int equipmetTypeId;
    private int equipmetId;
    private ArrayList<UserEquipmentChecklist.Response> listEquipmentChecklist;
    private AdapterUserEquipmentChecklist adapterEquipmentChecklist;
    private Handler handler;
    private String timeStamp;
    private Integer userCheckedId;
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
    public void onBackPressed() {

        finish();

        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity);

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

        Crashlytics.log(this.getLocalClassName());

        setContentView(R.layout.activity_equipment_checklist_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        equipmetTypeId = getIntent().getIntExtra("equipmet_type_id", 0);

        equipmetId = getIntent().getIntExtra("equipmet_id", 0);

        userCheckedId = getIntent().getIntExtra("user_checked_id", 0);

        emptyView = (ViewStub) findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        timeStamp = getIntent().getStringExtra("time_stamp");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        listEquipmentChecklist = new ArrayList<UserEquipmentChecklist.Response>();

        adapterEquipmentChecklist = new AdapterUserEquipmentChecklist(this, listEquipmentChecklist, timeStamp);

        //  swipeRefreshChecklistDetail = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_checklist_detail);

        recyclerChecklistDetail = (RecyclerView) findViewById(R.id.recycler_checklist_detail);

        //   swipeRefreshChecklistDetail.setColorSchemeResources(R.color.colorAccent);

        btEquipmentChecklistSubmit = (Button) findViewById(R.id.bt_equipment_checklist_submit);

        recyclerChecklistDetail.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerChecklistDetail.setLayoutManager(mLayoutManager);
        //recyclerChecklistDetail.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // recyclerChecklistDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerChecklistDetail.setAdapter(adapterEquipmentChecklist);

        checkEmptyView(adapterEquipmentChecklist);
       /* swipeRefreshChecklistDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(getCurrenDate());

                swipeRefreshChecklistDetail.setRefreshing(false);

            }
        });
*/
        if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {

            btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

        } else {

            btEquipmentChecklistSubmit.setVisibility(View.GONE);

        }

        btEquipmentChecklistSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentNotFilled = "";

                boolean submit = true;

                for (int i = 0; i < adapterEquipmentChecklist.listChecklist.size(); i++) {

                    if (adapterEquipmentChecklist.listChecklist.get(i).getBooleanAnswer() == 0) {

                        if (adapterEquipmentChecklist.listChecklist.get(i).getComment() == null || adapterEquipmentChecklist.listChecklist.get(i).getComment().equalsIgnoreCase("")) {

                            submit = false;

                            if (commentNotFilled.equalsIgnoreCase("")) {

                                commentNotFilled = adapterEquipmentChecklist.listChecklist.get(i).getCheckListName();

                            } else {

                                commentNotFilled = commentNotFilled + " , " + adapterEquipmentChecklist.listChecklist.get(i).getCheckListName();

                            }

                        }

                    }

                }


                if (submit) {


                    getEquipmentChecklistRequest();

                } else {

                    //    Snackbar.make(coordinatorLayout, "Please enter comment on these checklist : " + commentNotFilled, Snackbar.LENGTH_LONG)
                    //        .show();

                    Utilities.showSnackbar(coordinatorLayout, "Please enter comment on these checklist : " + commentNotFilled);

                }


            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    adapterEquipmentChecklist.notifyDataSetChanged();

                    checkEmptyView(adapterEquipmentChecklist);

                    loadImages();

                }

                if (msg.what == 101) {

                    onBackPressed();

                }

                return false;
            }
        });


        //   GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(getCurrenDate());

        getEuipmentCheckListRequest();
    }

    @AddTrace(name = "GetEuipmentCheckListRequest", enabled = true)
    private void getEuipmentCheckListRequest() {

        GetEuipmentCheckListRequest getEuipmentRequest = new GetEuipmentCheckListRequest(getCurrenDate());

    }

    @AddTrace(name = "EquipmentChecklistRequest", enabled = true)
    private void getEquipmentChecklistRequest() {

        EquipmentChecklistRequest equipmentChecklistRequest = new EquipmentChecklistRequest();

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);
            tvEmptyItem.setText("No Checklist Available");
            btEquipmentChecklistSubmit.setVisibility(View.GONE);

        } else {

            Log.e("adapter", " not null");
            emptyView.setVisibility(View.GONE);
            btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

        }

        if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {

            btEquipmentChecklistSubmit.setVisibility(View.VISIBLE);

        } else {

            btEquipmentChecklistSubmit.setVisibility(View.GONE);

        }


    }

    private void loadImages() {

        for (int i = 0; i < adapterEquipmentChecklist.getItemCount(); i++) {

            if (listEquipmentChecklist.get(i).getImage_path() != null && listEquipmentChecklist.get(i).getImage_path().size() != 0) {

                String imagePath = listEquipmentChecklist.get(i).getImage_path().get(0);

                if (imagePath != null) {

                    if (!imagePath.equalsIgnoreCase("")) {

                        //   ImageRequest imageRequest = new ImageRequest(i, listEquipmentChecklist.get(i).getImage_path().get(0));

                        getImageRequest(i, listEquipmentChecklist.get(i).getImage_path().get(0));

                    }

                }
            }

        }

    }

    @AddTrace(name = "EquipmentChecklistDetailImageRequest", enabled = true)
    private void getImageRequest(int i, String s) {

        ImageRequest imageRequest = new ImageRequest(i, s);

    }

    private String getCurrenDate() {

        Date date = new Date();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        return currentDate;

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
        checkEmptyView(adapterEquipmentChecklist);

    }

    private class EquipmentChecklistRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private ProgressDialog progressDialog;

        public EquipmentChecklistRequest() {

            progressDialog = Utilities.startProgressDialog(ActivityEquipmentChecklistDetail.this);

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userCheckedAnswer/save";
                args.putString(Global.ARGS_URI, url);

                getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinatorLayout, ActivityEquipmentChecklistDetail.this.getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //             .show();
                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.KEY_USER_ID, null);

                    String userName = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.KEY_USER_NAME, null);

                    Date currentDate = Utilities.currentDateTime();

                 /*   ArrayList<UserEquipmentChecklist.Response> responseArrayList = new ArrayList<>();

                    for (int i = 0; i < adapterEquipmentChecklist.listChecklist.size(); i++) {

                        adapterEquipmentChecklist.listChecklist.get(i).setModifiedDate(currentDate);
                        adapterEquipmentChecklist.listChecklist.get(i).setModifiedUser(Integer.parseInt(userid));

                        responseArrayList.add(adapterEquipmentChecklist.listChecklist.get(i));

                    }

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();
*/


                    EquipmentChecklistSubmit equipmentChecklistSubmit = new EquipmentChecklistSubmit();
                    equipmentChecklistSubmit.setDescription("");
                    equipmentChecklistSubmit.setEquipmentId(equipmetId);
                    equipmentChecklistSubmit.setModelNo("" + equipmetTypeId);
                    equipmentChecklistSubmit.setModified_date(currentDate);
                    equipmentChecklistSubmit.setUserid(Integer.parseInt(userid));
                    equipmentChecklistSubmit.setUsername(userName);


                    ArrayList<EquipmentChecklistSubmit.ChecklistDetails> responseArrayList = new ArrayList<>();

                    for (int i = 0; i < adapterEquipmentChecklist.listChecklist.size(); i++) {

                        EquipmentChecklistSubmit.ChecklistDetails checklistDetails = new EquipmentChecklistSubmit.ChecklistDetails();

                        // for to miles / hours to not setBooleanAnswer to 1.

                        if (adapterEquipmentChecklist.listChecklist.get(i).getChecklistType() == 1) {

                            checklistDetails.setBooleanAnswer(1);

                        } else {

                            checklistDetails.setBooleanAnswer(adapterEquipmentChecklist.listChecklist.get(i).getBooleanAnswer());

                        }

                        //checklistDetails.setBooleanAnswer(adapterEquipmentChecklist.listChecklist.get(i).getBooleanAnswer());

                        checklistDetails.setCheckListDescription(adapterEquipmentChecklist.listChecklist.get(i).getCheckListDescription());
                        checklistDetails.setCheckListName(adapterEquipmentChecklist.listChecklist.get(i).getCheckListName());
                        checklistDetails.setComment(adapterEquipmentChecklist.listChecklist.get(i).getComment());
                        checklistDetails.setEquipmentCheckListId(adapterEquipmentChecklist.listChecklist.get(i).getEquipmentCheckListId());
                        checklistDetails.setImages(adapterEquipmentChecklist.listChecklist.get(i).getImages());

                        checklistDetails.setFaultType(adapterEquipmentChecklist.listChecklist.get(i).getFaultType());
                        checklistDetails.setModifiedDate(currentDate);
                        checklistDetails.setModifiedUser(Integer.parseInt(userid));

                        responseArrayList.add(checklistDetails);

                    }

                    equipmentChecklistSubmit.setChecklistDetails(responseArrayList);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentChecklistSubmit);
                    Log.i("json String", "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(ActivityEquipmentChecklistDetail.this, httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                // Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //.show();
                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                Utilities.showSnackbar(coordinatorLayout, data.getMessage());
                handler.sendEmptyMessage(101);

            } else {


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class GetEuipmentCheckListRequest implements LoaderManager.LoaderCallbacks<UserEquipmentChecklist> {

        private static final int LOADER_GET_EQUIPMENT_CHECKLIST = 1;

        private ProgressDialog progressDialog;

        public GetEuipmentCheckListRequest(String selectedDate) {

            progressDialog = Utilities.startProgressDialog(ActivityEquipmentChecklistDetail.this);

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                //  progressDialog = Utilities.startProgressDialog(ActivityEquipmentChecklistDetail.this);

                Bundle args = new Bundle();

                String url = null;

                if (timeStamp.equalsIgnoreCase(null) || timeStamp.equalsIgnoreCase("")) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userCheckedAnswer/newChecklist/" + URLEncoder.encode(String.valueOf(equipmetId), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                } else {


                    try {
                        url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userCheckedAnswer/childScreen/" + URLEncoder.encode(String.valueOf(userCheckedId), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                        Crashlytics.logException(e);
                    }

                }

                args.putString(Global.ARGS_URI, url);

                getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<UserEquipmentChecklist> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<UserEquipmentChecklist>(ActivityEquipmentChecklistDetail.this, httpWrapper, UserEquipmentChecklist.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<UserEquipmentChecklist> loader, UserEquipmentChecklist data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentChecklist.clear();

                for (UserEquipmentChecklist.Response equipmentResponse : data.getResponse()) {

                    listEquipmentChecklist.add(equipmentResponse);

                }

                handler.sendEmptyMessage(100);

            } else {

                Utilities.showSnackbar(coordinatorLayout, data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<UserEquipmentChecklist> loader) {


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

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(ActivityEquipmentChecklistDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.CHECKLIST_ANSWER_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);


                getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityEquipmentChecklistDetail.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(ActivityEquipmentChecklistDetail.this, httpWrapper, ImageUrl.class);

                }

            } else {

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

                adapterEquipmentChecklist.updateImage(loaderId, path, imageBitmap);

            } else {

                //    Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

}

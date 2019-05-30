package com.cdlit.assetmaintenanceapp.Ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperFaultLog;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddFaultLogComment;
import com.cdlit.assetmaintenanceapp.Model.AddCommentReturn;
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.Model.FaultEquipment;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class FragmentFaultLog extends android.support.v4.app.Fragment implements ActionModeListener {

    private CoordinatorLayout coordinatorLayoutFaultLog;
    private SwipeRefreshLayout swipeRefreshFaultLog;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private Spinner spinnerSelectDate;
    private Spinner spinnerSelectLocation;
    private Spinner spinnerSelectEquipment;
    private ArrayAdapter<String> dateAdapter;
    private LinearLayout llCustomDate;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private ArrayList<FaultEquipment.Response> listLocation;
    private ArrayList<String> listLocationName;
    private Handler handler;
    private ArrayAdapter<String> locationAdapter;
    private ArrayList<FaultEquipment.Response> listEquipment;
    private ArrayList<String> listEquipmentName;
    private ArrayAdapter<String> equipmentAdapter;
    private ArrayList<EquipmentFalulList.Response> listFaults;
    private RecyclerView recyclerFaultLog;
    private AdaperFaultLog adapterFaultLog;
    private Set<String> listPrivilege;
    private ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private ArrayList<String> listImageResponse;
    private HashMap<Integer, String> mapImageResponse;
    private DialogAddFaultLogComment dialogAddFaultLogComment;
    private Integer faultId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fault_log, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentFaultLog.class.getName());

        coordinatorLayoutFaultLog = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_fault_log);

        swipeRefreshFaultLog = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_fault_log);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        spinnerSelectDate = (Spinner) getActivity().findViewById(R.id.spinner_select_date);

        spinnerSelectEquipment = (Spinner) getActivity().findViewById(R.id.spinner_select_equipment);

        llCustomDate = (LinearLayout) getActivity().findViewById(R.id.ll_custom_date);

        tvStartDate = (TextView) getActivity().findViewById(R.id.tv_start_date);

        tvEndDate = (TextView) getActivity().findViewById(R.id.tv_end_date);

        recyclerFaultLog = (RecyclerView) getActivity().findViewById(R.id.recycler_fault_log);

        swipeRefreshFaultLog.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshFaultLog.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (faultId != null) {

                    getFaults(faultId);

                }

                swipeRefreshFaultLog.setRefreshing(false);

            }

        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    equipmentAdapter.notifyDataSetChanged();

                }

                if (msg.what == 101) {

                    adapterFaultLog.notifyDataSetChanged();

                    checkEmptyView(adapterFaultLog);

                    loadImages();

                }

                if (msg.what == 102) {

                    dialogAddFaultLogComment.dismiss();

                    if (faultId != null) {

                        getFaults(faultId);

                    }

                }

                return false;

            }
        });

        listEquipment = new ArrayList<FaultEquipment.Response>();

        listEquipmentName = new ArrayList<String>();

        listFaults = new ArrayList<EquipmentFalulList.Response>();

        ArrayList<String> listDates = new ArrayList<>();

        listDates.add("Select Date");
        listDates.add("Last Week");
        listDates.add("Last Month");
        listDates.add("Quarterly");
        listDates.add("Custom");

        dateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listDates);

        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSelectDate.setAdapter(dateAdapter);

        spinnerSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 4) {
                    llCustomDate.setVisibility(View.VISIBLE);
                } else {
                    llCustomDate.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        equipmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEquipmentName);

        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSelectEquipment.setAdapter(equipmentAdapter);

        spinnerSelectEquipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    faultId = null;

                    listFaults.clear();
                    adapterFaultLog.notifyDataSetChanged();
                    checkEmptyView(adapterFaultLog);

                } else {

                    faultId = listEquipment.get(position - 1).getId();

                    getFaults(faultId);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(1);
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(2);
            }
        });


        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        adapterFaultLog = new AdaperFaultLog(FragmentFaultLog.this, listFaults, this);

        recyclerFaultLog.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerFaultLog.setLayoutManager(mLayoutManager);

        recyclerFaultLog.setAdapter(adapterFaultLog);

        checkEmptyView(adapterFaultLog);

        actionModeCallback = new ActionModeCallback();

        getEquipment();

    }

    private void checkEmptyView(AdaperFaultLog adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);
            tvEmptyItem.setText("No Fault Log Available");

        } else {

            Log.e("adapter", " not null");
            emptyView.setVisibility(View.GONE);

        }

    }

    @AddTrace(name = "GetLocationRequest", enabled = true)
    private void getFaults(int id) {

        GetEquipmentFaults getEquipmentFaults = new GetEquipmentFaults(id);

    }

    @AddTrace(name = "GetLocationRequest", enabled = true)
    private void getEquipment() {

        GetEquipmentRequest getEquipmentRequest = new GetEquipmentRequest();

    }

    private void loadImages() {

        listImageResponse = new ArrayList<String>();

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterFaultLog.getItemCount(); i++) {

            String imagePath;

            if (listFaults.get(i).getImagePath() == null || listFaults.get(i).getImagePath().size() == 0) {

                imagePath = null;

            } else {

                imagePath = listFaults.get(i).getImagePath().get(0);

            }

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    getImageRequest(i, listFaults.get(i).getImagePath().get(0));

                }

            }

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (i == 1) {

                            tvStartDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        } else if (i == 2) {

                            tvEndDate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    // to analyse this method performance
    @AddTrace(name = "EquipmentFaultLogRequest", enabled = true)
    private void getImageRequest(int i, String imagePath) {

        ImageRequest imageRequest = new ImageRequest(i, imagePath);

    }

    @Override
    public void onIconClicked(int position) {
        Log.e("onIconClicked", "" + position);

        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapterFaultLog.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            //    Toast.makeText(getActivity(), "Read: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleSelection(int position) {

        adapterFaultLog.toggleSelection(position);

        int count = adapterFaultLog.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //   actionMode = getActivity().startActionMode(actionModeCallback);
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    public void addCommentPositiveClick(String comment, EquipmentFalulList.Response response) {

        AddCommentRequest addCommentRequest = new AddCommentRequest(comment, response);

    }

    public void editCommentPositiveClick(String s, EquipmentFalulList.Response response) {


    }

    private class GetEquipmentRequest implements LoaderManager.LoaderCallbacks<FaultEquipment> {

        private static final int LOADER_GET_LOCATION = 1;

        private ProgressDialog progressDialog;

        public GetEquipmentRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //progressDialog = Utilities.startProgressDialog(getActivity());

                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                String locationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);

                Bundle args = new Bundle();
                String url = null;

                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/getPendingEquip/" + URLEncoder.encode("" + userid, "UTF-8") + "/" + URLEncoder.encode("" + locationId, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);

            } else {

                progressDialog.dismiss();
               /* Snackbar.make(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();*/

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<FaultEquipment> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<FaultEquipment>(getActivity(), httpWrapper, FaultEquipment.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<FaultEquipment> loader, FaultEquipment data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipment.clear();

                listEquipmentName.clear();

                listEquipmentName.add("Select Asset");

                for (FaultEquipment.Response response : data.getResponse()) {

                    listEquipment.add(response);

                    listEquipmentName.add(response.getModelNo() + " ( " + response.getLocation().getLocationName() + " )");

                }

                handler.sendEmptyMessage(100);

            } else {

                Utilities.showSnackbar(coordinatorLayoutFaultLog, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<FaultEquipment> loader) {

        }

    }

    private class GetEquipmentFaults implements LoaderManager.LoaderCallbacks<EquipmentFalulList> {

        private final int id;
        private static final int LOADER_GET_LOCATION = 1;

        private ProgressDialog progressDialog;

        public GetEquipmentFaults(int id) {

            this.id = id;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "faultLog/getPendingFaultLog/" + URLEncoder.encode("" + id, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);


            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));
            }
        }

        @Override
        public Loader<EquipmentFalulList> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<EquipmentFalulList>(getActivity(), httpWrapper, EquipmentFalulList.class);

                }

            } else {

                progressDialog.dismiss();

                //    Snackbar.make(coordinatorLayoutFaultLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentFalulList> loader, EquipmentFalulList data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listFaults.clear();

                for (EquipmentFalulList.Response response : data.getResponse()) {

                    listFaults.add(response);

                }

                handler.sendEmptyMessage(101);

            } else {

                Utilities.showSnackbar(coordinatorLayoutFaultLog, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentFalulList> loader) {

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
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.CHECKLIST_ANSWER_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                getActivity().getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                //   progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();
                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

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

                //       Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();
                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

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

                adapterFaultLog.updateImage(loaderId, path, imageBitmap);

            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode_faultlog, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshFaultLog.setEnabled(false);

            spinnerSelectEquipment.setEnabled(false);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            if (listPrivilege.contains(Utilities.FAULTLOG_WRITE)) {
                menu.getItem(0).setVisible(true);
            }

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_add_comment:
                    addComment();
                    mode.finish();
                    return true;

                case R.id.action_edit:
                    editItem();
                    mode.finish();
                    return true;

                default:
                    return false;

            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            adapterFaultLog.clearSelections();
            swipeRefreshFaultLog.setEnabled(true);
            spinnerSelectEquipment.setEnabled(true);

           /* if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

                fabAddLocation.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }*/

            actionMode = null;

            recyclerFaultLog.post(new Runnable() {
                @Override
                public void run() {
                    adapterFaultLog.resetAnimationIndex();
                }
            });

        }
    }

    private void addComment() {

        List<Integer> selectedItemPositions =
                adapterFaultLog.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        EquipmentFalulList.Response response = listFaults.get(selectedItemPositions.get(0));

        dialogAddFaultLogComment = DialogAddFaultLogComment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_fault_log_comment_dialog_title));

        bundle.putParcelable("response", response);

        dialogAddFaultLogComment.setArguments(bundle);
        dialogAddFaultLogComment.setTargetFragment(FragmentFaultLog.this, 0);
        dialogAddFaultLogComment.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterFaultLog.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

        EquipmentFalulList.Response response = listFaults.get(selectedItemPositions.get(0));

        ArrayList<EquipmentFalulList.Response> listResponse = new ArrayList<>();

        for (int i = 0; i < selectedItemPositions.size(); i++) {

            listResponse.add(listFaults.get(selectedItemPositions.get(i)));

        }

        context.menuClick("Maintenance Log", null, response, listResponse, null, null);

    }

    private class AddCommentRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_LOCATION = 1;
        private EquipmentFalulList.Response response;
        private String comment;
        private ProgressDialog progressDialog;

        public AddCommentRequest(String comment, EquipmentFalulList.Response response) {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                this.comment = comment;
                this.response = response;
                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "faultLog/addFaultLogComment";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_LOCATION, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userName = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_NAME, null);

                    AddCommentReturn addCommentReturn = new AddCommentReturn();

                    Integer Id;

                    if (response.getFaultLogComments() == null) {

                        Id = null;

                    } else {

                        Id = response.getFaultLogComments().get(0).getId();

                    }

                    addCommentReturn.setId(Id);
                    addCommentReturn.setComment(comment);
                    addCommentReturn.setUsername(userName);
                    addCommentReturn.setFaultLogId(response.getFaultLogId());

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(addCommentReturn);
                    Log.i(" json String :: ", "" + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinatorLayoutFaultLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(102);

            } else {

                Utilities.showSnackbar(coordinatorLayoutFaultLog, data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }


}

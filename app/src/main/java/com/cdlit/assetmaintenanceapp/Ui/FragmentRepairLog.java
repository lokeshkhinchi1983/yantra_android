package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperRepairLog;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Dialog.DialogRepairLog;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipment;
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.Location;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.Notification;
import com.cdlit.assetmaintenanceapp.Model.RepairLog;
import com.cdlit.assetmaintenanceapp.Model.RepairLogAdd;
import com.cdlit.assetmaintenanceapp.Model.RepairLogDetail;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerItemTouchHelper;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerItemTouchHelperListener;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 12-12-2017.
 */

public class FragmentRepairLog extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private FloatingActionButton fabAddRepairLog;
    private CoordinatorLayout coordinateRepairLog;
    private SwipeRefreshLayout swipeRefreshRepairLog;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private Handler handler;
    private Set<String> listPrivilege;
    private RecyclerView recyclerRepairLog;
    private AdaperRepairLog adapterRepairLog;
    private ArrayList<EquipmentResponse> listEquipment;
    private DialogRepairLog dialogRepairLog;
    private ArrayList<RepairLog.RepairlogResponse> listRepairLog;
    private ArrayList<AssignEquipment.AssignEquipmentResponse> listEquipmentTypeList;
    private static final String TAG_REPAIR = FragmentRepairLog.class.getSimpleName();
    private RepairLogDetail.RepairLogDetailResponse repairLogDetailResponse;
    private boolean editClick = false;
    private ArrayList<String> listImageResponse;
    private HashMap<Integer, String> mapImageResponse;
    private ArrayList<LocationResponse> listLocation;
    private ArrayList<String> listLocationName;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private RepairLog.RepairlogResponse deletedItem;
    private int deletedIndex;
    private boolean swipFlag;
    private ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private ArrayList<Integer> selectedRepairLogsIds;
    private EquipmentResponse equipmentResponce;
    private EquipmentFalulList.Response equipmentResponce1;
    private Notification.Response equipmentResponce2;
    private ArrayList<EquipmentFalulList.Response> equipmentResponce3;
    private ArrayList<Notification.Response> equipmentResponce4;
    public MenuItem searchItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_repair_log, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);

      /*  MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

      */

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<RepairLog.RepairlogResponse> filteredValues = new ArrayList<RepairLog.RepairlogResponse>(listRepairLog);

        for (RepairLog.RepairlogResponse value : listRepairLog) {

            if (!value.getModelName().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

        }

        adapterRepairLog = new AdaperRepairLog(FragmentRepairLog.this, filteredValues, this);

        recyclerRepairLog.setAdapter(adapterRepairLog);

        checkEmptyView(adapterRepairLog);

        return false;
    }

    private void resetSearch() {

        adapterRepairLog = new AdaperRepairLog(FragmentRepairLog.this, listRepairLog, this);
        recyclerRepairLog.setAdapter(adapterRepairLog);
        checkEmptyView(adapterRepairLog);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);
            tvEmptyItem.setText("No Maintenance Log Available");

        } else {

            Log.e("adapter", " not null");
            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentRepairLog.class.getName());

        fabAddRepairLog = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_repair_log);

        coordinateRepairLog = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_repair_log);

        swipeRefreshRepairLog = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_repair_log);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshRepairLog.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshRepairLog.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getRepairLogRequest();

                swipeRefreshRepairLog.setRefreshing(false);

            }
        });


        equipmentResponce = getArguments().getParcelable("equipment_repairlog");

        equipmentResponce1 = getArguments().getParcelable("equipment_faultlog");

        equipmentResponce2 = getArguments().getParcelable("equipment_notificationlog");

        equipmentResponce3 = getArguments().getParcelableArrayList("equipment_faultlog_list");

        equipmentResponce4 = getArguments().getParcelableArrayList("equipment_notificationlog_list");

        if (equipmentResponce != null) {

            String location = equipmentResponce.getLocation().getLocationName();
            String equipmentType = equipmentResponce.getEquipmentType().getEquipmentTypeName();
            String equipment = equipmentResponce.getModelNo();

            Log.e("location", "" + location);
            Log.e("equipmentType", "" + equipmentType);
            Log.e("equipment", "" + equipment);

        }

        if (equipmentResponce1 != null) {

            String location = equipmentResponce1.getLocation().getLocationName();
            String equipmentType = equipmentResponce1.getEquipmentType().getEquipmentTypeName();
            String equipment = equipmentResponce1.getEquipment().getModelNo();

            Log.e("location", "" + location);
            Log.e("equipmentType", "" + equipmentType);
            Log.e("equipment", "" + equipment);

            Log.e("equipment_faultlog_list", "" + equipmentResponce3.toString());
        }

        if (equipmentResponce2 != null) {

            String location = equipmentResponce2.getLocationname();
            String equipmentType = equipmentResponce2.getEquipmenttypename();
            String equipment = equipmentResponce2.getEquipmentname();

            Log.e("location", "" + location);
            Log.e("equipmentType", "" + equipmentType);
            Log.e("equipment", "" + equipment);

            //   Log.e("equipment_notificationlog_list", "" + equipmentResponce4.toString());

        }


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 101) {

                    //   getLocationRequest(1);

                } else if (msg.what == 102) {

                    dialogAddRepairLog();

                } else if (msg.what == 103) {

                    adapterRepairLog.notifyDataSetChanged();

                    checkEmptyView(adapterRepairLog);

                    if (equipmentResponce != null) {
                        dialogAddRepairLog();
                    } else if (equipmentResponce1 != null) {
                        dialogAddRepairLog();
                    } else if (equipmentResponce2 != null) {
                        dialogAddRepairLog();
                    }
                    loadImages();

                } else if (msg.what == 104) {

                    dialogRepairLog.dismiss();

                    equipmentResponce = null;

                    equipmentResponce1 = null;

                    equipmentResponce2 = null;
                    equipmentResponce3 = null;

                    equipmentResponce4 = null;
                    getRepairLogRequest();

                } else if (msg.what == 105) {

                    getRepairLogRequest();

                } else if (msg.what == 106) {

                    //   EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest(0);

                    //   getEquipmentTypeRequest(0);

                    // dialogUpdateRepairLog();

                    getLocationRequest(0);

                } else if (msg.what == 107) {

                    //   EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest();
                    //    getLocationRequest(0);

                } else if (msg.what == 108) {

                    dialogAddRepairLog();

                } else if (msg.what == 109) {

                    dialogUpdateRepairLog();

                }
                return false;
            }


        });


        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {

            fabAddRepairLog.setVisibility(View.GONE);

        }

        //    listEquipmentType = new ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse>();
        listEquipment = new ArrayList<EquipmentResponse>();

        listEquipmentTypeList = new ArrayList<>();


        fabAddRepairLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editClick = false;

                //   EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest(1);

                //   getEquipmentTypeRequest(1);

                getLocationRequest(1);

            }
        });

       /* listCategory = new ArrayList<CategoryResponse>();

        listEquipmentType = new ArrayList<EquipmentTypeResponse>();
*/
        listLocation = new ArrayList<LocationResponse>();
        listLocationName = new ArrayList<String>();

        listRepairLog = new ArrayList<RepairLog.RepairlogResponse>();

        recyclerRepairLog = (RecyclerView) getActivity().findViewById(R.id.recycler_repair_log);

        adapterRepairLog = new AdaperRepairLog(FragmentRepairLog.this, listRepairLog, this);

        recyclerRepairLog.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerRepairLog.setLayoutManager(mLayoutManager);

        recyclerRepairLog.setAdapter(adapterRepairLog);

        checkEmptyView(adapterRepairLog);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerRepairLog);
            itemTouchHelperCallback.setSwipeEnable(false);

        }

        actionModeCallback = new ActionModeCallback();

        getRepairLogRequest();

        /*if (equipmentResponce != null) {

            dialogAddRepairLog();

        }*/


    }

    public void addRepairLogNegativeClick() {


        if (equipmentResponce != null) {

            ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

            context.menuClick("Asset Setup", null, null, null, null, null);

        } else if (equipmentResponce1 != null) {

            ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

            context.menuClick("Fault Log", null, null, null, null, null);

        } else if (equipmentResponce2 != null) {

            ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

            context.menuClick("Notification", null, null, null, null, null);

        }

       /* equipmentResponce = null;
        equipmentResponce1 = null;
        equipmentResponce2 = null;
        equipmentResponce3 = null;
        equipmentResponce4 = null;*/

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_repairlog, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshRepairLog.setEnabled(false);

          /*  adapterEquipment.MyViewHolder.imageEquipment.setEnabled(false);
            adapterEquipment.MyViewHolder.imageEquipment.setClickable(false);*/

            if (listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {

                fabAddRepairLog.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //    Log.e("" + TAG, "onPrepareActionMode");

            if (adapterRepairLog.getSelectedItemCount() > 1) {

               /* if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }*/

                if (listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {
                    menu.getItem(0).setVisible(false);
                }

            } else {

               /* if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }*/

                if (listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {
                    menu.getItem(0).setVisible(true);
                }

            }

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            //   Log.e("" + TAG, "onActionItemClicked");
            switch (item.getItemId()) {
                case R.id.action_delete:

                    deleteItem();
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

            //Log.e("" + TAG, "onDestroyActionMode");

            adapterRepairLog.clearSelections();
            swipeRefreshRepairLog.setEnabled(true);

           /* adapterEquipment.holder.imageEquipment.setEnabled(true);
            adapterEquipment.holder.imageEquipment.setClickable(true);*/

            if (listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {

                fabAddRepairLog.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            actionMode = null;

            recyclerRepairLog.post(new Runnable() {
                @Override
                public void run() {

                    adapterRepairLog.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();

                }
            });

        }

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterRepairLog.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listRepairLog.get(selectedItemPositions.get(0)));

    }

    private void deleteItem() {

        adapterRepairLog.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterRepairLog.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listRepairLog.get(selectedItemPositions.get(0)), null);

        } else {

            selectedRepairLogsIds = new ArrayList<>();
            ArrayList<String> selectedRepairLogName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedRepairLogsIds.add(listRepairLog.get(pos).getRepairLogId());
                selectedRepairLogName.add(listRepairLog.get(pos).getModelName());

            }

            String deleteRepairLogName = "";

            for (int i = 0; i < selectedRepairLogName.size(); i++) {

                if (i == (selectedRepairLogName.size() - 1)) {
                    deleteRepairLogName = deleteRepairLogName + selectedRepairLogName.get(i);
                } else {
                    deleteRepairLogName = deleteRepairLogName + selectedRepairLogName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteRepairLogName);


        }


    }

    @AddTrace(name = "LocationRequest", enabled = true)
    private void getLocationRequest(int i) {

        GetLocationRequest getLocationRequest = new GetLocationRequest(i);

    }

    @AddTrace(name = "RepairLogRequest", enabled = true)
    private void getRepairLogRequest() {

        RepairLogRequest equipmentTypeRequest = new RepairLogRequest();

    }


    private void loadImages() {

        listImageResponse = new ArrayList<String>();

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterRepairLog.getItemCount(); i++) {

            String imagePath;

            if (listRepairLog.get(i).getImage_path() == null || listRepairLog.get(i).getImage_path().size() == 0) {
                imagePath = null;

            } else {
                imagePath = listRepairLog.get(i).getImage_path().get(0);

            }

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    //   ImageRequest imageRequest = new ImageRequest(i, listRepairLog.get(i).getImage_path().get(0));

                    getImageRequest(i, listRepairLog.get(i).getImage_path().get(0));

                }

            }

        }

    }

    @AddTrace(name = "RepairLogImageRequest", enabled = true)
    private void getImageRequest(int i, String s) {

        ImageRequest imageRequest = new ImageRequest(i, s);

    }

    private void dialogAddRepairLog() {

        dialogRepairLog = DialogRepairLog.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_repair_log_dialog_title));
        //  bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);
        bundle.putParcelableArrayList("list_location", listLocation);
        bundle.putStringArrayList("list_location_name", listLocationName);

        if (equipmentResponce != null) {

            bundle.putParcelable("equipment_response", equipmentResponce);

        } else if (equipmentResponce1 != null) {

            bundle.putParcelable("equipment_faultlog", equipmentResponce1);
            bundle.putParcelableArrayList("equipment_faultlog_list", equipmentResponce3);

        } else if (equipmentResponce2 != null) {

            bundle.putParcelable("equipment_notificationlog", equipmentResponce2);
            bundle.putParcelableArrayList("equipment_notificationlog_list", equipmentResponce4);
        }

        dialogRepairLog.setArguments(bundle);
        dialogRepairLog.setTargetFragment(FragmentRepairLog.this, 0);
        dialogRepairLog.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void dialogUpdateRepairLog() {

        dialogRepairLog = DialogRepairLog.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.edit_repair_log_dialog_title));
        //   bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);
        bundle.putParcelable("repair_log_response", repairLogDetailResponse);
        bundle.putParcelableArrayList("list_location", listLocation);
        bundle.putStringArrayList("list_location_name", listLocationName);

        dialogRepairLog.setArguments(bundle);
        dialogRepairLog.setTargetFragment(FragmentRepairLog.this, 0);
        dialogRepairLog.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "RepairLogAddSubmit", enabled = true)
    public void addRepairLogPositiveClick(Integer locationId, Integer equipmentType, Integer equipment, String repairLogDes, String repairLogCost, ArrayList<Bitmap> listBitmap, String agencyName, String contactPersonName, String repairlogDate, ArrayList<EquipmentFalulList.Response> equipmentFaultlistResponce, ArrayList<Notification.Response> equipmentNotificationlistResponce, String contactNo, ArrayList<Integer> listFaultLogId, ArrayList<Integer> listTaskLogId, String milesHoursType, String milesHours) {

        RepairLogAddSubmit repairLogAddSubmit = new RepairLogAddSubmit(locationId, equipmentType, equipment, repairLogDes, repairLogCost, listBitmap, agencyName, contactPersonName, repairlogDate, equipmentFaultlistResponce, equipmentNotificationlistResponce, contactNo, listFaultLogId, listTaskLogId, milesHoursType, milesHours);

    }

    @AddTrace(name = "RepairLogEditRequest", enabled = true)
    public void editClick(RepairLog.RepairlogResponse repairLogResponse) {

        editClick = true;

        RepairLogEditRequest repairLogEditRequest = new RepairLogEditRequest(repairLogResponse);

    }

    public void deleteClick(RepairLog.RepairlogResponse repairLogResponse, String selectedRepairLogsName) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", repairLogResponse);

        if (repairLogResponse == null) {
            bundle.putString("item", selectedRepairLogsName);
        } else {
            bundle.putString("item", repairLogResponse.getModelName());
        }

        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentRepairLog.this, 7);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "RepairLogUpdateSubmit", enabled = true)
    public void updateRepairLogPositiveClick(Integer locationId, Integer equipmentType, Integer equipment, String repairLogDes, String repairLogCost, ArrayList<Bitmap> listBitmap, RepairLogDetail.RepairLogDetailResponse repairLogResponse, String agencyName, String contactPersonName, String repairlogDate, String contactNumber, String milesHoursType, String milesHours) {

        RepairLogUpdateSubmit repairLogUpdateSubmit = new RepairLogUpdateSubmit(locationId, equipmentType, equipment, repairLogDes, repairLogCost, listBitmap, repairLogResponse, agencyName, contactPersonName, repairlogDate, contactNumber, milesHoursType, milesHours);

    }

    @AddTrace(name = "RepairLogDeleteRequest", enabled = true)
    public void deletePositiveClick(RepairLog.RepairlogResponse repairLogResponse) {

        RepairLogDeleteRequest repairLogDeleteRequest = new RepairLogDeleteRequest(repairLogResponse);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof AdaperRepairLog.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listRepairLog.get(viewHolder.getAdapterPosition()).getModelName();

            // backup of removed item for undo purpose
            deletedItem = listRepairLog.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterRepairLog.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;

            deleteClick(deletedItem, null);

        }

    }

    public void multiDeletePositiveClick() {

        multiDeleteRepairLogRequest(selectedRepairLogsIds);

    }

    @AddTrace(name = "MultiDeleteRepairLogRequest", enabled = true)
    private void multiDeleteRepairLogRequest(ArrayList<Integer> selectedRepairLogsIds) {

        MultiDeleteRepairLogRequest multiDeleteEquipementRequest = new MultiDeleteRepairLogRequest(selectedRepairLogsIds);

    }

    public void deleteNegativeClick(RepairLog.RepairlogResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterRepairLog.restoreItem(deletedItem, deletedIndex);
        }

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
        if (adapterRepairLog.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            //    Toast.makeText(getActivity(), "Read: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //   actionMode = getActivity().startActionMode(actionModeCallback);
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {

        adapterRepairLog.toggleSelection(position);

        int count = adapterRepairLog.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    private class RepairLogAddSubmit implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final Integer equipmentType;
        private final Integer equipment;
        private final String repairLogDes;
        private final ArrayList<Bitmap> listBitmap;
        private final String repairLogCost;
        private static final int LOADER_ADD_REPAIRLOG = 1;
        private final Integer locationId;
        private final String agencyName;
        private final String contactPersonName;
        private final String repairlogDate;
        private final ArrayList<EquipmentFalulList.Response> equipmentFaultlistResponce;
        private final ArrayList<Notification.Response> equipmentNotificationlistResponce;
        private final String contactNo;
        private final ArrayList<Integer> listFaultLogId;
        private final ArrayList<Integer> listTaskLogId;
        private final String milesHoursType;
        private final String milesHours;
        private ProgressDialog progressDialog;

        public RepairLogAddSubmit(Integer locationId, Integer equipmentType, Integer equipment, String repairLogDes, String repairLogCost, ArrayList<Bitmap> listBitmap, String agencyName, String contactPersonName, String repairlogDate, ArrayList<EquipmentFalulList.Response> equipmentFaultlistResponce, ArrayList<Notification.Response> equipmentNotificationlistResponce, String contactNo, ArrayList<Integer> listFaultLogId, ArrayList<Integer> listTaskLogId, String milesHoursType, String milesHours) {

            this.equipmentType = equipmentType;
            this.equipment = equipment;
            this.repairLogDes = repairLogDes;
            this.repairLogCost = repairLogCost;
            this.listBitmap = listBitmap;
            this.locationId = locationId;
            this.agencyName = agencyName;
            this.contactPersonName = contactPersonName;
            this.repairlogDate = repairlogDate;
            this.equipmentFaultlistResponce = equipmentFaultlistResponce;
            this.equipmentNotificationlistResponce = equipmentNotificationlistResponce;
            this.contactNo = contactNo;
            this.listFaultLogId = listFaultLogId;
            this.listTaskLogId = listTaskLogId;
            this.milesHoursType = milesHoursType;
            this.milesHours = milesHours;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();

                String url;
                if (equipmentFaultlistResponce == null && equipmentNotificationlistResponce == null) {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/add";
                } else {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/addWithFault";
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_REPAIRLOG, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    RepairLogAdd repairLogAdd = new RepairLogAdd();
                    repairLogAdd.setCost(Integer.parseInt(repairLogCost));
                    repairLogAdd.setDescription(repairLogDes);
                    repairLogAdd.setEquipment(null);
                    repairLogAdd.setId(0);
                    repairLogAdd.setModifiedDate(Utilities.currentDateTime());
                    repairLogAdd.setModifiedUser(Integer.parseInt(userid));
                    //repairLogAdd.setLocation_id(locationId);
                    repairLogAdd.setAgencyName(agencyName);
                    repairLogAdd.setContactPersonName(contactPersonName);
                    repairLogAdd.setRepairLogDate(convertDate(repairlogDate));
                    repairLogAdd.setContactNumber(contactNo);
                    repairLogAdd.setNoMilesOrHours(milesHours);
                    repairLogAdd.setUnitMilesOrHours(milesHoursType);

                    ArrayList<RepairLogAdd.Faults> faultlist = new ArrayList<RepairLogAdd.Faults>();


                 /*   if (listFaultLogId != null && listFaultLogId.size() != 0) {

                        for (int i = 0; i < listFaultLogId.size(); i++) {

                            RepairLogAdd.Faults faults = new RepairLogAdd.Faults();
                            faults.setFaultLogId(listFaultLogId.get(i));
                            faults.setStatus("close");

                            faultlist.add(faults);
                        }

                    }*/

                    repairLogAdd.setFaults(faultlist);

                    ArrayList<RepairLogAdd.Tasks> tasklist = new ArrayList<RepairLogAdd.Tasks>();

                   /* if (listTaskLogId != null && listTaskLogId.size() != 0) {

                        for (int i = 0; i < listTaskLogId.size(); i++) {

                            RepairLogAdd.Tasks tasks = new RepairLogAdd.Tasks();
                            tasks.setId(listTaskLogId.get(i));
                            tasks.setStatus("close");

                            tasklist.add(tasks);

                        }

                    }*/


                    //  repairLogAdd.setFaults(faultlist);

                    repairLogAdd.setTasks(tasklist);

                    if (equipmentFaultlistResponce != null) {
                        Log.e("equipmentFaultlistResponce", equipmentFaultlistResponce.toString());
                    }


                    if (equipmentNotificationlistResponce != null) {
                        Log.e("equipmentNotificationlistResponce", equipmentNotificationlistResponce.toString());
                    }


                    //   repairLogAdd.setFaultLogList(equipmentFaultlistResponce);


                    //   String taskType = equipmentNotificationlistResponce.get(0).getTasktype();


                    if (equipmentFaultlistResponce != null) {

                        ArrayList<RepairLogAdd.FaultLog> equipmentFaultlist = new ArrayList<RepairLogAdd.FaultLog>();

                        for (int i = 0; i < equipmentFaultlistResponce.size(); i++) {

                            RepairLogAdd.FaultLog faultLog = new RepairLogAdd.FaultLog();
                            faultLog.setFaultLogId(equipmentFaultlistResponce.get(i).getFaultLogId());
                            faultLog.setStatus("close");

                            equipmentFaultlist.add(faultLog);

                        }

                        repairLogAdd.setFaultLogList(equipmentFaultlist);
                    }


                    //  repairLogAdd.setNotificationLogList(equipmentNotificationlistResponce);


                    if (equipmentNotificationlistResponce != null) {

                        String taskType = equipmentNotificationlistResponce.get(0).getTasktype();

                        // service

                        if (taskType.equalsIgnoreCase("service")) {

                            ArrayList<RepairLogAdd.NotificationLog> equipmentNotificationlist = new ArrayList<RepairLogAdd.NotificationLog>();

                            for (int i = 0; i < equipmentNotificationlistResponce.size(); i++) {

                                RepairLogAdd.NotificationLog notificationLogLog = new RepairLogAdd.NotificationLog();
                                notificationLogLog.setId(equipmentNotificationlistResponce.get(i).getTaskid());
                                notificationLogLog.setStatus("close");

                                equipmentNotificationlist.add(notificationLogLog);

                            }

                            repairLogAdd.setNotificationLogList(equipmentNotificationlist);

                        } else {

                            ArrayList<RepairLogAdd.FaultLog> equipmentFaultlist = new ArrayList<RepairLogAdd.FaultLog>();

                            for (int i = 0; i < equipmentNotificationlistResponce.size(); i++) {

                                RepairLogAdd.FaultLog faultLog = new RepairLogAdd.FaultLog();
                                faultLog.setFaultLogId(equipmentNotificationlistResponce.get(i).getFaultLogId());
                                faultLog.setStatus("close");

                                equipmentFaultlist.add(faultLog);

                            }

                            repairLogAdd.setFaultLogList(equipmentFaultlist);

                        }

                    }


                    ArrayList<RepairLogAdd.RepairLogImages> listRepairLogImages = new ArrayList<RepairLogAdd.RepairLogImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        RepairLogAdd.RepairLogImages images = new RepairLogAdd.RepairLogImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        images.setBitmapstring(encodedString);
                        images.setId(0);
                        images.setModifiedDate(Utilities.currentDateTime());
                        images.setModifiedUser(Integer.parseInt(userid));

                        listRepairLogImages.add(images);

                    }

                    repairLogAdd.setRepairLogImages(listRepairLogImages);
                    repairLogAdd.setTemp_equipment_id(equipment);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(repairLogAdd);
                    Log.i(TAG_REPAIR, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                //  Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //   .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(104);

            } else {

                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private String convertDate(String repairlogDate) {

        Date convertedDate = null;

        String oldFormat = "dd-MM-yyyy";
        String newFormat = "yyyy-MM-dd";

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

    private class RepairLogRequest implements LoaderManager.LoaderCallbacks<RepairLog> {

        private static final int LOADER_GET_REPAIR_LOG = 1;
        private ProgressDialog progressDialog;

        public RepairLogRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                String userLocationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                String user_Type = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);
                String userId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = null;

                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/location/" + URLEncoder.encode("" + userId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

             /*   if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(user_Type)) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/location/" + URLEncoder.encode("" + userId.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(user_Type)) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog" + "/retriveAll" + URLEncoder.encode("" + userId.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
*/
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_REPAIR_LOG, args, this);

            } else {

                progressDialog.dismiss();
                //       Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //             .show();


                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RepairLog> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));
                    return new RestLoader<RepairLog>(getActivity(), httpWrapper, RepairLog.class);

                }

            } else {
                progressDialog.dismiss();
                //    Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RepairLog> loader, RepairLog data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listRepairLog.clear();

                for (RepairLog.RepairlogResponse repairlogResponse : data.getResponse()) {

                    listRepairLog.add(repairlogResponse);

                }

                handler.sendEmptyMessage(103);

            } else {

                //   Snackbar.make(coordinateRepairLog, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RepairLog> loader) {

        }

    }

    private class RepairLogDeleteRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_EQUIPMENT = 1;
        private final RepairLog.RepairlogResponse repairLogResponse;
        private ProgressDialog progressDialog;

        public RepairLogDeleteRequest(RepairLog.RepairlogResponse repairLogResponse) {

            this.repairLogResponse = repairLogResponse;

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                //  String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/deactive" + "/" + response.getId() + "/" + userid;

                String url = null;
                try {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/remove" + "/" + URLEncoder.encode(repairLogResponse.getRepairLogId().toString(), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(userid), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();
                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));


            }


        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {
            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(105);

            } else {

                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());

                handler.sendEmptyMessage(105);
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class RepairLogUpdateSubmit implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final Integer equipmentType;
        private final Integer equipment;
        private final String repairLogDes;
        private final ArrayList<Bitmap> listBitmap;
        private final String repairLogCost;
        private static final int LOADER_UPDATE_REPAIRLOG = 1;
        private final RepairLogDetail.RepairLogDetailResponse repairLogResponse;
        private final Integer locationId;
        private final String agencyName;
        private final String contactPersonName;
        private final String repairlogDate;
        private final String contactNumber;
        private final String milesHoursType;
        private final String milesHours;
        private ProgressDialog progressDialog;

        public RepairLogUpdateSubmit(Integer locationId, Integer equipmentType, Integer equipment, String repairLogDes, String repairLogCost, ArrayList<Bitmap> listBitmap, RepairLogDetail.RepairLogDetailResponse repairLogResponse, String agencyName, String contactPersonName, String repairlogDate, String contactNumber, String milesHoursType, String milesHours) {

            this.equipmentType = equipmentType;
            this.equipment = equipment;
            this.repairLogDes = repairLogDes;
            this.repairLogCost = repairLogCost;
            this.listBitmap = listBitmap;
            this.repairLogResponse = repairLogResponse;
            this.locationId = locationId;
            this.agencyName = agencyName;
            this.contactPersonName = contactPersonName;
            this.repairlogDate = repairlogDate;
            this.contactNumber = contactNumber;
            this.milesHoursType = milesHoursType;
            this.milesHours = milesHours;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());  update
                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_REPAIRLOG, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    RepairLogAdd repairLogAdd = new RepairLogAdd();

                    repairLogAdd.setCost(Integer.parseInt(repairLogCost));
                    repairLogAdd.setDescription(repairLogDes);
                    repairLogAdd.setEquipment(null);
                    repairLogAdd.setId(repairLogResponse.getId());
                    repairLogAdd.setModifiedDate(Utilities.currentDateTime());
                    repairLogAdd.setModifiedUser(Integer.parseInt(userid));
                    //   repairLogAdd.setLocation_id(locationId);
                    repairLogAdd.setAgencyName(agencyName);
                    repairLogAdd.setContactPersonName(contactPersonName);
                    repairLogAdd.setRepairLogDate(convertDate(repairlogDate));
                    repairLogAdd.setContactNumber(contactNumber);
                    repairLogAdd.setNoMilesOrHours(milesHours);
                    repairLogAdd.setUnitMilesOrHours(milesHoursType);


                    ArrayList<RepairLogAdd.RepairLogImages> listRepairLogImages = new ArrayList<RepairLogAdd.RepairLogImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        RepairLogAdd.RepairLogImages images = new RepairLogAdd.RepairLogImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        images.setBitmapstring(encodedString);
                        images.setId(0);
                        images.setModifiedDate(Utilities.currentDateTime());
                        images.setModifiedUser(Integer.parseInt(userid));

                        listRepairLogImages.add(images);

                    }

                    repairLogAdd.setRepairLogImages(listRepairLogImages);
                    repairLogAdd.setTemp_equipment_id(equipment);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(repairLogAdd);
                    Log.i(TAG_REPAIR, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                //     Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(104);

            } else {

                //   Snackbar.make(coordinateRepairLog, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class RepairLogEditRequest implements LoaderManager.LoaderCallbacks<RepairLogDetail> {

        private static final int LOADER_GET_REPAIR_LOG_DETAIL = 1;
        private ProgressDialog progressDialog;

        public RepairLogEditRequest(RepairLog.RepairlogResponse repairLogResponse) {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/" + URLEncoder.encode(repairLogResponse.getRepairLogId().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_REPAIR_LOG_DETAIL, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();


                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));
            }

        }


        @Override
        public Loader<RepairLogDetail> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<RepairLogDetail>(getActivity(), httpWrapper, RepairLogDetail.class);

                }

            } else {
                progressDialog.dismiss();
                //   Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();
                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));
            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RepairLogDetail> loader, RepairLogDetail data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                repairLogDetailResponse = data.getResponse();

                handler.sendEmptyMessage(106);

            } else {

                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RepairLogDetail> loader) {

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

                //   progressDialog.dismiss();
                //     Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //           .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));


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

                //  Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));


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

                adapterRepairLog.updateImage(loaderId, path, imageBitmap);

            } else {

                //     Snackbar.make(coordinateRepairLog, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

    private class GetLocationRequest implements LoaderManager.LoaderCallbacks<Location> {

        private static final int LOADER_GET_LOCATION = 1;
        private final int i;

        private ProgressDialog progressDialog;

        public GetLocationRequest(int i) {

            this.i = i;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);

            } else {

                progressDialog.dismiss();

                //  Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

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

                //       Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //             .show();
                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));

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
                if (i == 1) {
                    handler.sendEmptyMessage(108);
                } else {
                    handler.sendEmptyMessage(109);
                }


            } else {

                //    Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbarView(getView(), "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {

        }
    }

    private class MultiDeleteRepairLogRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedRepairLogsIds;
        private static final int LOADER_DELETE_MULTIPLE_REPAIRLOG = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteRepairLogRequest(List<Integer> selectedRepairLogsIds) {

            this.selectedRepairLogsIds = selectedRepairLogsIds;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;

                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "repairlog/remove/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MULTIPLE_REPAIRLOG, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    RepairLogIds repairLogIds = new RepairLogIds();

                    repairLogIds.setIds(selectedRepairLogsIds);

                    String jsonString = gson.toJson(repairLogIds);

                    Log.e("jsonString", "" + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateRepairLog, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //                      .show();


                Utilities.showSnackbar(coordinateRepairLog, getActivity().getResources().getString(R.string.network_connection_failed));


            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //               Snackbar.make(coordinateRepairLog, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());
                handler.sendEmptyMessage(105);

            } else {

//                Snackbar.make(coordinateRepairLog, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateRepairLog, "" + data.getMessage());

                handler.sendEmptyMessage(105);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    public class RepairLogIds {

        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }

}

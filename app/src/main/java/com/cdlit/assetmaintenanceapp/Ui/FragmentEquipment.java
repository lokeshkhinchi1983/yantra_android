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

import com.cdlit.assetmaintenanceapp.Adapter.AdapterEquipment;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEquipmentModelNotRequired;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEquipmentModelRequired;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentTimeline;
import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentTypeClone;
import com.cdlit.assetmaintenanceapp.Model.AddEquipment3Model;
import com.cdlit.assetmaintenanceapp.Model.AddEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Model.Equipment;
import com.cdlit.assetmaintenanceapp.Model.EquipmentAddReturn;
import com.cdlit.assetmaintenanceapp.Model.EquipmentAddReturnEquipmentImages;
import com.cdlit.assetmaintenanceapp.Model.EquipmentModelAddReturn;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTimeline;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.Location;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.RepairLogEquipmentType;
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
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 13-06-2017.
 */

public class FragmentEquipment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private static final String TAG = FragmentEquipment.class.getSimpleName();
    private FloatingActionButton fabAddEquipment;
    private CoordinatorLayout coordinateEquipment;
    private ArrayList<LocationResponse> listLocation;
    private Handler handler;
    private ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse> listEquipmentType;
    private RecyclerView recyclerEquipment;
    private AdapterEquipment adapterEquipment;
    private ArrayList<EquipmentResponse> listEquipment;
    private SwipeRefreshLayout swipeRefreshEquipment;
    private int flag;
    private EquipmentResponse equipmentResponse;
    private Set<String> listPrivilege;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<EquipmentTimeline.EquipmentTimelineResponse> listEquipmentTimeline;
    private DialogEquipmentTimeline dialogEquipmentTimeline;
    private Integer equipmentModleId;
    private DialogAddEquipmentModelNotRequired dialogAddEquipmentModelNotRequired;
    private EquipmentResponse deletedItem;
    private int deletedIndex;
    public ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback1;
    private ItemTouchHelper mItemTouchHelper;
    private DialogAddEquipmentModelRequired dialogAddEquipmentModelRequired;
    private boolean swipFlag;
    private ArrayList<Integer> selectedEquipmentsIds;
    private DialogEquipmentTypeClone dialogCloneEquipmentType;
    private Integer cloneEquipmentId = null;
    private EquipmentResponse equipmentCloneResponse;
    public MenuItem searchItem;
    private DialogEquipmentModel3 dialogEquipmentModel3;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_equipment, container, false);

        return rootView;

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

        List<EquipmentResponse> filteredValues = new ArrayList<EquipmentResponse>(listEquipment);

        for (EquipmentResponse value : listEquipment) {

            if (!value.getModelNo().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }

        }

        adapterEquipment = new AdapterEquipment(FragmentEquipment.this, filteredValues, this);

        recyclerEquipment.setAdapter(adapterEquipment);
        checkEmptyView(adapterEquipment);
        return false;

    }

    private void resetSearch() {

        adapterEquipment = new AdapterEquipment(FragmentEquipment.this, listEquipment, this);

        recyclerEquipment.setAdapter(adapterEquipment);

        checkEmptyView(adapterEquipment);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Asset Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentEquipment.class.getName());

        fabAddEquipment = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_equipment);

        coordinateEquipment = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_equipment);

        swipeRefreshEquipment = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_equipmen);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshEquipment.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshEquipment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getEquipmentRequest();

                swipeRefreshEquipment.setRefreshing(false);

            }
        });


        listLocation = new ArrayList<LocationResponse>();

        listEquipmentType = new ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse>();

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {

            fabAddEquipment.setVisibility(View.GONE);

        }


        fabAddEquipment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                cloneEquipmentId = null;

                flag = 0;

                getLocationRequiest();

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest();

                } else if (msg.what == 101) {

                    //  adapter notify
                    adapterEquipment.notifyDataSetChanged();
                    checkEmptyView(adapterEquipment);
                    //
                    //  openDialogAddEquipmentModel();
                    loadImages();

                } else if (msg.what == 102) {

                    //rakesh
                    openDialogAddEquipmentModel();

                } else if (msg.what == 103) {

                    //rakesh
                    dialogAddEquipmentModelRequired.dismiss();

                    openDialogAddEquipmentNotRequiredModel();

                } else if (msg.what == 104) {

                    //rakesh
                    openDialogUpdateEquipmentModel();

                } else if (msg.what == 105) {

                    getEquipmentRequest();

                } else if (msg.what == 106) {

                    timelineDialog();

                } else if (msg.what == 107) {

                    dialogAddEquipmentModelNotRequired.dismiss();

                    openDialogAddEquipmentModel3();

                    //   getEquipmentRequest();

                } else if (msg.what == 108) {

                    dialogAddEquipmentModelNotRequired.dismiss();

                    openDialogEditEquipmentModel3();

                    //   getEquipmentRequest();

                } else if (msg.what == 109) {

                    dialogAddEquipmentModelRequired.dismiss();

                    openDialogUpdateEquipmentNotRequiredModel();

                }


                return false;
            }
        });

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        listEquipment = new ArrayList<EquipmentResponse>();

        recyclerEquipment = (RecyclerView) getActivity().findViewById(R.id.recycler_equipment);

        adapterEquipment = new AdapterEquipment(FragmentEquipment.this, listEquipment, this);

        recyclerEquipment.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipment.setLayoutManager(mLayoutManager);

        recyclerEquipment.setAdapter(adapterEquipment);

        checkEmptyView(adapterEquipment);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param


        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerEquipment);
            itemTouchHelperCallback.setSwipeEnable(true);

        }

        actionModeCallback = new ActionModeCallback();

        getEquipmentRequest();

    }

    public void multiDeletePositiveClick() {

        multiDeleteEquipmentRequest(selectedEquipmentsIds);

    }

    public void addEquipmentTypeClone(String addEquipmentType, EquipmentResponse equipmentTypeResponse) {

        EquipmentTypeClone equipmentTypeClone = new EquipmentTypeClone(addEquipmentType, equipmentTypeResponse);

    }

    public void addEquipmentNegativeClick() {

        equipmentCloneResponse = null;

    }

    public void addEquipmentNotRequiredNegativeClick() {

        handler.sendEmptyMessage(107);

    }

    public void addEquipment3PositiveClick(ArrayList<AddEquipmentModel3> listEquipmentModel3, String title) {

        AddEquipment3Request addEquipment3Request = new AddEquipment3Request(listEquipmentModel3, title, null);

    }

    public void editEquipment3PositiveClick(ArrayList<AddEquipmentModel3> listEquipmentModel3, String title, EquipmentResponse equipmentResponse) {

        AddEquipment3Request addEquipment3Request = new AddEquipment3Request(listEquipmentModel3, title, equipmentResponse);


    }


    public void updateEquipment3NextClick() {

        handler.sendEmptyMessage(108);

    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_equipment, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshEquipment.setEnabled(false);

          /*  adapterEquipment.MyViewHolder.imageEquipment.setEnabled(false);
            adapterEquipment.MyViewHolder.imageEquipment.setClickable(false);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {

                fabAddEquipment.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("" + TAG, "onPrepareActionMode");

            if (adapterEquipment.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_TIMELINE_READ)) {
                    menu.getItem(1).setVisible(false);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(false);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(3).setVisible(false);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(4).setVisible(false);
                }

            } else {

                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_TIMELINE_READ)) {
                    menu.getItem(1).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(3).setVisible(false);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {
                    menu.getItem(4).setVisible(true);
                }

            }

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            Log.e("" + TAG, "onActionItemClicked");
            switch (item.getItemId()) {
                case R.id.action_delete:

                    deleteItem();
                    mode.finish();
                    return true;

                case R.id.action_timeline:

                    timelineItem();
                    mode.finish();
                    return true;

                case R.id.action_edit:

                    editItem();
                    mode.finish();
                    return true;

                case R.id.action_repairlog:

                    repairlogItem();
                    mode.finish();
                    return true;

                case R.id.action_clone:

                    List<Integer> selectedItemPositions =
                            adapterEquipment.getSelectedItems();

                    Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

                    if (listEquipment.get(selectedItemPositions.get(0)).getIsCheckList() == 1) {

                        cloneItem();

                    } else {

                        Utilities.showSnackbarView(getView(), "Clone option is not available for this equipment type");

                    }

                    mode.finish();

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            Log.e("" + TAG, "onDestroyActionMode");

            adapterEquipment.clearSelections();
            swipeRefreshEquipment.setEnabled(true);

           /* adapterEquipment.holder.imageEquipment.setEnabled(true);
            adapterEquipment.holder.imageEquipment.setClickable(true);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_WRITE)) {

                fabAddEquipment.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerEquipment.post(new Runnable() {
                @Override
                public void run() {

                    adapterEquipment.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();

                }
            });

        }

    }

    private void cloneItem() {

        List<Integer> selectedItemPositions =
                adapterEquipment.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        cloneClick(listEquipment.get(selectedItemPositions.get(0)));

    }

    private void cloneClick(EquipmentResponse equipmentResponse) {

        equipmentCloneResponse = equipmentResponse;
        cloneEquipmentId = equipmentResponse.getId();

        flag = 0;

        getLocationRequiest();

      /*  dialogCloneEquipmentType = DialogEquipmentTypeClone.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.equipmenttype_clone_title));
        bundle.putParcelable("equipment_type_response", equipmentResponse);

        dialogCloneEquipmentType.setArguments(bundle);
        dialogCloneEquipmentType.setTargetFragment(FragmentEquipment.this, 0);
        dialogCloneEquipmentType.show(getActivity().getSupportFragmentManager(), "dialog");*/

    }

    private void repairlogItem() {

        List<Integer> selectedItemPositions =
                adapterEquipment.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

        EquipmentResponse response = listEquipment.get(selectedItemPositions.get(0));

        context.menuClick("Maintenance Log", response, null, null, null, null);

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterEquipment.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listEquipment.get(selectedItemPositions.get(0)));

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
        if (adapterEquipment.getSelectedItemCount() > 0) {
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

        adapterEquipment.toggleSelection(position);

        int count = adapterEquipment.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    // deleting the messages from recycler view
    private void deleteItem() {

        adapterEquipment.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterEquipment.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listEquipment.get(selectedItemPositions.get(0)), null);

        } else {

            selectedEquipmentsIds = new ArrayList<>();
            ArrayList<String> selectedEquipmentsName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedEquipmentsIds.add(listEquipment.get(pos).getId());
                selectedEquipmentsName.add(listEquipment.get(pos).getModelNo());

            }

            String deleteEquipmentName = "";

            for (int i = 0; i < selectedEquipmentsName.size(); i++) {

                if (i == (selectedEquipmentsName.size() - 1)) {
                    deleteEquipmentName = deleteEquipmentName + selectedEquipmentsName.get(i);
                } else {
                    deleteEquipmentName = deleteEquipmentName + selectedEquipmentsName.get(i) + " , ";
                }

            }

            deleteClick(null, deleteEquipmentName);

        }

    }

    @AddTrace(name = "MultiDeleteEquipementRequest", enabled = true)
    private void multiDeleteEquipmentRequest(List<Integer> selectedEquipmentsIds) {

        MultiDeleteEquipementRequest multiDeleteEquipementRequest = new MultiDeleteEquipementRequest(selectedEquipmentsIds);

    }

    private void timelineItem() {

        List<Integer> selectedItemPositions =
                adapterEquipment.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        timeLineClick(listEquipment.get(selectedItemPositions.get(0)));

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof AdapterEquipment.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listEquipment.get(viewHolder.getAdapterPosition()).getModelNo();

            // backup of removed item for undo purpose
            deletedItem = listEquipment.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterEquipment.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;
            deleteClick(deletedItem, null);

        }

    }


    @AddTrace(name = "LocationRequiest", enabled = true)
    private void getLocationRequiest() {

        GetLocationRequest getLocationRequest = new GetLocationRequest();

    }


    @AddTrace(name = "EquipmentRequest", enabled = true)
    private void getEquipmentRequest() {

        GetEuipmentRequest getEuipmentRequest = new GetEuipmentRequest();

    }

    private void openDialogAddEquipmentNotRequiredModel() {

        dialogAddEquipmentModelNotRequired = DialogAddEquipmentModelNotRequired.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_equipment_model_dialog_title));

        bundle.putParcelableArrayList("list_location", listLocation);
        bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);

        dialogAddEquipmentModelNotRequired.setArguments(bundle);
        dialogAddEquipmentModelNotRequired.setTargetFragment(FragmentEquipment.this, 0);
        dialogAddEquipmentModelNotRequired.show(getActivity().getSupportFragmentManager(), "dialog");

    }


    private void openDialogUpdateEquipmentNotRequiredModel() {

        dialogAddEquipmentModelNotRequired = DialogAddEquipmentModelNotRequired.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.update_equipment_model_dialog_title));

        //  bundle.putParcelableArrayList("list_location", listLocation);
        //   bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);
        bundle.putParcelable("equipment_response", equipmentResponse);

        dialogAddEquipmentModelNotRequired.setArguments(bundle);
        dialogAddEquipmentModelNotRequired.setTargetFragment(FragmentEquipment.this, 0);
        dialogAddEquipmentModelNotRequired.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void timelineDialog() {

        dialogEquipmentTimeline = DialogEquipmentTimeline.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.equipment_timeline_dialog_title));

        bundle.putParcelableArrayList("list_equipment_timeline", listEquipmentTimeline);

        dialogEquipmentTimeline.setArguments(bundle);
        dialogEquipmentTimeline.setTargetFragment(FragmentEquipment.this, 0);
        dialogEquipmentTimeline.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void loadImages() {

        for (int i = 0; i < adapterEquipment.getItemCount(); i++) {

            String imagePath;

            if (listEquipment.get(i).getEquipmentImages() == null || listEquipment.get(i).getEquipmentImages().size() == 0) {

                imagePath = null;

            } else {

                imagePath = listEquipment.get(i).getEquipmentImages().get(0).getImagePath();

            }

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    getImageRequest(i, imagePath);

                }

            }

        }

    }

    @AddTrace(name = "EquipmentImageRequest", enabled = true)
    private void getImageRequest(int i, String imagePath) {

        ImageRequest imageRequest = new ImageRequest(i, imagePath);

    }

    private void openDialogUpdateEquipmentModel() {

        dialogAddEquipmentModelRequired = DialogAddEquipmentModelRequired.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.update_equipment_model_dialog_title));

        bundle.putParcelableArrayList("list_location", listLocation);
        bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);
        bundle.putParcelable("equipment_response", equipmentResponse);
        bundle.putStringArrayList("email_list", (ArrayList<String>) equipmentResponse.getEmailId());

        dialogAddEquipmentModelRequired.setArguments(bundle);
        dialogAddEquipmentModelRequired.setTargetFragment(FragmentEquipment.this, 0);
        dialogAddEquipmentModelRequired.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openDialogAddEquipmentModel() {

        dialogAddEquipmentModelRequired = DialogAddEquipmentModelRequired.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_equipment_model_dialog_title));

        bundle.putParcelableArrayList("list_location", listLocation);
        bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);

        bundle.putParcelable("equipment_clone", equipmentCloneResponse);

        dialogAddEquipmentModelRequired.setArguments(bundle);
        dialogAddEquipmentModelRequired.setTargetFragment(FragmentEquipment.this, 0);
        dialogAddEquipmentModelRequired.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openDialogAddEquipmentModel3() {

        dialogEquipmentModel3 = DialogEquipmentModel3.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_equipment_model_3_dialog_title));

        dialogEquipmentModel3.setArguments(bundle);
        dialogEquipmentModel3.setTargetFragment(FragmentEquipment.this, 0);
        dialogEquipmentModel3.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openDialogEditEquipmentModel3() {

        dialogEquipmentModel3 = DialogEquipmentModel3.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.edit_equipment_model_3_dialog_title));
        bundle.putParcelable("equipment_response", equipmentResponse);

        dialogEquipmentModel3.setArguments(bundle);
        dialogEquipmentModel3.setTargetFragment(FragmentEquipment.this, 0);
        dialogEquipmentModel3.show(getActivity().getSupportFragmentManager(), "dialog");


    }

    public void editClick(EquipmentResponse equipmentResponse) {

        cloneEquipmentId = null;

        flag = 1;

        this.equipmentResponse = equipmentResponse;

        getLocationRequiest();

    }

    public void deleteClick(EquipmentResponse equipmentResponse, String selectedEquipmentsName) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", equipmentResponse);

        if (equipmentResponse == null) {
            bundle.putString("item", selectedEquipmentsName);
        } else {
            bundle.putString("item", equipmentResponse.getModelNo());
        }

        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentEquipment.this, 4);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }


    @AddTrace(name = "AddEquipmentRequest", enabled = true)
    public void addEquipmentPositiveClick(int locationId, Integer equipmentTypeId, String equipmentModel,/* String inspectionDuration,*/ ArrayList<String> listEmail /* , String dueServiceDate, String frequency, String freqDay*/) {

        AddEquipmentRequest addEquipmentRequest = new AddEquipmentRequest(locationId, equipmentTypeId, equipmentModel,/* inspectionDuration,*/ listEmail/*, dueServiceDate, frequency, freqDay*/);

    }

    @AddTrace(name = "UpdateEquipmentRequest", enabled = true)
    public void editEquipmentPositiveClick(int locationId, Integer equipmentTypeId, String equipmentModel, /*String inspectionDuration,*/ ArrayList<String> listEmail/*, String dueServiceDate*/) {

        UpdateEquipmentRequest updateEquipmentRequest = new UpdateEquipmentRequest(locationId, equipmentTypeId, equipmentModel, /*inspectionDuration,*/ listEmail /*, dueServiceDate*/);

    }

    @AddTrace(name = "EquipmentDeactiveRequest", enabled = true)
    public void deletePositiveClick(EquipmentResponse response) {

        EquipmentDeactiveRequest categoryDeactiveRequest = new EquipmentDeactiveRequest(response);

    }

    @AddTrace(name = "EquipmentTimelineRequest", enabled = true)
    public void timeLineClick(EquipmentResponse equipmentResponse) {

        listEquipmentTimeline = new ArrayList<EquipmentTimeline.EquipmentTimelineResponse>();

        EquipmentTimelineRequest equipmentTimelineRequest = new EquipmentTimelineRequest(equipmentResponse);

    }

    public void addEquipmentNotRequiredPositiveClick(String equipmentModedes, ArrayList<Bitmap> listBitmap, String manufactureDate, String equipmentModelSerialno, String expiryDate, /*String lastServiceDate,*/ String reminderDuration, String remarks) {

        AddEquipmentNotRequiredRequest addEquipmentRequest = new AddEquipmentNotRequiredRequest(equipmentModedes, listBitmap, manufactureDate, equipmentModelSerialno, expiryDate, /*lastServiceDate,*/ reminderDuration, remarks);

    }

    public void editEquipmentNotRequiredPositiveClick(String equipmentModedes, ArrayList<Bitmap> listBitmap, String manufactureDate, String equipmentModelSerialno, String expiryDate, /*String lastServiceDate, */String reminderDuration, String remarks) {

        UpdateEquipmentNotRequiredRequest addEquipmentRequest = new UpdateEquipmentNotRequiredRequest(equipmentModedes, listBitmap, manufactureDate, equipmentModelSerialno, expiryDate,/* lastServiceDate, */reminderDuration, remarks);

    }

    public void updateEquipmentNegativeClick() {
        handler.sendEmptyMessage(109);
    }

    public void deleteNegativeClick(EquipmentResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterEquipment.restoreItem(deletedItem, deletedIndex);
        }

    }


    private class GetLocationRequest implements LoaderManager.LoaderCallbacks<Location> {

        private static final int LOADER_GET_LOCATION = 1;

        private ProgressDialog progressDialog;

        public GetLocationRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);


            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

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

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listLocation.clear();

                String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);
                String location_Id = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);

                for (LocationResponse location : data.getResponse()) {


                    if (equipmentCloneResponse != null) {

                        if (location.getId() == equipmentCloneResponse.getLocation().getId()) {

                            listLocation.add(location);

                        }


                    } else {

                        if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                            if (location.getId() == Integer.parseInt(location_Id)) {

                                listLocation.add(location);

                            }

                        } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                            listLocation.add(location);

                        }
                    }


                }

                handler.sendEmptyMessage(100);

            } else {

                //  Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipment, data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {


        }


    }

    private class GetEuipmentRequest implements LoaderManager.LoaderCallbacks<Equipment> {

        private static final int LOADER_GET_EQUIPMENT = 1;
        private ProgressDialog progressDialog;

        public GetEuipmentRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                String userLocationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                String userId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/all/" + URLEncoder.encode(userLocationId.toString(), "UTF-8");

                    } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/all";

                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);

                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();

                //     Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //           .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<Equipment> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<Equipment>(getActivity(), httpWrapper, Equipment.class);

                }

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Equipment> loader, Equipment data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipment.clear();

                for (EquipmentResponse equipmentResponse : data.getResponse()) {

                    listEquipment.add(equipmentResponse);

                }

                handler.sendEmptyMessage(101);

            } else {

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Equipment> loader) {


        }

    }

    private class EquipmentTypeRequest implements LoaderManager.LoaderCallbacks<RepairLogEquipmentType> {

        private static final int LOADER_GET_EQUIPMENT_TYPE = 1;
        private ProgressDialog progressDialog;

        public EquipmentTypeRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/getListofEquipmentTypeRevised";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

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
                //   Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<RepairLogEquipmentType> loader, RepairLogEquipmentType data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentType.clear();
                //   listEquipmentTypeName.clear();

                //   listEquipmentTypeName.add("Select Equipment Type");
                for (RepairLogEquipmentType.RepairLogEquipmentTypeResponse equipmentTypeResponse : data.getResponse()) {


                    if (equipmentCloneResponse != null) {

                        if (equipmentTypeResponse.getId() == equipmentCloneResponse.getEquipmentType().getId()) {

                            listEquipmentType.add(equipmentTypeResponse);

                        }

                    } else {

                        listEquipmentType.add(equipmentTypeResponse);
                        //     listEquipmentTypeName.add(equipmentTypeResponse.getName());

                    }


                }

                progressDialog.dismiss();


                //   handler.sendEmptyMessage(101);

                // add for 0
                if (flag == 0) {
                    handler.sendEmptyMessage(102);
                } else {
                    handler.sendEmptyMessage(104);
                }


            } else {

                progressDialog.dismiss();

                //   Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());
            }

        }

        @Override
        public void onLoaderReset(Loader<RepairLogEquipmentType> loader) {

        }
    }

    private class AddEquipment3Request implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private final String title;
        private final EquipmentResponse equipmentResponse;

        private ArrayList<AddEquipmentModel3> listEquipmentModel3;
        private ProgressDialog progressDialog;

        public AddEquipment3Request(ArrayList<AddEquipmentModel3> listEquipmentModel3, String title, EquipmentResponse equipmentResponse) {

            progressDialog = Utilities.startProgressDialog(getActivity());

            this.title = title;

            this.equipmentResponse = equipmentResponse;

            if (Utilities.isNetworkAvailable(getActivity())) {

                this.listEquipmentModel3 = listEquipmentModel3;

                Bundle args = new Bundle();

                String url;

                if (title.equalsIgnoreCase(getResources().getString(R.string.edit_equipment_model_3_dialog_title))) {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "serviceType/update";


                } else {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "serviceType/add";


                }


                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    ArrayList<AddEquipment3Model.ServiceTypes> listEquipment = new ArrayList<>();


                    for (int i = 0; i < listEquipmentModel3.size(); i++) {

                        AddEquipment3Model.ServiceTypes serviceTypes = new AddEquipment3Model.ServiceTypes();

                        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_equipment_model_3_dialog_title))) {

                            serviceTypes.setEquipmentId(equipmentResponse.getId());

                        } else {

                            serviceTypes.setEquipmentId(equipmentModleId);

                        }

                        serviceTypes.setFrequency(listEquipmentModel3.get(i).getServiceFreq());
                        serviceTypes.setFrequency_no(Integer.parseInt(listEquipmentModel3.get(i).getServiceNo()));

                        serviceTypes.setId(0);

                        serviceTypes.setLast_check_date(convertDateFormate(listEquipmentModel3.get(i).getLastServiceDate()));
                        serviceTypes.setNext_check_date(convertDateFormate(listEquipmentModel3.get(i).getNextServiceDate()));
                        serviceTypes.setService_name(listEquipmentModel3.get(i).getServiceName());
                        serviceTypes.setUserid(Integer.parseInt(userid));

                        listEquipment.add(serviceTypes);

                    }

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    AddEquipment3Model addEquipment3Model = new AddEquipment3Model();
                    addEquipment3Model.setServiceTypes(listEquipment);

                    String jsonString = gson.toJson(addEquipment3Model);

                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(105);

            } else {

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class AddEquipmentRequest implements LoaderManager.LoaderCallbacks<EquipmentModelAddReturn> {

        private static final int LOADER_ADD_CATEGORY = 1;
        //   private String frequency;
        //    private String freqDay;
        //private String dueServiceDate;
        private ArrayList<String> listEmail;
        //  private String dueServiceDateInterval;
        //private String inspectionDuration;
        private int locationId;
        private Integer equipmentTypeId;
        private String equipmentModel;
        private ProgressDialog progressDialog;

        public AddEquipmentRequest(int locationId, Integer equipmentTypeId, String equipmentModel, /*String inspectionDuration,*/ ArrayList<String> listEmail/*, String dueServiceDate, String frequency, String freqDay*/) {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                this.locationId = locationId;
                this.equipmentTypeId = equipmentTypeId;
                this.equipmentModel = equipmentModel;
                //     this.dueServiceDateInterval = dueServiceDateInterval;
                // this.inspectionDuration = inspectionDuration;
                this.listEmail = listEmail;

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/add_required";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }


        @Override
        public Loader<EquipmentModelAddReturn> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentAddReturn equipmentAddReturn = new EquipmentAddReturn();

                    equipmentAddReturn.setModifiedDate(Utilities.currentDateTime());

                    equipmentAddReturn.setEid(equipmentTypeId);
                    equipmentAddReturn.setLoc_id(locationId);
                    equipmentAddReturn.setModelNo(equipmentModel);
                    equipmentAddReturn.setModifiedUser(Integer.parseInt(userid));

                    equipmentAddReturn.setEmailId(listEmail);

                    equipmentAddReturn.setEquipmentCloneId(cloneEquipmentId);


                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<EquipmentModelAddReturn>(getActivity(), httpWrapper, EquipmentModelAddReturn.class);

                }

            } else {

                progressDialog.dismiss();

                // Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentModelAddReturn> loader, EquipmentModelAddReturn data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                equipmentModleId = data.getResponse().getId();
                handler.sendEmptyMessage(103);

            } else {

                //    Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentModelAddReturn> loader) {

        }
    }

    private class EquipmentDeactiveRequest implements LoaderManager.LoaderCallbacks<RestResponse> {


        private static final int LOADER_DEACTIVE_EQUIPMENT = 1;
        private final EquipmentResponse response;
        private ProgressDialog progressDialog;

        public EquipmentDeactiveRequest(EquipmentResponse response) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.response = response;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                //  String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/deactive" + "/" + response.getId() + "/" + userid;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/remove" + "/" + URLEncoder.encode(response.getId().toString(), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(userid), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //        .show();


                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));


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
//                Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //   .show();


                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));


            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(105);

            } else {

                //   Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

                handler.sendEmptyMessage(105);
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class UpdateEquipmentRequest implements LoaderManager.LoaderCallbacks<EquipmentModelAddReturn> {

        private static final int LOADER_UPDATE_EQUIPMENT = 1;
        private final int locationId;
        private final Integer equipmentTypeId;
        private final String equipmentModel;
        /*  private final String equipmentModelDes;
          private final EquipmentResponse equipmentResponse;
          private final ArrayList<Bitmap> listBitmap;
          private final String serviceDate;
          private final String manufactureDate;
          private final String serviceType;
          private final String serialNo;
          private final String expiryDate;
          private final String lastServiceDate;
          private final String dueServiceDate;*/
        //   private final String serviceInterval;
        //    private final String inspectionDuration;
        private final ArrayList<String> listEmail;
        private Date dateManufacturerDate;
        private ProgressDialog progressDialog;
        //   private String dueServiceDate;

        public UpdateEquipmentRequest(int locationId, Integer equipmentTypeId, String equipmentModel, /*String inspectionDuration,*/ ArrayList<String> listEmail/*, String dueServiceDate */) {

            this.locationId = locationId;
            this.equipmentTypeId = equipmentTypeId;
            this.equipmentModel = equipmentModel;

           /* this.equipmentModelDes = equipmentModelDes;
            this.equipmentResponse = equipmentResponse;
            this.listBitmap = listBitmap;
            this.serviceDate = serviceDate;
            this.manufactureDate = manufactureDate;
            this.serviceType = serviceType;
            this.serialNo = serialNo;
            this.expiryDate = expiryDate;
            this.lastServiceDate = lastServiceDate;
            this.dueServiceDate = dueServiceDate;*/

            //  this.serviceInterval = serviceInterval;
            //  this.inspectionDuration = inspectionDuration;
            this.listEmail = listEmail;
            //    this.dueServiceDate = dueServiceDate;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/update_required";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<EquipmentModelAddReturn> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentAddReturn equipmentAddReturn = new EquipmentAddReturn();

                    Log.e("currentDateTime", "" + Utilities.currentDateTime());
                    Log.e("dateManufacturerDate", "" + dateManufacturerDate);

                    /*equipmentAddReturn.setAnnualServiceDate(convertDateFormate(serviceDate));
                    equipmentAddReturn.setManufacturerDate(convertDateFormate(manufactureDate));
                    equipmentAddReturn.setExpiry_date(convertDateFormate(expiryDate));
                    equipmentAddReturn.setLast_service_date(convertDateFormate(lastServiceDate));
                    equipmentAddReturn.setDue_service_date(convertDateFormate(dueServiceDate));*/
                    equipmentAddReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentAddReturn.setModifiedUser(Integer.parseInt(userid));
                    //   equipmentAddReturn.setDescription(equipmentModelDes);
                    equipmentAddReturn.setEid(equipmentTypeId);
                    equipmentAddReturn.setEquipmentType(null);
                    equipmentAddReturn.setId(equipmentResponse.getId());
                    equipmentAddReturn.setLoc_id(locationId);
                    equipmentAddReturn.setLocation(null);
                    equipmentAddReturn.setModelNo(equipmentModel);
                    //    equipmentAddReturn.setNextServiceDate(convertDateFormate(dueServiceDate));




                 /*   equipmentAddReturn.setService_type(serviceType);
                    equipmentAddReturn.setSerialNo(serialNo);
*/
                    //    equipmentAddReturn.setDue_service_interval(serviceInterval);


                    //    equipmentAddReturn.setServiceFrequency(inspectionDuration);
                    equipmentAddReturn.setEmailId(listEmail);


                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();


                    String jsonString = gson.toJson(equipmentAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);


                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<EquipmentModelAddReturn>(getActivity(), httpWrapper, EquipmentModelAddReturn.class);

                }


            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentModelAddReturn> loader, EquipmentModelAddReturn data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                equipmentModleId = data.getResponse().getId();

                handler.sendEmptyMessage(109);

            } else {

//                Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentModelAddReturn> loader) {

        }
    }

    private Date convertDateFormate(String stringDate) {

        Date convertedDate = null;

        if (stringDate == null || stringDate.equalsIgnoreCase("")) {

            convertedDate = null;

        } else {
            String oldFormat = "dd-MM-yyyy";
            String newFormat = "yyyy-MM-dd HH:mm:ss";

            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

            String date = null;
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

        }
        return convertedDate;

    }

    private class EquipmentTimelineRequest implements LoaderManager.LoaderCallbacks<EquipmentTimeline> {

        private static final int LOADER_GET_EQUIPMENT_TIMELINE = 1;
        private ProgressDialog progressDialog;

        public EquipmentTimelineRequest(EquipmentResponse equipmentResponse) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "timeline/" + URLEncoder.encode(equipmentResponse.getId().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }


                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TIMELINE, args, this);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //           .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<EquipmentTimeline> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<EquipmentTimeline>(getActivity(), httpWrapper, EquipmentTimeline.class);

                }

            } else {
                progressDialog.dismiss();
                //    Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentTimeline> loader, EquipmentTimeline data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentTimeline.clear();

                for (EquipmentTimeline.EquipmentTimelineResponse equipmentResponse : data.getResponse()) {

                    listEquipmentTimeline.add(equipmentResponse);

                }


                handler.sendEmptyMessage(106);

            } else {

                //       Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentTimeline> loader) {

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

           /*
                       StringTokenizer string = new StringTokenizer(stringToken, ".PNG");


           while (string.hasMoreTokens()) {
                imageUrlPath = string.nextToken();
                //    Log.e("imageUrlPath---", "" + imageUrlPath);
            }*/

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

                //     Log.e("position:---", "" + position);

                getActivity().getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));


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

                //  Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();


                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

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

                adapterEquipment.updateImage(loaderId, path, imageBitmap);

            } else {

                //     Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

    private class AddEquipmentNotRequiredRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private final String equipmentModedes;
        private final ArrayList<Bitmap> listBitmap;
        //  private final String annualServiceDate;
        private final String manufactureDate;
        //   private final String equipmentModelServicetype;
        private final String equipmentModelSerialno;
        private final String expiryDate;
        //  private final String lastServiceDate;
        // private final String dueServiceDate;
        private final String reminderDuration;
        private final String remarks;
        private ProgressDialog progressDialog;

        public AddEquipmentNotRequiredRequest(String equipmentModedes, ArrayList<Bitmap> listBitmap, String manufactureDate, String equipmentModelSerialno, String expiryDate, /*String lastServiceDate,*/ String reminderDuration, String remarks) {

            this.equipmentModedes = equipmentModedes;
            this.listBitmap = listBitmap;
            // this.annualServiceDate = annualServiceDate;
            this.manufactureDate = manufactureDate;
            //   this.equipmentModelServicetype = equipmentModelServicetype;
            this.equipmentModelSerialno = equipmentModelSerialno;
            this.expiryDate = expiryDate;
            //   this.lastServiceDate = lastServiceDate;
            //   this.dueServiceDate = dueServiceDate;
            this.reminderDuration = reminderDuration;
            this.remarks = remarks;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/update_not_required";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentAddReturn equipmentAddReturn = new EquipmentAddReturn();


                    //    equipmentAddReturn.setEquipment_modle_id(equipmentModleId);
                    //      equipmentAddReturn.setAnnualServiceDate(convertDateFormate(annualServiceDate));
                    equipmentAddReturn.setManufacturerDate(convertDateFormate(manufactureDate));
                    equipmentAddReturn.setExpiry_date(convertDateFormate(expiryDate));
                    //  equipmentAddReturn.setLast_service_date(convertDateFormate(lastServiceDate));
                    //   equipmentAddReturn.setNextServiceDate(convertDateFormate(dueServiceDate));
                    equipmentAddReturn.setDescription(equipmentModedes);
                    //   equipmentAddReturn.setService_type(equipmentModelServicetype);
                    equipmentAddReturn.setSerialNo(equipmentModelSerialno);
                    equipmentAddReturn.setId(equipmentModleId);
                    equipmentAddReturn.setModifiedDate(Utilities.currentDateTime());

                    equipmentAddReturn.setModifiedUser(Integer.parseInt(userid));


                    Integer reminder_Duration = 0;
                    if (reminderDuration == null || reminderDuration.equals("")) {
                        reminder_Duration = 0;
                    } else {
                        reminder_Duration = Integer.parseInt(reminderDuration);
                    }

                    equipmentAddReturn.setRemainderDuration(reminder_Duration);
                    //  equipmentAddReturn.setRemainderDuration(Integer.parseInt(reminderDuration));
                    equipmentAddReturn.setRemarks(remarks);
                    equipmentAddReturn.setEquipmentCloneId(cloneEquipmentId);

                  /*  equipmentAddReturn.setEid(equipmentTypeId);
                    equipmentAddReturn.setLoc_id(locationId);
                    equipmentAddReturn.setModelNo(equipmentModel);
                    equipmentAddReturn.setDue_service_interval(dueServiceDateInterval);
                    equipmentAddReturn.setInspectionDuration(inspectionDuration);*/


                    ArrayList<EquipmentAddReturnEquipmentImages> listEquipmentImages = new ArrayList<EquipmentAddReturnEquipmentImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        EquipmentAddReturnEquipmentImages equipmentTypeAddReturnImages = new EquipmentAddReturnEquipmentImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        equipmentTypeAddReturnImages.setBitmapstring(encodedString);

                        equipmentTypeAddReturnImages.setModifiedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setModifiedUser(Integer.parseInt(userid));

                        equipmentTypeAddReturnImages.setState(1);

                        listEquipmentImages.add(equipmentTypeAddReturnImages);

                    }


                    equipmentAddReturn.setEquipmentImages(listEquipmentImages);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(107);

            } else {

                //               Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());
            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class UpdateEquipmentNotRequiredRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_UPDATE_EQUIPMENT = 1;
        private final String equipmentModedes;
        private final ArrayList<Bitmap> listBitmap;
        //  private final String annualServiceDate;
        private final String manufactureDate;
        //  private final String equipmentModelServicetype;
        private final String equipmentModelSerialno;
        private final String expiryDate;
        // private final String lastServiceDate;
        //  private final String dueServiceDate;
        private final String reminderDuration;
        private final String remarks;
        private ProgressDialog progressDialog;

        public UpdateEquipmentNotRequiredRequest(String equipmentModedes, ArrayList<Bitmap> listBitmap, String manufactureDate, String equipmentModelSerialno, String expiryDate, /*String lastServiceDate, */String reminderDuration, String remarks) {

            this.equipmentModedes = equipmentModedes;
            this.listBitmap = listBitmap;
            //this.annualServiceDate = annualServiceDate;
            this.manufactureDate = manufactureDate;
            //   this.equipmentModelServicetype = equipmentModelServicetype;
            this.equipmentModelSerialno = equipmentModelSerialno;
            this.expiryDate = expiryDate;
            //  this.lastServiceDate = lastServiceDate;
            // this.dueServiceDate = dueServiceDate;
            this.reminderDuration = reminderDuration;
            this.remarks = remarks;
            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/update_not_required";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_EQUIPMENT, args, this);

            } else {
                progressDialog.dismiss();
                //  Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //        .show();


                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentAddReturn equipmentAddReturn = new EquipmentAddReturn();

                    Log.e("currentDateTime", "" + Utilities.currentDateTime());


                    equipmentAddReturn.setManufacturerDate(convertDateFormate(manufactureDate));
                    equipmentAddReturn.setExpiry_date(convertDateFormate(expiryDate));


                    equipmentAddReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentAddReturn.setModifiedUser(Integer.parseInt(userid));
                    equipmentAddReturn.setDescription(equipmentModedes);

                    equipmentAddReturn.setEquipmentType(null);
                    equipmentAddReturn.setId(equipmentResponse.getId());
                    equipmentAddReturn.setLocation(null);

                    //  equipmentAddReturn.setService_type(equipmentModelServicetype);
                    equipmentAddReturn.setSerialNo(equipmentModelSerialno);

                    Integer reminder_Duration = 0;
                    if (reminderDuration == null || reminderDuration.equals("")) {
                        reminder_Duration = 0;
                    } else {
                        reminder_Duration = Integer.parseInt(reminderDuration);
                    }

                    equipmentAddReturn.setRemainderDuration(reminder_Duration);
                    equipmentAddReturn.setRemarks(remarks);


                    ArrayList<EquipmentAddReturnEquipmentImages> listEquipmentImages = new ArrayList<EquipmentAddReturnEquipmentImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        EquipmentAddReturnEquipmentImages equipmentTypeAddReturnImages = new EquipmentAddReturnEquipmentImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        equipmentTypeAddReturnImages.setBitmapstring(encodedString);
                        //   equipmentTypeAddReturnImages.setCreatedDate(Utilities.currentDateTime());
                        //    equipmentTypeAddReturnImages.setCreatedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setModifiedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setModifiedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setState(1);

                        listEquipmentImages.add(equipmentTypeAddReturnImages);

                    }

                    equipmentAddReturn.setEquipmentImages(listEquipmentImages);


                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();


                    String jsonString = gson.toJson(equipmentAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }


            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(108);

            } else {

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class MultiDeleteEquipementRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedEquipmentsIds;
        private static final int LOADER_DELETE_MMULTIPLE_EQUIPMENT = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteEquipementRequest(List<Integer> selectedEquipmentsIds) {

            this.selectedEquipmentsIds = selectedEquipmentsIds;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/remove/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MMULTIPLE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //       Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //            .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    EquipmentIds equipmentIds = new EquipmentIds();

                    equipmentIds.setEquipmentIds(selectedEquipmentsIds);

                    String jsonString = gson.toJson(equipmentIds);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //      Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

                handler.sendEmptyMessage(105);

            } else {

                //Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());

                handler.sendEmptyMessage(105);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    public class EquipmentIds {
        private List<Integer> equipmentIds;

        public void setEquipmentIds(List<Integer> equipmentIds) {
            this.equipmentIds = equipmentIds;
        }

        public List<Integer> getEquipmentIds() {
            return this.equipmentIds;
        }
    }

    private class EquipmentTypeClone implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_EQUIPMENT_TYPE_CLONE = 1;
        private String userid;
        private ProgressDialog progressDialog;

        public EquipmentTypeClone(String addEquipmentType, EquipmentResponse equipmentTypeResponse) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                Log.e("addEquipmentType", "" + addEquipmentType);

                String url = null;


                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/" + equipmentTypeResponse.getId() + "/"
                            + URLEncoder.encode(addEquipmentType, "UTF-8").replace("+", "%20") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_EQUIPMENT_TYPE_CLONE, args, this);

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //               Snackbar.make(coordinateEquipment, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //                     .show();

                Utilities.showSnackbar(coordinateEquipment, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(105);


            } else {

                //       Snackbar.make(coordinateEquipment, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipment, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

}

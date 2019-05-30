package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddCheckListItem;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklist;
import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklistResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeAddChecklistReturn;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 13-06-2017.
 */

public class FragmentEquipmentCheckList extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private static final String TAG = FragmentEquipmentCheckList.class.getSimpleName();
    private FloatingActionButton fabAddChecklist;
    private Spinner spinnerEquipmentType;
    private CoordinatorLayout coordinateChecklist;
    private ArrayList<LocationResponse> listLocation;
    private Handler handler;
    private ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse> listEquipmentType;
    private ArrayList<String> listLocationName;
    private ArrayAdapter<String> locationAdapter;
    private ArrayList<String> listEquipmentTypeName;
    private ArrayAdapter<String> equipmentTypeAdapter;
    private int locationId;
    private Integer equipmentTypeId;
    private ArrayList<EquipmentChecklistResponse> listEquipmentChecklist;
    private SwipeRefreshLayout swipeRefreshChecklist;
    private RecyclerView recyclerCheckList;
    private AdaperEquipmentChecklist adapterChecklist;
    private int equipmentTypepos;
    private DialogAddCheckListItem dialogAddCheckListItem;
    private Set<String> listPrivilege;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private EquipmentChecklistResponse deletedItem;
    private int deletedIndex;
    private boolean swipFlag;
    private ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private ArrayList<Integer> selectedChecklistIds;
    public MenuItem searchItem;

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_equipment_checklist, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

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

        List<EquipmentChecklistResponse> filteredValues = new ArrayList<EquipmentChecklistResponse>(listEquipmentChecklist);

        for (EquipmentChecklistResponse value : listEquipmentChecklist) {

            if (!value.getCheckListName().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

        }

        adapterChecklist = new AdaperEquipmentChecklist(FragmentEquipmentCheckList.this, filteredValues, this);

        recyclerCheckList.setAdapter(adapterChecklist);

        checkEmptyView(adapterChecklist, 0);

        //  tvEmptyItem.setText("Select Equipment Type");

        return false;

    }

    private void checkEmptyView(RecyclerView.Adapter adapter, int count) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            if (count == 0) {

                tvEmptyItem.setText("No Asset Checklist Available");

            } else if (count == 1) {

                tvEmptyItem.setText("Select Asset");

            }

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    private void resetSearch() {

        adapterChecklist = new AdaperEquipmentChecklist(FragmentEquipmentCheckList.this, listEquipmentChecklist, this);
        recyclerCheckList.setAdapter(adapterChecklist);
        checkEmptyView(adapterChecklist, 0);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentCategory.class.getName());

        //    Crashlytics.getInstance().crash(); // Force a crash

        fabAddChecklist = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_checklist);

        coordinateChecklist = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_equipment_checklist);

        spinnerEquipmentType = (Spinner) getActivity().findViewById(R.id.spinner_equipment_type);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        listLocation = new ArrayList<LocationResponse>();
        listLocationName = new ArrayList<String>();

        listEquipmentType = new ArrayList<RepairLogEquipmentType.RepairLogEquipmentTypeResponse>();
        listEquipmentTypeName = new ArrayList<String>();

        listEquipmentChecklist = new ArrayList<EquipmentChecklistResponse>();

        swipeRefreshChecklist = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_checklist);

        swipeRefreshChecklist.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshChecklist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("select asset")) {

                    //      Snackbar.make(coordinateChecklist, "Please select any asset", Snackbar.LENGTH_SHORT).show();

                    Utilities.showSnackbar(coordinateChecklist, "" + "Please select any asset");

                } else {

                    getEquipmentCheckListRequest();

                }

                swipeRefreshChecklist.setRefreshing(false);

            }
        });

        recyclerCheckList = (RecyclerView) getActivity().findViewById(R.id.recycler_checklist);

        adapterChecklist = new AdaperEquipmentChecklist(FragmentEquipmentCheckList.this, listEquipmentChecklist, this);

        recyclerCheckList.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerCheckList.setLayoutManager(mLayoutManager);

        recyclerCheckList.setAdapter(adapterChecklist);

        checkEmptyView(adapterChecklist, 0);

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_READ)) {

            fabAddChecklist.setVisibility(View.GONE);

        }


        fabAddChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("select asset")) {

                    Utilities.showSnackbar(coordinateChecklist, "" + "Please select any asset");

                } else {

                    dialogAddCheckListItem = DialogAddCheckListItem.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("title", getResources().getString(R.string.add_checklist_item_dialog_title));

                    dialogAddCheckListItem.setArguments(bundle);
                    dialogAddCheckListItem.setTargetFragment(FragmentEquipmentCheckList.this, 0);
                    dialogAddCheckListItem.show(getActivity().getSupportFragmentManager(), "dialog");

                }

            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 100) {

                    //   GetEquipmentCheckListRequest getEquipmentCheckListRequest = new GetEquipmentCheckListRequest();

                    getEquipmentCheckListRequest();

                } else if (msg.what == 101) {

                    equipmentTypeAdapter.notifyDataSetChanged();

                } else if (msg.what == 102) {

                    adapterChecklist.notifyDataSetChanged();

                    checkEmptyView(adapterChecklist, 0);

                } else if (msg.what == 103) {

                    dialogAddCheckListItem.dismiss();

                    getEquipmentCheckListRequest();
                }

            }
        };

        locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocationName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        equipmentTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEquipmentTypeName);
        equipmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEquipmentType.setAdapter(equipmentTypeAdapter);

        spinnerEquipmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (listEquipmentType != null && position == 0) {

                    listEquipmentChecklist.clear();

                    adapterChecklist.notifyDataSetChanged();

                    checkEmptyView(adapterChecklist, 1);
                }

                if (listEquipmentType != null && position != 0) {

                    equipmentTypeId = listEquipmentType.get(position - 1).getId();

                    //  GetEquipmentCheckListRequest getEquipmentCheckListRequest = new GetEquipmentCheckListRequest();

                    getEquipmentCheckListRequest();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerCheckList);
            itemTouchHelperCallback.setSwipeEnable(true);

        }

        actionModeCallback = new ActionModeCallback();

        getEquipmentTypeRequest();

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_equipment_checklist, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshChecklist.setEnabled(false);

            spinnerEquipmentType.setClickable(false);
            spinnerEquipmentType.setEnabled(false);

          /*  adapterEquipment.MyViewHolder.imageEquipment.setEnabled(false);
            adapterEquipment.MyViewHolder.imageEquipment.setClickable(false);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {

                fabAddChecklist.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("" + TAG, "onPrepareActionMode");

            if (adapterChecklist.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {
                    menu.getItem(1).setVisible(false);
                }


            } else {

                if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {
                    menu.getItem(1).setVisible(true);
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

            Log.e("" + TAG, "onDestroyActionMode");

            adapterChecklist.clearSelections();
            swipeRefreshChecklist.setEnabled(true);
            spinnerEquipmentType.setClickable(true);
            spinnerEquipmentType.setEnabled(true);

           /* adapterEquipment.holder.imageEquipment.setEnabled(true);
            adapterEquipment.holder.imageEquipment.setClickable(true);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {

                fabAddChecklist.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerCheckList.post(new Runnable() {
                @Override
                public void run() {
                    adapterChecklist.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterChecklist.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listEquipmentChecklist.get(selectedItemPositions.get(0)));

    }


    private void deleteItem() {

        adapterChecklist.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterChecklist.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listEquipmentChecklist.get(selectedItemPositions.get(0)), null);

        } else {

            selectedChecklistIds = new ArrayList<>();
            ArrayList<String> selectedChecklistName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedChecklistIds.add(listEquipmentChecklist.get(pos).getId());
                selectedChecklistName.add(listEquipmentChecklist.get(pos).getCheckListName());

            }

            String deleteChecklistName = "";

            for (int i = 0; i < selectedChecklistName.size(); i++) {

                if (i == (selectedChecklistName.size() - 1)) {
                    deleteChecklistName = deleteChecklistName + selectedChecklistName.get(i);
                } else {
                    deleteChecklistName = deleteChecklistName + selectedChecklistName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteChecklistName);


        }


    }

    // to analyse this method performance
    @AddTrace(name = "GetEquipmentCheckListRequest", enabled = true)
    private void getEquipmentCheckListRequest() {

        GetEquipmentCheckListRequest getEquipmentCheckListRequest = new GetEquipmentCheckListRequest();

    }


    // to analyse this method performance
    @AddTrace(name = "EquipmentTypeRequest", enabled = true)
    private void getEquipmentTypeRequest() {

        EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest();

    }

    @AddTrace(name = "EquipmentTypeAddChecklist", enabled = true)
    public void addChecklistItemPositiveClick(String itemName, String itemDes, String frequency, String freqDay, int checklistType) {

        EquipmentTypeAddChecklist equipmentTypeAddChecklist = new EquipmentTypeAddChecklist(itemName, itemDes, frequency, freqDay, checklistType);

    }

    public void editClick(EquipmentChecklistResponse equipmentChecklistResponse) {

        if (spinnerEquipmentType.getSelectedItem().toString().equalsIgnoreCase("select asset")) {

            //Snackbar.make(coordinateChecklist, "Please select any asset", Snackbar.LENGTH_SHORT).show();
            Utilities.showSnackbar(coordinateChecklist, "" + "Please select any asset");

        } else {

            dialogAddCheckListItem = DialogAddCheckListItem.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("title", getResources().getString(R.string.update_checklist_item_dialog_title));
            bundle.putParcelable("response", equipmentChecklistResponse);

            dialogAddCheckListItem.setArguments(bundle);
            dialogAddCheckListItem.setTargetFragment(FragmentEquipmentCheckList.this, 0);
            dialogAddCheckListItem.show(getActivity().getSupportFragmentManager(), "dialog");

        }

    }

    public void deleteClick(EquipmentChecklistResponse equipmentChecklistResponse, String selectedChecklisname) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", equipmentChecklistResponse);


        if (equipmentChecklistResponse == null) {
            bundle.putString("item", selectedChecklisname);
        } else {
            bundle.putString("item", equipmentChecklistResponse.getCheckListName());
        }

        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentEquipmentCheckList.this, 3);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "EquipmentChecklistDeactiveRequest", enabled = true)
    public void deletePositiveClick(EquipmentChecklistResponse response) {

        EquipmentChecklistDeactiveRequest categoryDeactiveRequest = new EquipmentChecklistDeactiveRequest(response);

    }

    @AddTrace(name = "UpdateEquipmentChecklistRequest", enabled = true)
    public void updateChecklistItemPositiveClick(String name, String des, EquipmentChecklistResponse response, String frequency, String freqDay, int checklistType) {

        UpdateEquipmentChecklistRequest updateEquipmentChecklistRequest = new UpdateEquipmentChecklistRequest(name, des, response, frequency, freqDay, checklistType);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdaperEquipmentChecklist.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listEquipmentChecklist.get(viewHolder.getAdapterPosition()).getCheckListName();

            // backup of removed item for undo purpose
            deletedItem = listEquipmentChecklist.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterChecklist.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;

            deleteClick(deletedItem, null);

        }
    }

    public void multiDeletePositiveClick() {

        multiDeleteEquipmentRequest(selectedChecklistIds);

    }

    @AddTrace(name = "MultiDeleteChecklistRequest", enabled = true)
    private void multiDeleteEquipmentRequest(ArrayList<Integer> selectedChecklistIds) {

        MultiDeleteChecklistRequest multiDeleteEquipementRequest = new MultiDeleteChecklistRequest(selectedChecklistIds);

    }

    public void deleteNegativeClick(EquipmentChecklistResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterChecklist.restoreItem(deletedItem, deletedIndex);
        }

    }

    @Override
    public void onIconClicked(int position) {

    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapterChecklist.getSelectedItemCount() > 0) {
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

        adapterChecklist.toggleSelection(position);

        int count = adapterChecklist.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    private class EquipmentTypeRequest implements LoaderManager.LoaderCallbacks<RepairLogEquipmentType> {

        private static final int LOADER_GET_EQUIPMENT_TYPE = 1;
        private ProgressDialog progressDialog;


        public EquipmentTypeRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                String locationid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                Bundle args = new Bundle();

                //    String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/getListofEquipmentTypeRevised";

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/all/" + URLEncoder.encode("" + userid, "UTF-8") + "/" + URLEncoder.encode("" + locationid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();
                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));
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

                //  Snackbar.make(coordinateChecklist, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RepairLogEquipmentType> loader, RepairLogEquipmentType data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                String usertype = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);


                listEquipmentType.clear();
                listEquipmentTypeName.clear();

                listEquipmentTypeName.add("Select Asset");

                for (RepairLogEquipmentType.RepairLogEquipmentTypeResponse equipmentTypeResponse : data.getResponse()) {

                    listEquipmentType.add(equipmentTypeResponse);

                    String equipmentName;

                    if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(usertype)) {

                        equipmentName = equipmentTypeResponse.getName() + " ( " + equipmentTypeResponse.getLocation() + " )";
                    } else {

                        equipmentName = equipmentTypeResponse.getName();

                    }

                    listEquipmentTypeName.add(equipmentName);

                }

                progressDialog.dismiss();

                handler.sendEmptyMessage(101);

               /* if (i == 0) {

                    handler.sendEmptyMessage(107);

                } else if (i == 1) {

                    handler.sendEmptyMessage(101);

                }*/

            } else {

                progressDialog.dismiss();

                //       Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());
            }

        }

        @Override
        public void onLoaderReset(Loader<RepairLogEquipmentType> loader) {

        }
    }

    private class GetEquipmentCheckListRequest implements LoaderManager.LoaderCallbacks<EquipmentChecklist> {

        private static final int LOADER_GET_EQUIPMENT_CHECKLIST = 1;
        private ProgressDialog progressDialog;

        public GetEquipmentCheckListRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //  String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/getCheckLists/" + equipmentTypeId;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/getCheckListstoShow/" + URLEncoder.encode(equipmentTypeId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();
                //       Snackbar.make(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));
            }


        }


        @Override
        public Loader<EquipmentChecklist> onCreateLoader(int id, Bundle args) {


            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<EquipmentChecklist>(getActivity(), httpWrapper, EquipmentChecklist.class);

                }

            } else {
                progressDialog.dismiss();
                //    Snackbar.make(coordinateChecklist, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentChecklist> loader, EquipmentChecklist data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentChecklist.clear();

                for (EquipmentChecklistResponse equipmentChecklistResponse : data.getResponse()) {

                    listEquipmentChecklist.add(equipmentChecklistResponse);

                }

                handler.sendEmptyMessage(102);

            } else {

                //   Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());
            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<EquipmentChecklist> loader) {

        }
    }

    private class EquipmentTypeAddChecklist implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_EQUIPMENT_TYPE_CHECKLIST = 1;
        private final String itemName;
        private final String itemDes;
        private final String frequency;
        private final String freqDay;
        private final int checklistType;

        private ProgressDialog progressDialog;
        private String userid;


        public EquipmentTypeAddChecklist(String itemName, String itemDes, String frequency, String freqDay, int checklistType) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.itemName = itemName;
            this.itemDes = itemDes;
            this.frequency = frequency;
            this.freqDay = freqDay;
            this.checklistType = checklistType;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_EQUIPMENT_TYPE_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentTypeAddChecklistReturn equipmentTypeAddChecklistReturn = new EquipmentTypeAddChecklistReturn();

                    equipmentTypeAddChecklistReturn.setCheckListName(itemName);
                    equipmentTypeAddChecklistReturn.setCreatedDate(Utilities.currentDateTime());
                    equipmentTypeAddChecklistReturn.setCreatedUser(Integer.parseInt(userid));
                    equipmentTypeAddChecklistReturn.setDescription(itemDes);
                    equipmentTypeAddChecklistReturn.setEquipmentType(null);
                    equipmentTypeAddChecklistReturn.setId(0);
                    equipmentTypeAddChecklistReturn.setE_id(equipmentTypeId);
                    equipmentTypeAddChecklistReturn.setIsActive(0);
                    equipmentTypeAddChecklistReturn.setLoc_id(locationId);
                    equipmentTypeAddChecklistReturn.setLocation(null);
                    equipmentTypeAddChecklistReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentTypeAddChecklistReturn.setCreatedDate(Utilities.currentDateTime());
                    equipmentTypeAddChecklistReturn.setModifiedUser(Integer.parseInt(userid));
                    equipmentTypeAddChecklistReturn.setFrequency(frequency);
                    equipmentTypeAddChecklistReturn.setFrequencyDay(freqDay);

                    if (checklistType == 1) {
                        equipmentTypeAddChecklistReturn.setChecklistType(0);
                    } else {
                        equipmentTypeAddChecklistReturn.setChecklistType(1);
                    }


                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentTypeAddChecklistReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }


        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(103);

            } else {

//                Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class EquipmentChecklistDeactiveRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_EQUIPMENT_CHECKLIST = 1;
        private EquipmentChecklistResponse response;
        private ProgressDialog progressDialog;

        public EquipmentChecklistDeactiveRequest(EquipmentChecklistResponse response) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.response = response;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //  String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/deactive" + "/" + response.getId() + "/" + userid;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/deactive" + "/" + URLEncoder.encode(response.getId().toString(), "UTF-8") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_EQUIPMENT_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //           .show();

                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

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
//                Snackbar.make(coordinateChecklist, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //                      .show();
                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(100);

            } else {

//                Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());

                handler.sendEmptyMessage(100);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class UpdateEquipmentChecklistRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_UPDATE_EQUIPMENT_TYPE_CHECKLIST = 1;
        private final String itemName;
        private final String itemDes;
        private final EquipmentChecklistResponse response;
        private final String frequency;
        private final String freqDay;
        private final int checklistType;
        private ProgressDialog progressDialog;
        private String userid;


        public UpdateEquipmentChecklistRequest(String itemName, String itemDes, EquipmentChecklistResponse response, String frequency, String freqDay, int checklistType) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.itemName = itemName;
            this.itemDes = itemDes;
            this.response = response;
            this.frequency = frequency;
            this.freqDay = freqDay;
            this.checklistType = checklistType;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_EQUIPMENT_TYPE_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentTypeAddChecklistReturn equipmentTypeAddChecklistReturn = new EquipmentTypeAddChecklistReturn();

                    equipmentTypeAddChecklistReturn.setCheckListName(itemName);
                    equipmentTypeAddChecklistReturn.setCreatedDate(response.getCreatedDate());
                    equipmentTypeAddChecklistReturn.setCreatedUser(response.getCreatedUser());
                    equipmentTypeAddChecklistReturn.setDescription(itemDes);
                    equipmentTypeAddChecklistReturn.setEquipmentType(null);
                    equipmentTypeAddChecklistReturn.setId(response.getId());
                    equipmentTypeAddChecklistReturn.setE_id(equipmentTypeId);
                    equipmentTypeAddChecklistReturn.setIsActive(response.getIsActive());
                    equipmentTypeAddChecklistReturn.setLoc_id(locationId);
                    equipmentTypeAddChecklistReturn.setLocation(null);
                    equipmentTypeAddChecklistReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentTypeAddChecklistReturn.setModifiedUser(Integer.parseInt(userid));
                    equipmentTypeAddChecklistReturn.setFrequency(frequency);
                    equipmentTypeAddChecklistReturn.setFrequencyDay(freqDay);

                    if (checklistType == 1) {
                        equipmentTypeAddChecklistReturn.setChecklistType(0);
                    } else {
                        equipmentTypeAddChecklistReturn.setChecklistType(1);
                    }

                    //   equipmentTypeAddChecklistReturn.setChecklistType(checklistType);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentTypeAddChecklistReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {
                progressDialog.dismiss();
                //  Snackbar.make(coordinateChecklist, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));


            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(103);

            } else {

                //   Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class MultiDeleteChecklistRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedChecklistIds;
        private static final int LOADER_DELETE_MMULTIPLE_CHECKLIST = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteChecklistRequest(List<Integer> selectedChecklistIds) {

            this.selectedChecklistIds = selectedChecklistIds;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment_check_list/deactive/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MMULTIPLE_CHECKLIST, args, this);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //            .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    ChecklistIds checklistIds = new ChecklistIds();

                    checklistIds.setIds(selectedChecklistIds);

                    String jsonString = gson.toJson(checklistIds);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateChecklist, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //
                //                      .show();


                Utilities.showSnackbar(coordinateChecklist, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());
                handler.sendEmptyMessage(100);

            } else {

                //      Snackbar.make(coordinateChecklist, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateChecklist, "" + data.getMessage());

                handler.sendEmptyMessage(100);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    public class ChecklistIds {

        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }
    }

}

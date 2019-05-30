package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperLocation;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddLocation;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Model.Location;
import com.cdlit.assetmaintenanceapp.Model.LocationAddReturn;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.LocationUpdateReturn;
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

public class FragmentLocation extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private static final String TAG = FragmentLocation.class.getSimpleName();
    private FloatingActionButton fabAddLocation;
    public CoordinatorLayout coordinateLocation;
    private Handler handler;
    private DialogAddLocation dialogAddLocation;
    private RecyclerView recyclerLocation;
    private List<LocationResponse> listLocation;
    private AdaperLocation adapterLocation;
    private SwipeRefreshLayout swipeRefreshLocation;
    private Set<String> listPrivilege;
    private ArrayList<String> listLocationName;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback1;
    private ActionModeCallback actionModeCallback;
    private LocationResponse deletedItem;
    private int deletedIndex;
    private boolean swipFlag;
    public ActionMode actionMode;
    private ArrayList<Integer> selectedLocationsIds;
    public MenuItem searchItem;
    private ArrayList<LocationResponse> listLocationMain;
    private ArrayList<String> listLocationNameMain;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_location, container, false);

    }

    @Override
    public void onDestroy() {

        Log.e("FragmentLocation: ", "onDestroy");

        super.onDestroy();

        MyApplication.getRefWatcher(getActivity()).watch(this);

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

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        //   SearchView searchView = (SearchView) searchItem.getActionView();

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

        List<LocationResponse> filteredValues = new ArrayList<LocationResponse>(listLocation);

        for (LocationResponse value : listLocation) {

            if (!value.getLocationName().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }

        }

        listLocation = filteredValues;

        adapterLocation = new AdaperLocation(FragmentLocation.this, listLocation, this);
        recyclerLocation.setAdapter(adapterLocation);
        checkEmptyView(adapterLocation);

        return false;

    }

    private void resetSearch() {

        listLocation = listLocationMain;

        adapterLocation = new AdaperLocation(FragmentLocation.this, listLocation, this);
        recyclerLocation.setAdapter(adapterLocation);
        checkEmptyView(adapterLocation);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Location Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentLocation.class.getName());

        fabAddLocation = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_location);

        coordinateLocation = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_location);

        swipeRefreshLocation = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_location);

        swipeRefreshLocation.setColorSchemeResources(R.color.colorAccent);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshLocation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getLocationRequest();

                swipeRefreshLocation.setRefreshing(false);

            }
        });


        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

            fabAddLocation.setVisibility(View.GONE);

        }

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAddLocation = DialogAddLocation.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.addlocation_dialog_title));

                dialogAddLocation.setArguments(bundle);
                dialogAddLocation.setTargetFragment(FragmentLocation.this, 0);
                dialogAddLocation.show(getActivity().getSupportFragmentManager(), "dialog");

            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 100) {

                    dialogAddLocation.dismiss();

                    getLocationRequest();

                } else if (msg.what == 101) {

                    adapterLocation.notifyDataSetChanged();

                    checkEmptyView(adapterLocation);

                } else if (msg.what == 102) {

                    getLocationRequest();

                }

            }
        };


        recyclerLocation = (RecyclerView) getActivity().findViewById(R.id.recycler_location);

        listLocation = new ArrayList<LocationResponse>();
        listLocationMain = new ArrayList<LocationResponse>();

        listLocationName = new ArrayList<String>();
        listLocationNameMain = new ArrayList<String>();

        adapterLocation = new AdaperLocation(FragmentLocation.this, listLocation, this);

        recyclerLocation.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerLocation.setLayoutManager(mLayoutManager);
        //  recyclerLocation.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //  recyclerLocation.setItemAnimator(new DefaultItemAnimator());
        recyclerLocation.setAdapter(adapterLocation);

        checkEmptyView(adapterLocation);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerLocation);
            itemTouchHelperCallback.setSwipeEnable(true);

        }

        actionModeCallback = new ActionModeCallback();

        getLocationRequest();

    }

    public void deleteNegativeClick(LocationResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterLocation.restoreItem(deletedItem, deletedIndex);
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
        if (adapterLocation.getSelectedItemCount() > 0) {
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

            AppCompatActivity activity = (AppCompatActivity) getActivity();

            actionMode = (activity).startSupportActionMode(actionModeCallback);

        }

        toggleSelection(position);

    }

    private void toggleSelection(int position) {

        adapterLocation.toggleSelection(position);

        int count = adapterLocation.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    public void multiDeletePositiveClick() {

        multiDeleteLocationRequest(selectedLocationsIds);

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_location, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLocation.setEnabled(false);

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

                fabAddLocation.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("" + TAG, "onPrepareActionMode");

            if (adapterLocation.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }

                if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {
                    menu.getItem(1).setVisible(false);
                }

            } else {

                if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }

                if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {
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

            adapterLocation.clearSelections();
            swipeRefreshLocation.setEnabled(true);

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

                fabAddLocation.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerLocation.post(new Runnable() {
                @Override
                public void run() {
                    adapterLocation.resetAnimationIndex();
                }
            });

        }

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterLocation.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listLocation.get(selectedItemPositions.get(0)));

    }

    private void deleteItem() {

        adapterLocation.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterLocation.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());


        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listLocation.get(selectedItemPositions.get(0)), null);

        } else {

            selectedLocationsIds = new ArrayList<>();

            ArrayList<String> selectedLocationsName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedLocationsIds.add(listLocation.get(pos).getId());
                selectedLocationsName.add(listLocation.get(pos).getLocationName());

            }


            String deleteLocationName = "";

            for (int i = 0; i < selectedLocationsName.size(); i++) {

                if (i == (selectedLocationsName.size() - 1)) {
                    deleteLocationName = deleteLocationName + selectedLocationsName.get(i);
                } else {
                    deleteLocationName = deleteLocationName + selectedLocationsName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteLocationName);

            //multiDeleteLocationRequest(selectedEquipmentsIds);

        }

    }

    @AddTrace(name = "MultiDeleteLocationRequest", enabled = true)
    private void multiDeleteLocationRequest(List<Integer> selectedLocationsIds) {

        MultiDeleteLocationRequest multiDeleteLocationRequest = new MultiDeleteLocationRequest(selectedLocationsIds);

    }

    @AddTrace(name = "GetLocationRequest", enabled = true)
    private void getLocationRequest() {

        GetLocationRequest getLocationRequest = new GetLocationRequest();

    }

    // to analyse this method performance
    @AddTrace(name = "AddLocationRequest", enabled = true)
    public void addLocationPositiveClick(String addlocation) {

        AddLocationRequest addLocationRequest = new AddLocationRequest(addlocation);

    }

    public void editClick(LocationResponse locationResponse) {

        dialogAddLocation = DialogAddLocation.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.editLocation_dialog_title));
        bundle.putParcelable("location_response", locationResponse);

        dialogAddLocation.setArguments(bundle);
        dialogAddLocation.setTargetFragment(FragmentLocation.this, 0);
        dialogAddLocation.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    public void deleteClick(LocationResponse locationResponse, String deleteLocationName) {


        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", locationResponse);

        if (locationResponse == null) {
            bundle.putString("item", deleteLocationName);
        } else {
            bundle.putString("item", locationResponse.getLocationName());
        }


        dialogDeleteConfirm.setArguments(bundle);
        dialogDeleteConfirm.setTargetFragment(FragmentLocation.this, 0);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "LocationUpdateRequest", enabled = true)
    public void updateLocationPositiveClick(String location, Parcelable locationResponse) {

        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest((LocationResponse) locationResponse, location);

    }

    @AddTrace(name = "LocationDeactiveRequest", enabled = true)
    public void deletePositiveClick(LocationResponse locationResponse) {

        LocationDeactiveRequest locationDeactiveRequest = new LocationDeactiveRequest(locationResponse);

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof AdaperLocation.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listLocation.get(viewHolder.getAdapterPosition()).getLocationName();

            // backup of removed item for undo purpose
            deletedItem = listLocation.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterLocation.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;
            deleteClick(deletedItem, null);

        }

    }


    private class AddLocationRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_LOCATION = 1;
        private String location;
        private ProgressDialog progressDialog;

        public AddLocationRequest(String addlocation) {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                location = addlocation;

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_LOCATION, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    LocationAddReturn locationAddReturn = new LocationAddReturn();

                    locationAddReturn.setCreatedDate(Utilities.currentDateTime());
                    locationAddReturn.setCreatedUser(Integer.parseInt(userid));
                    locationAddReturn.setId(0);
                    locationAddReturn.setIsActive(0);
                    locationAddReturn.setLocationName(location);

                    locationAddReturn.setModifiedDate(Utilities.currentDateTime());
                    locationAddReturn.setModifiedUser(Integer.parseInt(userid));


                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(locationAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateLocation, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(100);

            } else {

                //       Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateLocation, data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class GetLocationRequest implements LoaderManager.LoaderCallbacks<Location> {

        private static final int LOADER_GET_LOCATION = 1;

        private ProgressDialog progressDialog;

        public GetLocationRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_LOCATION, args, this);

            } else {

                progressDialog.dismiss();

                //  Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

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
                //  Snackbar.make(coordinateLocation, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listLocation.clear();
                listLocationName.clear();

                listLocationMain.clear();
                listLocationNameMain.clear();

              //  adapterLocation.notifyDataSetChanged();

                for (LocationResponse location : data.getResponse()) {

                    listLocation.add(location);
                    listLocationMain.add(location);

                    listLocationName.add(location.getLocationName());
                    listLocationNameMain.add(location.getLocationName());

                }

                handler.sendEmptyMessage(101);

            } else {

                Utilities.showSnackbar(coordinateLocation, data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {

        }
    }

    private class LocationUpdateRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_UPDATE_LOCATION = 1;
        private final LocationResponse locationResponse;
        private final String location;
        private ProgressDialog progressDialog;

        public LocationUpdateRequest(LocationResponse locationResponse, String location) {

            this.location = location;
            this.locationResponse = locationResponse;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_LOCATION, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    LocationUpdateReturn locationUpdateReturn = new LocationUpdateReturn();

                    //    locationUpdateReturn.setCreatedDate(locationResponse.getCreatedDate());
                    //     locationUpdateReturn.setCreatedUser(locationResponse.getCreatedUser());
                    locationUpdateReturn.setId(locationResponse.getId());
                    //   locationUpdateReturn.setIsActive(locationResponse.getIsActive());
                    locationUpdateReturn.setLocationName(location);
                    locationUpdateReturn.setModifiedDate(Utilities.currentDateTime());
                    locationUpdateReturn.setModifiedUser(Integer.parseInt(userid));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(locationUpdateReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateLocation, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //     Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateLocation, data.getMessage());
                handler.sendEmptyMessage(100);


            } else {

                //         Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(dialogAddLocation.view, data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class LocationDeactiveRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_LOCATION = 1;
        private LocationResponse locationResponse;
        private ProgressDialog progressDialog;

        public LocationDeactiveRequest(LocationResponse locationResponse) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.locationResponse = locationResponse;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //     progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                // String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location/deactive" + "/" + locationResponse.getId() + "/" + userid;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location/remove" + "/" + URLEncoder.encode(String.valueOf(locationResponse.getId()), "UTF-8") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_LOCATION, args, this);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

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
                Snackbar.make(coordinateLocation, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(102);

            } else {

                //Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateLocation, data.getMessage());
                handler.sendEmptyMessage(102);
            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {


        }
    }

    private class MultiDeleteLocationRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedLocationsIds;
        private static final int LOADER_DELETE_MMULTIPLE_EQUIPMENT = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteLocationRequest(List<Integer> selectedLocationsIds) {

            this.selectedLocationsIds = selectedLocationsIds;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {


                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "location/remove/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MMULTIPLE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    LocationIds locationIds = new LocationIds();

                    locationIds.setLocationIds(selectedLocationsIds);

                    String jsonString = gson.toJson(locationIds);

                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateLocation, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(coordinateLocation, getActivity().getResources().getString(R.string.network_connection_failed));


            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //         Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateLocation, data.getMessage());
                handler.sendEmptyMessage(102);

            } else {

                //       Snackbar.make(coordinateLocation, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateLocation, data.getMessage());
                handler.sendEmptyMessage(102);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }


    public class LocationIds {
        private List<Integer> locationIds;

        public List<Integer> getLocationIds() {
            return locationIds;
        }

        public void setLocationIds(List<Integer> locationIds) {
            this.locationIds = locationIds;
        }
    }


}

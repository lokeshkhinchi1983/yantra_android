package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import com.cdlit.assetmaintenanceapp.Adapter.AdapterNotification;
import com.cdlit.assetmaintenanceapp.Model.Notification;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.SEARCH_SERVICE;

public class FragmentNotification extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, ActionModeListener {

    private ViewStub emptyView;
    private CoordinatorLayout coordinateNotification;
    private SwipeRefreshLayout swipeRefreshNotification;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<Notification.Response> listNotification;
    private Handler handler;
    private AdapterNotification adapterNotification;
    private RecyclerView recyclerNotification;
    private ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private Set<String> listPrivilege;
    public MenuItem searchItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_notification, container, false);

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

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentNotification.class.getName());

        coordinateNotification = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_notification);

        swipeRefreshNotification = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_notification);

        recyclerNotification = (RecyclerView) getActivity().findViewById(R.id.recycler_notification);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        listNotification = new ArrayList<Notification.Response>();

        swipeRefreshNotification.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshNotification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getNotificationRequest();

                swipeRefreshNotification.setRefreshing(false);

            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    adapterNotification.notifyDataSetChanged();
                    checkEmptyView(adapterNotification);

                }

                return false;
            }
        });

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        listNotification = new ArrayList<>();

        adapterNotification = new AdapterNotification(this, listNotification, this);

        recyclerNotification.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerNotification.setLayoutManager(mLayoutManager);

        recyclerNotification.setAdapter(adapterNotification);

        checkEmptyView(adapterNotification);

        actionModeCallback = new ActionModeCallback();

        getNotificationRequest();

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_faultlog, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshNotification.setEnabled(false);

            //spinnerSelectEquipment.setEnabled(false);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            //     if (listPrivilege.contains(Utilities.FAULTLOG_WRITE)) {
            menu.getItem(0).setVisible(false);
            //    }
            return false;

        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {



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

            adapterNotification.clearSelections();
            swipeRefreshNotification.setEnabled(true);
            // spinnerSelectEquipmen.setEnabled(true);

           /* if (listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

                fabAddLocation.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }*/

            actionMode = null;

            recyclerNotification.post(new Runnable() {
                @Override
                public void run() {

                    adapterNotification.resetAnimationIndex();

                }
            });

        }
    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterNotification.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

        Notification.Response response = listNotification.get(selectedItemPositions.get(0));

        ArrayList<Notification.Response> listResponse = new ArrayList<>();

        for (int i = 0; i < selectedItemPositions.size(); i++) {

            listResponse.add(listNotification.get(selectedItemPositions.get(i)));

        }

        context.menuClick("Maintenance Log", null, null, null, response, listResponse);

    }

    private void checkEmptyView(AdapterNotification adapterNotification) {

        if (adapterNotification.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Notification Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @AddTrace(name = "NotificationRequest", enabled = true)
    private void getNotificationRequest() {

        GetNotificationRequest getNotificationRequest = new GetNotificationRequest();

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

        List<Notification.Response> filteredValues = new ArrayList<Notification.Response>(listNotification);

        for (Notification.Response value : listNotification) {

            if (!value.getChecklistname().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }

        }

        adapterNotification = new AdapterNotification(FragmentNotification.this, filteredValues, this);

        recyclerNotification.setAdapter(adapterNotification);
        checkEmptyView(adapterNotification);

        return false;

    }

    private void resetSearch() {

        adapterNotification = new AdapterNotification(FragmentNotification.this, listNotification, this);

        recyclerNotification.setAdapter(adapterNotification);

        checkEmptyView(adapterNotification);

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
        if (adapterNotification.getSelectedItemCount() > 0) {
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

    private void toggleSelection(int position) {

        adapterNotification.toggleSelection(position);

        int count = adapterNotification.getSelectedItemCount();

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

    private class GetNotificationRequest implements android.support.v4.app.LoaderManager.LoaderCallbacks<Notification> {

        private static final int LOADER_GET_EQUIPMENT = 1;

        private ProgressDialog progressDialog;

        public GetNotificationRequest() {
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                String userLocationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                String userId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                //  String userId="130";

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "task/open/" + URLEncoder.encode(userId.toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);

                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
        //        Snackbar.make(coordinateNotification, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
              //          .show();

                Utilities.showSnackbar(coordinateNotification, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<Notification> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<Notification>(getActivity(), httpWrapper, Notification.class);

                }

            } else {

                progressDialog.dismiss();
             //   Snackbar.make(coordinateNotification, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
               //         .show();

                Utilities.showSnackbar(coordinateNotification, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;

        }

        @Override
        public void onLoadFinished(Loader<Notification> loader, Notification data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listNotification.clear();

                listNotification.addAll(data.getResponse());

                handler.sendEmptyMessage(100);

            } else {

//                Snackbar.make(coordinateNotification, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateNotification, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Notification> loader) {


        }

    }
}

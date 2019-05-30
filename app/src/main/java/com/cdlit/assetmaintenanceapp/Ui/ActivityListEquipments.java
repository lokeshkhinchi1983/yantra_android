package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterListEquipments;
import com.cdlit.assetmaintenanceapp.Model.EquipmentList;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.squareup.leakcanary.RefWatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by rakesh on 04-09-2017.
 */

public class ActivityListEquipments extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerEquipments;
    private SwipeRefreshLayout swipeRefreshEquipments;
    private Handler handler;
    private ArrayList<EquipmentList.Response> listEquipments;
    private AdapterListEquipments adapterEquipments;
    private Integer equipmentTypeId;

    @Override
    protected void onRestart() {
        super.onRestart();

        //   GetEuipmentsListRequest getEuipmentRequest = new GetEuipmentsListRequest();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_search, menu);

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(this);

        return true;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Crashlytics.log(this.getLocalClassName());

        setContentView(R.layout.activity_list_equipments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("My Checklist / Equipment Type List");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        swipeRefreshEquipments = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_list_equipments);
        recyclerEquipments = (RecyclerView) findViewById(R.id.recycler_list_equipments);

        equipmentTypeId = getIntent().getIntExtra("equipment_type_id", 0);

        swipeRefreshEquipments.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshEquipments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //  GetEuipmentsListRequest getEuipmentRequest = new GetEuipmentsListRequest();

                getEuipmentsListRequest();
                swipeRefreshEquipments.setRefreshing(false);

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    adapterEquipments.notifyDataSetChanged();

                }

                return false;
            }
        });

        listEquipments = new ArrayList<EquipmentList.Response>();

        adapterEquipments = new AdapterListEquipments(this, listEquipments);

        recyclerEquipments.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerEquipments.setLayoutManager(mLayoutManager);
        recyclerEquipments.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerEquipments.setItemAnimator(new DefaultItemAnimator());
        recyclerEquipments.setAdapter(adapterEquipments);

        //    GetEuipmentsListRequest getEuipmentRequest = new GetEuipmentsListRequest();

        getEuipmentsListRequest();

    }

    @AddTrace(name = "GetEuipmentsListRequest", enabled = true)
    private void getEuipmentsListRequest() {

        GetEuipmentsListRequest getEuipmentRequest = new GetEuipmentsListRequest();

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

        ArrayList<EquipmentList.Response> filteredValues = new ArrayList<EquipmentList.Response>(listEquipments);

        for (EquipmentList.Response value : listEquipments) {

            if (!value.getModel_no().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }

        }

        adapterEquipments = new AdapterListEquipments(this, filteredValues);

        recyclerEquipments.setAdapter(adapterEquipments);


        return false;
    }

    private void resetSearch() {

        adapterEquipments = new AdapterListEquipments(this, listEquipments);
        recyclerEquipments.setAdapter(adapterEquipments);

    }


    private class GetEuipmentsListRequest implements LoaderManager.LoaderCallbacks<EquipmentList> {

        private static final int LOADER_GET_EQUIPMENT = 1;
        private ProgressDialog progressDialog;

        public GetEuipmentsListRequest() {
            progressDialog = Utilities.startProgressDialog(ActivityListEquipments.this);

            if (Utilities.isNetworkAvailable(ActivityListEquipments.this)) {


                Bundle args = new Bundle();
                //  String url = Utilities.getStringFromSharedPreferances(ActivityListEquipments.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/getListOfEquipmentsRevised/" + equipmentTypeId;
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(ActivityListEquipments.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/getListOfEquipmentsRevised/" + URLEncoder.encode(equipmentTypeId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinatorLayout, getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<EquipmentList> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(ActivityListEquipments.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<EquipmentList>(ActivityListEquipments.this, httpWrapper, EquipmentList.class);

                }

            } else {

                progressDialog.dismiss();
                //// Snackbar.make(coordinatorLayout, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();
                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentList> loader, EquipmentList data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipments.clear();

                for (EquipmentList.Response equipmentResponse : data.getResponse()) {

                    listEquipments.add(equipmentResponse);

                }

                handler.sendEmptyMessage(100);

            } else {

                //  Snackbar.make(coordinatorLayout, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinatorLayout, data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentList> loader) {

        }
    }

}

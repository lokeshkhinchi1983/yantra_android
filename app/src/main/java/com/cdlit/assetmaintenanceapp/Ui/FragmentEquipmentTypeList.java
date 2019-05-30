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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperEquipmentTypeList;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipment;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 23-08-2017.
 */

public class FragmentEquipmentTypeList extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener {

    private CoordinatorLayout coordinateEquipmentTypeList;
    private SwipeRefreshLayout swipeRefreshEquipmentTypeList;
    private ArrayList<AssignEquipment.AssignEquipmentResponse> listEquipmentTypeList;
    private Handler handler;
    private RecyclerView recyclerEquipmentTypeList;
    private AdaperEquipmentTypeList adapterEquipmentTypeList;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<AssignEquipment.AssignEquipments> listEquipmentList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_equipment_type_list, container, false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.e("FragmentEquipmentTypeList", "onResume 1");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("FragmentEquipmentTypeList", "onStart 1");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);

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

        Crashlytics.log("" + FragmentEquipmentTypeList.class.getName());

        listEquipmentTypeList = new ArrayList<>();

        listEquipmentList = new ArrayList<>();

        coordinateEquipmentTypeList = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_equipment_type_list);

        swipeRefreshEquipmentTypeList = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_equipment_type_list);

        swipeRefreshEquipmentTypeList.setColorSchemeResources(R.color.colorAccent);

        recyclerEquipmentTypeList = (RecyclerView) getActivity().findViewById(R.id.recycler_equipment_type_list);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        adapterEquipmentTypeList = new AdaperEquipmentTypeList(getActivity(), listEquipmentList);

        recyclerEquipmentTypeList.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentTypeList.setLayoutManager(mLayoutManager);
        //  recyclerEquipmentTypeList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //   recyclerEquipmentTypeList.setItemAnimator(new DefaultItemAnimator());
        recyclerEquipmentTypeList.setAdapter(adapterEquipmentTypeList);

        checkEmptyView(adapterEquipmentTypeList);

        swipeRefreshEquipmentTypeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getEquipmentListRequest();

                swipeRefreshEquipmentTypeList.setRefreshing(false);

            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    adapterEquipmentTypeList.notifyDataSetChanged();
                    checkEmptyView(adapterEquipmentTypeList);

                    loadImages();

                }

                return false;
            }
        });

        getEquipmentListRequest();

    }

    @AddTrace(name = "EquipmentTypeListRequest", enabled = true)
    private void getEquipmentListRequest() {

        EquipmentTypeListRequest equipmentTypeRequest = new EquipmentTypeListRequest();

    }

    private void loadImages() {

        ArrayList<String> listImageResponse = new ArrayList<String>();

        HashMap<Integer, String> mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterEquipmentTypeList.getItemCount(); i++) {

            //  String imagePath = listEquipmentTypeList.get(i).getImage_path().get(0);

            if (listEquipmentList.get(i).getImage_path().size() != 0 && listEquipmentList.get(i).getImage_path() != null) {

                String imagePath = listEquipmentList.get(i).getImage_path().get(0);

                if (imagePath != null) {

                    if (!imagePath.equalsIgnoreCase("")) {

                        // ImageRequest imageRequest = new ImageRequest(i, listEquipmentTypeList.get(i).getImage_path().get(0));
                        ImageRequest imageRequest = new ImageRequest(i, listEquipmentList.get(i).getImage_path().get(0));

                    }

                }

            }


        }

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

        ArrayList<AssignEquipment.AssignEquipments> filteredValues = new ArrayList<AssignEquipment.AssignEquipments>(listEquipmentList);

        for (AssignEquipment.AssignEquipments value : listEquipmentList) {

            if (!value.getModel_no().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }

        }

        adapterEquipmentTypeList = new AdaperEquipmentTypeList(getActivity(), filteredValues);

        recyclerEquipmentTypeList.setAdapter(adapterEquipmentTypeList);
        checkEmptyView(adapterEquipmentTypeList);

        return false;

    }

    private void resetSearch() {

        adapterEquipmentTypeList = new AdaperEquipmentTypeList(getActivity(), listEquipmentList);
        recyclerEquipmentTypeList.setAdapter(adapterEquipmentTypeList);

        checkEmptyView(adapterEquipmentTypeList);

    }


    // this method is called when user return to this fragment
    public void onRestart() {

        Log.e("FragmentEquipmentTypeList: ", "onRestart");

        getEquipmentListRequest();

    }

    private class EquipmentTypeListRequest implements LoaderManager.LoaderCallbacks<AssignEquipment> {

        private static final int LOADER_GET_EQUIPMENTS = 1;
        private final String userid;
        private ProgressDialog progressDialog;

        public EquipmentTypeListRequest() {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //     progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/getListOfEquipmentTypesOfUser/" + userid;
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/getListOfEquipmentTypesOfUser/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENTS, args, this);

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<AssignEquipment> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<AssignEquipment>(getActivity(), httpWrapper, AssignEquipment.class);

                }

            } else {

                progressDialog.dismiss();
              //  Snackbar.make(coordinateEquipmentTypeList, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
               //         .show();
                Utilities.showSnackbar(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<AssignEquipment> loader, AssignEquipment data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentTypeList.clear();
                listEquipmentList.clear();

                for (AssignEquipment.AssignEquipmentResponse response : data.getResponse()) {

                    listEquipmentTypeList.add(response);

                    for (int i = 0; i < response.getEquipments().size(); i++) {

                        listEquipmentList.add(response.getEquipments().get(i));
                    }

                }

                handler.sendEmptyMessage(100);

            } else {

             //   Snackbar.make(coordinateEquipmentTypeList, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentTypeList, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<AssignEquipment> loader) {


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

                    //     url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.EQUIPMENTS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");


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

                //    progressDialog.dismiss();
               // Snackbar.make(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
               //         .show();


                Utilities.showSnackbar(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed));
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

//                Snackbar.make(coordinateEquipmentTypeList, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
  //                      .show();

                Utilities.showSnackbar(coordinateEquipmentTypeList, getActivity().getResources().getString(R.string.network_connection_failed));

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

                adapterEquipmentTypeList.updateImage(loaderId, path, imageBitmap);

            } else {

                //   Snackbar.make(coordinateEquipmentTypeList, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }


}

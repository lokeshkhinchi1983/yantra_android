package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.cdlit.assetmaintenanceapp.Adapter.AdaperCategory;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddCategory;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Model.Category;
import com.cdlit.assetmaintenanceapp.Model.CategoryAdd;
import com.cdlit.assetmaintenanceapp.Model.CategoryAddReturn;
import com.cdlit.assetmaintenanceapp.Model.CategoryDeactive;
import com.cdlit.assetmaintenanceapp.Model.CategoryResponse;
import com.cdlit.assetmaintenanceapp.Model.CategoryUpdate;
import com.cdlit.assetmaintenanceapp.Model.CategoryUpdateReturn;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerTouchListener;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
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

public class FragmentCategory extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private static final String TAG = FragmentCategory.class.getSimpleName();
    private FloatingActionButton fabAddCategory;
    private CoordinatorLayout coordinateCategory;
    private Handler handler;
    private DialogAddCategory dialogAddCategory;
    private SwipeRefreshLayout swipeRefreshCategory;
    private RecyclerView recyclerCategory;
    private ArrayList<CategoryResponse> listCategory;
    private AdaperCategory adapterCategory;
    private Set<String> listPrivilege;
    public MenuItem searchItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_category, container, false);

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

        List<CategoryResponse> filteredValues = new ArrayList<CategoryResponse>(listCategory);

        for (CategoryResponse value : listCategory) {

            if (!value.getCategoryName().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

        }

        adapterCategory = new AdaperCategory(FragmentCategory.this, filteredValues);
        recyclerCategory.setAdapter(adapterCategory);

        return false;

    }

    private void resetSearch() {

        adapterCategory = new AdaperCategory(FragmentCategory.this, listCategory);
        recyclerCategory.setAdapter(adapterCategory);

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Crashlytics.log(""+FragmentCategory.class.getName());


        fabAddCategory = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_category);

        coordinateCategory = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_category);

        swipeRefreshCategory = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_category);

        swipeRefreshCategory.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetCategoryRequest getLocationRequest = new GetCategoryRequest();

                swipeRefreshCategory.setRefreshing(false);

            }
        });

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.CATEGORY_MASTER_WRITE)) {

            fabAddCategory.setVisibility(View.GONE);

        }

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAddCategory = DialogAddCategory.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.addCategory_dialog_title));

                dialogAddCategory.setArguments(bundle);
                dialogAddCategory.setTargetFragment(FragmentCategory.this, 0);
                dialogAddCategory.show(getActivity().getSupportFragmentManager(), "dialog");

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    dialogAddCategory.dismiss();

                    GetCategoryRequest getCategoryRequest = new GetCategoryRequest();

                } else if (msg.what == 101) {

                    adapterCategory.notifyDataSetChanged();

                } else if (msg.what == 102) {

                    GetCategoryRequest getCategoryRequest = new GetCategoryRequest();
                }


                return false;
            }
        });

        recyclerCategory = (RecyclerView) getActivity().findViewById(R.id.recycler_category);

        listCategory = new ArrayList<CategoryResponse>();
        adapterCategory = new AdaperCategory(FragmentCategory.this, listCategory);

        recyclerCategory.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerCategory.setLayoutManager(mLayoutManager);
        recyclerCategory.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerCategory.setAdapter(adapterCategory);

        recyclerCategory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerCategory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CategoryResponse category = listCategory.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                CategoryResponse category = listCategory.get(position);
            }

        }));

        GetCategoryRequest getLocationRequest = new GetCategoryRequest();

    }


    public void addCategoryPositiveClick(String category) {

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest(category);


    }

    public void editClick(CategoryResponse categoryResponse) {

        dialogAddCategory = DialogAddCategory.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.editCategory_dialog_title));
        bundle.putParcelable("category_response", categoryResponse);

        dialogAddCategory.setArguments(bundle);
        dialogAddCategory.setTargetFragment(FragmentCategory.this, 1);
        dialogAddCategory.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    public void deletePositiveClick(CategoryResponse categoryResponse) {

        CategoryDeactiveRequest categoryDeactiveRequest = new CategoryDeactiveRequest(categoryResponse);

    }

    public void deleteClick(CategoryResponse categoryResponse) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", categoryResponse);
        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentCategory.this, 1);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    public void editCategoryPositiveClick(String category, CategoryResponse categoryResponse) {

        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest(category, categoryResponse);

    }


    private class AddCategoryRequest implements LoaderManager.LoaderCallbacks<CategoryAdd> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private ProgressDialog progressDialog;
        private String addCategory;
        private String userid;

        public AddCategoryRequest(String category) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            if (Utilities.isNetworkAvailable(getActivity())) {

                addCategory = category;

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();

            }

        }


        @Override
        public Loader<CategoryAdd> onCreateLoader(int id, Bundle args) {


            if (Utilities.isNetworkAvailable(getActivity())) {
                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    CategoryAddReturn addCategoryReturn = new CategoryAddReturn();

                    addCategoryReturn.setCategoryName(addCategory);
                    addCategoryReturn.setCreatedDate(Utilities.currentDateTime());
                    addCategoryReturn.setCreatedUser(Integer.parseInt(userid));
                    addCategoryReturn.setId(0);
                    addCategoryReturn.setIsActive(0);
                    addCategoryReturn.setModifiedDate(Utilities.currentDateTime());
                    addCategoryReturn.setModifiedUser(Integer.parseInt(userid));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();


                    String jsonString = gson.toJson(addCategoryReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<CategoryAdd>(getActivity(), httpWrapper, CategoryAdd.class);

                }

            } else {
                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<CategoryAdd> loader, CategoryAdd data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(100);

            } else {

                Snackbar.make(coordinateCategory, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<CategoryAdd> loader) {

        }
    }

    private class GetCategoryRequest implements LoaderManager.LoaderCallbacks<Category> {


        private static final int LOADER_GET_CATEGORY = 1;
        private ProgressDialog progressDialog;

        public GetCategoryRequest() {

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();

            }

        }

        @Override
        public Loader<Category> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<Category>(getActivity(), httpWrapper, Category.class);

                }

            } else {
                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Category> loader, Category data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listCategory.clear();

                for (CategoryResponse category : data.getResponse()) {

                    listCategory.add(category);

                }

                handler.sendEmptyMessage(101);

            } else {

                Snackbar.make(coordinateCategory, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Category> loader) {

        }


    }

    private class CategoryDeactiveRequest implements LoaderManager.LoaderCallbacks<CategoryDeactive> {

        private static final int LOADER_DEACTIVE_CATEGORY = 1;
        private final CategoryResponse categoryResponse;
        private ProgressDialog progressDialog;


        public CategoryDeactiveRequest(CategoryResponse categoryResponse) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.categoryResponse = categoryResponse;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category/remove" + "/" + URLEncoder.encode(String.valueOf(categoryResponse.getId()), "UTF-8") + "/" + URLEncoder.encode(userid.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                //    String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category/deactive" + "/" + categoryResponse.getId() + "/" + userid;
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();

            }

        }

        @Override
        public Loader<CategoryDeactive> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {


                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<CategoryDeactive>(getActivity(), httpWrapper, CategoryDeactive.class);

                }

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<CategoryDeactive> loader, CategoryDeactive data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(102);

            } else {

                Snackbar.make(coordinateCategory, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<CategoryDeactive> loader) {

        }
    }

    private class CategoryUpdateRequest implements LoaderManager.LoaderCallbacks<CategoryUpdate> {

        private static final int LOADER_UPDATE_CATEGORY = 1;
        private final CategoryResponse categoryResponse;
        private final String category;
        private ProgressDialog progressDialog;


        public CategoryUpdateRequest(String category, CategoryResponse categoryResponse) {
            this.category = category;
            this.categoryResponse = categoryResponse;

            if (Utilities.isNetworkAvailable(getActivity())) {

                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();

            }

        }


        @Override
        public Loader<CategoryUpdate> onCreateLoader(int id, Bundle args) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    CategoryUpdateReturn categoryUpdateReturn = new CategoryUpdateReturn();

                    categoryUpdateReturn.setCategoryName(category);
                    //    categoryUpdateReturn.setCreatedDate(categoryResponse.getCreatedDate());
                    //    categoryUpdateReturn.setCreatedUser(categoryResponse.getCreatedUser());
                    categoryUpdateReturn.setId(categoryResponse.getId());
                    //   categoryUpdateReturn.setIsActive(categoryResponse.getIsActive());
                    categoryUpdateReturn.setModifiedDate(Utilities.currentDateTime());
                    categoryUpdateReturn.setModifiedUser(Integer.parseInt(userid));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(categoryUpdateReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<CategoryUpdate>(getActivity(), httpWrapper, CategoryUpdate.class);

                }

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateCategory, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<CategoryUpdate> loader, CategoryUpdate data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                Snackbar.make(coordinateCategory, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                handler.sendEmptyMessage(100);

            } else {

                Snackbar.make(coordinateCategory, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<CategoryUpdate> loader) {


        }

    }
}

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

import com.cdlit.assetmaintenanceapp.Adapter.AdaperEquipmentType;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEquipmentType;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentTypeClone;
import com.cdlit.assetmaintenanceapp.Model.Category;
import com.cdlit.assetmaintenanceapp.Model.CategoryResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentType;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeAddReturn;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeAddReturnCategory;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeAddReturnImages;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeResponse;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.MyApplication;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerItemTouchHelper;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerItemTouchHelperListener;
import com.cdlit.assetmaintenanceapp.Utils.RecyclerTouchListener;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 13-06-2017.
 */

public class FragmentEquipmentType extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private static final String TAG = FragmentEquipmentType.class.getSimpleName();
    private FloatingActionButton fabAddEquipmentType;
    private CoordinatorLayout coordinateEquipmentType;
    private ArrayList<EquipmentTypeResponse> listEquipmentType;
    private Handler handler;
    private RecyclerView recyclerEquipmentType;
    private AdaperEquipmentType adapterEquipmentType;
    private ArrayList<CategoryResponse> listCategory;
    private DialogAddEquipmentType dialogAddEquipmentType;
    private SwipeRefreshLayout swipeRefreshEquipmentType;
    private int flag;
    private EquipmentTypeResponse equipmentTypeResponse;
    private Set<String> listPrivilege;
    private DialogEquipmentTypeClone dialogCloneEquipmentType;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<String> listImageResponse;
    private HashMap<Integer, String> mapImageResponse;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private ActionModeCallback actionModeCallback;
    private ArrayList<Integer> selectedEquipmentTypesIds;
    private boolean swipFlag;
    private EquipmentTypeResponse deletedItem;
    private int deletedIndex;
    public ActionMode actionMode;
    public MenuItem searchItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_equipment_type, container, false);

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

        List<EquipmentTypeResponse> filteredValues = new ArrayList<EquipmentTypeResponse>(listEquipmentType);

        for (EquipmentTypeResponse value : listEquipmentType) {

            if (!value.getEquipmentTypeName().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

        }

        adapterEquipmentType = new AdaperEquipmentType(FragmentEquipmentType.this, filteredValues, this);

        recyclerEquipmentType.setAdapter(adapterEquipmentType);

        checkEmptyView(adapterEquipmentType);
        return false;
    }

    private void resetSearch() {

        adapterEquipmentType = new AdaperEquipmentType(FragmentEquipmentType.this, listEquipmentType, this);
        recyclerEquipmentType.setAdapter(adapterEquipmentType);
        checkEmptyView(adapterEquipmentType);
    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);
            tvEmptyItem.setText("No Asset Category Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentEquipmentType.class.getName());

        fabAddEquipmentType = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_equipment_type);

        coordinateEquipmentType = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_equipment_type);

        swipeRefreshEquipmentType = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_equipment_type);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshEquipmentType.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshEquipmentType.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            // to analyse this method performance
            @AddTrace(name = "EquipmentTypeRequest", enabled = true)
            public void onRefresh() {

                getEquipmentTypeRequest();

                swipeRefreshEquipmentType.setRefreshing(false);

            }
        });

        listEquipmentType = new ArrayList<EquipmentTypeResponse>();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    openEquipmentTypeDialog();

                } else if (msg.what == 101) {

                    adapterEquipmentType.notifyDataSetChanged();
                    checkEmptyView(adapterEquipmentType);

                    loadImages();

                } else if (msg.what == 102) {

                    dialogAddEquipmentType.dismiss();

                    getEquipmentTypeRequest();

                } else if (msg.what == 103) {

                    getEquipmentTypeRequest();

                } else if (msg.what == 104) {

                    editEquipmentTypeDialog();

                } else if (msg.what == 105) {

                    dialogCloneEquipmentType.dismiss();

                    getEquipmentTypeRequest();

                }

                return false;
            }
        });


        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_READ)) {

            fabAddEquipmentType.setVisibility(View.GONE);

        }

        fabAddEquipmentType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                flag = 0;

                getCategoryRequest();

            }
        });

        listCategory = new ArrayList<CategoryResponse>();

        listEquipmentType = new ArrayList<EquipmentTypeResponse>();

        recyclerEquipmentType = (RecyclerView) getActivity().findViewById(R.id.recycler_equipment_type);

        adapterEquipmentType = new AdaperEquipmentType(FragmentEquipmentType.this, listEquipmentType, this);

        recyclerEquipmentType.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentType.setLayoutManager(mLayoutManager);
        //   recyclerEquipmentType.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //   recyclerEquipmentType.setItemAnimator(new DefaultItemAnimator());
        recyclerEquipmentType.setAdapter(adapterEquipmentType);
        checkEmptyView(adapterEquipmentType);

        recyclerEquipmentType.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerEquipmentType, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                EquipmentTypeResponse equipmentTypeResponse = listEquipmentType.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

                EquipmentTypeResponse equipmentTypeResponse = listEquipmentType.get(position);

            }

        }));


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.EQUIPMENT_MASTER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerEquipmentType);
            itemTouchHelperCallback.setSwipeEnable(true);

        }


        actionModeCallback = new ActionModeCallback();

        getEquipmentTypeRequest();

    }

    public void multiDeletePositiveClick() {

        multiDeleteEquipmentTypeRequest(selectedEquipmentTypesIds);

    }

    @AddTrace(name = "MultiDeleteEquipementRequest", enabled = true)
    private void multiDeleteEquipmentTypeRequest(ArrayList<Integer> selectedEquipmentTypesIds) {

        MultiDeleteEquipementTypeRequest multiDeleteEquipementRequest = new MultiDeleteEquipementTypeRequest(selectedEquipmentTypesIds);

    }

    public void deleteNegativeClick(EquipmentTypeResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterEquipmentType.restoreItem(deletedItem, deletedIndex);
        }

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_equipment_type, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshEquipmentType.setEnabled(false);

          /*  adapterEquipment.MyViewHolder.imageEquipment.setEnabled(false);
            adapterEquipment.MyViewHolder.imageEquipment.setClickable(false);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {

                fabAddEquipmentType.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("" + TAG, "onPrepareActionMode");

            if (adapterEquipmentType.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {

                    menu.getItem(1).setVisible(false);
                }
               /* if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(false);
                }*/

            } else {

                if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {

                    menu.getItem(1).setVisible(true);

                    // 1 for add clone and 0 for clone not

                  /*  List<Integer> selectedItemPositions =
                            adapterEquipmentType.getSelectedItems();

                    Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());


                    if (listEquipmentType.get(selectedItemPositions.get(0)).getIsCheckList() == 1) {

                        menu.getItem(1).setVisible(true);

                    } else {

                        menu.getItem(1).setVisible(false);

                    }
*/
                    //  menu.getItem(1).setVisible(true);

                }

              /*  if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(true);
                }*/

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

               /* case R.id.action_clone:

                    List<Integer> selectedItemPositions =
                            adapterEquipmentType.getSelectedItems();

                    Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

                    if (listEquipmentType.get(selectedItemPositions.get(0)).getIsCheckList() == 1) {

                        cloneItem();

                    } else {

                        Snackbar.make(getView(), "Clone option is not available for this equipment type", Snackbar.LENGTH_SHORT).show();

                    }

                    mode.finish();
                    return true;*/

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

            adapterEquipmentType.clearSelections();
            swipeRefreshEquipmentType.setEnabled(true);

           /* adapterEquipment.holder.imageEquipment.setEnabled(true);
            adapterEquipment.holder.imageEquipment.setClickable(true);*/

            if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_WRITE)) {

                fabAddEquipmentType.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.EQUIPMENT_TYPE_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerEquipmentType.post(new Runnable() {
                @Override
                public void run() {
                    adapterEquipmentType.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterEquipmentType.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listEquipmentType.get(selectedItemPositions.get(0)));

    }

    private void cloneItem() {

        List<Integer> selectedItemPositions =
                adapterEquipmentType.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        cloneClick(listEquipmentType.get(selectedItemPositions.get(0)));

    }

    // deleting the messages from recycler view
    private void deleteItem() {

        adapterEquipmentType.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterEquipmentType.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listEquipmentType.get(selectedItemPositions.get(0)), null);

        } else {

            selectedEquipmentTypesIds = new ArrayList<>();
            ArrayList<String> selectedEquipmentTypesName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedEquipmentTypesIds.add(listEquipmentType.get(pos).getId());
                selectedEquipmentTypesName.add(listEquipmentType.get(pos).getEquipmentTypeName());

            }

            String deleteEquipmentTypeName = "";

            for (int i = 0; i < selectedEquipmentTypesName.size(); i++) {

                if (i == (selectedEquipmentTypesName.size() - 1)) {
                    deleteEquipmentTypeName = deleteEquipmentTypeName + selectedEquipmentTypesName.get(i);
                } else {
                    deleteEquipmentTypeName = deleteEquipmentTypeName + selectedEquipmentTypesName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteEquipmentTypeName);


        }

    }


    // to analyse this method performance
    @AddTrace(name = "GetCategoryRequest", enabled = true)
    private void getCategoryRequest() {

        GetCategoryRequest getLocationRequest = new GetCategoryRequest();

    }

    // to analyse this method performance
    @AddTrace(name = "EquipmentTypeRequest", enabled = true)
    private void getEquipmentTypeRequest() {

        EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest();

    }

    private void loadImages() {

        listImageResponse = new ArrayList<String>();

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterEquipmentType.getItemCount(); i++) {

            String imagePath;

            if (listEquipmentType.get(i).getEquipmentTypeImages() == null || listEquipmentType.get(i).getEquipmentTypeImages().size() == 0) {

                imagePath = null;

            } else {

                imagePath = listEquipmentType.get(i).getEquipmentTypeImages().get(0).getImagePath();

            }

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    getImageRequest(i, listEquipmentType.get(i).getEquipmentTypeImages().get(0).getImagePath());

                }

            }

        }

    }

    // to analyse this method performance
    @AddTrace(name = "EquipmentTypeImageRequest", enabled = true)
    private void getImageRequest(int i, String imagePath) {

        ImageRequest imageRequest = new ImageRequest(i, imagePath);

    }

    private void editEquipmentTypeDialog() {

        dialogAddEquipmentType = DialogAddEquipmentType.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.edit_equipmenttype_dialog_title));
        bundle.putParcelableArrayList("list_category", listCategory);
        bundle.putParcelable("equipment_type_response", equipmentTypeResponse);
        dialogAddEquipmentType.setArguments(bundle);
        dialogAddEquipmentType.setTargetFragment(FragmentEquipmentType.this, 0);
        dialogAddEquipmentType.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openEquipmentTypeDialog() {

        dialogAddEquipmentType = DialogAddEquipmentType.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_equipmenttype_dialog_title));
        bundle.putParcelableArrayList("list_category", listCategory);

        dialogAddEquipmentType.setArguments(bundle);
        dialogAddEquipmentType.setTargetFragment(FragmentEquipmentType.this, 0);
        dialogAddEquipmentType.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    // to analyse this method performance
    @AddTrace(name = "AddEquipmentTypeRequest", enabled = true)
    public void addEquipmentTypePositiveClick(/*int categoryId, */String equipmentType, String equipmentTypeDes, ArrayList<Bitmap> listBitmap) {

        AddEquipmentTypeRequest addEquipmentTypeRequest = new AddEquipmentTypeRequest(/*categoryId,*/ equipmentType, equipmentTypeDes, listBitmap);

    }


    public void editClick(EquipmentTypeResponse equipmentTypeResponse) {

        this.equipmentTypeResponse = equipmentTypeResponse;
        flag = 1;

        getCategoryRequest();
        //    GetCategoryRequest getLocationRequest = new GetCategoryRequest();

    }

    public void deleteClick(EquipmentTypeResponse equipmentTypeResponse, String selectedEquipmentTypesName) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", equipmentTypeResponse);

        if (equipmentTypeResponse == null) {
            bundle.putString("item", selectedEquipmentTypesName);
        } else {
            bundle.putString("item", equipmentTypeResponse.getEquipmentTypeName());
        }
        // bundle.putString("item", equipmentTypeResponse.getEquipmentTypeName());

        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentEquipmentType.this, 2);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    // to analyse this method performance
    @AddTrace(name = "EquipmentTypeDeactiveRequest", enabled = true)
    public void deletePositiveClick(EquipmentTypeResponse response) {

        EquipmentTypeDeactiveRequest categoryDeactiveRequest = new EquipmentTypeDeactiveRequest(response);

    }

    // to analyse this method performance
    @AddTrace(name = "UpdateEquipmentTypeRequest", enabled = true)
    public void editEquipmentTypePositiveClick(/*int categoryId,*/ String equipmentType, String equipmentTypeDes, EquipmentTypeResponse response, ArrayList<Bitmap> listBitmap) {

        UpdateEquipmentTypeRequest updateEquipmentTypeRequest = new UpdateEquipmentTypeRequest(/*categoryId,*/ equipmentType, equipmentTypeDes, response, listBitmap);

    }

    public void cloneClick(EquipmentTypeResponse equipmentTypeResponse) {

        dialogCloneEquipmentType = DialogEquipmentTypeClone.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.equipmenttype_clone_title));
        bundle.putParcelable("equipment_type_response", equipmentTypeResponse);

        dialogCloneEquipmentType.setArguments(bundle);
        dialogCloneEquipmentType.setTargetFragment(FragmentEquipmentType.this, 0);
        dialogCloneEquipmentType.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    // to analyse this method performance
    @AddTrace(name = "EquipmentTypeClone", enabled = true)
    public void addEquipmentTypeClone(String addEquipmentType, EquipmentTypeResponse equipmentTypeResponse) {

        EquipmentTypeClone equipmentTypeClone = new EquipmentTypeClone(addEquipmentType, equipmentTypeResponse);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdaperEquipmentType.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listEquipmentType.get(viewHolder.getAdapterPosition()).getEquipmentTypeName();

            // backup of removed item for undo purpose
            deletedItem = listEquipmentType.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterEquipmentType.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;
            deleteClick(deletedItem, null);

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
        if (adapterEquipmentType.getSelectedItemCount() > 0) {
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

        adapterEquipmentType.toggleSelection(position);

        int count = adapterEquipmentType.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    private class GetCategoryRequest implements LoaderManager.LoaderCallbacks<Category> {

        private static final int LOADER_GET_CATEGORY = 1;
        private ProgressDialog progressDialog;

        public GetCategoryRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "category";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
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
                Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
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

                // for add
                if (flag == 0) {
                    handler.sendEmptyMessage(100);
                }

                // for edit
                else {
                    handler.sendEmptyMessage(104);
                }


            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<Category> loader) {


        }


    }

    private class UpdateEquipmentTypeRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private ArrayList<Bitmap> listBitmap;
        private EquipmentTypeResponse response;
        private String equipmentType;
        private String equipmentTypeDes;
        private ProgressDialog progressDialog;
        //     private int category;

        public UpdateEquipmentTypeRequest(/*int category,*/ String equipmentType, String equipmentTypeDes, EquipmentTypeResponse response, ArrayList<Bitmap> listBitmap) {


            if (Utilities.isNetworkAvailable(getActivity())) {

                //   this.category = category;
                this.equipmentType = equipmentType;
                this.equipmentTypeDes = equipmentTypeDes;
                this.listBitmap = listBitmap;
                this.response = response;
                progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                        .show();

            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentTypeAddReturn equipmentTypeAddReturn = new EquipmentTypeAddReturn();

                    //   equipmentTypeAddReturn.setCatId(category);
                    //    equipmentTypeAddReturn.setCreatedDate(response.getCreatedDate());
                    //    equipmentTypeAddReturn.setCreatedUser(response.getCreatedUser());
                    equipmentTypeAddReturn.setEquipmentTypeName(equipmentType);
                    equipmentTypeAddReturn.setDescription(equipmentTypeDes);
                    equipmentTypeAddReturn.setId(response.getId());
                    //   equipmentTypeAddReturn.setIsActive(response.getIsActive());
                    equipmentTypeAddReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentTypeAddReturn.setModifiedUser(Integer.parseInt(userid));

                    EquipmentTypeAddReturnCategory equipmentTypeAddReturnCategory = new EquipmentTypeAddReturnCategory();

                    equipmentTypeAddReturn.setCategory(equipmentTypeAddReturnCategory);

                    List<EquipmentTypeAddReturnImages> equipmentTypeImages = new ArrayList<EquipmentTypeAddReturnImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        EquipmentTypeAddReturnImages equipmentTypeAddReturnImages = new EquipmentTypeAddReturnImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        equipmentTypeAddReturnImages.setBitmapstring(encodedString);
                        equipmentTypeAddReturnImages.setCreatedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setCreatedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setEquipmentTypeId(0);
                        equipmentTypeAddReturnImages.setId(0);
                        equipmentTypeAddReturnImages.setImagePath("");
                        equipmentTypeAddReturnImages.setModifiedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setModifiedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setState(1);

                        equipmentTypeImages.add(equipmentTypeAddReturnImages);

                    }

                    equipmentTypeAddReturn.setEquipmentTypeImages(equipmentTypeImages);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();
                    String jsonString = gson.toJson(equipmentTypeAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {
                progressDialog.dismiss();
                Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                        .show();
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {
                progressDialog.dismiss();
                handler.sendEmptyMessage(102);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());
            }

            // progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class AddEquipmentTypeRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ADD_CATEGORY = 1;
        private ArrayList<Integer> listState;
        private ArrayList<Bitmap> listBitmap;
        private int state;
        private String encodedBitmapString;
        private String equipmentType;
        private String equipmentTypeDes;
        private ProgressDialog progressDialog;
        //  private int category;
        private String userid;

        public AddEquipmentTypeRequest(/*int category,*/ String equipmentType, String equipmentTypeDes, ArrayList<Bitmap> listBitmap) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  this.category = category;
                this.equipmentType = equipmentType;
                this.equipmentTypeDes = equipmentTypeDes;
                this.listBitmap = listBitmap;
                this.listState = listState;
                this.state = state;
                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_CATEGORY, args, this);

            } else {

                progressDialog.dismiss();
                //Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //      .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

            }


        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                    EquipmentTypeAddReturn equipmentTypeAddReturn = new EquipmentTypeAddReturn();

                    // equipmentTypeAddReturn.setCatId(category);
                    equipmentTypeAddReturn.setCreatedDate(Utilities.currentDateTime());
                    equipmentTypeAddReturn.setCreatedUser(Integer.parseInt(userid));
                    equipmentTypeAddReturn.setEquipmentTypeName(equipmentType);
                    equipmentTypeAddReturn.setDescription(equipmentTypeDes);
                    equipmentTypeAddReturn.setId(0);
                    equipmentTypeAddReturn.setIsActive(0);
                    equipmentTypeAddReturn.setModifiedDate(Utilities.currentDateTime());
                    equipmentTypeAddReturn.setModifiedUser(Integer.parseInt(userid));

                    EquipmentTypeAddReturnCategory equipmentTypeAddReturnCategory = new EquipmentTypeAddReturnCategory();

                    equipmentTypeAddReturnCategory.setCategoryName("");
                    equipmentTypeAddReturnCategory.setCreatedDate(Utilities.currentDateTime());
                    equipmentTypeAddReturnCategory.setCreatedUser(0);
                    equipmentTypeAddReturnCategory.setId(0);
                    equipmentTypeAddReturnCategory.setIsActive(0);
                    equipmentTypeAddReturnCategory.setModifiedDate(Utilities.currentDateTime());
                    equipmentTypeAddReturnCategory.setModifiedUser(Integer.parseInt(userid));

                    equipmentTypeAddReturn.setCategory(equipmentTypeAddReturnCategory);

                    List<EquipmentTypeAddReturnImages> equipmentTypeImages = new ArrayList<EquipmentTypeAddReturnImages>();

                    for (int i = 0; i < listBitmap.size(); i++) {

                        EquipmentTypeAddReturnImages equipmentTypeAddReturnImages = new EquipmentTypeAddReturnImages();

                        Bitmap bitmap = listBitmap.get(i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

                        equipmentTypeAddReturnImages.setBitmapstring(encodedString);
                        equipmentTypeAddReturnImages.setCreatedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setCreatedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setEquipmentTypeId(0);
                        equipmentTypeAddReturnImages.setId(0);
                        equipmentTypeAddReturnImages.setImagePath("");
                        equipmentTypeAddReturnImages.setModifiedDate(Utilities.currentDateTime());
                        equipmentTypeAddReturnImages.setModifiedUser(Integer.parseInt(userid));
                        equipmentTypeAddReturnImages.setState(1);

                        equipmentTypeImages.add(equipmentTypeAddReturnImages);

                    }

                    equipmentTypeAddReturn.setEquipmentTypeImages(equipmentTypeImages);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(equipmentTypeAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                progressDialog.dismiss();

                handler.sendEmptyMessage(102);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());

            }

            //  progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }


    }

    private class EquipmentTypeRequest implements LoaderManager.LoaderCallbacks<EquipmentType> {

        private static final int LOADER_GET_EQUIPMENT_TYPE = 1;
        private ProgressDialog progressDialog;

        public EquipmentTypeRequest() {
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {


                Bundle args = new Bundle();

                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));
            }

        }

        @Override
        public Loader<EquipmentType> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<EquipmentType>(getActivity(), httpWrapper, EquipmentType.class);

                }

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<EquipmentType> loader, EquipmentType data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentType.clear();

                for (EquipmentTypeResponse equipmentTypeResponse : data.getResponse()) {

                    listEquipmentType.add(equipmentTypeResponse);

                }

                handler.sendEmptyMessage(101);

            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<EquipmentType> loader) {

        }
    }

    private class EquipmentTypeDeactiveRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_EQUIPMENT_TYPE = 1;
        private final EquipmentTypeResponse response;
        private ProgressDialog progressDialog;

        public EquipmentTypeDeactiveRequest(EquipmentTypeResponse response) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.response = response;

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //      String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/deactive" + "/" + response.getId() + "/" + userid;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/remove" + "/" + URLEncoder.encode(response.getId().toString(), "UTF-8") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

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
                //    Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //      .show();


                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(103);

            } else {

                // Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());

                handler.sendEmptyMessage(103);
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class EquipmentTypeClone implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_EQUIPMENT_TYPE_CLONE = 1;
        private String userid;
        private ProgressDialog progressDialog;

        public EquipmentTypeClone(String addEquipmentType, EquipmentTypeResponse equipmentTypeResponse) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //    progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                Log.e("addEquipmentType", "" + addEquipmentType);

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/" + equipmentTypeResponse.getId() + "/"
                            + URLEncoder.encode(addEquipmentType, "UTF-8").replace("+", "%20") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_EQUIPMENT_TYPE_CLONE, args, this);

            } else {

                progressDialog.dismiss();
                //     Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));


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
                //  Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //     .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(105);


            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

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
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.EQUIPMENT_TYPES_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
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
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

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
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

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

                adapterEquipmentType.updateImage(loaderId, path, imageBitmap);

            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

    private class MultiDeleteEquipementTypeRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedEquipmentTypesIds;

        private static final int LOADER_DELETE_MULTIPLE_EQUIPMENT_TYPE = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteEquipementTypeRequest(List<Integer> selectedEquipmentTypesIds) {

            this.selectedEquipmentTypesIds = selectedEquipmentTypesIds;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //    progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/remove/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MULTIPLE_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //     .show();
                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    EquipmentTypeIds equipmentTypeIds = new EquipmentTypeIds();

                    equipmentTypeIds.setIds(selectedEquipmentTypesIds);

                    String jsonString = gson.toJson(equipmentTypeIds);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateEquipmentType, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();

                Utilities.showSnackbar(coordinateEquipmentType, getActivity().getResources().getString(R.string.network_connection_failed));


            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //    Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());

                handler.sendEmptyMessage(103);

            } else {

                //  Snackbar.make(coordinateEquipmentType, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateEquipmentType, "" + data.getMessage());

                handler.sendEmptyMessage(103);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    public class EquipmentTypeIds {

        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }

    }
}

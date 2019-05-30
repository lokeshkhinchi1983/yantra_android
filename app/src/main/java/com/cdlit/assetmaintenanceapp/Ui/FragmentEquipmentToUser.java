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

import com.cdlit.assetmaintenanceapp.Adapter.AdaperAssignedEquipmentType;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAssignEquipmetUser;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipmentReturn;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipmentToUser;
import com.cdlit.assetmaintenanceapp.Model.AssignedEquipment;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.Model.UserBasic;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 13-06-2017.
 */

public class FragmentEquipmentToUser extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private FloatingActionButton fabAddAssignToUser;
    private CoordinatorLayout coordinateAssignToUser;
    private ArrayList<UserBasic.UserBasicResponse> listUsers;
    private Handler handler;
    private Spinner spinnerSelectUser;
    private ArrayAdapter<String> userAdapter;
    private ArrayList<String> listUsersNames;
    private Integer userId;
    private Set<String> listPrivilege;
    //private ArrayList<EquipmentTypeResponse> listEquipmentType;
    private DialogAssignEquipmetUser dialogAssignEquipmetUser;
    private RecyclerView recyclerAssignToUser;
    private SwipeRefreshLayout swipeRefreshAssignToUser;
    private AdaperAssignedEquipmentType adapterAssigned;
    private ArrayList<AssignedEquipment.Response> listAssignEquipment;
    private Integer userTypeId;
    private int userPosition;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<String> listFirstLastName;
    private AssignedEquipment.Response deletedItem;
    private int deletedIndex;
    private boolean swipFlag;
    public ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Integer> selectedEquipmentsIds;
    private ArrayList<Integer> selectedAssignedToUserIds;
    private ArrayList<AssignEquipmentToUser.Response> listEquipmentType;
    public MenuItem searchItem;

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_assign_equipment, container, false);

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

        List<AssignedEquipment.Response> filteredValues = new ArrayList<AssignedEquipment.Response>(listAssignEquipment);

        for (AssignedEquipment.Response value : listAssignEquipment) {

            if (!value.getEquipment_name().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }

        }

        adapterAssigned = new AdaperAssignedEquipmentType(FragmentEquipmentToUser.this, filteredValues, this);

        recyclerAssignToUser.setAdapter(adapterAssigned);
        checkEmptyView(adapterAssigned, 0);

        return false;

    }

    private void resetSearch() {

        adapterAssigned = new AdaperAssignedEquipmentType(FragmentEquipmentToUser.this, listAssignEquipment, this);
        recyclerAssignToUser.setAdapter(adapterAssigned);
        checkEmptyView(adapterAssigned, 0);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter, int count) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            if (count == 0) {

                tvEmptyItem.setText("No Assigned Asset Available");

            } else if (count == 1) {

                tvEmptyItem.setText("Select Personnel");

            }

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentEquipmentToUser.class.getName());

        fabAddAssignToUser = (FloatingActionButton) getActivity().findViewById(R.id.fab_assign_to_user);

        coordinateAssignToUser = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_assign_to_user);

        spinnerSelectUser = (Spinner) getActivity().findViewById(R.id.spinner_select_user);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshAssignToUser = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_assign_to_user);

        swipeRefreshAssignToUser.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshAssignToUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (spinnerSelectUser.getSelectedItem().toString().equalsIgnoreCase("Select Personnel")) {

                    //     Snackbar.make(coordinateAssignToUser, "Please select any personnel", Snackbar.LENGTH_SHORT).show();

                } else {

                    getUserEquipmentRequest();

                }

                swipeRefreshAssignToUser.setRefreshing(false);

            }
        });

        recyclerAssignToUser = (RecyclerView) getActivity().findViewById(R.id.recycler_assign_to_user);

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {

            fabAddAssignToUser.setVisibility(View.GONE);

        }


        fabAddAssignToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinnerSelectUser.getSelectedItem().toString().equalsIgnoreCase("Select Personnel")) {

                    Snackbar.make(coordinateAssignToUser, "Please select any personnel", Snackbar.LENGTH_SHORT).show();

                } else {

                    getEquipmentTypeRequest();

                }

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    userAdapter.notifyDataSetChanged();

                }

                if (msg.what == 101) {

                    openEquipmentTypeDialog();

                }

                if (msg.what == 102) {

                    adapterAssigned.notifyDataSetChanged();
                    checkEmptyView(adapterAssigned, 0);

                    loadImages();

                }

                if (msg.what == 103) {

                    dialogAssignEquipmetUser.dismiss();

                    getUserEquipmentRequest();
                }


                if (msg.what == 104) {

                    getUserEquipmentRequest();

                }

                return false;
            }
        });


        listEquipmentType = new ArrayList<AssignEquipmentToUser.Response>();
        listAssignEquipment = new ArrayList<AssignedEquipment.Response>();

        listUsersNames = new ArrayList<String>();
        listFirstLastName = new ArrayList<String>();

        listUsers = new ArrayList<UserBasic.UserBasicResponse>();

        userAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listUsersNames);

        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSelectUser.setAdapter(userAdapter);

        spinnerSelectUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinnerSelectUser.getSelectedItem().toString().equalsIgnoreCase("Select Personnel")) {

                    // Snackbar.make(coordinateAssignToUser, "Please select any personnel", Snackbar.LENGTH_SHORT).show();

                    listAssignEquipment.clear();
                    adapterAssigned.notifyDataSetChanged();

                    checkEmptyView(adapterAssigned, 1);

                } else {

                    userTypeId = listUsers.get(position - 1).getUserid();

                    Log.d("userTypeId", "" + userTypeId);

                    listAssignEquipment.clear();
                    adapterAssigned.notifyDataSetChanged();
                    checkEmptyView(adapterAssigned, 0);

                    getUserEquipmentRequest();
                }

                swipeRefreshAssignToUser.setRefreshing(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        adapterAssigned = new AdaperAssignedEquipmentType(FragmentEquipmentToUser.this, listAssignEquipment, this);
        recyclerAssignToUser.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerAssignToUser.setLayoutManager(mLayoutManager);
        //  recyclerAssignToUser.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // recyclerAssignToUser.setItemAnimator(new DefaultItemAnimator());
        recyclerAssignToUser.setAdapter(adapterAssigned);
        checkEmptyView(adapterAssigned, 0);

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerAssignToUser);
            itemTouchHelperCallback.setSwipeEnable(true);

        }


        actionModeCallback = new ActionModeCallback();

        getGetUserRequest();

    }

    public void multiDeletePositiveClick() {

        multiDeleteEquipmentRequest(selectedEquipmentsIds, selectedAssignedToUserIds);

    }


    @AddTrace(name = "MultiDeleteEquipmentRequest", enabled = true)
    private void multiDeleteEquipmentRequest(ArrayList<Integer> selectedEquipmentsIds, ArrayList<Integer> selectedAssignedToUserIds) {

        MultiDeleteEquipmentRequest multiDeleteLocationRequest = new MultiDeleteEquipmentRequest(selectedEquipmentsIds, selectedAssignedToUserIds);

    }

    public void deleteNegativeClick(AssignedEquipment.Response response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterAssigned.restoreItem(deletedItem, deletedIndex);
        }

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_assign_equipmenttype_user, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshAssignToUser.setEnabled(false);

            spinnerSelectUser.setEnabled(false);
            spinnerSelectUser.setClickable(false);

            if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {

                fabAddAssignToUser.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //    Log.e("" + TAG, "onPrepareActionMode");

            if (adapterAssigned.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }

               /* if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {
                    menu.getItem(1).setVisible(false);
                }*/

            } else {

                if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }

              /*  if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {
                    menu.getItem(1).setVisible(true);
                }*/

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

               /* case R.id.action_edit:

                    editItem();
                    mode.finish();
                    return true;*/

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            //   Log.e("" + TAG, "onDestroyActionMode");

            adapterAssigned.clearSelections();
            swipeRefreshAssignToUser.setEnabled(true);
            spinnerSelectUser.setEnabled(true);
            spinnerSelectUser.setClickable(true);

            if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {

                fabAddAssignToUser.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerAssignToUser.post(new Runnable() {
                @Override
                public void run() {
                    adapterAssigned.resetAnimationIndex();
                }
            });

        }

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterAssigned.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listAssignEquipment.get(selectedItemPositions.get(0)));

    }

    private void deleteItem() {

        adapterAssigned.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterAssigned.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;
            deleteClick(listAssignEquipment.get(selectedItemPositions.get(0)), null);

        } else {

            selectedEquipmentsIds = new ArrayList<>();
            selectedAssignedToUserIds = new ArrayList<>();

            ArrayList<String> selectedEquipmentsName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);

                selectedEquipmentsIds.add(listAssignEquipment.get(pos).getEquipment_id());

                selectedAssignedToUserIds.add(listAssignEquipment.get(pos).getAssigned_to_user_id());

                selectedEquipmentsName.add(listAssignEquipment.get(pos).getEquipment_name());

            }


            String deleteEquipmentsName = "";

            for (int i = 0; i < selectedEquipmentsName.size(); i++) {

                if (i == (selectedEquipmentsName.size() - 1)) {
                    deleteEquipmentsName = deleteEquipmentsName + selectedEquipmentsName.get(i);
                } else {
                    deleteEquipmentsName = deleteEquipmentsName + selectedEquipmentsName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteEquipmentsName);

            //multiDeleteLocationRequest(selectedEquipmentsIds);

        }
    }

    @AddTrace(name = "GetUserEquipmentRequest", enabled = true)
    private void getUserEquipmentRequest() {

        GetUserEquipmentRequest getUserEquipmentRequest = new GetUserEquipmentRequest();

    }

    @AddTrace(name = "EquipmentTypeRequest", enabled = true)
    private void getEquipmentTypeRequest() {

        EquipmentTypeRequest equipmentTypeRequest = new EquipmentTypeRequest();

    }

    @AddTrace(name = "AddEquipmentRequest", enabled = true)
    private void getGetUserRequest() {

        GetUserRequest getUserRequest = new GetUserRequest();

    }

    private void loadImages() {

        ArrayList<String> listImageResponse = new ArrayList<String>();

        HashMap<Integer, String> mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterAssigned.getItemCount(); i++) {

            String imagePath;
            if (listAssignEquipment.get(i).getImage_path().getImage_path().size() == 0 || listAssignEquipment.get(i).getImage_path().getImage_path() == null) {
                imagePath = null;
            } else {
                imagePath = listAssignEquipment.get(i).getImage_path().getImage_path().get(0);

            }

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    getImageRequest(i, listAssignEquipment.get(i).getImage_path().getImage_path().get(0));

                }

            }

        }

    }

    @AddTrace(name = "GetEquipmentToUserImageRequest", enabled = true)

    private void getImageRequest(int i, String path) {

        ImageRequest imageRequest = new ImageRequest(i, path);

    }


    private void openEquipmentTypeDialog() {

        dialogAssignEquipmetUser = DialogAssignEquipmetUser.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.assign_equipment_type));
        bundle.putParcelableArrayList("list_equipment_type", listEquipmentType);
        bundle.putParcelableArrayList("list_assigned_equipment_type", listAssignEquipment);

        dialogAssignEquipmetUser.setArguments(bundle);
        dialogAssignEquipmetUser.setTargetFragment(FragmentEquipmentToUser.this, 0);
        dialogAssignEquipmetUser.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "AddEquipmentTypeToUser", enabled = true)
    public void assignEquipmentToUser(ArrayList<Integer> listCheckedItemsId) {

        for (int i = 0; i < listCheckedItemsId.size(); i++) {
            Log.e("checked item id :", "" + listCheckedItemsId.get(i));
        }

        AddEquipmentTypeToUser addEquipmentTypeToUser = new AddEquipmentTypeToUser(listCheckedItemsId);

    }

    public void editClick(AssignedEquipment.Response equipmentResponse) {


    }

    public void deleteClick(AssignedEquipment.Response equipmentResponse, String deleteEquipmentName) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", equipmentResponse);
        // bundle.putString("item", equipmentResponse.getEquipment_type_name());

        if (equipmentResponse == null) {
            bundle.putString("item", deleteEquipmentName);
        } else {
            bundle.putString("item", equipmentResponse.getEquipment_name());
        }

        dialogDeleteConfirm.setArguments(bundle);

        dialogDeleteConfirm.setTargetFragment(FragmentEquipmentToUser.this, 5);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "EquipmentTypeDeleteRequest", enabled = true)
    public void deletePositiveClick(AssignedEquipment.Response response) {

        EquipmentTypeDeleteRequest equipmentTypeDeleteRequest = new EquipmentTypeDeleteRequest(response);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdaperAssignedEquipmentType.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listAssignEquipment
                    .get(viewHolder.getAdapterPosition()).getEquipment_name();

            // backup of removed item for undo purpose
            deletedItem = listAssignEquipment.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterAssigned.removeItem(viewHolder.getAdapterPosition());

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
        if (adapterAssigned.getSelectedItemCount() > 0) {
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

        adapterAssigned.toggleSelection(position);

        int count = adapterAssigned.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    private class GetUserRequest implements LoaderManager.LoaderCallbacks<UserBasic> {

        private static final int LOADER_GET_USERS = 1;
        private ProgressDialog progressDialog;

        public GetUserRequest() {

            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String locationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                String user_Type = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                String url = null;

                if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(user_Type)) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/all/" + URLEncoder.encode(locationId.toString(), "UTF-8") + "/" + "basic";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                } else if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(user_Type)) {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/allUsersForAdmin";

                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_USERS, args, this);

            } else {

                progressDialog.dismiss();

//                Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));
            }

        }

        @Override
        public Loader<UserBasic> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<UserBasic>(getActivity(), httpWrapper, UserBasic.class);

                }

            } else {

                progressDialog.dismiss();

                //       Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //             .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<UserBasic> loader, UserBasic data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                String usertype = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                listUsers.clear();

                listUsersNames.clear();

                listUsersNames.add("Select Personnel");

                for (UserBasic.UserBasicResponse user : data.getResponse()) {

                    listUsers.add(user);

                    String userName;

                    if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(usertype)) {

                        userName = user.getName() + " ( " + user.getLocationName() + " )" + " ( " + user.getUserType() + " )";

                    } else {

                        userName = user.getName() + " ( " + user.getUserType() + " )";

                    }

                    listUsersNames.add(userName);

                }

                handler.sendEmptyMessage(100);

            } else {

                //         Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());
            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<UserBasic> loader) {

        }
    }

    private class EquipmentTypeRequest implements LoaderManager.LoaderCallbacks<AssignEquipmentToUser> {

        private static final int LOADER_GET_EQUIPMENT_TYPE = 1;
        private ProgressDialog progressDialog;

        public EquipmentTypeRequest() {
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //     progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;

                try {

                    //  url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipmentType/notAssigned/" + URLEncoder.encode(userTypeId.toString(), "UTF-8");

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "equipment/notAssigned/" + URLEncoder.encode(userTypeId.toString(), "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //       Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //             .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<AssignEquipmentToUser> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<AssignEquipmentToUser>(getActivity(), httpWrapper, AssignEquipmentToUser.class);

                }

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));


            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<AssignEquipmentToUser> loader, AssignEquipmentToUser data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listEquipmentType.clear();

                for (AssignEquipmentToUser.Response equipmentTypeResponse : data.getResponse()) {

                    listEquipmentType.add(equipmentTypeResponse);

                }

                handler.sendEmptyMessage(101);

            } else {

                //  Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());

            }

            progressDialog.dismiss();


        }


        @Override
        public void onLoaderReset(Loader<AssignEquipmentToUser> loader) {

        }
    }

    private class AddEquipmentTypeToUser implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_ASSIGN_EQUIPMENT_TO_USER = 1;
        private final String userid;
        private final ArrayList<Integer> listCheckedItemsId;
        private ProgressDialog progressDialog;

        public AddEquipmentTypeToUser(ArrayList<Integer> listCheckedItemsId) {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            this.listCheckedItemsId = listCheckedItemsId;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ASSIGN_EQUIPMENT_TO_USER, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));


            }

        }


        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {


                    // for specific
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    AssignEquipmentReturn assignEquipmentReturn = new AssignEquipmentReturn();
                    assignEquipmentReturn.setCreated_date(Utilities.currentDateTime());
                    assignEquipmentReturn.setCreated_user_id(Integer.parseInt(userid));
                    //   assignEquipmentReturn.setEquipment_type_id(listCheckedItemsId);

                    assignEquipmentReturn.setE_id(listCheckedItemsId);
                    assignEquipmentReturn.setUser_id(userTypeId);

                    String jsonString = gson.toJson(assignEquipmentReturn);

                    Log.e("jsonString", "" + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(103);

            } else {

//                Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();


                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class GetUserEquipmentRequest implements LoaderManager.LoaderCallbacks<AssignedEquipment> {

        private static final int LOADER_GET_EQUIPMENTS = 1;
        private final String userid;
        private ProgressDialog progressDialog;

        public GetUserEquipmentRequest() {

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //  url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/getListOfEquipmentTypesOfUser/" + URLEncoder.encode(userTypeId.toString(), "UTF-8");

                String url = null;

                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/getListOfEquipmentsOfUser/" + URLEncoder.encode(userTypeId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_EQUIPMENTS, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<AssignedEquipment> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<AssignedEquipment>(getActivity(), httpWrapper, AssignedEquipment.class);

                }

            } else {
                progressDialog.dismiss();
                //    Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<AssignedEquipment> loader, AssignedEquipment data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listAssignEquipment.clear();

                for (AssignedEquipment.Response response : data.getResponse()) {

                    listAssignEquipment.add(response);

                }

                handler.sendEmptyMessage(102);

            } else {

//                Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());


            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<AssignedEquipment> loader) {

        }


    }

    private class EquipmentTypeDeleteRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_EQUIPMENT_TYPE = 1;
        private AssignedEquipment.Response response;
        private ProgressDialog progressDialog;

        public EquipmentTypeDeleteRequest(AssignedEquipment.Response response) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.response = response;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/deactive/" + URLEncoder.encode(response.getAssigned_to_user_id().toString(), "UTF-8") + "/" + URLEncoder.encode(userid.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_EQUIPMENT_TYPE, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

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
                //   Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();


                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(104);

            } else {

                //  Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());

                handler.sendEmptyMessage(104);
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

                //  progressDialog.dismiss();
                //   Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //   .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

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

            //      Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
            //            .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

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

                adapterAssigned.updateImage(loaderId, path, imageBitmap);

            } else {

                //     Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

  /*  private class MultiDeleteEquipmentRequest {
        public MultiDeleteEquipmentRequest(ArrayList<Integer> selectedEquipmentsIds) {
        }
    }*/

    private class MultiDeleteEquipmentRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedEquipmentsIds;
        private static final int LOADER_DELETE_MULTIPLE_EQUIPMENT = 1;
        private final ArrayList<Integer> selectedAssignedToUserIds;
        ProgressDialog progressDialog = null;

        public MultiDeleteEquipmentRequest(List<Integer> selectedEquipmentsIds, ArrayList<Integer> selectedAssignedToUserIds) {

            this.selectedEquipmentsIds = selectedEquipmentsIds;
            this.selectedAssignedToUserIds = selectedAssignedToUserIds;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "assignToUser/deactive/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MULTIPLE_EQUIPMENT, args, this);

            } else {

                progressDialog.dismiss();
               //   Snackbar.make(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
               //         .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));

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

                    equipmentIds.setIds(selectedAssignedToUserIds);
                    String jsonString = gson.toJson(equipmentIds);

                    Log.i("json String :: ", jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
//                Snackbar.make(coordinateAssignToUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
  //                      .show();

                Utilities.showSnackbar(coordinateAssignToUser, getActivity().getResources().getString(R.string.network_connection_failed));


            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

//                Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());

                handler.sendEmptyMessage(104);

            } else {

                //Snackbar.make(coordinateAssignToUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateAssignToUser, "" + data.getMessage());
                handler.sendEmptyMessage(104);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }


    public class EquipmentIds {

        private List<Integer> ids;

        public List<Integer> getIds() {
            return ids;
        }

        public void setIds(List<Integer> ids) {
            this.ids = ids;
        }

    }

}





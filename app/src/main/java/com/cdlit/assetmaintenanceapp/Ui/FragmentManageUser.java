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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperUsers;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddUser;
import com.cdlit.assetmaintenanceapp.Dialog.DialogDeleteConfirm;
import com.cdlit.assetmaintenanceapp.Dialog.DialogUserPrivilege;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.PrivilegeReturn;
import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.cdlit.assetmaintenanceapp.Model.User;
import com.cdlit.assetmaintenanceapp.Model.UserAdd;
import com.cdlit.assetmaintenanceapp.Model.UserAddReturn;
import com.cdlit.assetmaintenanceapp.Model.UserPrivilege;
import com.cdlit.assetmaintenanceapp.Model.UserPrivilegeUpdate;
import com.cdlit.assetmaintenanceapp.Model.UserResponse;
import com.cdlit.assetmaintenanceapp.Model.UserUpdate;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by rakesh on 13-06-2017.
 */


public class FragmentManageUser extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener, RecyclerItemTouchHelperListener, ActionModeListener {

    private static final String TAG = FragmentManageUser.class.getSimpleName();
    private CoordinatorLayout coordinateAddUser;
    private FloatingActionButton fabAddUser;
    private DialogAddUser dialogAddUser;
    private Handler handler;
    private SwipeRefreshLayout swipeRefreshMagageUsers;
    private RecyclerView recyclerMagageUsers;
    private ArrayList<UserResponse> listUsers;
    private AdaperUsers adapterUsers;
    private Set<String> listPrivilege;
    private List<PrivilegeReturn> listPrivilegeReturn;
    private DialogUserPrivilege dialogUserPrivilege;
    private String userName;
    private Integer userTempId;
    private ArrayList<String> listUserPrivileges;
    private Integer userId;
    private HashMap<String, Integer> mapUserPrivileges;
    private ArrayList<Integer> listUserPrivilegesId;
    //   private List<UserPrivilegeUpdate.PrivilegeUpdate> listPrivileges;
    private ArrayList<String> listPrivilegeItem;
    private String userType;
    private String tempUserType;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<String> listImageResponse;
    private HashMap<Integer, String> mapImageResponse;
    private RecyclerItemTouchHelper itemTouchHelperCallback;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback1;
    private ItemTouchHelper mItemTouchHelper;
    private UserResponse deletedItem;
    private int deletedIndex;
    private boolean swipFlag;
    private ActionModeCallback actionModeCallback;
    public ActionMode actionMode;
    private ArrayList<Integer> selectedUsersIds;
    public MenuItem searchItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_manage_user, container, false);

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

     /*   MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();*/

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

        List<UserResponse> filteredValues = new ArrayList<UserResponse>(listUsers);

        List<UserResponse> filteredData = new ArrayList<UserResponse>();

        for (UserResponse value : listUsers) {

         /*   if (!value.getFirstName().toLowerCase().contains(newText.toLowerCase())) {

                filteredValues.remove(value);

            }*/

            if (value.getFirstName() != null && value.getLastName() != null && value.getLocation() != null && value.getUserType() != null) {

                if (value.getFirstName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredData.add(value);
                } else if (value.getLastName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredData.add(value);
                } else if (value.getLocation().getLocationName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredData.add(value);
                } else if (value.getUserType().toLowerCase().contains(newText.toLowerCase())) {
                    filteredData.add(value);
                }

            }

        }

        adapterUsers = new AdaperUsers(FragmentManageUser.this, filteredData, this);
        recyclerMagageUsers.setAdapter(adapterUsers);
        checkEmptyView(adapterUsers);

        return false;
    }

    private void resetSearch() {

        adapterUsers = new AdaperUsers(FragmentManageUser.this, listUsers, this);

        recyclerMagageUsers.setAdapter(adapterUsers);

        checkEmptyView(adapterUsers);

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Personnel Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Crashlytics.log("" + FragmentManageUser.class.getName());

        fabAddUser = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_user);

        coordinateAddUser = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout_adduser);

        swipeRefreshMagageUsers = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_manage_users);

        emptyView = (ViewStub) getActivity().findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        swipeRefreshMagageUsers.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshMagageUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getUserRequest();

                swipeRefreshMagageUsers.setRefreshing(false);

            }
        });

        listUsers = new ArrayList<UserResponse>();

        adapterUsers = new AdaperUsers(FragmentManageUser.this, listUsers, this);

        recyclerMagageUsers = (RecyclerView) getActivity().findViewById(R.id.recycler_manage_users);

        recyclerMagageUsers.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerMagageUsers.setLayoutManager(mLayoutManager);
        //  recyclerMagageUsers.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //  recyclerMagageUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerMagageUsers.setAdapter(adapterUsers);

        checkEmptyView(adapterUsers);

        listPrivilege = Utilities.getSetFromSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {

            fabAddUser.setVisibility(View.GONE);

        }


        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddUserDialog();

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 100) {

                    //  adapterUsers.notifyDataSetChanged();

                    //  checkEmptyView(adapterUsers);

                    //   loadImages();

                    adapterUsers.notifyDataSetChanged();

                    checkEmptyView(adapterUsers);

                } else if (msg.what == 101) {

                    dialogAddUser.dismiss();

                    opneUserPrivilege();

                } else if (msg.what == 102) {

                    getUserRequest();

                } else if (msg.what == 103) {

                    dialogUserPrivilege.dismiss();

                    getUserRequest();

                    ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

                    context.addMenutItem(false);

                } else if (msg.what == 104) {

                    dialogAddUser.dismiss();

                    getUserRequest();

                    ActivityNavigationDrawerAdmin context = (ActivityNavigationDrawerAdmin) getActivity();

                    context.addMenutItem(false);

                } else if (msg.what == 105) {

                    openUpdateUserPrivilegeDialog();

                } else if (msg.what == 106) {

//                  adapterUsers.updateImage(position, imageResponse);

                }


                return false;
            }
        });


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        if (listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {

            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerMagageUsers);
            itemTouchHelperCallback.setSwipeEnable(true);

        }

        actionModeCallback = new ActionModeCallback();

        getUserRequest();

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.menu_action_mode_manage_user, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshMagageUsers.setEnabled(false);

          /*  adapterEquipment.MyViewHolder.imageEquipment.setEnabled(false);
            adapterEquipment.MyViewHolder.imageEquipment.setClickable(false);*/

            if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {

                fabAddUser.setVisibility(View.GONE);

            }

            if (listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(false);

            }

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            Log.e("" + TAG, "onPrepareActionMode");

            if (adapterUsers.getSelectedItemCount() > 1) {

                if (listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {
                    menu.getItem(1).setVisible(false);
                }
                if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(false);
                }

            } else {

                if (listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {
                    menu.getItem(0).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {
                    menu.getItem(1).setVisible(true);
                }
                if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {
                    menu.getItem(2).setVisible(true);
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

                case R.id.action_privilege:

                    privilegeItem();
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

            adapterUsers.clearSelections();
            swipeRefreshMagageUsers.setEnabled(true);

           /* adapterEquipment.holder.imageEquipment.setEnabled(true);
            adapterEquipment.holder.imageEquipment.setClickable(true);*/

            if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {

                fabAddUser.setVisibility(View.VISIBLE);

            }

            if (listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {

                itemTouchHelperCallback.setSwipeEnable(true);

            }

            actionMode = null;

            recyclerMagageUsers.post(new Runnable() {
                @Override
                public void run() {
                    adapterUsers.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    private void privilegeItem() {

        List<Integer> selectedItemPositions =
                adapterUsers.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        updatePrivilegeClick(listUsers.get(selectedItemPositions.get(0)));

    }

    private void editItem() {

        List<Integer> selectedItemPositions =
                adapterUsers.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        editClick(listUsers.get(selectedItemPositions.get(0)));

    }

    // deleting the messages from recycler view
    private void deleteItem() {

        adapterUsers.resetAnimationIndex();

        List<Integer> selectedItemPositions =
                adapterUsers.getSelectedItems();

        Log.e("selectedItemPositions: ", "" + selectedItemPositions.toString());

        if (selectedItemPositions.size() == 1) {

            swipFlag = false;

            deleteClick(listUsers.get(selectedItemPositions.get(0)), null);

        } else {

            selectedUsersIds = new ArrayList<>();
            ArrayList<String> selectedUsersName = new ArrayList<>();

            for (int i = 0; i < selectedItemPositions.size(); i++) {

                int pos = selectedItemPositions.get(i);
                selectedUsersIds.add(listUsers.get(pos).getId());
                selectedUsersName.add(listUsers.get(pos).getFirstName() + " " + listUsers.get(pos).getLastName());

            }

            String deleteUserName = "";

            for (int i = 0; i < selectedUsersName.size(); i++) {

                if (i == (selectedUsersName.size() - 1)) {
                    deleteUserName = deleteUserName + selectedUsersName.get(i);
                } else {
                    deleteUserName = deleteUserName + selectedUsersName.get(i) + " , ";
                }

            }


            deleteClick(null, deleteUserName);


        }

    }


    @AddTrace(name = "GetUserRequest", enabled = true)
    private void getUserRequest() {

        GetUserRequest getUserRequest = new GetUserRequest();

    }

    private void loadImages() {

        listImageResponse = new ArrayList<String>();

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterUsers.getItemCount(); i++) {

            String imagePath = listUsers.get(i).getImagePath();

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                      getImageRequest(i, listUsers.get(i).getImagePath());

                //    createImageUrl(i, listUsers.get(i).getImagePath());

                }

            }

        }

    }

    private void createImageUrl(int position, String imagePath) {

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

        String url = null;

        try {
            url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/get/" + Utilities.USERS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("url---", "" + url);
        // adapterUsers.updateUserImage(position, url);

        listUsers.get(position).setImage_path(url);

    }

    @AddTrace(name = "ManageUserImageRequest", enabled = true)
    private void getImageRequest(int i, String imagePath) {

        ImageRequest imageRequest = new ImageRequest(i, imagePath);

    }


    private void openUpdateUserPrivilegeDialog() {

        dialogUserPrivilege = DialogUserPrivilege.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.update_user_privilege_dialog_title));
        bundle.putString("user_name", userName);
        bundle.putInt("user_temp_id", userId);
        bundle.putString("user_type", userType);
        bundle.putString("temp_user_type", tempUserType);

        bundle.putStringArrayList("list_user_privileges", listUserPrivileges);
        bundle.putIntegerArrayList("list_user_privileges_id", listUserPrivilegesId);

        dialogUserPrivilege.setArguments(bundle);
        dialogUserPrivilege.setTargetFragment(FragmentManageUser.this, 0);
        dialogUserPrivilege.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void opneUserPrivilege() {

        dialogUserPrivilege = DialogUserPrivilege.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.add_user_privilege_dialog_title));
        bundle.putString("user_name", userName);
        bundle.putInt("user_temp_id", userTempId);

        dialogUserPrivilege.setArguments(bundle);
        dialogUserPrivilege.setTargetFragment(FragmentManageUser.this, 0);
        dialogUserPrivilege.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openUpdateUserDialog(UserResponse userResponse) {

        dialogAddUser = DialogAddUser.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.updateUser_dialog_title));
        bundle.putParcelable("response", userResponse);

        dialogAddUser.setArguments(bundle);
        dialogAddUser.setTargetFragment(FragmentManageUser.this, 0);
        dialogAddUser.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    private void openAddUserDialog() {

        dialogAddUser = DialogAddUser.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.addUser_dialog_title));

        dialogAddUser.setArguments(bundle);
        dialogAddUser.setTargetFragment(FragmentManageUser.this, 0);
        dialogAddUser.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @AddTrace(name = "AddUserRequest", enabled = true)
    public void addUserPositiveClick(String encodedString, String firstName, String lastName, String userName, String password,
                                     String confirmPassword, String emailId, String employeeId, String originalPass, UserResponse response, Integer loactionId) {

        this.userName = userName;
        AddUserRequest addUserRequest = new AddUserRequest(encodedString, firstName, lastName, userName, password, confirmPassword, emailId, employeeId, originalPass, loactionId);

    }

    public void editClick(UserResponse userResponse) {

        openUpdateUserDialog(userResponse);

    }

    public void deleteClick(UserResponse userResponse, String selectedUserName) {

        DialogDeleteConfirm dialogDeleteConfirm = DialogDeleteConfirm.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("response", userResponse);

        if (userResponse == null) {
            bundle.putString("item", selectedUserName);
        } else {
            bundle.putString("item", userResponse.getFirstName() + " " + userResponse.getLastName());
        }

        //    bundle.putString("item", userResponse.getFirstName() + " " + userResponse.getLastName());

        dialogDeleteConfirm.setArguments(bundle);
        dialogDeleteConfirm.setTargetFragment(FragmentManageUser.this, 6);
        dialogDeleteConfirm.show(getActivity().getSupportFragmentManager(), "dialog");

    }


    @AddTrace(name = "UserDeactiveRequest", enabled = true)
    public void deletePositiveClick(UserResponse response) {

        UserDeactiveRequest locationDeactiveRequest = new UserDeactiveRequest(response);

    }

    @AddTrace(name = "UpdateUserRequest", enabled = true)
    public void updateUserPositiveClick(String encodedString, String firstName, String lastName, String userName, String emailId, String employeeId, UserResponse response, Integer loactionId) {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(encodedString, firstName, lastName, userName, emailId, employeeId, response, loactionId);

    }

    @AddTrace(name = "SendPrivilege", enabled = true)
    public void addUserPrivilegePositiveClick(List<PrivilegeReturn> listPrivilegeReturn) {

        SendPrivilege sendPrivilege = new SendPrivilege(listPrivilegeReturn);

    }

    @AddTrace(name = "GetUserPrivilege", enabled = true)
    public void updatePrivilegeClick(UserResponse userResponse) {

        userId = userResponse.getId();

        Log.e("userId", "" + userId);

        GetUserPrivilege getUserPrivilege = new GetUserPrivilege(userId);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof AdaperUsers.MyViewHolder) {

            // get the removed item name to display it in snack bar
            String name = listUsers.get(viewHolder.getAdapterPosition()).getFirstName();

            // backup of removed item for undo purpose
            deletedItem = listUsers.get(viewHolder.getAdapterPosition());
            deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapterUsers.removeItem(viewHolder.getAdapterPosition());

            swipFlag = true;

            deleteClick(deletedItem, null);

        }


    }

    public void multiDeletePositiveClick() {

        multiDeleteUserRequest(selectedUsersIds);

    }

    @AddTrace(name = "MultiDeleteUserRequest", enabled = true)
    private void multiDeleteUserRequest(ArrayList<Integer> selectedUsersIds) {

        MultiDeleteUserRequest multiDeleteEquipementRequest = new MultiDeleteUserRequest(selectedUsersIds);

    }

    public void deleteNegativeClick(UserResponse response) {

        if (swipFlag) {
            // undo is selected, restore the deleted item
            adapterUsers.restoreItem(deletedItem, deletedIndex);
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

    private void toggleSelection(int position) {

        adapterUsers.toggleSelection(position);

        int count = adapterUsers.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (adapterUsers.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            //    Toast.makeText(getActivity(), "Read: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //   actionMode = getActivity().startActionMode(actionModeCallback);
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }


    private class AddUserRequest implements LoaderManager.LoaderCallbacks<UserAdd> {

        private ProgressDialog progressDialog;
        private static final int LOADER_ADD_USER = 1;
        private final Integer loactionId;
        private String encodedString;
        private String firstName;
        private String lastName;
        private String userName;
        private String password;
        private String confirmPassword;
        private String emailId;
        private String employeeId;
        private String userid;
        private String originalPass;


        public AddUserRequest(String encodedString, String firstName, String lastName, String userName, String password, String confirmPassword, String emailId, String employeeId, String originalPass, Integer loactionId) {

            this.encodedString = encodedString;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.emailId = emailId;
            this.employeeId = employeeId;
            this.originalPass = originalPass;
            this.loactionId = loactionId;

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //     progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/add";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_USER, args, this);

            } else {

                progressDialog.dismiss();
                //      Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //            .show();
                //      Toast.makeText(getActivity(), "" + R.string.network_connection_failed, Toast.LENGTH_SHORT).show();

                Utilities.showToast(getActivity(), getActivity().getResources().getString(R.string.network_connection_failed));

            }
        }

        @Override
        public Loader<UserAdd> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    UserAddReturn userAddReturn = new UserAddReturn();
                    userAddReturn.setId(0);
                    userAddReturn.setCreatedDate(Utilities.currentDateTime());
                    userAddReturn.setCreatedUser(Integer.parseInt(userid));
                    userAddReturn.setEmailId(emailId);
                    userAddReturn.setFirstName(firstName);
                    userAddReturn.setImagePath(null);
                    userAddReturn.setImage_bitmap(encodedString);
                    userAddReturn.setIsActive(0);
                    userAddReturn.setLastName(lastName);
                    userAddReturn.setModifiedDate(Utilities.currentDateTime());
                    userAddReturn.setModifiedUser(Integer.parseInt(userid));
                    userAddReturn.setPassword(password);
                    userAddReturn.setEmployee_no(employeeId);
                    userAddReturn.setPasswordChange(1);
                    userAddReturn.setUserName(userName);
                    userAddReturn.setOriginal_password(originalPass);
                    userAddReturn.setTemp_location_id(loactionId);
                    userAddReturn.setLocation(null);

                    //userAddReturn.setUsertype(null);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(userAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<UserAdd>(getActivity(), httpWrapper, UserAdd.class);

                }

            } else {

                progressDialog.dismiss();

                //   Toast.makeText(getActivity(), "" + R.string.network_connection_failed, Toast.LENGTH_SHORT).show();

                Utilities.showToast(getActivity(), getActivity().getResources().getString(R.string.network_connection_failed));

                //    Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<UserAdd> loader, UserAdd data) {
            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                userTempId = data.getResponse().getId();
                Log.e("userTempId", "" + userTempId);

                handler.sendEmptyMessage(101);

            } else {

                Toast.makeText(getActivity(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                //   Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<UserAdd> loader) {


        }
    }

    private class GetUserRequest implements LoaderManager.LoaderCallbacks<User> {

        private static final int LOADER_GET_USERS = 1;
        private ProgressDialog progressDialog;

        public GetUserRequest() {
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());
                String locationId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, null);
                String userType = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_TYPE, null);

                Log.e("userType", "" + userType);

                Bundle args = new Bundle();

                String url = null;

                if (getActivity().getResources().getString(R.string.Administrator).equalsIgnoreCase(userType)) {

                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/all";

                } else if (getActivity().getResources().getString(R.string.Supervisor).equalsIgnoreCase(userType)) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/all/" + URLEncoder.encode(locationId.toString(), "UTF-8") + "/" + "all";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                } else if (getActivity().getResources().getString(R.string.Operator).equalsIgnoreCase(userType)) {

                    try {
                        url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/all/" + URLEncoder.encode(locationId.toString(), "UTF-8") + "/" + "all";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                }

                //    String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_USERS, args, this);

            } else {

                progressDialog.dismiss();
                // Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //         .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<User>(getActivity(), httpWrapper, User.class);

                }

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();


                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<User> loader, User data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listUsers.clear();

                for (int i = 0; i < data.getResponse().size(); i++) {

                    UserResponse response = data.getResponse().get(i);

                    //response.setImage_path(createImageUrl(i, response.getImagePath()));

                    listUsers.add(response);

                //    createImageUrl(i, listUsers.get(i).getImagePath());

                }

               /* for (UserResponse user : data.getResponse()) {

                    listUsers.add(user);
                    createImageUrl(i, listUsers.get(i).getImagePath());


                }*/

                loadImages();

                handler.sendEmptyMessage(100);

            } else {

                //  Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAddUser, "" + data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<User> loader) {


        }


    }

    private class UserDeactiveRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private static final int LOADER_DEACTIVE_USER = 1;

        private UserResponse response;
        private ProgressDialog progressDialog;

        public UserDeactiveRequest(UserResponse response) {

            String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

            this.response = response;

            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //  progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //   String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/deactive" + "/" + response.getId() + "/" + userid;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/deactive" + "/" + URLEncoder.encode(response.getId().toString(), "UTF-8") + "/" + URLEncoder.encode(userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DEACTIVE_USER, args, this);

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //      .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

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
                //   Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //        .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {
            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                handler.sendEmptyMessage(102);

            } else {

                //  Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAddUser, "" + data.getMessage());

                handler.sendEmptyMessage(102);
            }

            progressDialog.dismiss();
        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    private class UpdateUserRequest implements LoaderManager.LoaderCallbacks<UserUpdate> {

        private final UserResponse response;
        private static final int LOADER_UPDATE_USER = 1;
        private final String encodedString;
        private final String firstName;
        private final String lastName;
        private final String userName;
        private final String emailId;
        private final String employeeId;
        private final String userid;
        private final Integer loactionId;
        private ProgressDialog progressDialog;


        public UpdateUserRequest(String encodedString, String firstName, String lastName, String userName, String emailId, String employeeId, UserResponse response, Integer loactionId) {

            this.encodedString = encodedString;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.emailId = emailId;
            this.employeeId = employeeId;
            this.response = response;
            this.loactionId = loactionId;

            userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                //   progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/update";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_UPDATE_USER, args, this);

            } else {

                progressDialog.dismiss();
                //              Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                    .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<UserUpdate> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    UserAddReturn userAddReturn = new UserAddReturn();

                    userAddReturn.setContactNo(null);
                    userAddReturn.setCreatedDate(response.getCreatedDate());
                    userAddReturn.setCreatedUser(response.getCreatedUser());
                    userAddReturn.setEmailId(emailId);
                    userAddReturn.setEmployee_no(employeeId);
                    userAddReturn.setFirstName(firstName);
                    userAddReturn.setId(response.getId());
                    userAddReturn.setImagePath(response.getImagePath());
                    userAddReturn.setImage_bitmap(encodedString);
                    userAddReturn.setIsActive(response.getIsActive());
                    userAddReturn.setLastName(lastName);
                    userAddReturn.setModifiedDate(Utilities.currentDateTime());
                    userAddReturn.setModifiedUser(Integer.parseInt(userid));
                    userAddReturn.setPassword(null);
                    userAddReturn.setPasswordChange(null);
                    userAddReturn.setUserName(response.getUserName());
                    userAddReturn.setOriginal_password(null);
                    //   userAddReturn.setUsertype(null);
                    userAddReturn.setTemp_location_id(loactionId);
                    userAddReturn.setLocation(null);

                    // for specific
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(userAddReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<UserUpdate>(getActivity(), httpWrapper, UserUpdate.class);

                }

            } else {
                progressDialog.dismiss();

                //      Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //            .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<UserUpdate> loader, UserUpdate data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                String loginuserId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);
                String loginuserName = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_NAME, null);

                Log.e("loginuserId", "" + loginuserId);

                String userName = data.getResponse().getUserName();
                Log.e("userName", "" + userName);

                String userEmail = data.getResponse().getEmailId();
                Integer id = data.getResponse().getId();
                Log.e("userid", "" + id);
                String userFirstLastName = data.getResponse().getFirstName() + " " + data.getResponse().getLastName();

                String imagePath = data.getResponse().getImage_path();

                String bitmap = data.getResponse().getImage_bitmap();

                Integer locationId = data.getResponse().getLocationId();

                String locationName = data.getResponse().getLocationName();

                if (loginuserName.equals(userName)) {

                    //   if (id == Integer.valueOf(loginuserId)) {
                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_NAME, userName);
                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_FIRST_LAST_NAME, userFirstLastName);

                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_EMAIL, userEmail);
                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_ID, id.toString());

                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_IMAGE, bitmap);
                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_IMAGE_PATH, imagePath);

                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_ID, locationId.toString());
                    Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_LOCATION_NAME, locationName);

                }

                //     userTempId = data.getResponse().getId();
                handler.sendEmptyMessage(104);

            } else {

                //    Toast.makeText(getActivity(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                //    Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

                Utilities.showSnackbar(coordinateAddUser, "" + data.getMessage());

            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<UserUpdate> loader) {

        }
    }

    private class SendPrivilege implements LoaderManager.LoaderCallbacks<UserPrivilegeUpdate> {

        private static final int LOADER_SUBMIT_CPRIVILEGE = 1;
        private final List<PrivilegeReturn> listPrivilegeReturn;
        private ProgressDialog progressDialog;

        public SendPrivilege(List<PrivilegeReturn> listPrivilegeReturn) {

            this.listPrivilegeReturn = listPrivilegeReturn;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                Bundle args = new Bundle();
                String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userPrivilege/save";
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_SUBMIT_CPRIVILEGE, args, this);

            } else {

                progressDialog.dismiss();

                Utilities.showToast(getActivity(), getActivity().getResources().getString(R.string.network_connection_failed));

            }
        }

        @Override
        public Loader<UserPrivilegeUpdate> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    String jsonString = gson.toJson(listPrivilegeReturn);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_POST, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);
                    return new RestLoader<UserPrivilegeUpdate>(getActivity(), httpWrapper, UserPrivilegeUpdate.class);

                }

            } else {

                progressDialog.dismiss();

                //     Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_connection_failed), Toast.LENGTH_SHORT).show();
                //      Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //               .show();

                Utilities.showToast(getActivity(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<UserPrivilegeUpdate> loader, UserPrivilegeUpdate data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                String loginuserId = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                int userIdUpdate = data.getResponse().getUserid();

                List<UserPrivilegeUpdate.PrivilegeUpdate> listPrivileges = new ArrayList<>(data.getResponse().getPrivilege());

                listPrivilegeItem = new ArrayList<String>();

                Utilities.setStringInSharedPreferances(getActivity(), Global.KEY_USER_TYPE, listPrivileges.get(0).getType());

                for (UserPrivilegeUpdate.PrivilegeUpdate privilege : listPrivileges) {
                    listPrivilegeItem.add(privilege.getAction());
                }

                Set<String> setPrivilegeItem = new HashSet<String>(listPrivilegeItem);

                if (userIdUpdate == Integer.valueOf(loginuserId)) {

                    Utilities.setStringArrayInSharedPreferances(getActivity(), Global.KEY_PRIVILEGE_ID, setPrivilegeItem);

                }

                handler.sendEmptyMessage(103);

            } else {

                //     Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_LONG).show();

                Utilities.showToast(getActivity(), data.getMessage());

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<UserPrivilegeUpdate> loader) {

        }
    }

    private class GetUserPrivilege implements LoaderManager.LoaderCallbacks<UserPrivilege> {

        private static final int LOADER_GET_USERS_PRIVILEGES = 1;
        private ProgressDialog progressDialog;
        private Integer userId;

        public GetUserPrivilege(Integer userId) {

            this.userId = userId;
            progressDialog = Utilities.startProgressDialog(getActivity());

            if (Utilities.isNetworkAvailable(getActivity())) {

                // progressDialog = Utilities.startProgressDialog(getActivity());

                Bundle args = new Bundle();

                //   String url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userPrivilege/getUserPrivilege/" + userId;

                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "userPrivilege/getUserPrivilege/" + URLEncoder.encode(userId.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_USERS_PRIVILEGES, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //       .show();
                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<UserPrivilege> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<UserPrivilege>(getActivity(), httpWrapper, UserPrivilege.class);

                }

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<UserPrivilege> loader, UserPrivilege data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                listUserPrivileges = new ArrayList<String>();
                listUserPrivilegesId = new ArrayList<Integer>();

                mapUserPrivileges = new HashMap<String, Integer>();

                for (int i = 0; i < data.getResponse().size(); i++) {

                    listUserPrivileges.add(data.getResponse().get(i).getAction().getAction());
                    listUserPrivilegesId.add(data.getResponse().get(i).getId());

                    mapUserPrivileges.put(data.getResponse().get(i).getAction().getAction(), data.getResponse().get(i).getId());

                    userType = data.getResponse().get(i).getAction().getType();
                    userName = data.getResponse().get(i).getUser().getUserName();

                    tempUserType = data.getResponse().get(i).getTemp_user_type();

                }

                progressDialog.dismiss();
                handler.sendEmptyMessage(105);

            } else {

                listUserPrivileges = new ArrayList<String>();
                listUserPrivilegesId = new ArrayList<Integer>();
                mapUserPrivileges = new HashMap<String, Integer>();

                userType = "";
                userName = "";

                progressDialog.dismiss();
                handler.sendEmptyMessage(105);
                //     Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

            progressDialog.dismiss();


        }

        @Override
        public void onLoaderReset(Loader<UserPrivilege> loader) {

        }
    }

    private class ImageRequest implements LoaderManager.LoaderCallbacks<ImageUrl> {

        private final int position;
        private String imagePath = null;
        private ProgressDialog progressDialog;
        //   private static final int LOADER_ADD_USER = 1;
        //   private int pos;

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
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.USERS_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                //     Log.e("position:---", "" + position);

                getActivity().getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                //      progressDialog.dismiss();
                //     Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();

                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));

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

                //   Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //       .show();


                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));


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

                adapterUsers.updateImage(loaderId, path, imageBitmap);

                //   adapterUsers.updateUserImage(loaderId, path, imageBitmap);

            } else {

                //   Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

    private class MultiDeleteUserRequest implements LoaderManager.LoaderCallbacks<RestResponse> {

        private final List<Integer> selectedUsersIds;
        private static final int LOADER_DELETE_MMULTIPLE_USER = 1;
        ProgressDialog progressDialog = null;

        public MultiDeleteUserRequest(List<Integer> selectedUsersIds) {

            this.selectedUsersIds = selectedUsersIds;
            progressDialog = Utilities.startProgressDialog(getActivity());
            if (Utilities.isNetworkAvailable(getActivity())) {

                //progressDialog = Utilities.startProgressDialog(getActivity());
                String userid = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_ID, null);

                Bundle args = new Bundle();
                String url = null;
                try {
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "user/deactive/" + URLEncoder.encode("" + userid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                args.putString(Global.ARGS_URI, url);

                getActivity().getSupportLoaderManager().restartLoader(LOADER_DELETE_MMULTIPLE_USER, args, this);

            } else {

                progressDialog.dismiss();
                //  Snackbar.make(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //          .show();
                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));
            }

        }

        @Override
        public Loader<RestResponse> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    Gson gson = new GsonBuilder().
                            setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls()
                            .create();

                    UsersId usersId = new UsersId();

                    usersId.setUserIds(selectedUsersIds);

                    String jsonString = gson.toJson(usersId);
                    Log.i(TAG, "json String :: " + jsonString);

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_PUT, Global.JSON_RESPONSE, HttpWrapper.CONTENT_TYPE_JSON, args.getString(Global.ARGS_URI), jsonString);

                    return new RestLoader<RestResponse>(getActivity(), httpWrapper, RestResponse.class);

                }

            } else {

                progressDialog.dismiss();
                //    Snackbar.make(coordinateAddUser, R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //         .show();
                Utilities.showSnackbar(coordinateAddUser, getActivity().getResources().getString(R.string.network_connection_failed));
            }


            return null;
        }

        @Override
        public void onLoadFinished(Loader<RestResponse> loader, RestResponse data) {

            if (null != data && data.getStatus().equalsIgnoreCase("success")) {

                //         Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateAddUser, "" + data.getMessage());

                handler.sendEmptyMessage(102);

            } else {

                //     Snackbar.make(coordinateAddUser, "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();
                Utilities.showSnackbar(coordinateAddUser, "" + data.getMessage());

                handler.sendEmptyMessage(102);

            }

            progressDialog.dismiss();

        }

        @Override
        public void onLoaderReset(Loader<RestResponse> loader) {

        }
    }

    public class UsersId {

        private List<Integer> userIds;

        public List<Integer> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<Integer> userIds) {
            this.userIds = userIds;
        }

    }

}

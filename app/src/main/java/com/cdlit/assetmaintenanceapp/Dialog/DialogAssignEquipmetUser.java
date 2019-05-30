package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperAssignEquipmentType;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipmentToUser;
import com.cdlit.assetmaintenanceapp.Model.AssignedEquipment;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentToUser;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogAssignEquipmetUser extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddCategory;
    private String okString;
    private CheckBox cbAssignToUser;
    private ImageView imageEquipmentType;
    private TextView tvEquipmentType;
    private ArrayList<AssignEquipmentToUser.Response> listEquipmentType;
    private RecyclerView recyclerEquipmentType;
    private AdaperAssignEquipmentType adapterEquipmentType;
    private ArrayList<AssignedEquipment.Response> listAssignedEquipmentType;
    private ArrayList<Integer> listAssignedEquipmentTypeIds;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private ArrayList<String> listImageResponse;
    private HashMap<Integer, String> mapImageResponse;

    public static DialogAssignEquipmetUser newInstance() {

        DialogAssignEquipmetUser dialogAssignEquipmetUser = new DialogAssignEquipmetUser();

        return dialogAssignEquipmetUser;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        listEquipmentType = args.getParcelableArrayList("list_equipment_type");

        listAssignedEquipmentType = args.getParcelableArrayList("list_assigned_equipment_type");

        listAssignedEquipmentTypeIds = new ArrayList<Integer>();

        for (int i = 0; i < listAssignedEquipmentType.size(); i++) {

            listAssignedEquipmentTypeIds.add(listAssignedEquipmentType.get(i).getEquipment_id());

        }

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_assign_equipment_user, null);

        recyclerEquipmentType = (RecyclerView) view.findViewById(R.id.recycler_equipment_type);

        emptyView = (ViewStub) view.findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        adapterEquipmentType = new AdaperAssignEquipmentType(getActivity(), listEquipmentType, listAssignedEquipmentTypeIds);

        recyclerEquipmentType.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentType.setLayoutManager(mLayoutManager);

        recyclerEquipmentType.setAdapter(adapterEquipmentType);

        checkEmptyView(adapterEquipmentType);

        loadImages();

        if (title.equalsIgnoreCase(getResources().getString(R.string.assign_equipment_type))) {

            okString = getResources().getString(R.string.add_bt_string);

        } else {

            okString = getResources().getString(R.string.update_bt_string);

        }

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.assign_equipment_type))) {

                    if (adapterEquipmentType.getItemCount() == 0) {

                        Utilities.showSnackbar(v, "No asset available to assign");

                    } else if (adapterEquipmentType.listAssignedIds.size() == 0) {

                        //    Snackbar.make(v, "Please select any Asset Category", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please select any Asset Category");

                    } else {

                        ((FragmentEquipmentToUser) getTargetFragment()).assignEquipmentToUser(adapterEquipmentType.listAssignedEquipmentTypeIds);

                    }

                } else {


                }

            }
        });

        return dialog;
    }

    private void loadImages() {

        listImageResponse = new ArrayList<String>();

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < adapterEquipmentType.getItemCount(); i++) {

            String imagePath;

            if (listEquipmentType.get(i).getEquipmentImages() == null || listEquipmentType.get(i).getEquipmentImages().size() == 0) {
                imagePath = null;
            } else {
                imagePath = listEquipmentType.get(i).getEquipmentImages().get(0).getImagePath();
            }


            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    ImageRequest imageRequest = new ImageRequest(i, listEquipmentType.get(i).getEquipmentImages().get(0).getImagePath());

                }

            }

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

                //                Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

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

//                Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //                      .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

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

                //   Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }

}

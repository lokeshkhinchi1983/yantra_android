package com.cdlit.assetmaintenanceapp.Ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterMultiImages;
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.R;
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

public class ActivityFaultLogDetail extends AppCompatActivity {

    private TextView tvEquipment;
    private TextView tvComment;
    private TextView tvFaultType;
    private TextView tvCheckedBy;
    private RecyclerView recyclerFaultLog;
    private EquipmentFalulList.Response faultDetail;
    private String equipment_name;
    private String comment;
    private int fault_type;
    private String user_name;
    private String logged_date;
    private ArrayList<String> image_path;
    private ArrayList<Bitmap> listBitmap;
    private AdapterMultiImages adapterMultiImages;
    private HashMap<Integer, String> mapImageResponse;
    public CoordinatorLayout coordinatorLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fault_log_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvEquipment = (TextView) findViewById(R.id.tv_equipment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvFaultType = (TextView) findViewById(R.id.tv_fault_type);
        tvCheckedBy = (TextView) findViewById(R.id.tv_checked_by);

        recyclerFaultLog = (RecyclerView) findViewById(R.id.recycler_fault_log);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        equipment_name = getIntent().getStringExtra("equipment_name");
        comment = getIntent().getStringExtra("comment");
        fault_type = getIntent().getIntExtra("fault_type", 0);
        user_name = getIntent().getStringExtra("user_name");
        logged_date = getIntent().getStringExtra("logged_date");
        image_path = getIntent().getStringArrayListExtra("image_path");

        tvEquipment.setText(equipment_name);

        tvComment.setText(comment);

        if (fault_type == 0) {
            tvFaultType.setText("Minor fault");
        } else if (fault_type == 1) {
            tvFaultType.setText("Major fault");
        } else {
            tvFaultType.setText("");
        }

        tvCheckedBy.setText( user_name + " on " + logged_date);

        listBitmap = new ArrayList<Bitmap>();

        for (int i = 0; i < image_path.size(); i++) {
            listBitmap.add(null);
        }


        adapterMultiImages = new AdapterMultiImages(this, listBitmap);

        recyclerFaultLog.setHasFixedSize(true);

        int numberOfColumns = 3;

        recyclerFaultLog.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        recyclerFaultLog.setAdapter(adapterMultiImages);

        loadImages();


    }

    private void loadImages() {

        mapImageResponse = new HashMap<Integer, String>();

        for (int i = 0; i < image_path.size(); i++) {

            String imagePath = image_path.get(i);

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    ImageRequest imageRequest = new ImageRequest(i, image_path.get(i));

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


            Log.e("imageUrlPath---", "" + imageUrlPath);

            if (Utilities.isNetworkAvailable(ActivityFaultLogDetail.this)) {

                Bundle args = new Bundle();
                String url = null;
                try {

                    url = Utilities.getStringFromSharedPreferances(ActivityFaultLogDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.CHECKLIST_ANSWER_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");

                    //     url = Utilities.getStringFromSharedPreferances(ActivityFaultLogDetail.this, Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.EQUIPMENT_TYPES_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                //     Log.e("position:---", "" + position);

                //final DialogAddEquipmentType.ImageRequest imageRequest = this;

                getSupportLoaderManager().restartLoader(position, args, this);

            } else {

                progressDialog.dismiss();
                //   Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                //    .show();

                Utilities.showSnackbar(coordinatorLayout, ActivityFaultLogDetail.this.getResources().getString(R.string.network_connection_failed));

            }

        }


        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {

            if (Utilities.isNetworkAvailable(ActivityFaultLogDetail.this)) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(getBaseContext(), httpWrapper, ImageUrl.class);

                }

            } else {

                //     Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                //       .show();

                Utilities.showSnackbar(coordinatorLayout, getBaseContext().getResources().getString(R.string.network_connection_failed));

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

                adapterMultiImages.updateImage(loaderId, path, imageBitmap);

            } else {

                //  Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    }


}

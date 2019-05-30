package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterMultiImages;
import com.cdlit.assetmaintenanceapp.Model.ImageUrl;
import com.cdlit.assetmaintenanceapp.Model.UserEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityEquipmentChecklistDetail;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.HttpWrapper;
import com.cdlit.assetmaintenanceapp.Utils.RestLoader;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogEquipmentChecklistImage extends DialogFragment {

    public static final String IMAGE_DIRECTORY_NAME = "assert_maintenance_directory";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = DialogEquipmentChecklistImage.class.getSimpleName();
    private static DialogEquipmentChecklistImage dialogEquipmentChecklistImage;
    private AlertDialog.Builder alertDialogBuilder;
    private FloatingActionButton fabSelectCamera;
    private Uri fileUri;
    private String filePath;
    private Bitmap decodedByte;
    private ArrayList<Bitmap> listBitmap;
    private AdapterMultiImages adapterMultiImages;
    private RecyclerView recyclerEquipmentChecklist;
    public boolean imageChanges;
    private UserEquipmentChecklist.Response userChecklistResponse;
    private ArrayList<String> listImagePath;
    private ImageRequest imageRequest;

    public static DialogEquipmentChecklistImage newInstance() {

        dialogEquipmentChecklistImage = new DialogEquipmentChecklistImage();
        return dialogEquipmentChecklistImage;

    }

    /* private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

         ExifInterface ei = new ExifInterface(selectedImage.getPath());
         int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

         switch (orientation) {
             case ExifInterface.ORIENTATION_ROTATE_90:
                 return rotateImage(img, 90);
             case ExifInterface.ORIENTATION_ROTATE_180:
                 return rotateImage(img, 180);
             case ExifInterface.ORIENTATION_ROTATE_270:
                 return rotateImage(img, 270);
             default:
                 return img;
         }
     }

     private Bitmap rotateImage(Bitmap img, int degree) {
         Matrix matrix = new Matrix();
         matrix.postRotate(degree);
         Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
         img.recycle();
         return rotatedImg;
     }

     private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
         int width = image.getWidth();
         int height = image.getHeight();

         float bitmapRatio = (float) width / (float) height;
         if (bitmapRatio > 0) {
             width = maxSize;
             height = (int) (width / bitmapRatio);
         } else {
             height = maxSize;
             width = (int) (height * bitmapRatio);
         }
         return Bitmap.createScaledBitmap(image, width, height, true);
     }
 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

               /* // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                filePath = fileUri.getPath();
                final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
*/


                Bitmap bitmap = null;

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, fileUri.getPath());

                } catch (IOException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                listBitmap.add(bitmap);

                adapterMultiImages.notifyDataSetChanged();

                imageChanges = true;

                Log.e("filePath", "" + fileUri.getPath());

                File file = new File(fileUri.getPath());

                if (file.delete()) {
                    Log.e("filePath", "" + file.getName() + "is deleted");
                } else {
                    Log.e("filePath", "" + file.getName() + "is not deleted");
                }

            } else if (resultCode == RESULT_CANCELED) {


            } else {


            }

        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        final String title = args.getString("title");
        final int position = args.getInt("position");
        final String timeStamp = args.getString("time_stamp");

        userChecklistResponse = args.getParcelable("user_checklist_response");
        //  Boolean todayDate = args.getBoolean("todayDate");

        listBitmap = new ArrayList<Bitmap>();
        listImagePath = new ArrayList<String>();
        imageChanges = false;

        if (userChecklistResponse.getImage_path() != null) {

            for (int i = 0; i < userChecklistResponse.getImage_path().size(); i++) {

                listBitmap.add(null);

                String imagePath = userChecklistResponse.getImage_path().get(i);

                Log.d("imagePath", "" + imagePath);

                listImagePath.add(imagePath);

            }

        }

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

//        alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
      //  customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_equipment_checklist, null);

        fabSelectCamera = (FloatingActionButton) view.findViewById(R.id.fab_select_camera);


        if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {

            fabSelectCamera.setEnabled(true);

        } else {

            fabSelectCamera.setEnabled(false);

        }

        adapterMultiImages = new AdapterMultiImages(getActivity(), DialogEquipmentChecklistImage.this, listBitmap, timeStamp);

        recyclerEquipmentChecklist = (RecyclerView) view.findViewById(R.id.recycler_equipment_checklist);
        recyclerEquipmentChecklist.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentChecklist.setLayoutManager(mLayoutManager);
        //  recyclerEquipmentChecklist.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //    recyclerEquipmentChecklist.setItemAnimator(new DefaultItemAnimator());

        int numberOfColumns = 3;

        recyclerEquipmentChecklist.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        recyclerEquipmentChecklist.setAdapter(adapterMultiImages);

        loadImages();

        fabSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                Log.e("fileUri", "" + fileUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            }

        });

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.bt_positive), new DialogInterface.OnClickListener() {
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
        //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {

                    ((ActivityEquipmentChecklistDetail) getActivity()).addEquipmentTypePositiveClick(listBitmap, position);

                } else {


                }

            }
        });

        return dialog;
    }

    private void loadImages() {
        for (int i = 0; i < adapterMultiImages.getItemCount(); i++) {

            String imagePath = listImagePath.get(i);

            if (imagePath != null) {

                if (!imagePath.equalsIgnoreCase("")) {

                    ImageRequest imageRequest = new ImageRequest(i, listImagePath.get(i));

                }

            }

        }
    }

    /**
     * Creating file uri to store image/video
     */


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;

    }


    LoaderManager.LoaderCallbacks<ImageUrl> imageUrlLoaderCallbacks = new LoaderManager.LoaderCallbacks<ImageUrl>() {

        @Override
        public Loader<ImageUrl> onCreateLoader(int id, Bundle args) {
            if (Utilities.isNetworkAvailable(getActivity())) {

                if (null != args && args.containsKey(Global.ARGS_URI)) {

                    HttpWrapper httpWrapper = new HttpWrapper(Global.HTTP_GET, Global.JSON_RESPONSE, args.getString(Global.ARGS_URI));

                    return new RestLoader<ImageUrl>(getActivity(), httpWrapper, ImageUrl.class);

                }

            } else {

                //Snackbar.make(getView(), R.string.network_connection_failed, Snackbar.LENGTH_LONG)
                  //      .show();

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

                adapterMultiImages.updateImage(loaderId, path, imageBitmap);

            } else {

                //       Snackbar.make(getView(), "" + data.getMessage(), Snackbar.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onLoaderReset(Loader<ImageUrl> loader) {

        }
    };


    public class ImageRequest {

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
                    url = Utilities.getStringFromSharedPreferances(getActivity(), Global.API_BASE_URL_KEY, Global.TEST_BASE_URL) + "image/" + Utilities.CHECKLIST_ANSWER_IMAGE + "/" + URLEncoder.encode(imageUrlPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                args.putString(Global.ARGS_URI, url);
                args.putInt("position", position);

                ((ActivityEquipmentChecklistDetail) getActivity()).getSupportLoaderManager().restartLoader(position, args, imageUrlLoaderCallbacks);

            } else {

                progressDialog.dismiss();
                //Snackbar.make(getView(), getActivity().getResources().getString(R.string.network_connection_failed), Snackbar.LENGTH_LONG)
                  //      .show();

                Utilities.showSnackbar(getView(), getActivity().getResources().getString(R.string.network_connection_failed));

            }

        }

    }

}

package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rakesh on 10-10-2017.
 */

public class DialogUserImage extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    // private FloatingActionButton fabRemoveImage;
    private FloatingActionButton fabAddImage;
    private ImageView imageUser;
    private AlertDialog.Builder alertDialogImage;
    private String encodedString = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2;
    private static final String TAG = DialogAddUser.class.getSimpleName();
    private static final int RESULT_LOAD = 3;
    private static final int PICK_IMAGE_REQUEST_CODE = 4;
    private Uri fileUri;
    private Bitmap bitmap = null;
    private String filePath;
    private String bitmapString;
    private ImageView imageRemove;
    private TextView tvAddImage;

    public static DialogUserImage newInstance() {

        DialogUserImage dialogUserImage = new DialogUserImage();
        return dialogUserImage;

    }

   /* private Bitmap rotateImageIfRequired(Bitmap img, String path) throws IOException {

        ExifInterface ei = new ExifInterface(path);
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

    }*/

    @Override
    public void onPause() {
        Log.e("DialogUserImage", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("DialogUserImage", "onStop");
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                imageUser.setImageBitmap(null);

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, fileUri.getPath());
                } catch (IOException e) {

                    e.printStackTrace();
                    Crashlytics.logException(e);
                }

                imageUser.setImageBitmap(bitmap);

                encodedString = getEncodedString(bitmap);

            } else if (resultCode == RESULT_CANCELED) {


            } else {


            }

        }

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                // imageUser.setImageBitmap(null);

                // imageUser.setImageResource(R.mipmap.ic_default_rounded_img);

                imageUser.setImageResource(R.drawable.ic_no_img);
                // Get the Image from data
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                Log.e("filePath", "" + filePath.toString());


                try {
                    bitmap = BitmapFactory
                            .decodeFile(filePath);
                    bitmap = Utilities.getResizedBitmap(bitmap, 500);
                    bitmap = Utilities.rotateImageIfRequired(bitmap, filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }


                // Set the Image in ImageView after decoding the String
                imageUser.setImageBitmap(bitmap);

                encodedString = getEncodedString(bitmap);

            } else if (requestCode == RESULT_CANCELED) {


            } else {


            }


        }

    }

    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        encodedString = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedString;

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("DialogUserImage", "onCreateDialog");

        ActivityNavigationDrawerAdmin.backroundflag = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.e("DialogUserImage", "onCreateDialog");

        Bundle args = getArguments();

        final String title = args.getString("title");

        // encodedString = args.getString("encodedString");

        encodedString = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_IMAGE, null);

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //  alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        //    customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
        customTitle.setText(title);

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_user_image, null);

        //  fabRemoveImage = (FloatingActionButton) view.findViewById(R.id.fab_remove_image);

        fabAddImage = (FloatingActionButton) view.findViewById(R.id.fab_add_image);

        imageUser = (ImageView) view.findViewById(R.id.image_user);
        imageRemove = (ImageView) view.findViewById(R.id.image_remove);

        tvAddImage = (TextView) view.findViewById(R.id.tv_add_image);


        //encodedString = Utilities.getStringFromSharedPreferances(getActivity(), Global.KEY_USER_IMAGE, null);

        //encodedString = response.getImage_bitmap();

        if (encodedString == null || encodedString.equalsIgnoreCase("")) {

            encodedString = "";

            bitmap = null;

            //imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
            imageUser.setImageResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageUser.setImageBitmap(bitmap);

        }

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (encodedString != null && !encodedString.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", encodedString);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(getActivity().getSupportFragmentManager(), "dialog");

                }
            }
        });


        // imageUser.setImageBitmap(bitmap);

        //  fabRemoveImage.setVisibility(View.VISIBLE);
        //  fabAddImage.setVisibility(View.INVISIBLE);

      /*  fabRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                encodedString = "";
                bitmap = null;
                //    imageUser.setImageBitmap(bitmap);
                imageUser.setImageResource(R.mipmap.ic_default_rounded_img);

                fabRemoveImage.setVisibility(View.INVISIBLE);
                fabAddImage.setVisibility(View.VISIBLE);

            }
        });*/

        imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                encodedString = "";
                bitmap = null;
                //    imageUser.setImageBitmap(bitmap);
                //    imageUser.setImageResource(R.mipmap.ic_default_rounded_img);

                imageUser.setImageResource(R.drawable.ic_no_img);

                //   fabRemoveImage.setVisibility(View.INVISIBLE);
                //   fabAddImage.setVisibility(View.VISIBLE);

            }
        });

        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageSelectionDialog();

            }
        });

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.update_bt_string), new DialogInterface.OnClickListener() {
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

        // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ActivityNavigationDrawerAdmin) getActivity()).updateUserImage(encodedString);

            }
        });

        return dialog;
    }

    private void openImageSelectionDialog() {

        alertDialogImage = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        alertDialogImage.setTitle("" + "Select Image");

        String[] listImageSelection = {"Select From Camera", "Select From Gallary"};

        alertDialogImage.setItems(listImageSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {

                    dialog.dismiss();
                    ActivityNavigationDrawerAdmin.backroundflag = false;
                    openCamera();

                } else {

                    dialog.dismiss();
                    ActivityNavigationDrawerAdmin.backroundflag = false;
                    openGallary();

                }

            }

        });

        final AlertDialog dialog = alertDialogImage.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;
        dialog.show();

    }


    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        Log.e("fileUri", fileUri.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

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
        } /*else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }*/ else {
            return null;
        }

        Log.e("mediaFile", mediaFile.toString());

        return mediaFile;
    }

    private void openGallary() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);
    }

}

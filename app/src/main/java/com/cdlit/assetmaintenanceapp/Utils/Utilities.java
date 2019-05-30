package com.cdlit.assetmaintenanceapp.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cdlit.assetmaintenanceapp.R;
import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by rakesh on 08-06-2017.
 */

public class Utilities {

    public static final String CATEGORY_MASTER_READ = "CATEGORY_MASTER_READ";
    public static final String CATEGORY_MASTER_WRITE = "CATEGORY_MASTER_WRITE";
    public static final String CATEGORY_MASTER_DELETE = "CATEGORY_MASTER_DELETE";

    public static final String LOCATION_MASTER_READ = "LOCATION_MASTER_READ";
    public static final String LOCATION_MASTER_WRITE = "LOCATION_MASTER_WRITE";
    public static final String LOCATION_MASTER_DELETE = "LOCATION_MASTER_DELETE";

    public static final String EQUIPMENT_MASTER_READ = "EQUIPMENT_MASTER_READ";
    public static final String EQUIPMENT_MASTER_WRITE = "EQUIPMENT_MASTER_WRITE";
    public static final String EQUIPMENT_MASTER_DELETE = "EQUIPMENT_MASTER_DELETE";
    //  public static final String EQUIPMENT_MASTER_TIMELINE = "EQUIPMENT_MASTER_TIMELINE";

    public static final String EQUIPMENT_TYPE_MASTER_READ = "EQUIPMENT_TYPE_MASTER_READ";
    public static final String EQUIPMENT_TYPE_MASTER_WRITE = "EQUIPMENT_TYPE_MASTER_WRITE";
    public static final String EQUIPMENT_TYPE_MASTER_DELETE = "EQUIPMENT_TYPE_MASTER_DELETE";

    public static final String USER_MASTER_READ = "USER_MASTER_READ";
    public static final String USER_MASTER_WRITE = "USER_MASTER_WRITE";
    public static final String USER_MASTER_DELETE = "USER_MASTER_DELETE";

    public static final String PERMISSION_MASTER_READ = "PERMISSION_MASTER_READ";
    public static final String PERMISSION_MASTER_WRITE = "PERMISSION_MASTER_WRITE";
    public static final String PERMISSION_MASTER_DELETE = "PERMISSION_MASTER_DELETE";

    public static final String EQUIPMENT_CHECKLIST_MASTER_READ = "EQUIPMENT_CHECKLIST_MASTER_READ";
    public static final String EQUIPMENT_CHECKLIST_MASTER_WRITE = "EQUIPMENT_CHECKLIST_MASTER_WRITE";
    public static final String EQUIPMENT_CHECKLIST_MASTER_DELETE = "EQUIPMENT_CHECKLIST_MASTER_DELETE";

    public static final String ROLE_MASTER_READ = "ROLE_MASTER_READ";
    public static final String ROLE_MASTER_WRITE = "ROLE_MASTER_WRITE";
    public static final String ROLE_MASTER_DELETE = "ROLE_MASTER_DELETE";

    public static final String ASSIGN_TO_USER_READ = "ASSIGN_TO_USER_READ";
    public static final String ASSIGN_TO_USER_WRITE = "ASSIGN_TO_USER_WRITE";
    public static final String ASSIGN_TO_USER_DELETE = "ASSIGN_TO_USER_DELETE";

    public static final String MYCHECKLIST_READ = "MYCHECKLIST_READ";
    public static final String MYCHECKLIST_WRITE = "MYCHECKLIST_WRITE";
    public static final String MYCHECKLIST_DELETE = "MYCHECKLIST_DELETE";

    public static final String REPAIRLOG_READ = "REPAIRLOG_READ";
    public static final String REPAIRLOG_WRITE = "REPAIRLOG_WRITE";
    public static final String REPAIRLOG_DELETE = "REPAIRLOG_DELETE";


    public static final String EQUIPMENT_TIMELINE_READ = "EQUIPMENT_TIMELINE_READ";
    public static final String EQUIPMENT_TIMELINE_WRITE = "EQUIPMENT_TIMELINE_WRITE";
    public static final String EQUIPMENT_TIMELINE_DELETE = "EQUIPMENT_TIMELINE_DELETE";


    public static final String FAULTLOG_READ = "FAULTLOG_READ";
    public static final String FAULTLOG_WRITE = "FAULTLOG_WRITE";
    public static final String FAULTLOG_DELETE = "FAULTLOG_DELETE";


    public static final String CHECKLIST_ANSWER_IMAGE = "checklist_answer";
    public static final String EQUIPMENT_TYPES_IMAGE = "equipment_types";
    public static final String EQUIPMENTS_IMAGE = "equipments";


    public static final String REPAIR_LOG_EQUIPMENTS_IMAGE = "repair_log";

    public static final String EQUIPMENTS_TYPE_TO_USER_IMAGE = "equipments_types_to_user";

    public static final String REPAIR_LOG_IMAGE = "repair_log";
    public static final String USERS_IMAGE = "users";

    private ArrayList<String> listPrivilege;

    public ArrayList<String> getListPrivilege() {
        return listPrivilege;
    }

    public void setPrivilige(ArrayList<String> listPrivilege) {
        this.listPrivilege = listPrivilege;
    }


    private static final String SHARED_PREF_NAME = "app_shared_pref";

    public static boolean isNetworkAvailable(Activity activity) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static Map<String, String> getHTTPDefaultHeaders() {
        Map<String, String> defaultHeaderMap = new HashMap();
        defaultHeaderMap.put("x-hp50-debug", "h4uyp$*50)r%");


       /* defaultHeaderMap.put("x-hp50-debug", "h4uyp$*50)r%");
        defaultHeaderMap.put("x-hp50-debug", "h4uyp$*50)r%");
        defaultHeaderMap.put("x-hp50-debug", "h4uyp$*50)r%");
*/


     /*   defaultHeaderMap.put("Authorization", "Basic Q0RMQWRtaW46RzA0d2F5");
        defaultHeaderMap.put("Content-Type", "application/x-www-form-urlencoded");
        defaultHeaderMap.put("Accept", "application/json");*/

        return defaultHeaderMap;
    }

    public static void handleException(Exception ex, boolean shouldReport, String tag, String msg) {
        Log.e(tag, msg, ex);
        if (shouldReport) {
        }
    }

    public static String getStringFromSharedPreferances(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static Set<String> getSetFromSharedPreferances(Context context, String key, Set<String> defaultValue) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getStringSet(key, defaultValue);
    }

    public static String getDeviceSerialNumber() {
        String str = Build.SERIAL;
        if ((str == null) || ("".equals(str))) {
        }
        try {
            Class localClass = Class.forName("android.os.SystemProperties");
            str = (String) localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localClass, "ro.serialno", "unknown");
            return str;
        } catch (Exception localException) {
            Crashlytics.logException(localException);

            handleException(localException, true, Utilities.class.getSimpleName(), "Error getting device serial number ");
        }
        return str;
    }

    /**
     * @param context Context Object
     * @param key     Key for the String Value
     * @param strVal  String value to be put in Shared Preference
     */
    public static void setStringInSharedPreferances(Context context, String key, String strVal) {

        SharedPreferences.Editor localEditor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();

        localEditor.putString(key, strVal);
        if (Build.VERSION.SDK_INT > 8) {
            localEditor.apply();
            return;
        }
        localEditor.commit();

    }

    /**
     * @param context  Context Object
     * @param key      Key for the String Value
     * @param setValue set  value to be put in Shared Preference
     */
    public static void setStringArrayInSharedPreferances(Context context, String key, Set<String> setValue) {

        SharedPreferences.Editor localEditor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();

        localEditor.putStringSet(key, setValue);

        if (Build.VERSION.SDK_INT > 8) {
            localEditor.apply();
            return;
        }

        localEditor.commit();

    }

    /**
     * Removes object from shared preferences
     *
     * @param context Context
     * @param key     key to identify object in Shared Preference
     */
    public static void removeObjectFromSharedPreferances(Context context, String key) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        localEditor.remove(key);
        localEditor.commit();
    }


    public static Date currentDateTime() {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        Date date = StringDateToDate(utcTime);
        return date;

    }


    public static Date StringDateToDate(String utcTime) {

        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            dateToReturn = (Date) dateFormat.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);

        }

        return dateToReturn;

    }


    public static Bitmap rotateImageIfRequired(Bitmap img, String path) throws IOException {

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

    public static Bitmap rotateImage(Bitmap img, int degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;

    }

    public static String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedString = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedString;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {

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

        Bitmap bitmap = Bitmap.createScaledBitmap(image, width, height, true);
        image.recycle();
        return bitmap;

    }

    public static String capitalizeString(String name) {

        String updatename = "";

        if (name != null && !name.trim().equalsIgnoreCase("")) {
            String tokens[] = name.trim().split(" ");
            for (int i = 0; i < name.trim().split(" ").length; i++)
                updatename = updatename + " " + tokens[i].substring(0, 1).toUpperCase() + tokens[i].substring(1).toLowerCase();
        }

        return updatename;

    }

    public static ProgressDialog startProgressDialog(Activity activity) {

        ProgressDialog progressDialog = new ProgressDialog(activity);
        //  ProgressDialog progressDialog = new ProgressDialog(activity, R.style.MyDialogTheme);
        progressDialog.setMessage("Please wait...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        progressDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;

        // progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        progressDialog.show();
        progressDialog.setCancelable(false);

        return progressDialog;

    }


    public static void showSnackbar(View coordinateLocation, String string) {

        Snackbar.make(coordinateLocation, string, 5000)
                .show();

    }


    public static void showSnackbarView(View view, String string) {

        Snackbar.make(view, string, 5000)
                .show();

    }

    public static void showToast(Context activity, String string) {

        Toast.makeText(activity, "" + string, Toast.LENGTH_LONG).show();

    }

}

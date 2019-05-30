package com.cdlit.assetmaintenanceapp.Utils;

public class Global {

    public static final int JSON_RESPONSE = 111;
    public static final int STRING_RESPONSE = 222;
    public static final int HTTP_GET = 333;
    public static final int HTTP_POST = 444;
    public static final int HTTP_DELETE = 555;
    public static final int HTTP_PUT = 666;
    public static final String KEY_PRIVILEGE_ID = "SET_PRIVILEGE";

    public static int SUCCESS_CODE = 200;
    public static int FAILURE_CODE = 404;
    public static int FAILURE_SERVER = 531;
    public static int LOCATION_NOT_FOUND_ERROR = 532;
    public static int DATE_INCORRECT = 533;
    public static int ACTION_PENDING = 533;
    public static int NO_INTERNET = 1000;
    public static final int NO_PRODUCT_CODE = 630;

    public static final String ARGS_URI = "ARGS_URI";
    public static final String ARGS_USER_NAME = "ARGS_USER_NAME";
    public static final String ARGS_JSON = "JSON_STRING";
    public static final String ARGS_ORDER_ID = "ARGS_ORDER_ID";
    public static final String ARGS_OLD_PASSWORD = "ARGS_OLD_PASSWORD";
    public static final String ARGS_PASSWORD = "ARGS_PASSWORD";
    public static final String ARGS_NEW_PASSWORD = "ARGS_NEW_PASSWORD";

    public static final String ARGS_DRIVER = "ARGS_DRIVER";

    public static final String API_BASE_URL_KEY = "api_base_url";

    public static String TEST_BASE_URL = "http://35.197.246.132:8084/api/"; // Test Server

    //  public static String TEST_BASE_URL = "http://192.168.1.2:8080/api/"; //rakesh

    //  public static String TEST_BASE_URL = "http://192.168.1.23:8080/api/"; // Kush

    //  public static String TEST_BASE_URL = "http://192.168.1.15:8080/api/"; // Zinal
    //  public static String TEST_BASE_URL = "http://192.168.1.4:8080/api/"; // devang
    //  public static String TEST_BASE_URL = "http://192.168.1.171:8080/api/"; //Bhargav
    //  public static String TEST_BASE_URL = "http://192.168.1.16:8080/api/"; //Kalpesh

    // public static String TEST_BASE_URL = "http://203.88.158.186:8080/api/"; //

    public static String connectedServer = "Woolwich Server Connected";
    public static String STOCK_ACCESS = "stock_access";
    public static final String SENDER_ID = "782889423059";

    //Shared Preferences Key

    public static final String KEY_REGID = "regId";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_FIRST_LAST_NAME = "user_first_last_name";
    public static final String KEY_USER_TYPE = "user_type";
    //   public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_IMAGE_PATH = "user_image_path";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_LOCATION_ID = "user_location_id";
    public static final String KEY_USER_LOCATION_NAME = "user_location_name";

    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_GCM_REG_ID = "gcm_registration_id";
    public static final String KEY_APP_VERSION = "appVersion";

    public static final String KEY_DRIVER = "driver";
    public static final String USER_TYPE_ADMIN = "admin";

    /* for pop up message */
    public static final String SUCCESS_TITLE = "Success !";
    public static final String FAILURE_TITLE = "Failure !";

    public static final String EXIT_TITLE = "Want to Exit";
    public static final String EXIT_MESSAGE = "Do you sure want to exit ?";

    public static final String CONNECTION_FAILED_TITLE = "Internet is not available";
    public static final String CONNECTION_FAILED_MESSAGE = "Please check your Internet connection";
    public static final String SUCCESS_PASSWORD_MESSAGE = "Password changed successfully";

    public static final String AUTH_FAILED_TITLE = "Authentication failed !";
    public static final String AUTH_FAILED_MESSAGE = "User id or password is incorrect";
    public static final String AUTH_FAILED_MESSAGE_OLD_PASSWORD = "Current password is incorrect";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    //  public static final String SHARED_PREF = "ah_firebase";

}

package com.cdlit.assetmaintenanceapp.Utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.cdlit.assetmaintenanceapp.Model.RestResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by hemraj on 30-12-2014.
 */
public class RestLoader<T extends RestResponse> extends AsyncTaskLoader<T> {

    private static final String TAG = RestLoader.class.getSimpleName();
    private Context context;
    private HttpWrapper httpWrapper;
    private boolean result = true;
    private String message;
    private Class<T> classType;
    private T mRestResponse;
    private long mLastLoad;
    private static final long STALE_DELTA = 300000; // 5mins

    public RestLoader(Context context, HttpWrapper httpWrapper, Class<T> classType) {
        super(context);
        this.context = context;
        this.httpWrapper = httpWrapper;
        this.classType = classType;
    }

    @Override
    public T loadInBackground() {

        WSRestClient wsRestClient = new WSRestClient();

        try {
            String response = wsRestClient.getResponse(httpWrapper);
            Log.i(TAG, "Response : " + response);
            if (httpWrapper.getHttpResponseType() == Global.STRING_RESPONSE) {
                return classType.cast(response);
            } else {
                T gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create().fromJson(response, classType);

//                T gson = new Gson().fromJson(response, classType);
                return gson;
            }
        } catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException) {
            this.result = false;
            Utilities.handleException(localSSLPeerUnverifiedException, false, TAG, localSSLPeerUnverifiedException.getMessage());
            Crashlytics.logException(localSSLPeerUnverifiedException);
            return null;
        } catch (SSLException localSSLException) {
            this.result = false;
            Utilities.handleException(localSSLException, false, TAG, localSSLException.getMessage());
            Crashlytics.logException(localSSLException);
            return null;
        } catch (HttpHostConnectException localHttpHostConnectException) {
            this.result = false;
            Utilities.handleException(localHttpHostConnectException, false, TAG, localHttpHostConnectException.getMessage());
            Crashlytics.logException(localHttpHostConnectException);

            return null;
        } catch (UnknownHostException localUnknownHostException) {
            this.result = false;
            Utilities.handleException(localUnknownHostException, false, TAG, localUnknownHostException.getMessage());
            Crashlytics.logException(localUnknownHostException);

            return null;
        } catch (JsonSyntaxException localJsonSyntaxException) {
            this.result = false;
            Utilities.handleException(localJsonSyntaxException, false, TAG, localJsonSyntaxException.getMessage());
            Crashlytics.logException(localJsonSyntaxException);

            return null;
        } catch (SocketException localSocketException) {
            this.result = false;
            Utilities.handleException(localSocketException, false, TAG, localSocketException.getMessage());

            Crashlytics.logException(localSocketException);

            return null;
        } catch (ConnectTimeoutException localConnectTimeoutException) {
            this.result = false;
            Utilities.handleException(localConnectTimeoutException, false, TAG, localConnectTimeoutException.getMessage());

            Crashlytics.logException(localConnectTimeoutException);

            return null;
        } catch (ParseException localParseException) {
            this.result = false;
            this.message = "Error occurred while parsing response.";
            Utilities.handleException(localParseException, true, TAG, this.message);
            Crashlytics.logException(localParseException);

            return null;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            this.result = false;
            this.message = "Error occurred while getting response.";
            Utilities.handleException(localIllegalArgumentException, false, TAG, this.message);
            Crashlytics.logException(localIllegalArgumentException);

            return null;
        } catch (Exception localException) {
            this.result = false;
            Utilities.handleException(localException, true, TAG, this.message);
            Crashlytics.logException(localException);

        }
       /* catch (SocketTimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        return null;
    }

    @Override
    public void deliverResult(T data) {
        mRestResponse = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mRestResponse != null) {
            // We have a cached result, so we can just
            // return right away.
            super.deliverResult(mRestResponse);
        }

        // If our response is null or we have hung onto it for a long time,
        // then we perform a force load.
        if (mRestResponse == null || System.currentTimeMillis() - mLastLoad >= STALE_DELTA)
            forceLoad();
        mLastLoad = System.currentTimeMillis();
    }

    @Override
    protected void onStopLoading() {
        // This prevents the AsyncTask backing this
        // loader from completing if it is currently running.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Stop the Loader if it is currently running.
        onStopLoading();

        // Get rid of our cache if it exists.
        mRestResponse = null;

        // Reset our stale timer.
        mLastLoad = 0;
    }

}

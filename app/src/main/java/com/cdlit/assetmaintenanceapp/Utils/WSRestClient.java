package com.cdlit.assetmaintenanceapp.Utils;

import android.util.Log;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hemraj on 29-12-2014.
 */
public class WSRestClient<T> {

    private static final String TAG = WSRestClient.class.getSimpleName();

    public String getResponse(HttpWrapper paramHTTPRequestWrapper) throws Exception {

        String url = paramHTTPRequestWrapper.getUrl();
        Log.i(TAG, "Url : " + url);
        int reqType = paramHTTPRequestWrapper.getHttpRequestType();
        String responseStr = null;
        switch (reqType) {

            case Global.HTTP_GET:
                responseStr = executeGet(paramHTTPRequestWrapper);
                break;
            case Global.HTTP_POST:
                responseStr = executePost(paramHTTPRequestWrapper);
                break;
            case Global.HTTP_DELETE:
                responseStr = executeDelete(paramHTTPRequestWrapper);
                break;
            case Global.HTTP_PUT:
                responseStr = executePut(paramHTTPRequestWrapper);
                break;

        }

        return responseStr;
    }


    private String executePut(HttpWrapper httpWrapper) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(httpWrapper.getUrl());

        if (null != httpWrapper.getJsonEntity()) {
            StringEntity data = new StringEntity(httpWrapper.getJsonEntity(), "UTF-8");
            if (null != httpWrapper.getContentType()) {
                data.setContentType(httpWrapper.getContentType());
            }
            httpPut.setEntity(data);
        } else {
            ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            for (String paramKey : httpWrapper.getParamsMap().keySet()) {
                params.add(new BasicNameValuePair(paramKey, httpWrapper.getParamsMap().get(paramKey)));
            }
            httpPut.setEntity(new UrlEncodedFormEntity(params));
            for (String headerKey : httpWrapper.getHeadersMap().keySet()) {
                httpPut.addHeader(headerKey, httpWrapper.getHeadersMap().get(headerKey));
            }
        }
        //httpPost.getParams().setParameter("User-Agent", System.getProperty("http.agent"));
        return EntityUtils.toString(httpClient.execute(httpPut).getEntity());

    }


    private String executePost(HttpWrapper httpWrapper) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(httpWrapper.getUrl());

        if (null != httpWrapper.getJsonEntity()) {

            StringEntity data = new StringEntity(httpWrapper.getJsonEntity(), "UTF-8");
            if (null != httpWrapper.getContentType()) {
                data.setContentType(httpWrapper.getContentType());
            }
            httpPost.setEntity(data);

        } else {

            ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            for (String paramKey : httpWrapper.getParamsMap().keySet()) {
                params.add(new BasicNameValuePair(paramKey, httpWrapper.getParamsMap().get(paramKey)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            for (String headerKey : httpWrapper.getHeadersMap().keySet()) {
                httpPost.addHeader(headerKey, httpWrapper.getHeadersMap().get(headerKey));
            }

        }

        //httpPost.getParams().setParameter("User-Agent", System.getProperty("http.agent"));
        String rakesh = EntityUtils.toString(httpClient.execute(httpPost).getEntity());

        return rakesh;

    }

    private String executeGet(HttpWrapper httpWrapper) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        BasicHttpContext httpContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(httpWrapper.getUrl());

        for (String headerKey : httpWrapper.getHeadersMap().keySet()) {
            httpGet.addHeader(headerKey, httpWrapper.getHeadersMap().get(headerKey));
        }

        return EntityUtils.toString(httpClient.execute(httpGet, httpContext).getEntity());

    }

    private String executeDelete(HttpWrapper httpWrapper) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(httpWrapper.getUrl());
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        for (String headerKey : httpWrapper.getHeadersMap().keySet()) {
            httpDelete.addHeader(headerKey, httpWrapper.getHeadersMap().get(headerKey));
        }
        for (String paramKey : httpWrapper.getParamsMap().keySet()) {
            basicHttpParams.setParameter(paramKey, httpWrapper.getParamsMap().get(paramKey));
        }
        httpDelete.setParams(basicHttpParams);
        return EntityUtils.toString(httpClient.execute(httpDelete).getEntity());
    }
}

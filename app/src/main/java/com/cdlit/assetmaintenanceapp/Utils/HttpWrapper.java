package com.cdlit.assetmaintenanceapp.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hemraj on 29-12-2014.
 */
public class HttpWrapper {

    private Map<String, String> headersMap = new HashMap();
    private int httpRequestType;
    private int httpResponseType;
    private Map<String, String> paramsMap;
    private String url;

    private String jsonEntity = null;
    private String contentType = null;
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    public HttpWrapper() {
        this.headersMap.putAll(Utilities.getHTTPDefaultHeaders());
        this.paramsMap = new HashMap();
    }

    public HttpWrapper(int httpRequestType, int httpResponseType, String contentType, String url, String jsonEntity) {
        this(httpRequestType, httpResponseType, url);
        this.jsonEntity = jsonEntity;
        this.contentType = contentType;
    }

    /**
     * @param httpRequestType  Http Request Type
     * @param httpResponseType Expected Response Type (Json/String/Xml)
     * @param url              URL to be calledfor data
     */
    public HttpWrapper(int httpRequestType, int httpResponseType, String url) {
        this();
        this.httpRequestType = httpRequestType;
        this.httpResponseType = httpResponseType;
        //if ((paramString != null) && (paramString.indexOf("http:") != -1)) {
        //   url = url.replace("http:", "https:");
        //}
        this.url = url;
    }

    public void addToHeadersMap(String headerKey, String headerValue) {
        this.headersMap.put(headerKey, headerValue);
    }

    public void addToParamsMap(String paramKey, String paramValue) {
        this.paramsMap.put(paramKey, paramValue);
    }

    public Map<String, String> getHeadersMap() {
        return this.headersMap;
    }

    public int getHttpRequestType() {
        return this.httpRequestType;
    }

    public int getHttpResponseType() {
        return this.httpResponseType;
    }

    public Map<String, String> getParamsMap() {
        return this.paramsMap;
    }

    public String getUrl() {
        return this.url;
    }

    public void setHeadersMap(Map<String, String> headerMap) {
        if (this.headersMap == null) {
            this.headersMap = headerMap;
            this.headersMap.putAll(Utilities.getHTTPDefaultHeaders());
            return;
        }
        this.headersMap.putAll(headerMap);
    }

    public void setHttpRequestType(int httpRequestType) {
        this.httpRequestType = httpRequestType;
    }

    public void setHttpResponseType(int httpResponseType) {
        this.httpResponseType = httpResponseType;
    }

    public void setParamsMap(Map<String, String> paramMap) {
        if (this.paramsMap == null) {
            this.paramsMap = paramMap;
            return;
        }
        this.paramsMap.putAll(paramMap);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsonEntity() {
        return jsonEntity;
    }

    public void setJsonEntity(String jsonEntity) {
        this.jsonEntity = jsonEntity;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}

package com.cdlit.assetmaintenanceapp.Model;

import java.util.List;

/**
 * Created by rakesh on 16-06-2017.
 */

public class Category extends RestResponse {

    private List<CategoryResponse> response;

    public List<CategoryResponse> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryResponse> response) {
        this.response = response;
    }

}

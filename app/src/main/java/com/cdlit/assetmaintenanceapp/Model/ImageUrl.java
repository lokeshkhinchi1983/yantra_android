package com.cdlit.assetmaintenanceapp.Model;

/**
 * Created by rakesh on 18-12-2017.
 */

public class ImageUrl extends RestResponse {

    private ImageUrlResponse response;

    public ImageUrlResponse getResponse() {
        return response;
    }

    public void setResponse(ImageUrlResponse response) {
        this.response = response;
    }

    public class ImageUrlResponse {

        public String bitmap;

        public String path;

        public String getBitmap() {
            return bitmap;
        }

        public void setBitmap(String bitmap) {
            this.bitmap = bitmap;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }


    }
}

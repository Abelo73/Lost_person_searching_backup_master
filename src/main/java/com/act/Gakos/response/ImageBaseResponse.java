package com.act.Gakos.response;

import com.act.Gakos.request.UploadedFileMetadata;

import java.util.List;

public class ImageBaseResponse {

    private String message;
    private boolean success;
    private List<UploadedFileMetadata> data;

    // Updated constructor to properly initialize fields
    public ImageBaseResponse(String message, boolean success, List<UploadedFileMetadata> data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Getter and setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter and setter for data
    public List<UploadedFileMetadata> getData() {
        return data;
    }

    public void setData(List<UploadedFileMetadata> data) {
        this.data = data;
    }
}

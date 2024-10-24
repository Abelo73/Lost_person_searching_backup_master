package com.act.Gakos.request;

public class UploadedFileMetadata {

    private String fileName;
    private long fileSize;
    private String contentType;
    private String filePath;
    private String base64Image;
    private String descriptions;


    public UploadedFileMetadata(String originalFilename, long size, String contentType, String descriptions) {
        this.fileName = originalFilename;
        this.fileSize = size;
        this.descriptions = descriptions;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}

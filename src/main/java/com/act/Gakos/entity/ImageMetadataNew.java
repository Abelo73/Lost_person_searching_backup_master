package com.act.Gakos.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "image_metadata")
public class ImageMetadataNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fileName;
    private long fileSize;
    private String contentType;
    private String filePath;
    private String base64Image;
    private String descriptions;
    private LocalDateTime uploadDate;


    public ImageMetadataNew() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public ImageMetadataNew(Long id, String fileName, long fileSize, String contentType, String filePath, String base64Image, String descriptions) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.filePath = filePath;
        this.base64Image = base64Image;
        this.descriptions = descriptions;
    }

    public void setUserId(Integer userId) {
        this.id = Long.valueOf(userId);
    }




    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "ImageMetadataNew{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", contentType='" + contentType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", base64Image='" + base64Image + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}

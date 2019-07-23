
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("file_id")
    @Expose
    private String fileId;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

}

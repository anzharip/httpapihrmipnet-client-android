
package com.anzhari.hrmipnetmobile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAttachments {

    @SerializedName("data")
    @Expose
    private List<Attachment> attachment = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
        this.attachment = attachment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

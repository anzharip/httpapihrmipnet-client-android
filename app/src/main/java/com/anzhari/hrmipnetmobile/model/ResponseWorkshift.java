
package com.anzhari.hrmipnetmobile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWorkshift {

    @SerializedName("data")
    @Expose
    private List<Workshift> workshift = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Workshift> getWorkshift() {
        return workshift;
    }

    public void setWorkshift(List<Workshift> workshift) {
        this.workshift = workshift;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}


package com.anzhari.hrmipnetmobile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseReligion {

    @SerializedName("data")
    @Expose
    private List<Religion> religion = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Religion> getReligion() {
        return religion;
    }

    public void setReligion(List<Religion> religion) {
        this.religion = religion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

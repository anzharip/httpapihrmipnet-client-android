
package com.anzhari.hrmipnetmobile.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseNationality {

    @SerializedName("data")
    @Expose
    private List<Nationality> nationality = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Nationality> getNationality() {
        return nationality;
    }

    public void setNationality(List<Nationality> nationality) {
        this.nationality = nationality;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

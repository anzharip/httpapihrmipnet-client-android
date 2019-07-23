
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePersonalDetail {

    @SerializedName("data")
    @Expose
    private PersonalDetail personalDetail;
    @SerializedName("message")
    @Expose
    private String message;

    public PersonalDetail getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetail personalDetail) {
        this.personalDetail = personalDetail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

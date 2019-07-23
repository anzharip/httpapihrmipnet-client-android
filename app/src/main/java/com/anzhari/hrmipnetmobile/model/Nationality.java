
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nationality {

    @SerializedName("nation_code")
    @Expose
    private String nationCode;
    @SerializedName("nation_name")
    @Expose
    private String nationName;

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    @Override
    public String toString() {
        return nationName;
    }
}

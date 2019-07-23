
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Religion {

    @SerializedName("religion_code")
    @Expose
    private String religionCode;
    @SerializedName("religion_name")
    @Expose
    private String religionName;

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    @Override
    public String toString() {
        return religionName;
    }
}

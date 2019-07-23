
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Workshift {

    @SerializedName("workshift_code")
    @Expose
    private String workshiftCode;
    @SerializedName("workshift_name")
    @Expose
    private String workshiftName;

    public String getWorkshiftCode() {
        return workshiftCode;
    }

    public void setWorkshiftCode(String workshiftCode) {
        this.workshiftCode = workshiftCode;
    }

    public String getWorkshiftName() {
        return workshiftName;
    }

    public void setWorkshiftName(String workshiftName) {
        this.workshiftName = workshiftName;
    }

    @Override
    public String toString() {
        return workshiftName;
    }
}

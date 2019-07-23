
package com.anzhari.hrmipnetmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalDetail {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("no_ktp")
    @Expose
    private String noKtp;
    @SerializedName("drivers_license_number")
    @Expose
    private String driversLicenseNumber;
    @SerializedName("license_expiry_date")
    @Expose
    private String licenseExpiryDate;
    @SerializedName("no_bpjs_kesehatan")
    @Expose
    private String noBpjsKesehatan;
    @SerializedName("no_npwp")
    @Expose
    private String noNpwp;
    @SerializedName("no_bpjs_ketenagakerjaan")
    @Expose
    private String noBpjsKetenagakerjaan;
    @SerializedName("work_shift")
    @Expose
    private String workShift;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("place_of_birth")
    @Expose
    private String placeOfBirth;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getDriversLicenseNumber() {
        return driversLicenseNumber;
    }

    public void setDriversLicenseNumber(String driversLicenseNumber) {
        this.driversLicenseNumber = driversLicenseNumber;
    }

    public String getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(String licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getNoBpjsKesehatan() {
        return noBpjsKesehatan;
    }

    public void setNoBpjsKesehatan(String noBpjsKesehatan) {
        this.noBpjsKesehatan = noBpjsKesehatan;
    }

    public String getNoNpwp() {
        return noNpwp;
    }

    public void setNoNpwp(String noNpwp) {
        this.noNpwp = noNpwp;
    }

    public String getNoBpjsKetenagakerjaan() {
        return noBpjsKetenagakerjaan;
    }

    public void setNoBpjsKetenagakerjaan(String noBpjsKetenagakerjaan) {
        this.noBpjsKetenagakerjaan = noBpjsKetenagakerjaan;
    }

    public String getWorkShift() {
        return workShift;
    }

    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

}

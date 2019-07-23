package com.anzhari.hrmipnetmobile.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.activity.LoginActivity;
import com.anzhari.hrmipnetmobile.model.Nationality;
import com.anzhari.hrmipnetmobile.model.PersonalDetail;
import com.anzhari.hrmipnetmobile.model.Religion;
import com.anzhari.hrmipnetmobile.model.ResponseBase;
import com.anzhari.hrmipnetmobile.model.ResponseNationality;
import com.anzhari.hrmipnetmobile.model.ResponsePersonalDetail;
import com.anzhari.hrmipnetmobile.model.ResponseReligion;
import com.anzhari.hrmipnetmobile.model.ResponseWorkshift;
import com.anzhari.hrmipnetmobile.model.Workshift;
import com.anzhari.hrmipnetmobile.network.ApiClient;
import com.anzhari.hrmipnetmobile.network.ApiServices;
import com.anzhari.hrmipnetmobile.util.Helper;
import com.anzhari.hrmipnetmobile.util.UserSessionManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailFragment extends Fragment {


    @BindView(R.id.txt_first_name)
    EditText txtFirstName;

    @BindView(R.id.txt_middle_name)
    EditText txtMiddleName;

    @BindView(R.id.txt_last_name)
    EditText txtLastName;

    @BindView(R.id.txt_employee_id)
    EditText txtEmployeeId;

    @BindView(R.id.txt_no_ktp)
    EditText txtNoKtp;

    @BindView(R.id.txt_driver_license)
    EditText txtDriverLicense;

    @BindView(R.id.txt_license_expiry)
    EditText txtLicenseExpiry;

    @BindView(R.id.txt_bpjs_kesehatan)
    EditText txtBpjsKesehatan;

    @BindView(R.id.txt_no_npwp)
    EditText txtNoNpwp;

    @BindView(R.id.txt_bpjs_ketenagakerjaan)
    EditText txtBpjsKetenagakerjaan;

    @BindView(R.id.txt_tgl_of_birth)
    EditText txtTglOfBirth;

    @BindView(R.id.txt_place_of_birth)
    EditText txtPlaceOfBirth;

    @BindView(R.id.sp_work_shift)
    Spinner spWorkShift;

    @BindView(R.id.sp_gender)
    Spinner spGender;

    @BindView(R.id.sp_marital_status)
    Spinner spMaritalStatus;

    @BindView(R.id.sp_nationality)
    Spinner spNationality;

    @BindView(R.id.sp_religion)
    Spinner spReligion;

    @BindView(R.id.btn_edit)
    Button btnEdit;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private UserSessionManager sessionManager;
    private ApiServices apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressDialog dialog;
    private Unbinder unbinder;
    private List<View> views = new ArrayList<>();


    public PersonalDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new UserSessionManager(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        initComponent();
        viewLoading();
        getPersonalDetail();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initComponent(){
        ArrayAdapter<String> workShift = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, Collections.singletonList("Loading..."));
        workShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkShift.setAdapter(workShift);

        ArrayAdapter<String> gender = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, getResources().getStringArray(R.array.gender));
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(gender);

        ArrayAdapter<String> maritalStatus = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, getResources().getStringArray(R.array.marital_status));
        maritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaritalStatus.setAdapter(maritalStatus);

        ArrayAdapter<String> nationality = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, Collections.singletonList("Loading..."));
        nationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNationality.setAdapter(nationality);

        ArrayAdapter<String> religion = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, Collections.singletonList("Loading..."));
        religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReligion.setAdapter(religion);

    }

    private void viewLoading(){
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
    }

    private void nextAction(){
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void loadComponent(PersonalDetail personalDetail){
        views.add(txtFirstName);
        views.add(txtMiddleName);
        views.add(txtLastName);
        views.add(txtNoKtp);
        views.add(txtLicenseExpiry);
        views.add(txtBpjsKesehatan);
        views.add(txtNoNpwp);
        views.add(txtBpjsKetenagakerjaan);
        views.add(txtPlaceOfBirth);
        views.add(spWorkShift);
        views.add(spGender);
        views.add(spMaritalStatus);
        views.add(spNationality);
        views.add(spReligion);

        for (int i = 0; i < views.size(); i++) {
            views.get(i).setClickable(false);
            views.get(i).setEnabled(false);
        }

        txtFirstName.setText(personalDetail.getFirstName());
        txtMiddleName.setText(personalDetail.getMiddleName());
        txtLastName.setText(personalDetail.getLastName());
        txtEmployeeId.setText(personalDetail.getEmployeeId());
        txtNoKtp.setText(personalDetail.getNoKtp());
        txtDriverLicense.setText(personalDetail.getDriversLicenseNumber());
        txtLicenseExpiry.setText(personalDetail.getLicenseExpiryDate());
        txtBpjsKesehatan.setText(personalDetail.getNoBpjsKesehatan());
        txtNoNpwp.setText(personalDetail.getNoNpwp());
        txtBpjsKetenagakerjaan.setText(personalDetail.getNoBpjsKetenagakerjaan());
        txtTglOfBirth.setText(personalDetail.getDateOfBirth());
        txtPlaceOfBirth.setText(personalDetail.getPlaceOfBirth());

        spGender.setSelection((Integer.valueOf(personalDetail.getGender()) - 1));
        spMaritalStatus.setSelection(getIndex(spMaritalStatus, personalDetail.getMaritalStatus()));

        btnEdit.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);

        btnEdit.setOnClickListener(v -> {
            btnEdit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);

            for (int i = 0; i < views.size(); i++) {
                views.get(i).setClickable(true);
                views.get(i).setEnabled(true);
            }
        });

        btnCancel.setOnClickListener(v -> getPersonalDetail());
        btnSubmit.setOnClickListener(v -> actionSubmit());
    }

    private void getPersonalDetail(){
        dialog.show();
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getPersonalDetail(headers)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePersonalDetail>() {
                            @Override
                            public void onSuccess(ResponsePersonalDetail responsePersonalDetail) {
                                if (responsePersonalDetail != null){
                                    if (responsePersonalDetail.getPersonalDetail() != null){
                                        PersonalDetail personalDetail = responsePersonalDetail.getPersonalDetail();
                                        loadComponent(personalDetail);
                                        getWorkshift(personalDetail.getWorkShift());
                                        getNationality(personalDetail.getNationality());
                                        getReligion(personalDetail.getReligion());
                                    }
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();

                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private void getNationality(String dnationality){
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getNationality(headers)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseNationality>() {
                            @Override
                            public void onSuccess(ResponseNationality responseNationality) {
                                if (responseNationality != null){
                                    if (responseNationality.getNationality() != null
                                            && responseNationality.getNationality().size() > 0){
                                        ArrayAdapter<Nationality> nationality = new ArrayAdapter<>(getActivity(),
                                                R.layout.spinner_item, responseNationality.getNationality());
                                        nationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spNationality.setAdapter(nationality);
                                        spNationality.setSelection(getNationalityId(spNationality, dnationality));
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();

                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private void getWorkshift(String dworkshift){
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getWorkshift(headers)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseWorkshift>() {
                            @Override
                            public void onSuccess(ResponseWorkshift responseWorkshift) {
                                if (responseWorkshift != null){
                                    if (responseWorkshift.getWorkshift() != null
                                            && responseWorkshift.getWorkshift().size() > 0){
                                        ArrayAdapter<Workshift> workShift = new ArrayAdapter<>(getActivity(),
                                                R.layout.spinner_item, responseWorkshift.getWorkshift());
                                        workShift.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spWorkShift.setAdapter(workShift);
                                        spWorkShift.setSelection(getWorkshiftId(spWorkShift, dworkshift));
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();

                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private void getReligion(String dreligion){
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getReligion(headers)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseReligion>() {
                            @Override
                            public void onSuccess(ResponseReligion responseReligion) {
                                if (responseReligion != null){
                                    if (responseReligion.getReligion() != null
                                            && responseReligion.getReligion().size() > 0){
                                        ArrayAdapter<Religion> religion = new ArrayAdapter<>(getActivity(),
                                                R.layout.spinner_item, responseReligion.getReligion());
                                        religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spReligion.setAdapter(religion);
                                        spReligion.setSelection(getReligionId(spReligion, dreligion));
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();

                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private void actionSubmit(){
        dialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name", txtFirstName.getText().toString());
        jsonObject.addProperty("middle_name",txtMiddleName.getText().toString());
        jsonObject.addProperty("last_name", txtLastName.getText().toString());
        jsonObject.addProperty("no_ktp", txtNoKtp.getText().toString());
        jsonObject.addProperty("license_expiry_date", txtLicenseExpiry.getText().toString());
        jsonObject.addProperty("no_bpjs_kesehatan", txtBpjsKesehatan.getText().toString());
        jsonObject.addProperty("no_npwp", txtNoNpwp.getText().toString());
        jsonObject.addProperty("no_bpjs_ketenagakerjaan", txtBpjsKetenagakerjaan.getText().toString());
        jsonObject.addProperty("place_of_birth", txtPlaceOfBirth.getText().toString());

        jsonObject.addProperty("work_shift", ((Workshift)spWorkShift.getSelectedItem()).getWorkshiftCode());
        jsonObject.addProperty("nationality", ((Nationality)spNationality.getSelectedItem()).getNationCode());
        jsonObject.addProperty("religion", ((Religion)spReligion.getSelectedItem()).getReligionCode());
        jsonObject.addProperty("marital_status", spMaritalStatus.getSelectedItem().toString());
        jsonObject.addProperty("gender", (spGender.getSelectedItem().toString().equalsIgnoreCase("Male")) ? "1" : "2");

        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.updatePersonalDetail(headers, jsonObject)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBase>() {
                            @Override
                            public void onSuccess(ResponseBase responseBase) {
                                if (responseBase != null){
                                    if (responseBase.getMessage() != null
                                            && !responseBase.getMessage().isEmpty()){
                                        getPersonalDetail();
                                        Toast.makeText(getActivity(), responseBase.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private int getWorkshiftId(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0; i < spinner.getCount(); i++){
            if (((Workshift)spinner.getItemAtPosition(i)).getWorkshiftCode().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private int getNationalityId(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0; i < spinner.getCount(); i++){
            if (((Nationality)spinner.getItemAtPosition(i)).getNationCode().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private int getReligionId(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0; i < spinner.getCount(); i++){
            if (((Religion)spinner.getItemAtPosition(i)).getReligionCode().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

}

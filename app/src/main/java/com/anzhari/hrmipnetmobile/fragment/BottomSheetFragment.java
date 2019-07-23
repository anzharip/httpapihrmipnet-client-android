package com.anzhari.hrmipnetmobile.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.activity.LoginActivity;
import com.anzhari.hrmipnetmobile.model.ResponseBase;
import com.anzhari.hrmipnetmobile.network.ApiClient;
import com.anzhari.hrmipnetmobile.network.ApiServices;
import com.anzhari.hrmipnetmobile.util.Helper;
import com.anzhari.hrmipnetmobile.util.KeyboardUtils;
import com.anzhari.hrmipnetmobile.util.UserSessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.uchitel.eventex.UIEvent;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;

    @BindView(R.id.txt_select_file)
    EditText txtSelectFile;

    @BindView(R.id.txt_comment)
    EditText txtComment;

    @BindView(R.id.txt_current_file)
    EditText txtCurrentFile;

    @BindView(R.id.txt_tgl_added)
    EditText txtTglAdded;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private Unbinder unbinder;
    private ApiServices apiService;
    private UserSessionManager sessionManager;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressDialog dialog;
    private View view;
    private FragmentTransaction fragmentTransaction;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    public static BottomSheetFragment newInstance(String param1, String param2, String param3, String param4) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }

        sessionManager = new UserSessionManager(getActivity());
        apiService = ApiClient.getClient(getActivity()).create(ApiServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);

        viewLoading();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mParam1.isEmpty()){
            txtTitle.setText("Upload Attachment");
            txtSelectFile.setVisibility(View.VISIBLE);
            txtCurrentFile.setVisibility(View.GONE);
            txtTglAdded.setVisibility(View.GONE);
            btnSubmit.setText("Upload");
        }else {
            txtTitle.setText("Edit Attachment");
            txtSelectFile.setVisibility(View.GONE);
            txtCurrentFile.setVisibility(View.VISIBLE);
            txtTglAdded.setVisibility(View.VISIBLE);
            txtCurrentFile.setText(mParam2);
            txtTglAdded.setText(mParam3);

            btnSubmit.setText("Update");
        }

        txtComment.setText(mParam4);
        txtSelectFile.setOnClickListener(v -> actionPicker());
        btnCancel.setOnClickListener(v -> dismiss());
        btnSubmit.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(v);
            dialog.show();
            if (btnSubmit.getText().toString().equalsIgnoreCase("Upload")){
                uploadAttachment();
            }

            if (btnSubmit.getText().toString().equalsIgnoreCase("Update")){
                updateAttachment(mParam1);
            }
        });
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() > 0) {
                        txtSelectFile.setText(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
                    }else {
                        Toast.makeText(getActivity(), "Missing Image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).size() > 0){
                        txtSelectFile.setText(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0));
                    }else {
                        Toast.makeText(getActivity(), "Missing File.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
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

    private void actionPicker(){
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showPickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Helper.showSettingsDialog(getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showPickerOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose :");

        String[] image = {"Photo", "Document"};
        builder.setItems(image, (dialog, which) -> {
            switch (which) {
                case 0:
                    onPickPhoto();
                    break;
                case 1:
                    onPickDoc();
                    break;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onPickPhoto() {
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle("Please Select Photo")
                .enableVideoPicker(false)
                .enableCameraSupport(false)
                .showGifs(true)
                .showFolderView(true)
                .enableSelectAll(false)
                .enableImagePicker(true)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .pickPhoto(this);
    }

    private void onPickDoc() {
        String[] zips = { ".zip", ".rar" };
        FilePickerBuilder.getInstance()
//                .addFileSupport("ZIP", zips)
                .setMaxCount(1)
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle("Please Select Document")
                .enableDocSupport(true)
                .enableSelectAll(false)
                .sortDocumentsBy(SortingTypes.name)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .pickFile(this);
    }

    private void uploadAttachment(){
        File file = new File(txtSelectFile.getText().toString());
        String fileEncode = Helper.encodeFileToBase64(file);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("select_file", fileEncode);
        jsonObject.addProperty("file_name", file.getName());
        jsonObject.addProperty("comment", txtComment.getText().toString());

        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.uploadAttachment(headers, jsonObject)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBase>() {
                            @Override
                            public void onSuccess(ResponseBase responseBase) {
                                if (responseBase != null){
                                    if (responseBase.getMessage() != null
                                            && !responseBase.getMessage().isEmpty()){
                                        new UIEvent("click.button.submit").setText(responseBase.getMessage()).post(view);
                                    }
                                }
                                dialog.dismiss();
                                dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                dismiss();
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

    private void updateAttachment(String fileId){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("file_id", fileId);
        jsonObject.addProperty("comment", txtComment.getText().toString());

        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.updateAttachment(headers, jsonObject)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseBase>() {
                            @Override
                            public void onSuccess(ResponseBase responseBase) {
                                if (responseBase != null){
                                    if (responseBase.getMessage() != null
                                            && !responseBase.getMessage().isEmpty()){
                                        new UIEvent("click.button.submit").setText(responseBase.getMessage()).post(view);
                                    }
                                }
                                dialog.dismiss();
                                dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                dismiss();
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
}

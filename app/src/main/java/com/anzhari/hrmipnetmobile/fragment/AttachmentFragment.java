package com.anzhari.hrmipnetmobile.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anzhari.hrmipnetmobile.BuildConfig;
import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.activity.LoginActivity;
import com.anzhari.hrmipnetmobile.adapter.AttachmentAdapter;
import com.anzhari.hrmipnetmobile.model.Attachment;
import com.anzhari.hrmipnetmobile.model.ResponseAttachment;
import com.anzhari.hrmipnetmobile.model.ResponseAttachments;
import com.anzhari.hrmipnetmobile.model.ResponseBase;
import com.anzhari.hrmipnetmobile.network.ApiClient;
import com.anzhari.hrmipnetmobile.network.ApiServices;
import com.anzhari.hrmipnetmobile.util.Helper;
import com.anzhari.hrmipnetmobile.util.UserSessionManager;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.uchitel.eventex.UIEvent;
import dev.uchitel.eventex.UIEventListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttachmentFragment extends Fragment implements UIEventListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btn_add)
    Button btnAdd;

    private Unbinder unbinder;
    private AttachmentAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private ApiServices apiService;
    private UserSessionManager sessionManager;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressDialog dialog;

    public AttachmentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_attachment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        viewLoading();
        getListAttachment();

        btnAdd.setOnClickListener(v -> {
            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance("", "", "", "");
            bottomSheetFragment.setCancelable(false);
            bottomSheetFragment.show(getChildFragmentManager(), BottomSheetFragment.class.getSimpleName());
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    private void loadDataAttachment(List<Attachment> attachments){
        adapter = new AttachmentAdapter(getActivity(), attachments);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position, result) -> {
            if (view.getId() == R.id.btn_download){
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    dialog.show();
                                    getAttachment(result.getFileId());
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

            if (view.getId() == R.id.btn_edit){
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(result.getFileId(), result.getFileName(), result.getDateAdded(), result.getComment());
                bottomSheetFragment.setCancelable(false);
                bottomSheetFragment.show(getChildFragmentManager(), BottomSheetFragment.class.getSimpleName());
            }

            if (view.getId() == R.id.btn_delete){
                dialog.show();
                deleteAttachment(result.getFileId());
            }
        });
    }

    public void getListAttachment(){
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getAttachments(headers, "all")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseAttachments>() {
                            @Override
                            public void onSuccess(ResponseAttachments responseAttachments) {
                                if (responseAttachments != null
                                        && responseAttachments.getAttachment().size() > 0){
                                    loadDataAttachment(responseAttachments.getAttachment());
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_LONG).show();
                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
        );
    }

    private void getAttachment(String fileId){
        ArrayMap<String, String> headers = new ArrayMap<>();
        headers.put("Authorization", String.format("Bearer %s", sessionManager.getToken()));
        disposable.add(
                apiService.getAttachment(headers, fileId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseAttachment>() {
                            @Override
                            public void onSuccess(ResponseAttachment attachment) {
                                if (attachment.getAttachment() != null){
                                    String status = Helper.decodeFileFromBase64(attachment.getAttachment().getFile(), attachment.getAttachment().getFileName());
                                    if (!status.isEmpty()){
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Download "+status, Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(getActivity(), Helper.getErrorMessage(responseBody), Toast.LENGTH_LONG).show();
                                    int code = ((HttpException) e).response().code();
                                    if (code == 401){
                                        sessionManager.logoutUser();
                                        nextAction();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        })
        );
    }

    private void deleteAttachment(String fileId){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"file_id\": \""+fileId+"\"}");
        Request request = new Request.Builder()
                .url(BuildConfig.BASEURL+"myinfo/personaldetail/attachment")
                .delete(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+sessionManager.getToken())
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                dialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.isSuccessful()){
                        Gson gson = new Gson();
                        ResponseBase responseBase = gson.fromJson(response.body().string(), ResponseBase.class);
                        if (responseBase.getMessage() != null
                                && !responseBase.getMessage().isEmpty()){
                            getActivity().runOnUiThread(() -> {
                                getListAttachment();
                                Toast.makeText(getActivity(), responseBase.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        }
                        dialog.dismiss();
                    }else {
                        dialog.dismiss();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onMessage(@NonNull UIEvent uiEvent) {
        switch (uiEvent.what) {
            case "click.button.submit":
                getListAttachment();
                Toast.makeText(getActivity(), uiEvent.getText(), Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}

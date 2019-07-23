package com.anzhari.hrmipnetmobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.model.ResponseLogin;
import com.anzhari.hrmipnetmobile.network.ApiClient;
import com.anzhari.hrmipnetmobile.network.ApiServices;
import com.anzhari.hrmipnetmobile.util.Helper;
import com.anzhari.hrmipnetmobile.util.KeyboardUtils;
import com.anzhari.hrmipnetmobile.util.UserSessionManager;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txt_username)
    EditText txtUsername;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    private Unbinder unbinder;
    private UserSessionManager sessionManager;
    private ApiServices apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        sessionManager = new UserSessionManager(this);
        apiService = ApiClient.getClient(this).create(ApiServices.class);

        if (sessionManager.isUserLoggedIn()){
            nextAction();
        }

        actionButton();
        viewLoading();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.dispose();
    }

    private void viewLoading(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
    }

    private void actionButton() {
        btnLogin.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(v);
            dialog.show();
            actionLogin();
        });
    }

    private void actionLogin(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",txtUsername.getText().toString());
        jsonObject.addProperty("password",txtPassword.getText().toString());

        disposable.add(
                apiService.login(jsonObject)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseLogin>() {
                            @Override
                            public void onSuccess(ResponseLogin responseLogin) {
                                if (responseLogin != null){
                                    if (responseLogin.getAccessToken() != null
                                            && !responseLogin.getAccessToken().isEmpty()){
                                        sessionManager.setIsUserLogin(true);
                                        sessionManager.setToken(responseLogin.getAccessToken());
                                        nextAction();
                                    }
                                    Toast.makeText(LoginActivity.this, responseLogin.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dialog.dismiss();
                                if (e instanceof HttpException) {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    Toast.makeText(LoginActivity.this, Helper.getErrorMessage(responseBody), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
        );
    }

    private void nextAction(){
        startActivity(new Intent(LoginActivity.this, PersonalDetailActivity.class));
        finish();
    }
}

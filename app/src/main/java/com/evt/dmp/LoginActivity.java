package com.evt.dmp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evt.dmp.protocal.DmpWebService;
import com.evt.dmp.protocal.dto.NewAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by everitime5 on 2018-01-30.
 * author : sanch
 * 카카오톡 로그인 activity
 */

public class LoginActivity extends AppCompatActivity{

    private SessionCallback sessionCallback;
    private Button button,button1;
    private EditText editId,editPass;
    public static String id;
    private DmpWebService dmpWebService;
    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("sanch", "생성");

        try{
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
            Log.d("packageName",getPackageName());
            for(Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("keyhash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
//                Log.d("keyhash", Base64.encodeBase64URLSafeString(messageDigest.digest()));
            }
        } catch (Exception e){
            Log.d("error", "PackageInfo error is " + e.toString());
        }


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(DmpWebService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dmpWebService = retrofit.create(DmpWebService.class);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        initUi(savedInstanceState);
    }


    public void initUi(Bundle savedInstanceState){
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        editId = (EditText) findViewById(R.id.editText);
        editPass = (EditText) findViewById(R.id.editText1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAdmin newAdmin = new NewAdmin();


                id = editId.getText().toString();
                if(id.equals("")){
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                }else if(editPass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                }else{
                    newAdmin.setId(id);
                    newAdmin.setPass(editPass.getText().toString());

                    getApiLogin(newAdmin);
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAdminActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getApiLogin(final NewAdmin newAdmin) {
        Call<ArrayList<NewAdmin>> comment = dmpWebService.setLogin();
        comment.enqueue(new Callback<ArrayList<NewAdmin>>() {
            @Override
            public void onResponse(Call<ArrayList<NewAdmin>> call, Response<ArrayList<NewAdmin>> response) {
                boolean flag = false;

                if (response.isSuccessful()) {

                    ArrayList<NewAdmin> newAdmins = new ArrayList<>();
                    newAdmins = response.body();

                    for(int i=0; i<newAdmins.size(); i++) {
                        if(newAdmin.getPass().equals(newAdmins.get(i).getPass()) & newAdmin.getId().equals(newAdmins.get(i).getId())){
                            flag = true;
                        }
                    }
                    if(flag == true) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplication(), "아이디, 비번이 틀립니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "로그인 실패", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewAdmin>> call, Throwable t) {
                Toast.makeText(getApplication(),"로그인 실패",Toast.LENGTH_LONG).show();
                Log.e("setApiPlan", "error:" + t.getMessage());
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){

            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void request(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("error", "Session Closed Error is " + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {
                Log.d("sanch", "onNotSignedUp");
            }

            //사용자 정보 넘겨주는 메소드 (전역변수로 id값 지정했음)
            @Override
            public void onSuccess(UserProfile result) {
                Toast.makeText(LoginActivity.this, "사용자 이름은 " + result.getEmail(), Toast.LENGTH_SHORT).show();
                id=result.getEmail();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d("sanch", "onNotSignedUp");
            request();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("error", "Session Fail Error is " + exception.getMessage().toString());
        }
    }
}

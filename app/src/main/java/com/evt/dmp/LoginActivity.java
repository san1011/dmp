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

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;

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
                id = editId.getText().toString();
                if(id.equals("")){
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                }else if(editPass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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

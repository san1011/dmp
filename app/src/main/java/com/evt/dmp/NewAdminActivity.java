package com.evt.dmp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evt.dmp.protocal.DmpWebService;
import com.evt.dmp.protocal.dto.NewAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewAdminActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editId, editPass, editPassConfirm;
    private Button button;
    private String password, passwordConfirm = "";
    private DmpWebService dmpWebService;
    private Retrofit retrofit;
    private NewAdmin newAdmin = new NewAdmin();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newadmin);

        initUi(savedInstanceState);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(DmpWebService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dmpWebService = retrofit.create(DmpWebService.class);
    }

    private void initUi(Bundle savedInstanceState) {
        textView = (TextView) findViewById(R.id.textView);
        editId = (EditText) findViewById(R.id.editText);
        editPass = (EditText) findViewById(R.id.editText1);
        editPassConfirm = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = editPass.getText().toString();
                passwordConfirm = editPassConfirm.getText().toString();
                LoginActivity.id = editId.getText().toString();

                if (LoginActivity.id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_LONG).show(); //todo 중복확인기능넣어야함
                } else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                } else if (passwordConfirm.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호 재입력을 해주세요", Toast.LENGTH_LONG).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
                } else {
                    setIdCheck(LoginActivity.id);
                }
            }
        });
    }

    private void setIdCheck(String id){
        Call<String> comment = dmpWebService.getIdCheck(id);
        comment.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String flag = response.body();
                    Log.v("sanch",flag+"");

                    if(flag.equals("true")){
                        Toast.makeText(getApplication(), "아이디 중복입니다.", Toast.LENGTH_LONG).show();
                    }else{
                        newAdmin.setId(LoginActivity.id);
                        newAdmin.setPass(password);
                        setApiId(newAdmin);
                    }

                } else {
                    Toast.makeText(getApplication(), "서버 오류123", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 오류", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setApiId(NewAdmin newAdmin) {
        Log.d("sanch", newAdmin.getId()+ "");

        Call<NewAdmin> comment = dmpWebService.setAdmin(newAdmin);
        comment.enqueue(new Callback<NewAdmin>() {
            @Override
            public void onResponse(Call<NewAdmin> call, Response<NewAdmin> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), "회원가입 실패", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewAdmin> call, Throwable t) {
                Toast.makeText(getApplication(), "서버 오류", Toast.LENGTH_LONG).show();
                Log.e("setApiId", "error:" + t.getMessage());
            }
        });

    }
}

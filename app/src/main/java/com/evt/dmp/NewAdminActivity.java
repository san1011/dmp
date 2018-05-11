package com.evt.dmp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evt.dmp.LoginActivity;
import com.evt.dmp.MainActivity;
import com.evt.dmp.R;

public class NewAdminActivity extends AppCompatActivity{

    private TextView textView;
    private EditText editId,editPass,editPassConfirm;
    private Button button;
    private String password,passwordConfirm = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newadmin);

        initUi(savedInstanceState);
    }

    private void initUi(Bundle savedInstanceState){
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

               if(LoginActivity.id.equals("")){
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_LONG).show();
               }
               else if(password.equals("")){
                   Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
               }else if(passwordConfirm.equals("")){
                   Toast.makeText(getApplicationContext(),"비밀번호 재입력을 해주세요",Toast.LENGTH_LONG).show();
               }else if(!password.equals(passwordConfirm)){
                   Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_LONG).show();
               }else {
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
               }
            }
        });
    }
}

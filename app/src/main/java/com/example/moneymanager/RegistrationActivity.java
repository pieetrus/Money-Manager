package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnReg;
    private TextView mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registration();

    }

    private void registration() {

        mEmail = findViewById(R.id.et_email_reg);
        mPass = findViewById(R.id.et_password_reg);
        btnReg = findViewById(R.id.btn_reg);
        mSignin = findViewById(R.id.tv_signup);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email jest wymagany!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPass.setError("Hasło jest wymagane!");
                }
            }
        });


        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }




}

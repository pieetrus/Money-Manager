package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPass;
    private Button btnReg;
    private TextView etSignin;
    private ProgressDialog progressDialog;


    // Firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        registration();

    }

    private void registration() {

        etEmail = findViewById(R.id.et_email_reg);
        etPass = findViewById(R.id.et_password_reg);
        btnReg = findViewById(R.id.btn_reg);
        etSignin = findViewById(R.id.tv_login);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    etEmail.setError("Email jest wymagany!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    etPass.setError("Hasło jest wymagane!");
                    return;
                }
                progressDialog.setMessage("Przetwarzanie...");

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Zarejestrowano pomyślnie!", Toast.LENGTH_SHORT).show();
                            // Redirect to home activity
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Coś poszło nie tak :/ Skontaktuj się z obsługą.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        etSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }




}

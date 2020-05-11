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
    private TextView etSignIn;
    private ProgressDialog progressDialog;
    // Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialize();

        registrationHandler();
    }

    private void registrationHandler() {
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

                // Create user and add to database
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Zarejestrowano pomyślnie!", Toast.LENGTH_SHORT).show();
                            // Redirect to home activity
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Coś poszło nie tak :/", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Already signIn text view handler
        etSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    // Connect variables to layout
    private void initialize(){
        etEmail = findViewById(R.id.et_email_reg);
        etPass = findViewById(R.id.et_password_reg);
        btnReg = findViewById(R.id.btn_reg);
        etSignIn = findViewById(R.id.tv_login);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }




}

package com.example.moneymanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymanager.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    View view;

    // Floating button
    private FloatingActionButton btn_main;
    private FloatingActionButton btn_income;
    private FloatingActionButton btn_expense;

    // Floating button textviews
    private TextView tv_main;
    private TextView tv_income;
    private TextView tv_expense;

    // Determine if main button was clicked
    private boolean isOpen;

    // Animation
    private Animation fadeOpen;
    private Animation fadeClose;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference incomeDatabase;
    private DatabaseReference expenseDatabase;
    private FirebaseUser firebaseUser;

    // Inputs
    private EditText etAmount;
    private EditText etType;
    private EditText etNote;

    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        initialize();
        addData();

        // Main button click
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
            }
        });
        return view;
    }

    private void floatingButtonAnimation(){
        if (isOpen){
            btn_income.startAnimation(fadeClose);
            btn_expense.startAnimation(fadeClose);
            btn_income.setClickable(false);
            btn_expense.setClickable(false);

            tv_income.startAnimation(fadeClose);
            tv_expense.startAnimation(fadeClose);
            tv_income.setClickable(false);
            tv_expense.setClickable(false);
            isOpen = false;
        } else{
            btn_income.startAnimation(fadeOpen);
            btn_expense.startAnimation(fadeOpen);
            btn_income.setClickable(true);
            btn_expense.setClickable(true);

            tv_income.startAnimation(fadeOpen);
            tv_expense.startAnimation(fadeOpen);
            tv_income.setClickable(true);
            tv_expense.setClickable(true);
            isOpen = true;
        }
    }

    private void addData(){
        // fab button income

        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    private void incomeDataInsert(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View insertDataView = inflater.inflate(R.layout.custom_layout_for_insert_data, null);
        dialogBuilder.setView(insertDataView);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);

        Button btnSave = insertDataView.findViewById(R.id.btnSave);
        Button btnCancel = insertDataView.findViewById(R.id.btnCancel);

        etAmount = insertDataView.findViewById(R.id.et_amount);
        etType = insertDataView.findViewById(R.id.et_type);
        etNote = insertDataView.findViewById(R.id.et_note);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString().trim();
                String type = etType.getText().toString().trim();
                String note = etNote.getText().toString().trim();


                if (TextUtils.isEmpty(amount)){
                    etAmount.setError("Pole jest wymagane");
                    return;
                }
                int amount_int = Integer.parseInt(amount);

                if (TextUtils.isEmpty(type)){
                    etType.setError("Pole jest wymagane");
                    return;
                }
                if (TextUtils.isEmpty(note)){
                    etNote.setError("Pole jest wymagane");
                    return;
                }

                String id = incomeDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(id, amount_int, type, note, date);

                incomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Pomyślnie dodano!", Toast.LENGTH_SHORT).show();

                floatingButtonAnimation();
                dialog.dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void expenseDataInsert(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View insertDataView = inflater.inflate(R.layout.custom_layout_for_insert_data, null);
        dialogBuilder.setView(insertDataView);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);

        Button btnSave = insertDataView.findViewById(R.id.btnSave);
        Button btnCancel = insertDataView.findViewById(R.id.btnCancel);

        etAmount = insertDataView.findViewById(R.id.et_amount);
        etType = insertDataView.findViewById(R.id.et_type);
        etNote = insertDataView.findViewById(R.id.et_note);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString().trim();
                String type = etType.getText().toString().trim();
                String note = etNote.getText().toString().trim();


                if (TextUtils.isEmpty(amount)){
                    etAmount.setError("Pole jest wymagane");
                    return;
                }
                int amount_int = Integer.parseInt(amount);

                if (TextUtils.isEmpty(type)){
                    etType.setError("Pole jest wymagane");
                    return;
                }
                if (TextUtils.isEmpty(note)){
                    etNote.setError("Pole jest wymagane");
                    return;
                }

                String id = expenseDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(id, amount_int, type, note, date);

                incomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Pomyślnie dodano!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    // Connect all variables to layout
    private void initialize(){
        btn_main = view.findViewById(R.id.btn_ft_main);
        btn_income = view.findViewById(R.id.btn_ft_income);
        btn_expense = view.findViewById(R.id.btn_ft_expense);
        tv_income = view.findViewById(R.id.tv_ft_income);
        tv_expense = view.findViewById(R.id.tv_ft_expense);
        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);




        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        incomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId);
        expenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId);
    }

}

package com.example.moneymanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private View view;

    // Floating button
    private FloatingActionButton btn_main;
    private FloatingActionButton btn_income;
    private FloatingActionButton btn_expense;

    // Floating button textviews
    private TextView tv_total;
    private TextView tv_income;
    private TextView tv_expense;

    // Determine if main button was clicked
    private boolean isOpen;

    // Animation
    private Animation fadeOpen;
    private Animation fadeClose;

    // Dashboard income and expense result
    private TextView tv_totalIncome;
    private TextView tv_totalExpense;

    private DatabaseReference incomeDatabase;
    private DatabaseReference expenseDatabase;

    // Inputs
    private EditText etAmount;
    private EditText etType;
    private EditText etNote;

    // Recycler view
    private RecyclerView recyclerIncome;
    private RecyclerView recyclerExpense;

    private int totalCash;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        initialize();
        floatingButtonHandler();
        calculateIncomeAndExpense();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(
                Data.class,
                R.layout.dashboard_income,
                DashboardFragment.IncomeViewHolder.class,
                incomeDatabase
        ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data data, int i) {
                incomeViewHolder.setIncomeAmount(data.getAmount());
                incomeViewHolder.setIncomeDate(data.getDate());
                incomeViewHolder.setIncomeType(data.getType());
            }
        };

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(
                Data.class,
                R.layout.dashboard_expense,
                DashboardFragment.ExpenseViewHolder.class,
                expenseDatabase
        ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data data, int i) {
                expenseViewHolder.setExpenseAmount(data.getAmount());
                expenseViewHolder.setExpenseDate(data.getDate());
                expenseViewHolder.setExpenseType(data.getType());
            }
        };

        recyclerIncome.setAdapter(incomeAdapter);
        recyclerExpense.setAdapter(expenseAdapter);


    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{

        View incomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            incomeView = itemView;
        }

        void setIncomeType(String type){
            TextView tvType = incomeView.findViewById(R.id.type_income_ds);
            tvType.setText(type);
        }

        void setIncomeAmount(int amount){
            TextView tvAmount = incomeView.findViewById(R.id.amount_income_ds);
            tvAmount.setText(amount + " zł");
        }

        void setIncomeDate(String date){
            TextView tvDate = incomeView.findViewById(R.id.date_income_ds);
            tvDate.setText(String.valueOf(date));
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        View expenseView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseView = itemView;
        }

        void setExpenseType(String type){
            TextView tvType = expenseView.findViewById(R.id.type_expense_ds);
            tvType.setText(type);
        }

        void setExpenseAmount(int amount){
            TextView tvAmount = expenseView.findViewById(R.id.amount_expense_ds);
            tvAmount.setText(amount + " zł");
        }

        void setExpenseDate(String date){
            TextView tvDate = expenseView.findViewById(R.id.date_expense_ds);
            tvDate.setText(String.valueOf(date));
        }
    }

    private void calculateIncomeAndExpense(){

        // Recycler
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        recyclerIncome.setHasFixedSize(true);
        recyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        recyclerExpense.setHasFixedSize(true);
        recyclerExpense.setLayoutManager(layoutManagerExpense);

        // Calculate total income
        incomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int sum = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Data data = snapshot.getValue(Data.class);
                    sum += data.getAmount();
                }
                totalCash += sum;
                tv_total.setText(totalCash + " zł");
                tv_totalIncome.setText(sum + " zł");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                totalCash = 0;
            }
        });

        // Calculate total expense
        expenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int sum = 0;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Data data = snapshot.getValue(Data.class);
                    sum += data.getAmount();
                }
                totalCash -= sum;
                tv_total.setText(totalCash + " zł");
                tv_totalExpense.setText(sum + " zł");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                totalCash = 0;
            }
        });
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

    private void floatingButtonHandler(){
        // Main floating button click
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButtonAnimation();
            }
        });

        // Add income button click
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });
        // Add expense button click
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

                expenseDatabase.child(id).setValue(data);

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
        tv_total = view.findViewById(R.id.tv_total_amount);
        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);
        tv_totalIncome = view.findViewById(R.id.tv_income_amount);
        tv_totalExpense = view.findViewById(R.id.tv_expense_amount);

        recyclerIncome = view.findViewById(R.id.recycler_income);
        recyclerExpense = view.findViewById(R.id.recycler_expense);

        //Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        incomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId);
        expenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId);

        totalCash = 0;
        isOpen = false;
    }

}

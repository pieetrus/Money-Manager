package com.example.moneymanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moneymanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public class IncomeFragment extends Fragment {

    // Firebase database
    private FirebaseAuth firebaseAuth;
    private DatabaseReference incomeDatabase;
    private FirebaseUser firebaseUser;

    // Recycler view
    private RecyclerView recyclerView;

    private TextView tvIncomeTotal;

    // Update
    private EditText etUpdAmount;
    private EditText etUpdType;
    private EditText etUpdNote;

    private Button btnUpdUpdate;
    private Button btnUpdDelete;

    // Data item
    private String type;
    private String note;
    private int amount;

    String post_key;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String id = firebaseUser.getUid();

        incomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(id);

        recyclerView = view.findViewById(R.id.recycle_income);

        tvIncomeTotal = view.findViewById(R.id.tv_income_total);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        incomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalValue = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Data data = snapshot.getValue(Data.class);
                    totalValue += data.getAmount();

                    tvIncomeTotal.setText(String.valueOf(totalValue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.income_recycler_data,
                MyViewHolder.class,
                incomeDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data data, final int i) {
                myViewHolder.setType(data.getType());
                myViewHolder.setNote(data.getNote());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setAmount(data.getAmount());
                myViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key = getRef(i).getKey();
                        type = data.getType();
                        note = data.getNote();
                        amount = data.getAmount();

                        updateDataItem();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View myView;

        public MyViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        private void setType(String type){
            TextView tvType = myView.findViewById(R.id.tv_type_income);
            tvType.setText(type);
        }

        private void setNote(String note){
            TextView tvNote = myView.findViewById(R.id.tv_note_income);
            tvNote.setText(note);
        }

        private void setDate(String date){
            TextView tvDate = myView.findViewById(R.id.tv_date_income);
            tvDate.setText(date);
        }

        private void setAmount(int amount){
            TextView tvAmount = myView.findViewById(R.id.tv_amount_income);
            tvAmount.setText(String.valueOf(amount));
        }

    }

    private void updateDataItem() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.update_data_item, null);
        dialogBuilder.setView(view);

        etUpdAmount = view.findViewById(R.id.et_upd_amount);
        etUpdNote = view.findViewById(R.id.et_upd_note);
        etUpdType = view.findViewById(R.id.et_upd_type);

        btnUpdDelete = view.findViewById(R.id.btn_upd_delete);
        btnUpdUpdate = view.findViewById(R.id.btn_upd_update);

        // Set data
        etUpdType.setText(type);
        etUpdNote.setText(note);
        etUpdAmount.setText(String.valueOf(amount));
        etUpdType.setSelection(type.length());
        etUpdNote.setSelection(note.length());
        etUpdAmount.setSelection(String.valueOf(amount).length());

        final AlertDialog dialog = dialogBuilder.create();

        btnUpdUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = etUpdType.getText().toString().trim();
                note = etUpdNote.getText().toString().trim();
                amount = Integer.parseInt(etUpdAmount.getText().toString().trim());
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(post_key, amount, type, note, date);

                incomeDatabase.child(post_key).setValue(data);
                dialog.dismiss();
            }
        });

        btnUpdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }


}

package com.example.moneymanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            protected void populateViewHolder(MyViewHolder myViewHolder, Data data, int i) {
                myViewHolder.setType(data.getType());
                myViewHolder.setNote(data.getNote());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setAmount(data.getAmount());
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

}

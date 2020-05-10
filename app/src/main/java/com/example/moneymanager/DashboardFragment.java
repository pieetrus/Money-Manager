package com.example.moneymanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        initialize();

        // Main button click
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        return view;
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
    }

}

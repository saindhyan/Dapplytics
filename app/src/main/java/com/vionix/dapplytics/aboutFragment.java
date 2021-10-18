package com.vionix.dapplytics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class aboutFragment extends Fragment {
    String s1="Version- 1.0.0.1  ";
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_about, container, false);

       textView = view.findViewById(R.id.t1);
       textView.setText(s1);
       return  view;
    }
}
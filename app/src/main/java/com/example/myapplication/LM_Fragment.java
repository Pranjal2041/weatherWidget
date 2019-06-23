package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.myapplication.HomeActivity.MyPREFERENCES;

public class LM_Fragment extends Fragment {


    static int i;//theme position
    private static final String TAG = "LM_Fragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedpreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp=sharedpreferences.getString("Theme","");

        View view=null;

       if(temp.equals(Constants.themes[1]))
            view= inflater.inflate(R.layout.op_v1_widget, container, false);
        else if(temp.equals(Constants.themes[2]))
            view= inflater.inflate(R.layout.op_v2_widget, container, false);
        else
            view=inflater.inflate(R.layout.pixel_widget, container, false);

        Constants constants=new Constants();
        constants.modifyUI(view,temp);



        return view;
    }// onCreateView
}// class
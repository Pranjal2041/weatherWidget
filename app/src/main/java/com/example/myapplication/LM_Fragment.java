package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static com.example.myapplication.HomeActivity.MyPREFERENCES;

public class LM_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedpreferences=this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp=sharedpreferences.getString("Theme","");

        Fragment frg = null;
        frg = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.frag1);
        final FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();



        if(temp.equals(Constants.themes[1]))
            return inflater.inflate(R.layout.op_v1_widget, container, false);
        else if(temp.equals(Constants.themes[2]))
            return inflater.inflate(R.layout.op_v2_widget, container, false);
        return inflater.inflate(R.layout.pixel_widget, container, false);
    }



}
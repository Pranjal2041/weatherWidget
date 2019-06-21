package com.example.myapplication;

import android.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.myapplication.Constants.themes;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    public static final String MyPREFERENCES = "MyPref" ;

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);















        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp="";
        try {
            temp = sharedpreferences.getString("Theme", "");
            Toast.makeText(this, "temp", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Theme",themes[0]);
            editor.apply();
            temp=themes[0];
        }




        Spinner spinner=(Spinner)findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);



        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,themes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);
        int item=0;
        for(int i=0;i<themes.length;i++)
        {
            if(themes[i].equals(temp))
                 item=i;
        }


       spinner.setSelection(item);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), themes[position], Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("Theme",themes[position]);
        editor.commit();
        Toast.makeText(this, "edited theme", Toast.LENGTH_SHORT).show();
updateWidget();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
    }

    void updateWidget()
    {
        Intent intent = new Intent(this, WidgetActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WidgetActivity.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

    }








}

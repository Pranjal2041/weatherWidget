package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Constants.themes;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "HomeActivity";
    // shared pref
    public static final String MyPREFERENCES = "MyPref" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Transparent Status bar @Salazar
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        // init SharedPreference
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp="";
        try {
            temp = sharedpreferences.getString("Theme", "");
            // Toast.makeText(this, "temp", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Theme",themes[0]);
            editor.apply();
            temp=themes[0];
        }

        /* init recyclerView */
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler);
        // adapter
        List<RecyclerData> list = getData();
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(list,getApplication());
        recyclerView.setAdapter(adapter);
        // layoutManager
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL,false));
        // Scroll Snap Helper
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        /* end recyclerView*/


        // Spinner - select widget for home screen
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

    } //onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(getApplicationContext(), themes[position], Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("Theme",themes[position]);
        editor.commit();
        // Toast.makeText(this, "edited theme", Toast.LENGTH_SHORT).show();
        //updateFragment();
        updateWidget();
    }// spinner - onItemSelected

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
    }// spinner - onNothingSelected

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

    }// updateWidget

    void updateFragment()
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.cards,new LM_Fragment());

        fragmentTransaction.commit();

    }// updateFragment


    private List<RecyclerData> getData()
    {
        List<RecyclerData> list = new ArrayList<>();
        list.add(new RecyclerData());
        list.add(new RecyclerData());
        list.add(new RecyclerData());

        return list;
    }// getData

}//class

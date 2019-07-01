package com.example.myapplication;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.myapplication.Constants.s_font_size_i;
import static com.example.myapplication.Constants.s_font_style_i;
import static com.example.myapplication.Constants.themes;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    // var for appBar @Salazar
    public static final int MESSAGE_ANIMATION_START = 1;
    public static final int DELAYT_BEFORE_FIRST_MENU_ANIMATION = 1000;
    public static final int DELAY_BETWEEN_MENU_ANIMATION_AND_CLICK = 800;
    private Handler menuAnimationHandler = new MenuAnimationHandler();
    private View.OnClickListener onMenuClickListener;
    private ImageView iconMenu;
    // shared pref
    public static final String MyPREFERENCES = "MyPref";
    SharedPreferences sharedpreferences;
    // var for recyclerView @Salazar
    CustomLinearLayoutManager cllm;
    int position;
    Constants c;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_with_navbar);
        c=new Constants();

        // AppBar @Salazar
        drawerLayout = findViewById(R.id.drawer_layout);
        iconMenu = (ImageView) findViewById(R.id.ap_ic_menu);
        iconMenu.setImageDrawable(AnimatedVectorDrawableCompat.create(this, R.drawable.ab_ic_menu_animated));
        menuAnimationHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION_START, DELAYT_BEFORE_FIRST_MENU_ANIMATION);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (settings.isAnimateMenu()) {
                animateMenuImage();
                drawerLayout.openDrawer(Gravity.START);
//                    iconMenu.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (onMenuClickListener != null) {
//                                onMenuClickListener.onClick(iconMenu);
//                            }// if-inner
//                        }// run()
//                    }, DELAY_BETWEEN_MENU_ANIMATION_AND_CLICK);
//                }// if-outer
//                else {
//                    if (onMenuClickListener != null) {
//                        onMenuClickListener.onClick(iconMenu);
//                    }// if-inner
//                }// else
            }// onClick
        }); // setOnClickListener


        // Transparent Status bar @Salazar
        // window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.eo_dblue));
        loadPreferences();  // Initialise preferences

        /* init recyclerView */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        // adapter
        List<RecyclerData> list = getData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, getApplication());
        recyclerView.setAdapter(adapter);
        // layoutManager
        cllm = new CustomLinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(cllm);
        // Page Snap Helper - scrollSnap
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        // scrollListener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = getCurrentItem();
                }//if
            }// onScrollStateChanged
        });// addOnScrollListener
        /* end recyclerView*/
        initialiseSpinners();

        // apply widget on FAB press @Salazar
        final FloatingActionButton fab = findViewById(R.id.apply_widget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSharedPreferences();
                updateWidget();
                Snackbar.make(fab, "Widget Applied", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }// onClick()
        });// setOnClickListener

        // Open color dialog on FAB press
        final FloatingActionButton fab_font_color=findViewById(R.id.font_colour_fab);
        fab_font_color.setBackgroundTintList(ColorStateList.valueOf(Constants.s_color));
        fab_font_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColourDialog1 cd1=new ColourDialog1(HomeActivity.this);
                cd1.show();
                cd1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        if(ColourDialog1.color_dialog_type==1)
                        {
                        ColourDialog cd=new ColourDialog(HomeActivity.this);
                        cd.show();
                        cd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                fab_font_color.setBackgroundTintList(ColorStateList.valueOf(Constants.s_color));
                                updatePreview(getRecyclerViewPosition());
                                ColourDialog1.color_dialog_type=0;
                            }
                        });}

                    }
                });


            }
        });// setOnClickListener
        initialiseToggleButtons();
        updatePreview(position);
    }// onCreate


    // Returns recycler view position
    int getRecyclerViewPosition()
    {
        return this.position;
    }

    // function to animate appBar menu icon @Salazar
    private void animateMenuImage() {
        final Drawable drawable = iconMenu.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) iconMenu.getDrawable()).start();
        }// if
    }// animateMenuImage

    // get current postion on recyclerView scroll
    private int getCurrentItem() {
        updatePreview(cllm.findFirstVisibleItemPosition());
        return cllm.findFirstVisibleItemPosition();
    }// getCurrentItem()

    // update home-screen widget
    void updateWidget() {
        Intent intent = new Intent(this, WidgetActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WidgetActivity.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

    }// updateWidget

    void updateSharedPreferences()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Theme", themes[position]);
        editor.putInt("Font Type",s_font_style_i);
        editor.putInt("Font Size",Constants.s_font_size_i);
        editor.putInt("Font Color",Constants.s_color);
        editor.putInt("Weather Unit",Constants.temp_unit);
        editor.putBoolean("Clock Format 24",Constants.clock_format_24);
        editor.apply();

    }

    void loadPreferences()
    {

        // init SharedPreference
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp = "";
        try {
            temp = sharedpreferences.getString("Theme", "");
            Constants.s_font_style_i= sharedpreferences.getInt("Font Type", 0);
            Constants.s_font_size_i= sharedpreferences.getInt("Font Size", 6);
            Constants.s_color= sharedpreferences.getInt("Font Color", 0xFFFFFF);
            Constants.temp_unit= sharedpreferences.getInt("Weather Unit", 0);
            Constants.clock_format_24= sharedpreferences.getBoolean("Clock Format 24", true);

        } catch (Exception e) {
            updateSharedPreferences();
        }
    }

    private List<RecyclerData> getData() {
        List<RecyclerData> list = new ArrayList<>();
        list.add(new RecyclerData());
        list.add(new RecyclerData());
        list.add(new RecyclerData());

        return list;
    }// getData
    // Spinner Functions
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List<RecyclerData> list = getData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, getApplication());
        Log.d(TAG, "onItemSelected: ");

        int parentId = parent.getId();
        switch (parentId) {
            case R.id.font_spinner:
                Constants.s_font_style_i = position;
                Log.d(TAG, "onItemSelected: ");
                break;
            case R.id.font_spinner2:
                Constants.s_font_size_i = position;
                Log.d(TAG, "onItemSelected: Size");
                break;
        }
        updatePreview(this.position);
    }

    void updatePreview(int position)
    {
        if(position==1)
            c.modifyUI(findViewById(R.id.include_op_v1), themes[1]);
        else if(position==2)
            c.modifyUI(findViewById(R.id.include_op_v2), themes[2]);
        else if(position==0)
            c.modifyUI(findViewById(R.id.include_pixel), themes[0]);


        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // handler for menu icon animation @Salazar
    class MenuAnimationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_ANIMATION_START:
                    final Drawable drawable = iconMenu.getDrawable();
                    if (drawable instanceof AnimatedVectorDrawableCompat) {
                        ((AnimatedVectorDrawableCompat) iconMenu.getDrawable()).start();
                        sendEmptyMessageDelayed(MESSAGE_ANIMATION_START, 5000);
                    }// if
                    break;
            }// switch
        }// handleMessage
    }// class MenuAnimatorHandler

    void getSpinnerPref()
    {
        int i=(findViewById(R.id.font_spinner).getVerticalScrollbarPosition());

    }

    void initialiseSpinners() {
        Constants constants = new Constants();
        Log.d(TAG, "initialiseSpinners: id=" + R.id.font_spinner);
        {// font_spinner
            Spinner spinner = findViewById(R.id.font_spinner);
            spinner.setOnItemSelectedListener(this);
            List spinner_list = new ArrayList();
            spinner_list.addAll(Arrays.asList(constants.s_font_style));
            SpinnerAdapter<String> dataAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(s_font_style_i);
        }

        {// font_size_spinner
            Spinner spinner = (Spinner) findViewById(R.id.font_spinner2);
            spinner.setOnItemSelectedListener(this);
            List spinner_list = new ArrayList();
            spinner_list.addAll(Arrays.asList(constants.s_font_size));
            SpinnerAdapter<String> dataAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(s_font_size_i);

        }


    }

    void initialiseToggleButtons()
    {
        //Toggle Button to switch clock format
        ToggleButton toggleButton=findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: checked="+isChecked);
                Constants.clock_format_24 = isChecked;
                updatePreview(getRecyclerViewPosition());
            }
        });

        ToggleButton toggleButton_weather_unit=findViewById(R.id.temp_unit_toggleButton);
        toggleButton_weather_unit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: checked="+isChecked);
                if(isChecked)
                    Constants.temp_unit=1;
                else
                    Constants.temp_unit=0;
                updatePreview(getRecyclerViewPosition());
            }
        });
    }


}//class

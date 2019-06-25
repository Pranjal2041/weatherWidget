package com.example.myapplication;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Constants.themes;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "HomeActivity";
    // var for appBar @Salazar
    public static final int MESSAGE_ANIMATION_START = 1;
    public static final int DELAYT_BEFORE_FIRST_MENU_ANIMATION = 1000;
    public static final int DELAY_BETWEEN_MENU_ANIMATION_AND_CLICK = 800;
    private Handler menuAnimationHandler = new MenuAnimationHandler();
    private View.OnClickListener onMenuClickListener;;
    private ImageView iconMenu;
    DrawerLayout drawerLayout;
    // shared pref
    public static final String MyPREFERENCES = "MyPref";
    SharedPreferences sharedpreferences;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_with_navbar);

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
        Window window = getWindow();
        // window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.eo_dblue));

        // init SharedPreference
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String temp = "";
        try {
            temp = sharedpreferences.getString("Theme", "");
        } catch (Exception e) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Theme", themes[0]);
            editor.apply();
            temp = themes[0];
        }

        /* init recyclerView */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        // adapter
        List<RecyclerData> list = getData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, getApplication());
        recyclerView.setAdapter(adapter);
        // layoutManager
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        // Scroll Snap Helper
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        /* end recyclerView*/

        // Spinner - select widget for home screen
        Spinner spinner = (Spinner) findViewById(R.id.simpleSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, themes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        int item = 0;
        for (int i = 0; i < themes.length; i++) {
            if (themes[i].equals(temp))
                item = i;
        }// for
        spinner.setSelection(item);

        // apply widget change when response on FAB click @Salazar
        FloatingActionButton fab = findViewById(R.id.apply_widget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateWidget();
                Snackbar.make(view, "Widget Applied", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }// onClick()
        });// setOnClickListener
    }// onCreate

    // function to animate appBar menu icon @Salazar
    private void animateMenuImage() {
        final Drawable drawable = iconMenu.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) iconMenu.getDrawable()).start();
        }// if
    }// animateMenuImage

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(getApplicationContext(), themes[position], Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("Theme", themes[position]);
        editor.commit();
        // Toast.makeText(this, "edited theme", Toast.LENGTH_SHORT).show();
        //updateFragment();
        updateWidget();
    }// spinner - onItemSelected

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
    }// spinner - onNothingSelected

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

    private List<RecyclerData> getData() {
        List<RecyclerData> list = new ArrayList<>();
        list.add(new RecyclerData());
        list.add(new RecyclerData());
        list.add(new RecyclerData());

        return list;
    }// getData

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

    //    void updateFragment()
    //    {
    //
    //        FragmentManager fragmentManager = getSupportFragmentManager();
    //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //        fragmentTransaction.replace(R.id.cards,new LM_Fragment());
    //
    //        fragmentTransaction.commit();
    //
    //    }// updateFragment

}//class

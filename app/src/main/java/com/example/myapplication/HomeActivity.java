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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.Constants.themes;

public class HomeActivity extends AppCompatActivity {

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
        // window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Window window = getWindow();
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
        cllm = new CustomLinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(cllm);
        // Scroll Snap Helper
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

        // apply widget change when response on FAB click @Salazar
        FloatingActionButton fab = findViewById(R.id.apply_widget);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWidget();
                Snackbar.make(view, "Widget Applied", Snackbar.LENGTH_SHORT)
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

    private int getCurrentItem() {
        return cllm.findFirstVisibleItemPosition();
    }// getCurrentItem()

    void updateWidget() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Theme", themes[position]);
        editor.commit();
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

}//class

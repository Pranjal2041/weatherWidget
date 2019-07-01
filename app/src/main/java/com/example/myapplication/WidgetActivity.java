package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class WidgetActivity extends AppWidgetProvider {




    public static final String MyPREFERENCES = "MyPref" ;

    SharedPreferences sharedpreferences;





    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
     /*   //for(int i=0; i<appWidgetIds.length; i++){


            int currentWidgetId = appWidgetIds[0];
            String url = "https://www.accuweather.com/en/in/india-weather";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));

            PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.activity_main);

            views.setOnClickPendingIntent(R.id.button, pending);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.activity_main);
            remoteViews.setTextViewText(R.id.button, "Set button text here");



        //}*/

     String theme="";
    Constants constants=new Constants();



         sharedpreferences=context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        try {
             theme = sharedpreferences.getString("Theme", "");
//            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Theme",Constants.themes[0]);
            editor.apply();
            theme="Pixel";
        }

//        Toast.makeText(context, "Current theme is "+theme, Toast.LENGTH_SHORT).show();
        RemoteViews date=null;
        RemoteViews temp=null;
        RemoteViews clock_view;
        int date_id=0;
        int temp_id=0;
        int s_color=sharedpreferences.getInt("Font Color", 0xFFFFFF);
        int s_font_size_i=sharedpreferences.getInt("Font Size", 6);
        Constants.temp_unit= sharedpreferences.getInt("Weather Unit", 0);





        for (int widgetId : appWidgetIds) {

            switch (theme)
            {
                case "Pixel"://Pixel 3




                    date_id=R.id.textView;
                    temp_id=R.id.textView3;
                    date = new RemoteViews(context.getPackageName(),
                            R.layout.pixel_widget);
                    temp = new RemoteViews(context.getPackageName(),
                            R.layout.pixel_widget);

                    Intent intent = new Intent(context, WidgetActivity.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);                         //Source Android Authority
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    date.setOnClickPendingIntent(R.id.textView, pendingIntent);



                    break;

                case "OnePlus-1":   //onePlus v1

                    date_id=R.id.onePlusDate;
                    temp_id=R.id.onePlusV1Temp;
                    clock_view = new RemoteViews(context.getPackageName(),R.layout.op_v1_widget);
                    temp = new RemoteViews(context.getPackageName(),R.layout.op_v1_widget);




                    date = new RemoteViews(context.getPackageName(),
                            R.layout.op_v1_widget);



                    //date.setTextColor(R.id.onePlusDate,);

                    clock_view.setTextColor(R.id.digitalclock,s_color);
                    /*if (Constants.clock_format_24) {
                        textClock.setFormat24Hour("kk:mm");
                    } else {
                        textClock.setFormat24Hour("h:mm A");
                    }*/



                    appWidgetManager.updateAppWidget(widgetId,clock_view);





                    break;

                case "OnePlus-2":   //onePlus v2

                    date_id=R.id.onePlusV2Date;
                    temp_id=R.id.onePlusV2Temp;
                    temp = new RemoteViews(context.getPackageName(),R.layout.op_v2_widget);

                    date = new RemoteViews(context.getPackageName(),
                            R.layout.op_v2_widget);


                    break;

                    default:


            }// switch block


            if(date!=null&&temp!=null) {
                date.setTextViewText(date_id, constants.getDate());
                temp.setTextViewText(temp_id, constants.getTemp());
                date.setTextColor(date_id, s_color);
                temp.setTextColor(temp_id, s_color);
                date.setTextViewTextSize(date_id, 0, Float.parseFloat(Constants.s_font_size[s_font_size_i]));
                temp.setTextViewTextSize(temp_id, 0, Float.parseFloat(Constants.s_font_size[s_font_size_i]));

                appWidgetManager.updateAppWidget(widgetId, date);
                appWidgetManager.updateAppWidget(widgetId, temp);
            }


        }


    }






}
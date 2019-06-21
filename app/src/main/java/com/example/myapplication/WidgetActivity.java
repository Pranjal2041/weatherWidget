package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Theme",Constants.themes[0]);
            editor.apply();
            theme="Pixel";
        }

        Toast.makeText(context, "Current theme is "+theme, Toast.LENGTH_SHORT).show();
        RemoteViews date;
        RemoteViews temp;


        for (int widgetId : appWidgetIds) {

            switch (theme)
            {
                case "Pixel"://Pixel 3





                    date = new RemoteViews(context.getPackageName(),
                            R.layout.pixel_widget);
                    date.setTextViewText(R.id.textView, constants.getDate());

                    RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
                            R.layout.pixel_widget);
                    remoteViews2.setTextViewText(R.id.textView3, constants.getTemp());

                    Intent intent = new Intent(context, WidgetActivity.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);                         //Source Android Authority
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    date.setOnClickPendingIntent(R.id.textView, pendingIntent);


                    appWidgetManager.updateAppWidget(widgetId, date);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews2);

                    break;

                case "OnePlus-1":   //onePlus v1

                    RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.op_v1_widget);
                    temp = new RemoteViews(context.getPackageName(),R.layout.op_v1_widget);




                    date = new RemoteViews(context.getPackageName(),
                            R.layout.op_v1_widget);
                    date.setTextViewText(R.id.onePlusDate, constants.getDate());
                    temp.setTextViewText(R.id.onePlusV1Temp,constants.getTemp());


                    //date.setTextColor(R.id.onePlusDate,);


                    appWidgetManager.updateAppWidget(widgetId,views);
                    appWidgetManager.updateAppWidget(widgetId,date);
                    appWidgetManager.updateAppWidget(widgetId,temp);



                    break;

                case "OnePlus-2":   //onePlus v2

                    temp = new RemoteViews(context.getPackageName(),R.layout.op_v2_widget);

                    date = new RemoteViews(context.getPackageName(),
                            R.layout.op_v2_widget);
                    date.setTextViewText(R.id.onePlusV2Date, constants.getDateShort());
                    temp.setTextViewText(R.id.onePlusV2Temp,constants.getTemp());
                    //date.setTextColor(R.id.onePlusDate,);


                    appWidgetManager.updateAppWidget(widgetId,temp);
                    appWidgetManager.updateAppWidget(widgetId,date);


                    break;

                    default:


            }




        }


    }






}
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

    final String Day[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    final String Months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};


    public static final String MyPREFERENCES = "MyPref" ;

    SharedPreferences sharedpreferences;
    String[] themes={"Pixel","OnePlus-1","OnePlus-2"};





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




         sharedpreferences=context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        try {
             theme = sharedpreferences.getString("Theme", "");
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Theme",themes[0]);
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
                    date.setTextViewText(R.id.textView, getDate());

                    RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
                            R.layout.pixel_widget);
                    remoteViews2.setTextViewText(R.id.textView3, getTemp());

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
                    date.setTextViewText(R.id.onePlusDate, getDate());
                    temp.setTextViewText(R.id.onePlusV1Temp,getTemp());


                    //date.setTextColor(R.id.onePlusDate,);


                    appWidgetManager.updateAppWidget(widgetId,views);
                    appWidgetManager.updateAppWidget(widgetId,date);
                    appWidgetManager.updateAppWidget(widgetId,temp);



                    break;

                case "OnePlus-2":   //onePlus v2

                    temp = new RemoteViews(context.getPackageName(),R.layout.op_v2_widget);

                    date = new RemoteViews(context.getPackageName(),
                            R.layout.op_v2_widget);
                    date.setTextViewText(R.id.onePlusV2Date, getDateShort());
                    temp.setTextViewText(R.id.onePlusV2Temp,getTemp());
                    //date.setTextColor(R.id.onePlusDate,);


                    appWidgetManager.updateAppWidget(widgetId,temp);
                    appWidgetManager.updateAppWidget(widgetId,date);


                    break;

                    default:


            }




        }


    }


    String getDateShort()
    {
        Calendar calendar = Calendar.getInstance();
        String date = Months[calendar.get(Calendar.MONTH)].toUpperCase()+" "+ calendar.get(Calendar.DATE)+", "+Day[calendar.get(Calendar.DAY_OF_WEEK)].substring(0,3).toUpperCase() ;//+calendar.get(Calendar.YEAR);
        return date;



    }

    String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        String date = Day[calendar.get(Calendar.DAY_OF_WEEK)] + ", " + calendar.get(Calendar.DATE) + "  " + Months[calendar.get(Calendar.MONTH)];//+calendar.get(Calendar.YEAR);
        return date;

    }

    String getTemp()
    {

        String temp = (new Random().nextInt(80) - 30) + " °C";
        return temp;

    }



}
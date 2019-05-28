package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppWidgetProvider {

    final String Day[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    final String Months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};



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

            for (int i = 0; i < appWidgetIds.length; i++) {
                    int widgetId = appWidgetIds[i];
                    String number = (new Random().nextInt(80) - 30)+" C";

Calendar calendar=Calendar.getInstance();
String date= Day[calendar.get(Calendar.DAY_OF_WEEK)]+"  "+calendar.get(Calendar.DATE)+"  "+Months[calendar.get(Calendar.MONTH)];//+calendar.get(Calendar.YEAR);




                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.activity_main);
                    remoteViews.setTextViewText(R.id.textView, date);

                RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
                        R.layout.activity_main);
                remoteViews2.setTextViewText(R.id.textView3, number);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);                         //Source Android Authority
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.textView, pendingIntent);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }


    }
}
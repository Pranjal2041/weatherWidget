package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class Constants {

    private static final String TAG = "Constants";
    final String[] Day = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};        //  FOR
    final String[] Months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};    // WIDGET ACTIVITY
    protected static final String[] themes = {"Pixel", "OnePlus-1", "OnePlus-2"}; // widget theme list

    String s_font_style[] = {"Theme Font", "Device Font", "Choose custom Font"};
    static int s_font_style_i;
    static String s_font_size[] = {"12", "14", "16", "18", "20", "24", "28", "32", "36", "40", "44", "48"};
    static int s_font_size_i=6;
    static int s_color=0xFFFFFF;
    static boolean clock_format_24=true;
    static int temp_unit; // 0 for °C ; 1 for °F

    protected void modifyUI(View view, String theme) {
        TextView date = null;
        TextView temp = null;
        TextClock textClock = null;
        switch (theme) {
            case "Pixel"://Pixel 3
                try {
                    date = view.findViewById(R.id.textView);
                    temp = view.findViewById(R.id.textView3);

                    date.setText(getDate() + " |");
                    temp.setText(getTemp());
                } catch (Exception e) {
                    Log.d(TAG, "modifyUI: error");
                    e.printStackTrace();
                }
                break;

            case "OnePlus-1":   //onePlus v1
                try {
                    date = view.findViewById(R.id.onePlusDate);
                    temp = view.findViewById(R.id.onePlusV1Temp);
                    textClock = view.findViewById(R.id.digitalclock);


                    date.setText(getDate());
                    temp.setText(getTemp());
                } catch (Exception e) {
                    Log.d(TAG, "modifyUI: error");
                    e.printStackTrace();
                }
                break;

            case "OnePlus-2":   //onePlus v2
                try {
                    date = view.findViewById(R.id.onePlusV2Date);
                    temp = view.findViewById(R.id.onePlusV2Temp);

                    date.setText(getDateShort());
                    temp.setText(getTemp());
                } catch (Exception e) {
                    Log.d(TAG, "modifyUI: error");
                    e.printStackTrace();
                }
                break;
        }// switch

        try {
            if (textClock != null) {
                textClock.setTextColor(Color.rgb((s_color / 256) / 256, (s_color / 256) % 256, s_color % 256));
                if (clock_format_24) {
                    textClock.setFormat24Hour("kk:mm");
                } else {
                    textClock.setFormat24Hour("h:mm A");
                }
            }
            if (date != null && temp != null) {
                date.setTextColor(Color.rgb((s_color / 256) / 256, (s_color / 256) % 256, s_color % 256));
                temp.setTextColor(Color.rgb((s_color / 256) / 256, (s_color / 256) % 256, s_color % 256));
                date.setTextSize(Float.parseFloat(s_font_size[s_font_size_i]));
                temp.setTextSize(Float.parseFloat(s_font_size[s_font_size_i]));
            }
        } catch (NumberFormatException e) {
            Log.d(TAG, "modifyUI: error");
            e.printStackTrace();
        }

    }// modifyUI()

    String getDateShort() {
        Calendar calendar = Calendar.getInstance();
        String date = Months[calendar.get(Calendar.MONTH)].toUpperCase() + " " + calendar.get(Calendar.DATE) + ", " + Day[(calendar.get(Calendar.DAY_OF_WEEK) - 1)].substring(0, 3).toUpperCase();//+calendar.get(Calendar.YEAR);
        return date;
    }// getDateShort()

    String getDate() {
        Calendar calendar = Calendar.getInstance();
        String date = Day[(calendar.get(Calendar.DAY_OF_WEEK) - 1)] + ", " + calendar.get(Calendar.DATE) + "  " + Months[calendar.get(Calendar.MONTH)];//+calendar.get(Calendar.YEAR);
        return date;
    }// getDate()

    String getTemp() {
        String temp="";
        if(temp_unit==0)
            temp = (new Random().nextInt(80) - 30) + " °C";
        else if(temp_unit==1)
            temp=(new Random().nextInt(80) - 30) + " °F";


        return temp;
    }// getTemp
}// class

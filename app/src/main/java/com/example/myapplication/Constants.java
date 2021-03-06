package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class Constants {

    private static final String TAG = "Constants";
    final String[] Day = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};        //  FOR
    final String[] Months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};    // WIDGET ACTIVITY
    protected static final String[] themes = {"Pixel", "OnePlus-1", "OnePlus-2"}; // widget theme list

    protected void modifyUI(View view, String theme) {
        TextView date;
        TextView temp;

        switch (theme) {
            case "Pixel"://Pixel 3
                date = view.findViewById(R.id.textView);
                temp = view.findViewById(R.id.textView3);

                date.setText(getDate() + " |");
                temp.setText(getTemp());
                break;

            case "OnePlus-1":   //onePlus v1
                date = view.findViewById(R.id.onePlusDate);
                temp = view.findViewById(R.id.onePlusV1Temp);

                date.setText(getDate());
                temp.setText(getTemp());
                break;

            case "OnePlus-2":   //onePlus v2
                date = view.findViewById(R.id.onePlusV2Date);
                temp = view.findViewById(R.id.onePlusV2Temp);

                date.setText(getDateShort());
                temp.setText(getTemp());
                break;
        }// switch
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
        String temp = (new Random().nextInt(80) - 30) + " °C";
        return temp;
    }// getTemp
}// class

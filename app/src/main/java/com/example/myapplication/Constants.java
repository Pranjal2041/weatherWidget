package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import static com.example.myapplication.HomeActivity.MyPREFERENCES;

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
    static float current_temp=25;
    static Context context;

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
                textClock.setTextColor(s_color);
                if (clock_format_24) {
                    textClock.setFormat24Hour("kk:mm");
                } else {
                    textClock.setFormat24Hour("h:mm A");
                }
            }
            if (date != null && temp != null) {
                date.setTextColor(s_color);
                temp.setTextColor(s_color);
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
    String getDateForAWS()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
    }

    String getTemp() {
        Log.d(TAG, "getTemp: "+getDateForAWS());
        final String url="http://aws.imd.gov.in/AWS/dataview.php?a=aws&b=DELHI&c=NEW_DELHI&d=NEW_DELHI&e="+getDateForAWS()+"&f="+getDateForAWS()+"&g=ALL_HOUR&h=ALL_MINUTE";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        float temperature=getTempFromWeb(response);
                        setTemp(temperature);


                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: "+error.getMessage());
                    }
                });


                    RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.context.getApplicationContext());
                    requestQueue.add(stringRequest);

        String temp="";
        if(temp_unit==0)
            temp = current_temp + " °C";
        else if(temp_unit==1)
            temp=convertToFarheniet(current_temp) + " °F";


        return temp;
    }// getTemp

    float convertToFarheniet(float temp)
    {
        float f=(float) ((temp*1.8)+32);
        return (float) (f-(f%0.1));
    }

    void setTemp(float t)
    {
        current_temp=t;
        SharedPreferences sharedPreferences;
        sharedPreferences=HomeActivity.context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Current Temperature",t);
        editor.apply();
    }

    private float getTempFromWeb(String sHTML) {


        if (sHTML.equals("")) return current_temp;
        String sNodes = "";

        try
        {

            Document doc = Jsoup.parse(sHTML);


            Elements innerTable = doc.getElementsByTag("table");
            Elements rows=innerTable.select("tr");
            Element row=rows.get(rows.size()-1);
            Elements cells= row.select("td");
            Element temp=cells.get(6);

            String a=temp.toString().replace("</td>","");
            a=a.replace("<td>","");
            return Float.parseFloat(a);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d(TAG, "fp_Contacts_Response: "+sNodes);
        return current_temp;
    }

}// class

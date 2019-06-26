package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter<String> extends ArrayAdapter<String> { // https://stackoverflow.com/questions/5483495/how-to-set-font-custom-font-to-spinner-text-programmatically
    // Initialise custom font, for example:
    /*Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "font/opslate_black.ttf");*/
    Typeface font1 = ResourcesCompat.getFont(getContext(), R.font.googlesans_regular);
    Typeface font2 = ResourcesCompat.getFont(getContext(), R.font.opslate_regular);

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    SpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);

        view.setTypeface(font1);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        Typeface font;
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        switch (position) {
            case 0:
                font = font1;
                break;
            case 1:
                font = font2;
                break;
            default:
                font = font1;

        }
        view.setTypeface(font);
        // view.setTextSize(12);
        return view;
    }
}

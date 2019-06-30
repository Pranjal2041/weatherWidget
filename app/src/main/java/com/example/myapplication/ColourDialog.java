package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rarepebble.colorpicker.ColorPickerView;
import com.rarepebble.colorpicker.ColorPreference;

import static com.example.myapplication.Constants.themes;

public class ColourDialog extends Dialog implements android.view.View.OnClickListener {

    Context context;

    public ColourDialog(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_dialog);
        final ColorPickerView colorPickerView=findViewById(R.id.color_picker);
        colorPickerView.setOriginalColor(Constants.s_color);
        // colorPickerView.setColor(PreferenceManager.getDefaultSharedPreferences(context).getInt("selected_color_pref", 0));
        Button button=findViewById(R.id.save_color);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.s_color=colorPickerView.getColor();
                dismiss();

            }
        });


/*
        final SeekBar seekBar_red=initialiseSeekBar(R.id.seekBar_colour_red,Color.RED);
        final TextView textView_red=initialiseTextView(R.id.seekBar_colour_red_text,Color.RED,seekBar_red);

        final SeekBar seekBar_green=initialiseSeekBar(R.id.seekBar_colour_green,Color.GREEN);
        final TextView textView_green=initialiseTextView(R.id.seekBar_colour_green_text,Color.GREEN,seekBar_green);

        final SeekBar seekBar_blue=initialiseSeekBar(R.id.seekBar_colour_blue,Color.BLUE);
        final TextView textView_blue=initialiseTextView(R.id.seekBar_colour_blue_text,Color.BLUE,seekBar_blue);



        SeekBar.OnSeekBarChangeListener sb=new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int red=seekBar_red.getProgress();
                textView_red.setText(""+red);
                int green=seekBar_green.getProgress();
                textView_green.setText(""+green);
                int blue=seekBar_blue.getProgress();
                textView_blue.setText(""+blue);


                updateColour(red,green,blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int red=seekBar_red.getProgress();
                textView_red.setText(""+red);
                int green=seekBar_green.getProgress();
                textView_green.setText(""+green);
                int blue=seekBar_blue.getProgress();
                textView_blue.setText(""+blue);


                updateColour(red,green,blue);

            }
        };


        seekBar_red.setOnSeekBarChangeListener(sb);
        seekBar_green.setOnSeekBarChangeListener(sb);
        seekBar_blue.setOnSeekBarChangeListener(sb);
        updateColour(seekBar_red.getProgress(),seekBar_green.getProgress(),seekBar_blue.getProgress());

*/


    }
    @Override
    public void onClick(View v) {

    }

    SeekBar initialiseSeekBar(int id,int colour)
    {

        SeekBar seekBar=findViewById(id);
        //seekBar.setProgressTintList(ColorStateList.valueOf(colour));
        seekBar.setThumbTintList(ColorStateList.valueOf(colour));
        Drawable progressDrawable = seekBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(colour, PorterDuff.Mode.MULTIPLY);
        seekBar.setProgressDrawable(progressDrawable);
        return seekBar;
    }
    TextView initialiseTextView(int id,int colour, SeekBar sb)
    {
        final TextView textView=findViewById(id);
        textView.setText(""+sb.getProgress());
        textView.setTextColor(colour);
        return textView;
    }

    void updateColour(int red,int green,int blue)
    {
        RelativeLayout relativeLayout=findViewById(R.id.colour_preview);
        relativeLayout.setBackgroundColor(Color.rgb(red,green,blue));

    }
}

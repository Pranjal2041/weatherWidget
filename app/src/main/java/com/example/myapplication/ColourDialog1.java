package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;

public class ColourDialog1 extends Dialog implements android.view.View.OnClickListener {

    Context context;
    static int color_dialog_type;

    public ColourDialog1(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_dialog_1);
        FloatingActionButton floatingActionButton=findViewById(R.id.color_dialog_color_more);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color_dialog_type=1;
                dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {

    }


}
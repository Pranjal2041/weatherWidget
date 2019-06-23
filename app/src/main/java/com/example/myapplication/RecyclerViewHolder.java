package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/** User defined data type
 * Stores View for widgets
 * */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    View[] views=new View[Constants.themes.length];

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        views[0]=itemView.findViewById(R.id.include_pixel);
        views[1]=itemView.findViewById(R.id.include_op_v1);
        views[2]=itemView.findViewById(R.id.include_op_v2);
    }// constructor
}// class

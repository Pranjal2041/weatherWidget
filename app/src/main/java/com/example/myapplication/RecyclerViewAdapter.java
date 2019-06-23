package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    List<RecyclerData> list = Collections.emptyList();

    Context context;

    public RecyclerViewAdapter(List<RecyclerData> list, Context context)
    {
        this.list = list;
        this.context = context;
    }// constructor

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();



        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d(TAG, "onCreateViewHolder: "+i);
        // Inflate the layout

        View photoView = inflater.inflate(R.layout.card_view,
                viewGroup, false);

        /*LM_Fragment.i=i;*/
        /*FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.cards_ll,new LM_Fragment());

        fragmentTransaction.commit();*/



        RecyclerViewHolder viewHolder = new RecyclerViewHolder(photoView);





        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {

            for(int j=0;j<3;j++)
            {
                recyclerViewHolder.views[j].setVisibility(View.GONE);
            }
            recyclerViewHolder.views[i].setVisibility(View.VISIBLE);

            new Constants().modifyUI(recyclerViewHolder.views[i],Constants.themes[i]);

    }// onBindViewHolder


    @Override
    public int getItemCount() {
        return list.size();
    }
}// class

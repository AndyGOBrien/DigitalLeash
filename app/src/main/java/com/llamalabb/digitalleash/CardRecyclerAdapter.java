package com.llamalabb.digitalleash;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by andy on 9/26/17.
 */

public class CardRecyclerAdapter extends Adapter {

    private String msg;
    private int direction;


    CardRecyclerAdapter(int direction, String msg){
        this.msg = msg;
        this.direction = direction;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new IntroViewHolder(new IntroCard(parent.getContext(), direction, msg));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return 1;
    }

    private class IntroViewHolder extends RecyclerView.ViewHolder {
        public IntroViewHolder(View itemView) {
            super(itemView);
        }
    }

}

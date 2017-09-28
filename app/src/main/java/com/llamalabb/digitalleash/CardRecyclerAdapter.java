package com.llamalabb.digitalleash;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by andy on 9/26/17.
 */

public class CardRecyclerAdapter extends Adapter {

    private List<String> msg;
    private int direction;


    CardRecyclerAdapter(int direction, List<String> msg){
        this.msg = msg;
        this.direction = direction;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("CARDADAPTER", "Create");
        return new IntroViewHolder(new IntroCard(parent.getContext(), direction));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        IntroViewHolder tempHolder;
        if(holder instanceof IntroViewHolder){
            tempHolder = (IntroViewHolder)holder;
            tempHolder.getIntroCard().setMsg(msg.get(position));
        }

        Log.d("CARDADAPTER", msg.get(position));

    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    private class IntroViewHolder extends RecyclerView.ViewHolder {
        IntroCard introCard;

        public IntroViewHolder(View introCard) {
            super(introCard);
            this.introCard = (IntroCard)introCard;
        }

        public IntroCard getIntroCard() {
            return introCard;
        }

        public void setIntroCard(IntroCard introCard) {
            this.introCard = introCard;
        }
    }

}

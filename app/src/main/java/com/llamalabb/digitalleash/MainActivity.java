package com.llamalabb.digitalleash;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends Activity {

    private final int LEFT = ItemTouchHelper.LEFT;
    private final int RIGHT = ItemTouchHelper.RIGHT;
    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        messages = new ArrayList<>();


        Collections.addAll(messages, getResources().getStringArray(R.array.IntroMessages));


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new UnscrollableLinearLayoutManager(this));
        recyclerView.setAdapter(new CardRecyclerAdapter(ItemTouchHelper.RIGHT, messages.get(0)));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            int index = 0;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == LEFT && index < messages.size()-1)
                    recyclerView.setAdapter(new CardRecyclerAdapter(RIGHT, messages.get(++index)));
                else if(direction == RIGHT && index > 0)
                    recyclerView.setAdapter(new CardRecyclerAdapter(LEFT, messages.get(--index)));
                else if(direction == RIGHT && index == 0)
                    recyclerView.setAdapter(new CardRecyclerAdapter(RIGHT, messages.get(index)));
                else if(direction == LEFT && index == messages.size()-1)
                    recyclerView.setAdapter(new CardRecyclerAdapter(LEFT, messages.get(index)));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

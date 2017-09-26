package com.llamalabb.digitalleash;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MainActivity extends Activity {

    private final int LEFT = ItemTouchHelper.LEFT;
    private final int RIGHT = ItemTouchHelper.RIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        class UnscrollableLinearLayoutManager extends LinearLayoutManager {
            public UnscrollableLinearLayoutManager(Context context){
                super(context);
            }
            public boolean canScrollVertically(){
                return false;
            }
        }


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new UnscrollableLinearLayoutManager(this));
        recyclerView.setAdapter(new CardRecyclerAdapter(ItemTouchHelper.LEFT));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == LEFT)
                    recyclerView.setAdapter(new CardRecyclerAdapter(RIGHT));
                else if(direction == RIGHT)
                    recyclerView.setAdapter(new CardRecyclerAdapter(LEFT));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

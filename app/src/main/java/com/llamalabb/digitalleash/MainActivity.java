package com.llamalabb.digitalleash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;

import layout.YesNoDialogFragment;



public class MainActivity extends FragmentActivity {

    private final int LEFT = ItemTouchHelper.LEFT;
    private final int RIGHT = ItemTouchHelper.RIGHT;
    private ArrayList<String> messages;
    private Fragment yesNoDialog;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        messages = new ArrayList<>();


        Collections.addAll(messages, getResources().getStringArray(R.array.IntroMessages));


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new UnscrollableLinearLayoutManager(this));
        recyclerView.setAdapter(new CardRecyclerAdapter(ItemTouchHelper.RIGHT, messages.get(0)));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            int index = 0;
            Boolean showYesNoFrag = false;

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

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

                if(index == 2 && showYesNoFrag == false) {
                    showYesNoFrag = true;
                    goToYesNoFragment();
                }
                else if(index != 2 && showYesNoFrag == true){
                    fragmentManager.popBackStack();
                    showYesNoFrag = false;
                }


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void goToYesNoFragment(){
        yesNoDialog = new YesNoDialogFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,
                                                R.anim.slide_down,
                                                R.anim.slide_up,
                                                R.anim.slide_down);
        fragmentTransaction.replace(R.id.fragment_holder, yesNoDialog);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

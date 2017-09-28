package com.llamalabb.digitalleash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import layout.ChildParentDialogFragment;
import layout.YesNoDialogFragment;


public class MainActivity extends FragmentActivity {

    private final int LEFT = ItemTouchHelper.LEFT;
    private final int RIGHT = ItemTouchHelper.RIGHT;
    private List<String> mMessages = new ArrayList<>();
    private Fragment yesNoDialog, childParentDialog;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CardRecyclerAdapter cardRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Collections.addAll(mMessages, getResources().getStringArray(R.array.IntroMessages));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new CardRecyclerAdapter(ItemTouchHelper.RIGHT, mMessages));

//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback
//                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
//
//            int index = 0;
//            Boolean showYesNoFrag = false;
//            Boolean showChildParentFrag = false;
//
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView,
//                                  RecyclerView.ViewHolder viewHolder,
//                                  RecyclerView.ViewHolder target) {
//
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//
//                if(direction == LEFT && index < mMessages.size()-1)
//                    recyclerView.setAdapter(new CardRecyclerAdapter(RIGHT, mMessages.get(++index)));
//                else if(direction == RIGHT && index > 0)
//                    recyclerView.setAdapter(new CardRecyclerAdapter(LEFT, mMessages.get(--index)));
//                else if(direction == RIGHT && index == 0)
//                    recyclerView.setAdapter(new CardRecyclerAdapter(RIGHT, mMessages.get(index)));
//                else if(direction == LEFT && index == mMessages.size()-1)
//                    recyclerView.setAdapter(new CardRecyclerAdapter(LEFT, mMessages.get(index)));
//
//
//                if(index == 2 && showYesNoFrag == false) {
//                    showYesNoFrag = true;
//                    if(showChildParentFrag == true){
//                        fragmentManager.popBackStack();
//                        showChildParentFrag = false;
//                    }
//                    goToYesNoFragment();
//                }
//                else if(index != 2 && showYesNoFrag == true){
//                    fragmentManager.popBackStack();
//                    showYesNoFrag = false;
//                }
//
//                if(index == 3 && showChildParentFrag == false) {
//                    showChildParentFrag = true;
//                    goToChildParentFragment();
//                }
//                else if(index != 3 && showChildParentFrag == true){
//                    fragmentManager.popBackStack();
//                    showChildParentFrag = false;
//                }
//
//
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//
//        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void goToYesNoFragment(){
        yesNoDialog = new YesNoDialogFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                                                R.anim.slide_down_bot,
                                                R.anim.slide_up_bot,
                                                R.anim.slide_down_bot);
        fragmentTransaction.replace(R.id.fragment_holder_bot, yesNoDialog);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToChildParentFragment(){
        childParentDialog = new ChildParentDialogFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_down_top,
                R.anim.slide_up_top,
                R.anim.slide_down_top,
                R.anim.slide_up_top);
        fragmentTransaction.replace(R.id.fragment_holder_top, childParentDialog);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

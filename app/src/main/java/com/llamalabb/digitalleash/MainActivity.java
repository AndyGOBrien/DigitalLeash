package com.llamalabb.digitalleash;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import layout.ChildParentDialogFragment;
import layout.SignUpFragment;
import layout.YesNoDialogFragment;


public class MainActivity extends FragmentActivity {

    private List<String> mMessages = new ArrayList<>();
    private Fragment yesNoDialog, childParentDialog, signUpFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean showYesNoFrag = false;
    private boolean showChildParentFrag = false;
    private boolean showSignUp = false;
    private CardPagerAdapter cardPagerAdapter;
    private SharedPreferences mSettings = getSharedPreferences("MySettingsFile", MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        cardPagerAdapter = new CardPagerAdapter();

        Collections.addAll(mMessages, getResources().getStringArray(R.array.IntroMessages));

        createCardItemsForPagerAdapter();

        viewPager.setAdapter(cardPagerAdapter);
        setPageChangeListener(viewPager);


    }

    private void createCardItemsForPagerAdapter(){
        for(int i = 0; i < mMessages.size(); i++) {
            cardPagerAdapter.addCardItem(new CardItem(mMessages.get(i)));
        }
    }

    private void goToYesNoFragment(){
        yesNoDialog = new YesNoDialogFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                                                R.anim.slide_down_bot,
                                                R.anim.slide_up_bot,
                                                R.anim.slide_down_bot);
        fragmentTransaction.replace(R.id.fragment_holder, yesNoDialog);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void goToChildParentFragment(){
        childParentDialog = new ChildParentDialogFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                R.anim.slide_down_bot,
                R.anim.slide_up_bot,
                R.anim.slide_down_bot);
        fragmentTransaction.replace(R.id.fragment_holder, childParentDialog);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void goToSignUpFragment(){
        signUpFragment = new SignUpFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                R.anim.slide_down_bot,
                R.anim.slide_up_bot,
                R.anim.slide_down_bot);
        fragmentTransaction.replace(R.id.fragment_holder, signUpFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    private void setPageChangeListener(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setFragmentOnPagePosition(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFragmentOnPagePosition(int position){
        if(position == 2 && showYesNoFrag == false) {
            showYesNoFrag = true;
            if(showChildParentFrag == true){
                fragmentManager.popBackStack();
                showChildParentFrag = false;
            }
            goToYesNoFragment();
        }
        else if(position != 2 && showYesNoFrag == true){
            fragmentManager.popBackStack();
            showYesNoFrag = false;
        }


        if(position == 3 && showChildParentFrag == false) {
            showChildParentFrag = true;
            if(showSignUp == true) {
                fragmentManager.popBackStack();
                showSignUp = false;
            }
            goToChildParentFragment();
        }
        else if(position != 3 && showChildParentFrag == true){
            fragmentManager.popBackStack();
            showChildParentFrag = false;
        }


        if(position == 4 && showSignUp == false){
            goToSignUpFragment();
            showSignUp = true;
        }
        else if(position != 4 && showSignUp == true){
            fragmentManager.popBackStack();
            showSignUp = false;
        }
    }

}

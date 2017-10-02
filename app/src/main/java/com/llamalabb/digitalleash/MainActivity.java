package com.llamalabb.digitalleash;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import layout.ChildParentDialogFragment;
import layout.SignUpFragment;
import layout.YesNoDialogFragment;


public class MainActivity extends FragmentActivity {

    private List<String> mMessages = new ArrayList<>();
    private Fragment mYesNoDialog, mChildParentDialog, mSignUpFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private boolean mShowYesNoFrag = false;
    private boolean mShowChildParentFrag = false;
    private boolean mShowSignUp = false;
    private CardPagerAdapter mCardPagerAdapter;
    private SharedPreferences mSettings = getSharedPreferences("MySettingsFile", MODE_PRIVATE);
    private SharedPreferences.Editor editor = mSettings.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        mCardPagerAdapter = new CardPagerAdapter();

        Collections.addAll(mMessages, getResources().getStringArray(R.array.IntroMessages));

        createCardItemsForPagerAdapter();

        viewPager.setAdapter(mCardPagerAdapter);
        setPageChangeListener(viewPager);


    }

    private void createCardItemsForPagerAdapter(){
        for(int i = 0; i < mMessages.size(); i++) {
            mCardPagerAdapter.addCardItem(new CardItem(mMessages.get(i)));
        }
    }

    private void goToYesNoFragment(){
        mYesNoDialog = new YesNoDialogFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                                                R.anim.slide_down_bot,
                                                R.anim.slide_up_bot,
                                                R.anim.slide_down_bot);
        mFragmentTransaction.replace(R.id.fragment_holder, mYesNoDialog);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    private void goToChildParentFragment(){
        mChildParentDialog = new ChildParentDialogFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                R.anim.slide_down_bot,
                R.anim.slide_up_bot,
                R.anim.slide_down_bot);
        mFragmentTransaction.replace(R.id.fragment_holder, mChildParentDialog);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    private void goToSignUpFragment(){
        mSignUpFragment = new SignUpFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                R.anim.slide_down_bot,
                R.anim.slide_up_bot,
                R.anim.slide_down_bot);
        mFragmentTransaction.replace(R.id.fragment_holder, mSignUpFragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
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
        if(position == 2 && mShowYesNoFrag == false) {
            mShowYesNoFrag = true;
            if(mShowChildParentFrag == true){
                mFragmentManager.popBackStack();
                mShowChildParentFrag = false;
            }
            goToYesNoFragment();
        }
        else if(position != 2 && mShowYesNoFrag == true){
            mFragmentManager.popBackStack();
            mShowYesNoFrag = false;
        }


        if(position == 3 && mShowChildParentFrag == false) {
            mShowChildParentFrag = true;
            if(mShowSignUp == true) {
                mFragmentManager.popBackStack();
                mShowSignUp = false;
            }
            goToChildParentFragment();
        }
        else if(position != 3 && mShowChildParentFrag == true){
            mFragmentManager.popBackStack();
            mShowChildParentFrag = false;
        }


        if(position == 4 && mShowSignUp == false){
            goToSignUpFragment();
            mShowSignUp = true;
        }
        else if(position != 4 && mShowSignUp == true){
            mFragmentManager.popBackStack();
            mShowSignUp = false;
        }
    }

}

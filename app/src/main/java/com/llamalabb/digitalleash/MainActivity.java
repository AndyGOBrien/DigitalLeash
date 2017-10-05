package com.llamalabb.digitalleash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import layout.ChildParentDialogFragment;
import layout.ChildSignUpFragment;
import layout.ParentSignInFragment;
import layout.ParentSignUpFragment;
import layout.YesNoDialogFragment;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


import static com.llamalabb.digitalleash.CardValues.CHILD_PARENT_CARD;
import static com.llamalabb.digitalleash.CardValues.SIGN_UP_CARD;
import static com.llamalabb.digitalleash.CardValues.YES_NO_CARD;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private List<String> mMessages = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private boolean mShowYesNoFrag = false;
    private boolean mShowChildParentFrag = false;
    private boolean showSignUp = false;
    private CardPagerAdapter mCardPagerAdapter;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private MyLocationManager mMyLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        if(mSettings.getInt("isParent", -1) == 1 && mSettings.getInt("introComplete", 0) == 1){
            Intent intent = new Intent(this, ParentActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        mCardPagerAdapter = CardPagerAdapter.getInstance();

        Collections.addAll(mMessages, getResources().getStringArray(R.array.IntroMessages));

        createCardItemsForPagerAdapter();

        viewPager.setAdapter(mCardPagerAdapter);
        setPageChangeListener(viewPager);

        MainActivityPermissionsDispatcher.createLocationManagerWithPermissionCheck(this);
        createLocationManager();


    }

    private void createCardItemsForPagerAdapter(){
        for(int i = 0; i < mMessages.size(); i++) {
            mCardPagerAdapter.addCardItem(new CardItem(mMessages.get(i)));
        }
    }


    private void setFragmentInHolder(Fragment fragClass){

        try{
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.slide_up_bot,
                    R.anim.slide_down_bot,
                    R.anim.slide_up_bot,
                    R.anim.slide_down_bot);
            mFragmentTransaction.replace(R.id.fragment_holder, fragClass);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        }
        catch (Exception ex){

        }
    }


    private void setPageChangeListener(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setFragmentOnPagerPosition(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFragmentOnPagerPosition(int position){
        if(position == YES_NO_CARD && mShowYesNoFrag == false) {
            mShowYesNoFrag = true;
            if(mShowChildParentFrag == true){
                mFragmentManager.popBackStack();
                mShowChildParentFrag = false;
            }
            setFragmentInHolder(new YesNoDialogFragment());
        }
        else if(position != YES_NO_CARD && mShowYesNoFrag == true){
            mFragmentManager.popBackStack();
            mShowYesNoFrag = false;
        }


        if(position == CHILD_PARENT_CARD && mShowChildParentFrag == false) {
            mShowChildParentFrag = true;
            if(showSignUp == true) {
                mFragmentManager.popBackStack();
                showSignUp = false;
            }
            setFragmentInHolder(new ChildParentDialogFragment());
        }
        else if(position != CHILD_PARENT_CARD && mShowChildParentFrag == true){
            mFragmentManager.popBackStack();
            mShowChildParentFrag = false;
        }


        if(position == SIGN_UP_CARD && showSignUp == false){
            TextView textView = (TextView) mCardPagerAdapter.getCardViewAt(SIGN_UP_CARD).findViewById(R.id.intro_text);
            showSignUp = true;
            if(mSettings.getInt("isParent", -1) == 1) {
                if(mSettings.getInt("isPreviousUser", -1) == 0) {
                    textView.setText(getResources().getString(R.string.parent_sign_up));
                    setFragmentInHolder(new ParentSignUpFragment());
                }
                else if(mSettings.getInt("isPreviousUser", -1) == 1){
                    textView.setText(getResources().getString(R.string.parent_sign_in));
                    setFragmentInHolder(new ParentSignInFragment());
                }
            }
            else if(mSettings.getInt("isParent", -1) == 0) {
                textView.setText(getResources().getString(R.string.child_sign_up));
                setFragmentInHolder(new ChildSignUpFragment());
            }
        }
        else if(position != SIGN_UP_CARD && showSignUp == true){
            mFragmentManager.popBackStack();
            showSignUp = false;
        }
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public void createLocationManager(){
        mMyLocationManager = MyLocationManager.getInstance(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
package com.llamalabb.digitalleash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import Service.LocationIntentService;

import fragments.dialogs.ParentSettingsDialogFragment;
import fragments.dialogs.ParentSignInDialogFragment;
import fragments.dialogs.ParentSignInDialogFragment.ParentSignInDialogListener;
import fragments.dialogs.ParentSettingsDialogFragment.ParentSettingsDialogListener;

public class ParentActivity extends AppCompatActivity implements ParentSettingsDialogListener, ParentSignInDialogListener{

    private MyLocationManager mMyLocationManager;
    private TextView mStatusText,mUsernameText;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private Intent mLocationIntent;
    private BroadcastReceiver broadcastReceiver;
    private ImageView mParentImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ParentActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        mSettings = getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();

        mMyLocationManager = MyLocationManager.getInstance(this);
        mStatusText = (TextView) findViewById(R.id.status_text);
        mUsernameText = (TextView) findViewById(R.id.username_textView);
        mUsernameText.setText("Username: " + mSettings.getString(getString(R.string.username), ""));
        mParentImage = (ImageView) findViewById(R.id.parent_imageView);

        showWaitingBackground();

        startBackgroundLocationBroadcast();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.parent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.parent_menu_settings:
                showSettingsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onParentChangeButtonClicked() {
        showParentSignInDialog();
    }

    @Override
    public void locationSettingChanged() {
        showWaitingBackground();
    }

    @Override
    public void onParentSignInButtonClicked(String username) {
        mUsernameText.setText("Username: " + username);
        showWaitingBackground();
    }

    private void showSettingsDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ParentSettingsDialogFragment settingsDialog = ParentSettingsDialogFragment.newInstance("Settings");
        settingsDialog.show(fm, "fragment_alert");
    }

    private void showParentSignInDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ParentSignInDialogFragment settingsDialog = ParentSignInDialogFragment.newInstance("Settings");
        settingsDialog.show(fm, "fragment_alert");
    }

    private void showWaitingBackground(){
        mStatusText.setText(getString(R.string.wait));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.primaryLightColor));
        mParentImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.if_hour_glass));
    }

    private void showParentSuccessBackground(){
        mStatusText.setText(getString(R.string.child_okay));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.secondaryLightColor));
        mParentImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.status_success2x));
    }

    private void showChildNotInZoneBackground(){
        mStatusText.setText(getString(R.string.child_not_okay));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        mParentImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.status_fail2x));
    }

    private void showChildNotSetupBackground(){
        mStatusText.setText(getString(R.string.child_not_set));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
        mParentImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.if_exclamation_point));
    }

    private void startBackgroundLocationBroadcast(){
        mLocationIntent = new Intent(this, LocationIntentService.class);
        mLocationIntent.putExtra(LocationIntentService.CHILD_OR_PARENT, "");

        broadcastReceiver = new BroadcastReceiver(){
            @Override
			/*
			 * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
			 * During this time you can use the other methods on BroadcastReceiver to view/modify
			 * the current result values.
			 */
            public void onReceive(Context arg0, Intent intent) {
                if(intent.getBooleanExtra(LocationIntentService.CHILD_NOT_SET, true)){
                    showChildNotSetupBackground();
                }
                else if(intent.getBooleanExtra(LocationIntentService.IN_BOUND, true)){
                    showParentSuccessBackground();
                }
                else
                {
                    showChildNotInZoneBackground();
                }
            }
        };

        startService(mLocationIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(LocationIntentService.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver,filter);
    }

    public void onPause()
    {
        unregisterReceiver(broadcastReceiver);
        stopService(mLocationIntent);
        super.onPause();
    }

}
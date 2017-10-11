package com.llamalabb.digitalleash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import Service.LocationIntentService;

public class ChildActivity extends AppCompatActivity {


    private MyLocationManager mMyLocationManager;
    private TextView mCurrentParentText, mStatusText;
    private ImageView mChildImage;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private Intent mLocationIntent;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ParentActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mSettings = getSharedPreferences("MySettingsFile", MODE_PRIVATE);
        mEditor = mSettings.edit();
        mStatusText = (TextView) findViewById(R.id.status_text);
        mChildImage = (ImageView) findViewById(R.id.child_imageView);
        mCurrentParentText = (TextView) findViewById(R.id.current_parent_textView);
        mCurrentParentText.setText("Parent: " + mSettings.getString(getString(R.string.username), "null"));
        mMyLocationManager = MyLocationManager.getInstance(this);

        showWaitingBackground();

        startBackgroundLocationBroadcast();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.parent_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.parent_menu_settings:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void showWaitingBackground(){
        mStatusText.setText(getString(R.string.wait));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.primaryLightColor));
        mChildImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.if_hour_glass));
    }

    private void showChildSuccessBackground(){
        mStatusText.setText(getString(R.string.reporting));
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        mChildImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.if_check_mark));
    }


    private void startBackgroundLocationBroadcast(){
        mLocationIntent = new Intent(this, LocationIntentService.class);
        mLocationIntent.putExtra(LocationIntentService.CHILD_OR_PARENT, "child_");

        broadcastReceiver = new BroadcastReceiver(){
            @Override
			/*
			 * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
			 * During this time you can use the other methods on BroadcastReceiver to view/modify
			 * the current result values.
			 */
            public void onReceive(Context arg0, Intent intent) {
                showChildSuccessBackground();
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

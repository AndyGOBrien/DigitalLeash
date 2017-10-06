package com.llamalabb.digitalleash;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import fragments.dialogs.ParentSettingsDialogFragment.ParentSettingsDialogListener;

import fragments.dialogs.ParentSettingsDialogFragment;
import fragments.dialogs.ParentSignInDialogFragment;

public class ParentActivity extends AppCompatActivity implements ParentSettingsDialogListener {

    private MyLocationManager mMyLocationManager;
    private TextView mStatusText;
    private boolean mOpenParentSignInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ParentActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        mMyLocationManager = MyLocationManager.getInstance(this);
        mStatusText = (TextView) findViewById(R.id.status_text);
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

    @Override
    public void onParentChangeButtonClicked(boolean bool) {
        mOpenParentSignInFragment = bool;
        showParentSignInDialog();
    }

}
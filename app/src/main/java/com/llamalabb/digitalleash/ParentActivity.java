package com.llamalabb.digitalleash;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fragments.dialogs.ParentSignInDialogFragment;

public class ParentActivity extends AppCompatActivity {

    MyLocationManager myLocationManager;
    TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        myLocationManager = MyLocationManager.getInstance(this);
        statusText = (TextView) findViewById(R.id.status_text);
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
        ParentSignInDialogFragment settingsDialog = ParentSignInDialogFragment.newInstance("Settings");
        settingsDialog.show(fm, "fragment_alert");
    }
}
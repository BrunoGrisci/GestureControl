package com.bruno.gesturecontrol;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;


public class GestureActivation extends ActionBarActivity {

    Switch switch_volume;
    Switch switch_camera;
    Switch switch_phone;
    Switch switch_contact;
    Switch switch_navigation;
    Switch switch_twitter;
    Switch switch_mute_notifications;
    Switch switch_flashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_activation);

        switch_volume = (Switch) findViewById(R.id.switch_volume);
        switch_camera = (Switch) findViewById(R.id.switch_camera);
        switch_phone = (Switch) findViewById(R.id.switch_phone);
        switch_contact = (Switch) findViewById(R.id.switch_contact);
        switch_navigation = (Switch) findViewById(R.id.switch_navigation);
        switch_twitter = (Switch) findViewById(R.id.switch_twitter);
        switch_mute_notifications = (Switch) findViewById(R.id.switch_mute_notifications);
        switch_flashlight = (Switch) findViewById(R.id.switch_flashlight);

        loadSwitchStatus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gesture_activation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSwitchStatus();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSwitchStatus();

    }

    protected void saveSwitchStatus() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedSwitchStatus.edit();
        editor.putBoolean(getResources().getString(R.string.switch_volume), switch_volume.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_camera), switch_camera.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_phone), switch_phone.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_contact), switch_contact.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_navigation), switch_navigation.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_twitter), switch_twitter.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_mute_notifications), switch_mute_notifications.isChecked());
        editor.putBoolean(getResources().getString(R.string.switch_flashlight), switch_flashlight.isChecked());
        editor.commit();
    }

    protected void loadSwitchStatus() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        switch_volume.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false));
        switch_camera.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_camera), false));
        switch_phone.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false));
        switch_contact.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false));
        switch_navigation.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_navigation), false));
        switch_twitter.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false));
        switch_mute_notifications.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false));
        switch_flashlight.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false));
    }
}

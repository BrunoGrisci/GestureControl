package com.bruno.gesturecontrol;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class Commands extends ActionBarActivity {

    Button button_volume_increase;
    Button button_volume_decrease;
    Button button_camera;
    Button button_phone;
    Button button_contact;
    Button button_navigation;
    Button button_twitter;
    Button button_mute_notifications;
    Button button_flashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commands);

        button_volume_increase = (Button) findViewById(R.id.button_volume_increase);
        button_volume_decrease = (Button) findViewById(R.id.button_volume_decrease);
        button_camera = (Button) findViewById(R.id.button_camera);
        button_phone = (Button) findViewById(R.id.button_phone);
        button_contact = (Button) findViewById(R.id.button_contact);
        button_navigation = (Button) findViewById(R.id.button_navigation);
        button_twitter = (Button) findViewById(R.id.button_twitter);
        button_mute_notifications = (Button) findViewById(R.id.button_mute_notifications);
        button_flashlight = (Button) findViewById(R.id.button_flashlight);

        loadButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadButtons();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commands, menu);
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

    public void loadButtons() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        button_volume_increase.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false));
        button_volume_decrease.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false));
        button_camera.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_camera), false));
        button_phone.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false));
        button_contact.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false));
        button_navigation.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_navigation), false));
        button_twitter.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false));
        button_mute_notifications.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false));
        button_flashlight.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false));
    }
}

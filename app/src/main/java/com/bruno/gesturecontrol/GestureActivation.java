package com.bruno.gesturecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class GestureActivation extends ActionBarActivity {

    private final int PICK_CONTACT = 15;

    Switch switch_volume;
    Switch switch_camera;
    Switch switch_phone;
    Switch switch_contact;
    Switch switch_navigation;
    Switch switch_twitter;
    Switch switch_mute_notifications;
    Switch switch_flashlight;

    Button button_select_contact;
    Button button_select_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_activation);

        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_flashlight_available), Toast.LENGTH_SHORT).show();
        }

        if (!isCameraAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera_available), Toast.LENGTH_SHORT).show();
        }

        button_select_contact = (Button) findViewById(R.id.button_contact_selection);
        button_select_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        button_select_navigation = (Button) findViewById(R.id.button_navigation_selection);
        button_select_navigation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

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

    @Override
    protected void onStop() {
        super.onStop();
        saveSwitchStatus();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhoneNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String telephone = null;

                        if (Integer.parseInt(hasPhoneNumber) > 0) {
                            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                                    new String[]{contactID}, null);

                            if (cursorPhone.moveToFirst()) {
                                telephone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            cursorPhone.close();

                            Log.d("name", name);
                            Log.d("has phone number", hasPhoneNumber);
                            Log.d("telephone", telephone);
                            saveContact(name, telephone);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), name + " " + getResources().getString(R.string.no_phone), Toast.LENGTH_SHORT).show();
                        }
                    }
                    c.close();
                }
                break;
        }
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

    public void saveContact(String name, String telephone) {
        SharedPreferences savedContact = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedContact.edit();
        editor.putString("contactName", name);
        editor.putString("contactNumber", telephone);
        editor.commit();
        button_select_contact.setText(name);
    }

    protected void loadSwitchStatus() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        switch_volume.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false));
        if (!isCameraAvailable(getApplicationContext())) {
            switch_camera.setEnabled(false);
            switch_camera.setChecked(false);
        }
        else {
            switch_camera.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_camera), false));
        }
        switch_phone.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false));
        switch_contact.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false));
        switch_navigation.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_navigation), false));
        switch_twitter.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false));
        switch_mute_notifications.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false));
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            switch_flashlight.setEnabled(false);
            switch_flashlight.setChecked(false);
        }
        else {
            switch_flashlight.setChecked(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false));
        }
    }

    public static boolean isCameraAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

}

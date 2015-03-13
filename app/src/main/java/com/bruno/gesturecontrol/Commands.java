package com.bruno.gesturecontrol;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        button_volume_increase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increaseVolume(getApplicationContext());
            }
        });

        button_volume_decrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                decreaseVolume(getApplicationContext());
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchCamera();
            }
        });

        button_phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchPhone();
            }
        });

        button_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callContact();
            }
        });

        button_navigation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlace();
            }
        });

        button_twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postTwitter();
            }
        });

        button_mute_notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                SharedPreferences.Editor editor = savedSwitchStatus.edit();
                AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);

                muteNotifications(audioManager);
                switch(audioManager.getRingerMode()){
                    case AudioManager.RINGER_MODE_NORMAL:
                        button_mute_notifications.setText(getResources().getString(R.string.button_mute_notifications));
                        //editor.putInt(getResources().getString(R.string.button_unmute_notifications), AudioManager.RINGER_MODE_SILENT);
                        //editor.commit();
                        break;
                    case AudioManager.RINGER_MODE_SILENT:
                        button_mute_notifications.setText(getResources().getString(R.string.button_unmute_notifications));
                        //editor.putInt(getResources().getString(R.string.button_unmute_notifications), AudioManager.RINGER_MODE_NORMAL);
                        //editor.commit();
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        button_mute_notifications.setText(getResources().getString(R.string.button_unmute_notifications));
                        //editor.putInt(getResources().getString(R.string.button_unmute_notifications), AudioManager.RINGER_MODE_NORMAL);
                        //editor.commit();
                        break;
                }
            }
        });

        button_flashlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                turnFlashlight();
            }
        });

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

        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                button_mute_notifications.setText(getResources().getString(R.string.button_mute_notifications));
                break;
            case AudioManager.RINGER_MODE_SILENT:
                button_mute_notifications.setText(getResources().getString(R.string.button_unmute_notifications));
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                button_mute_notifications.setText(getResources().getString(R.string.button_unmute_notifications));
                break;
        }
    }

    public void increaseVolume (Context context) {
        AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

    public void decreaseVolume (Context context) {
        AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

    public void launchCamera () {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void launchPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, null);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void callContact() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:955538002"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void goToPlace() {
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void postTwitter() {
        System.out.println("twitter");
    }

    public void muteNotifications(AudioManager audioManager) {

        switch(audioManager.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }
    }

    public void turnFlashlight() {
        System.out.println("flashlight");
        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
    }

}
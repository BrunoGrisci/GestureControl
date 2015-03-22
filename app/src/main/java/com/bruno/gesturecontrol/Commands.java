package com.bruno.gesturecontrol;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Commands extends ActionBarActivity {

    Button button_volume_increase;
    Button button_volume_decrease;
    Button button_camera;
    Button button_voice;
    Button button_music;
    Button button_phone;
    Button button_contact;
    Button button_message;
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
        button_voice = (Button) findViewById(R.id.button_voice);
        button_music = (Button) findViewById(R.id.button_music);
        button_phone = (Button) findViewById(R.id.button_phone);
        button_contact = (Button) findViewById(R.id.button_contact);
        button_message = (Button) findViewById(R.id.button_message);
        button_navigation = (Button) findViewById(R.id.button_navigation);
        button_twitter = (Button) findViewById(R.id.button_twitter);
        button_mute_notifications = (Button) findViewById(R.id.button_mute_notifications);
        button_flashlight = (Button) findViewById(R.id.button_flashlight);

        button_volume_increase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //increaseVolume(getApplicationContext());
                GestureFunctions.startActionIncreaseVolume(getApplicationContext());
            }
        });

        button_volume_decrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //decreaseVolume(getApplicationContext());
                GestureFunctions.startActionDecreaseVolume(getApplicationContext());
            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //launchCamera();
                if (MainActivity.isCameraOn) {
                    MainActivity.turnCamera();
                }
                GestureFunctions.startActionLaunchCamera(getApplicationContext());
            }
        });

        button_voice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestureFunctions.startActionLaunchVoice(getApplicationContext());
            }
        });

        button_music.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestureFunctions.startActionLaunchMusic(getApplicationContext());
            }
        });

        button_phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //launchPhone();
                GestureFunctions.startActionLaunchPhone(getApplicationContext());
            }
        });

        button_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //callContact();
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                String tel = savedSwitchStatus.getString("contactNumber", "tel:955538002");
                GestureFunctions.startActionCallContact(getApplicationContext(), tel);
            }
        });

        button_message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {;
                GestureFunctions.startActionLaunchMessage(getApplicationContext());
            }
        });

        button_navigation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                String latitude = savedSwitchStatus.getString("locationLatitude", "64.282062");
                String longitude = savedSwitchStatus.getString("locationLongitude", "-20.346240");
                GestureFunctions.startActionNavigate(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            }
        });

        button_twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestureFunctions.startActionPostTwitter(getApplicationContext());
            }
        });

        button_mute_notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
                switch(audioManager.getRingerMode()){
                    case AudioManager.RINGER_MODE_NORMAL:
                        button_mute_notifications.setText(getResources().getString(R.string.button_unmute_notifications));
                        muteNotifications(getApplicationContext());
                        break;
                    case AudioManager.RINGER_MODE_SILENT:
                        button_mute_notifications.setText(getResources().getString(R.string.button_mute_notifications));
                        unmuteNotifications(getApplicationContext());
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        button_mute_notifications.setText(getResources().getString(R.string.button_mute_notifications));
                        unmuteNotifications(getApplicationContext());
                        break;
                }
            }
        });

        button_flashlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //GestureFunctions.startActionTurnFlashlight(getApplicationContext());
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false)) {
                    playConfirmationSound();
                    MainActivity.turnCamera();
                }
                else {
                    informGestureDisabled();
                }
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
        button_voice.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_voice), false));
        button_music.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_music), false));
        button_phone.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false));
        button_contact.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false));
        button_message.setEnabled(savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_message), false));
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

    public void muteNotifications(Context context) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false)) {
            AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
        }
    }

    public void unmuteNotifications(Context context) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false)) {
            AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        }
    }

    private void informGestureDisabled() {
        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.noconfirmation);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.start();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                v.vibrate(200);
                break;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playConfirmationSound() {
        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.confirmation);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.start();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                break;
        }
    }

}
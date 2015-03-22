package com.bruno.gesturecontrol;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GestureFunctions extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_INCREASE_VOLUME = "com.bruno.gesturecontrol.action.INCREASE_VOLUME";
    private static final String ACTION_DECREASE_VOLUME = "com.bruno.gesturecontrol.action.DECREASE_VOLUME";
    private static final String ACTION_LAUNCH_CAMERA = "com.bruno.gesturecontrol.action.LAUNCH_CAMERA";
    private static final String ACTION_LAUNCH_PHONE = "com.bruno.gesturecontrol.action.LAUNCH_PHONE";
    private static final String ACTION_CALL_CONTACT = "com.bruno.gesturecontrol.action.CALL_CONTACT";
    private static final String ACTION_LAUNCH_MESSAGE = "com.bruno.gesturecontrol.action.LAUNCH_MESSAGE";
    private static final String ACTION_NAVIGATE = "com.bruno.gesturecontrol.action.NAVIGATE";
    private static final String ACTION_POST_TWITTER = "com.bruno.gesturecontrol.action.POST_TWITTER";
    //private static final String ACTION_MUTE_NOTIFICATIONS = "com.bruno.gesturecontrol.action.MUTE_NOTIFICATIONS";
    private static final String ACTION_TURN_FLASHLIGHT = "com.bruno.gesturecontrol.action.TURN_FLASHLIGHT";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.bruno.gesturecontrol.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.bruno.gesturecontrol.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService

    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
    Intent intent = new Intent(context, GestureFunctions.class);
    intent.setAction(ACTION_BAZ);
    intent.putExtra(EXTRA_PARAM1, param1);
    intent.putExtra(EXTRA_PARAM2, param2);
    context.startService(intent);
    }*/


    public static void startActionIncreaseVolume(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_INCREASE_VOLUME);
        context.startService(intent);
    }

    public static void startActionDecreaseVolume(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_DECREASE_VOLUME);
        context.startService(intent);
    }

    public static void startActionLaunchCamera(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_LAUNCH_CAMERA);
        context.startService(intent);
    }

    public static void startActionLaunchPhone(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_LAUNCH_PHONE);
        context.startService(intent);
    }


    public static void startActionCallContact(Context context, String param1) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_CALL_CONTACT);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public static void startActionLaunchMessage(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_LAUNCH_MESSAGE);
        context.startService(intent);
    }

    public static void startActionNavigate(Context context, double param1, double param2) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_NAVIGATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionPostTwitter(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_POST_TWITTER);
        context.startService(intent);
    }

    /*public static void startActionMuteNotifications(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_MUTE_NOTIFICATIONS);
        context.startService(intent);
    }*/

    public static void startActionTurnFlashlight(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_TURN_FLASHLIGHT);
        context.startService(intent);
    }

    public GestureFunctions() {
        super("GestureFunctions");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INCREASE_VOLUME.equals(action)) {
                handleActionIncreaseVolume();
            }
            else if (ACTION_DECREASE_VOLUME.equals(action)) {
                handleActionDecreaseVolume();
            }
            else if (ACTION_LAUNCH_CAMERA.equals(action)) {
                handleActionLaunchCamera();
            }
            else if (ACTION_LAUNCH_PHONE.equals(action)) {
                handleActionLaunchPhone();
            }
            else if (ACTION_CALL_CONTACT.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionCallContact(param1);
            }
            else if (ACTION_LAUNCH_MESSAGE.equals(action)) {
                handleActionLaunchMessage();
            }
            else if (ACTION_NAVIGATE.equals(action)) {
                final double param1 = intent.getDoubleExtra(EXTRA_PARAM1, 0.0);
                final double param2 = intent.getDoubleExtra(EXTRA_PARAM2, 0.0);
                handleActionNavigate(param1, param2);
            }
            else if (ACTION_POST_TWITTER.equals(action)) {
                handleActionPostTwitter();
            }
            /*else if (ACTION_MUTE_NOTIFICATIONS.equals(action)) {
                handleActionMuteNotifications();
            }*/
            else if (ACTION_TURN_FLASHLIGHT.equals(action)) {
                handleActionTurnFlashlight();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionIncreaseVolume() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false)) {
            AudioManager audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        }
        else {
            informGestureDisabled();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDecreaseVolume() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_volume), false)) {
            AudioManager audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionLaunchCamera() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_camera), false)) {
            playConfirmationSound();
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionLaunchPhone() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false)) {
            playConfirmationSound();
            Intent intent = new Intent(Intent.ACTION_DIAL, null);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionCallContact(String tel) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false)) {
            playConfirmationSound();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionLaunchMessage() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_message), false)) {
            playConfirmationSound();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("sms:"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionNavigate(double latitude, double longitude) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_navigation), false)) {
            playConfirmationSound();
            String uri = "google.navigation:q=%f, %f";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.US, uri, latitude, longitude)));

            //Uri gmmIntentUri = Uri.parse(coord);
            //Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            //intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_navigation), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        else {
            informGestureDisabled();
        }
    }

    private void handleActionPostTwitter() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false)) {
            playConfirmationSound();
            Location userLocation;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);;
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Geocoder geocoder = new Geocoder(GestureFunctions.this, Locale.getDefault());
            String address = "";
            try {
                List<Address> addLines;
                addLines = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
                if (addLines.isEmpty()) {
                    address = getResources().getString(R.string.unknown_address);
                }
                else {
                    for (int i = 0; i < addLines.get(0).getMaxAddressLineIndex(); i++) {
                        address = address + addLines.get(0).getAddressLine(i) + ", ";
                    }
                    address = address + addLines.get(0).getCountryCode();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String address_final = "I am at " + address;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), address_final, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            informGestureDisabled();
        }
    }

    /*private void handleActionMuteNotifications() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false)) {
            AudioManager audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
            switch (audioManager.getRingerMode()) {
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
        else {
            //informGestureDisabled();
        }
    }*/

    private void handleActionTurnFlashlight() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false)) {
            playConfirmationSound();
            System.out.println("flashlight");
            Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
        }
        else {
            //informGestureDisabled();
        }
    }

    private void informGestureDisabled() {
        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.noconfirmation);
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

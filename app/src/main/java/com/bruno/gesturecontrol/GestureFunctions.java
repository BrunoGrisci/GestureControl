package com.bruno.gesturecontrol;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.widget.Toast;

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
    private static final String ACTION_NAVIGATE = "com.bruno.gesturecontrol.action.NAVIGATE";
    private static final String ACTION_POST_TWITTER = "com.bruno.gesturecontrol.action.POST_TWITTER";
    private static final String ACTION_MUTE_NOTIFICATIONS = "com.bruno.gesturecontrol.action.MUTE_NOTIFICATIONS";
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

    public static void startActionNavigate(Context context, String param1) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_NAVIGATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public static void startActionPostTwitter(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_POST_TWITTER);
        context.startService(intent);
    }

    public static void startActionMuteNotifications(Context context) {
        Intent intent = new Intent(context, GestureFunctions.class);
        intent.setAction(ACTION_MUTE_NOTIFICATIONS);
        context.startService(intent);
    }

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
            else if (ACTION_NAVIGATE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionNavigate(param1);
            }
            else if (ACTION_POST_TWITTER.equals(action)) {
                handleActionPostTwitter();
            }
            else if (ACTION_MUTE_NOTIFICATIONS.equals(action)) {
                handleActionMuteNotifications();
            }
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionLaunchCamera() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_camera), false)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionLaunchPhone() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_phone), false)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, null);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionCallContact(String tel) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_contact), false)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionNavigate(String coord) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_navigation), false)) {
            Uri gmmIntentUri = Uri.parse(coord);
            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionPostTwitter() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false)) {
            System.out.println("twitter");
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionMuteNotifications() {
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleActionTurnFlashlight() {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false)) {
            System.out.println("flashlight");
            Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
        }
    }
}
